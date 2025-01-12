package com.cleveloper.apidesign.strategy;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProcessStrategyFactory {
    
    private final Map<String, ProcessStrategy> strategies;

    public ProcessStrategyFactory(Map<String, ProcessStrategy> strategies) {
        this.strategies = strategies;
    }

    public ProcessStrategy findStrategy(String type) {
        ProcessStrategy strategy = strategies.get(type);
        
        if (strategy == null) {
            throw new IllegalArgumentException(
                String.format("No strategy found for type: %s", type));
        }
        
        return strategy;
    }
} 