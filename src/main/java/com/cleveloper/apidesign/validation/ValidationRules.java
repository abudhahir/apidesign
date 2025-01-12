package com.cleveloper.apidesign.validation;

import com.cleveloper.apidesign.model.Process;
import org.springframework.stereotype.Component;
import java.util.function.Predicate;

@Component
public class ValidationRules {
    
    public Predicate<Process> notNullCode() {
        return p -> p.getCode() != null;
    }
    
    public Predicate<Process> notEmptyCode() {
        return p -> !p.getCode().isEmpty();
    }
    
    public Predicate<Process> startsWithP() {
        return p -> p.getCode().startsWith("P");
    }
    
    public Predicate<Process> validLength() {
        return p -> p.getCode().length() == 3;
    }
} 