### Flow diagram

```mermaid
graph LR
    A[Client Request] --> B[ProcessController]
    B --> C{PaymentMethod Type}
    C -->|CASH| D[CashStrategy]
    C -->|CARD| E[CardStrategy]
    
    subgraph Strategy Processing
        D --> F[ProcessPipeline]
        E --> F
        F -->|1| G[Validate]
        G -->|2| H[Execute]
        H -->|3| I[Complete]
    end
    
    I --> J[Response]
    
    style A fill:#f9f,stroke:#333
    style J fill:#f9f,stroke:#333
    style G fill:#bbf,stroke:#333
    style H fill:#bbf,stroke:#333
    style I fill:#bbf,stroke:#333
```

The flow diagram shows:
1. Client initiates request
2. ProcessController determines payment method
3. Appropriate strategy (Cash/Card) is selected
4. Strategy processing occurs in three phases:
   - Validation of input
   - Execution of business logic
   - Completion and status setting
5. Response is returned to client

### Class diagram

```mermaid
classDiagram
    class ProcessController {
        -ProcessStrategyFactory strategyFactory
        +getProduct(code: String): ResponseEntity<Process>
        +createProduct(process: Process): ResponseEntity<Process>
    }

    class Process {
        -String code
        -String paymentMethod
        -String status
        +getters()
        +setters()
    }

    class ProcessPipeline {
        -Process process
        +of(process: Process): ProcessPipeline
        +validate(validator: Predicate<Process>): ProcessPipeline
        +execute(business: Function<Process, Process>): ProcessPipeline
        +complete(responseHandler: Function<Process, Process>): Process
    }

    class ProcessStrategyFactory {
        -Map<String, ProcessStrategy> strategies
        +findStrategy(type: String): ProcessStrategy
    }

    class ProcessStrategy {
        <<interface>>
        +executeGet(process: Process): Process
        +executePost(process: Process): Process
        +getProductCode(): String
    }

    class CardStrategy {
        -ValidationRules validationRules
        +executeGet(process: Process): Process
        +executePost(process: Process): Process
        +getProductCode(): String
    }

    class CashStrategy {
        -ValidationRules validationRules
        +executeGet(process: Process): Process
        +executePost(process: Process): Process
        +getProductCode(): String
    }

    class ValidationRules {
        +notNullCode(): Predicate<Process>
        +notEmptyCode(): Predicate<Process>
        +startsWithP(): Predicate<Process>
        +validLength(): Predicate<Process>
    }

    class PaymentMethod {
        <<enumeration>>
        P01_CASH
        P02_CARD
        -String code
        -String type
        +getCode(): String
        +getType(): String
        +getTypeByCode(code: String): String
    }

    ProcessController --> ProcessStrategyFactory
    ProcessStrategyFactory --> ProcessStrategy
    ProcessStrategy <|.. CardStrategy
    ProcessStrategy <|.. CashStrategy
    CardStrategy --> ValidationRules
    CashStrategy --> ValidationRules
    CardStrategy --> ProcessPipeline
    CashStrategy --> ProcessPipeline
    ProcessPipeline --> Process
    CardStrategy --> PaymentMethod
    CashStrategy --> PaymentMethod
```

### Sequence diagram

```mermaid
sequenceDiagram
    participant Client
    participant ProcessController
    participant ProcessStrategyFactory
    participant Strategy as CardStrategy/CashStrategy
    participant ProcessPipeline
    participant ValidationRules
    participant Process
    participant PaymentMethod

    %% GET Request Flow
    Client->>ProcessController: GET /api/v2/po?code=P01
    ProcessController->>PaymentMethod: getTypeByCode(code)
    PaymentMethod-->>ProcessController: payment type (CASH/CARD)
    ProcessController->>ProcessStrategyFactory: findStrategy(type)
    ProcessStrategyFactory-->>ProcessController: strategy instance
    ProcessController->>Strategy: executeGet(process)
    
    %% Strategy Processing
    Strategy->>ProcessPipeline: of(process)
    activate ProcessPipeline
    
    ProcessPipeline->>ValidationRules: validate(notNullCode)
    ValidationRules-->>ProcessPipeline: validation result
    ProcessPipeline->>ValidationRules: validate(notEmptyCode)
    ValidationRules-->>ProcessPipeline: validation result
    ProcessPipeline->>ValidationRules: validate(startsWithP)
    ValidationRules-->>ProcessPipeline: validation result
    ProcessPipeline->>ValidationRules: validate(validLength)
    ValidationRules-->>ProcessPipeline: validation result
    
    ProcessPipeline->>Process: setPaymentMethod()
    Process-->>ProcessPipeline: updated process
    
    ProcessPipeline->>Process: setStatus("SUCCESS")
    Process-->>ProcessPipeline: completed process
    
    deactivate ProcessPipeline
    
    ProcessPipeline-->>Strategy: process result
    Strategy-->>ProcessController: process result
    ProcessController-->>Client: HTTP Response

    %% POST Request Flow
    Client->>ProcessController: POST /api/v2/po
    Note over Client,ProcessController: Request body contains Process object
    
    ProcessController->>PaymentMethod: getTypeByCode(process.code)
    PaymentMethod-->>ProcessController: payment type
    ProcessController->>ProcessStrategyFactory: findStrategy(type)
    ProcessStrategyFactory-->>ProcessController: strategy instance
    ProcessController->>Strategy: executePost(process)
    
    %% Similar processing flow
    Strategy->>ProcessPipeline: of(process)
    activate ProcessPipeline
    
    ProcessPipeline->>ValidationRules: validate(notNullCode)
    ValidationRules-->>ProcessPipeline: validation result
    ProcessPipeline->>ValidationRules: validate(notEmptyCode)
    ValidationRules-->>ProcessPipeline: validation result
    ProcessPipeline->>ValidationRules: validate(startsWithP)
    ValidationRules-->>ProcessPipeline: validation result
    ProcessPipeline->>ValidationRules: validate(validLength)
    ValidationRules-->>ProcessPipeline: validation result
    
    ProcessPipeline->>Process: setPaymentMethod()
    Process-->>ProcessPipeline: updated process
    
    ProcessPipeline->>Process: setStatus("SUCCESS")
    Process-->>ProcessPipeline: completed process
    
    deactivate ProcessPipeline
    
    ProcessPipeline-->>Strategy: process result
    Strategy-->>ProcessController: process result
    ProcessController-->>Client: HTTP Response
```

The sequence diagram shows two main flows:

1. GET Request Flow:
   - Client makes a GET request with a payment code
   - System validates and processes the request through various components
   - Each validation rule is executed sequentially
   - Returns the processed result

2. POST Request Flow:
   - Client makes a POST request with a Process object
   - System follows similar validation and processing steps
   - Each validation rule is executed sequentially
   - Returns the processed result

Key components interaction:
- ProcessController handles incoming requests
- PaymentMethod determines the payment type
- ProcessStrategyFactory provides the appropriate strategy
- Strategy uses ProcessPipeline for execution
- ProcessPipeline handles individual validations sequentially
- Process object maintains the state throughout
