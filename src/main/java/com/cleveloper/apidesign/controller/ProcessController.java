package com.cleveloper.apidesign.controller;

import com.cleveloper.apidesign.constants.PaymentMethod;
import com.cleveloper.apidesign.model.Process;
import com.cleveloper.apidesign.strategy.ProcessStrategy;
import com.cleveloper.apidesign.strategy.ProcessStrategyFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/po")
public class ProcessController {

    private final ProcessStrategyFactory strategyFactory;

    public ProcessController(ProcessStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @GetMapping
    public ResponseEntity<Process> getProduct(@RequestParam String code) {
        Process process = new Process(code);
        String type = PaymentMethod.getTypeByCode(code);
        Process result = strategyFactory.findStrategy(type).executeGet(process);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Process> createProduct(@RequestBody Process process) {
        String type = PaymentMethod.getTypeByCode(process.getCode());
        Process result = strategyFactory.findStrategy(type).executePost(process);
        return ResponseEntity.ok(result);
    }
} 