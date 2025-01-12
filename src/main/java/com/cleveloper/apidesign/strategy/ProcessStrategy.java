package com.cleveloper.apidesign.strategy;

import com.cleveloper.apidesign.model.Process;

public interface ProcessStrategy {
    Process executeGet(Process process);
    Process executePost(Process process);
    String getProductCode();
} 