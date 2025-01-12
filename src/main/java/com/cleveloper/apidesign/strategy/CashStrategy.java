package com.cleveloper.apidesign.strategy;

import com.cleveloper.apidesign.constants.PaymentMethod;
import com.cleveloper.apidesign.model.Process;
import com.cleveloper.apidesign.pipeline.ProcessPipeline;
import com.cleveloper.apidesign.validation.ValidationRules;
import org.springframework.stereotype.Component;

@Component("CASH")
public class CashStrategy implements ProcessStrategy {
    
    private final ValidationRules validationRules;
    
    public CashStrategy(ValidationRules validationRules) {
        this.validationRules = validationRules;
    }
    
    @Override
    public Process executeGet(Process process) {
        return ProcessPipeline.of(process)
            .validate(validationRules.notNullCode())
            .validate(validationRules.notEmptyCode())
            .validate(validationRules.startsWithP())
            .validate(validationRules.validLength())
            .execute(p -> {
                p.setPaymentMethod(PaymentMethod.P01_CASH.getType());
                return p;
            })
            .complete(p -> {
                p.setStatus("SUCCESS");
                return p;
            });
    }

    @Override
    public Process executePost(Process process) {
        return ProcessPipeline.of(process)
            .validate(validationRules.notNullCode())
            .validate(validationRules.notEmptyCode())
            .validate(validationRules.startsWithP())
            .validate(validationRules.validLength())
            .execute(p -> {
                p.setPaymentMethod(PaymentMethod.P01_CASH.getType());
                return p;
            })
            .complete(p -> {
                p.setStatus("SUCCESS");
                return p;
            });
    }

    @Override
    public String getProductCode() {
        return PaymentMethod.P01_CASH.getCode();
    }
} 