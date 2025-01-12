package com.cleveloper.apidesign.pipeline;

import com.cleveloper.apidesign.model.Process;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProcessPipeline {
    private final Process process;

    public ProcessPipeline(Process process) {
        this.process = process;
    }

    public static ProcessPipeline of(Process process) {
        return new ProcessPipeline(process);
    }

    public ProcessPipeline validate(Predicate<Process> validator) {
        if (!validator.test(process)) {
            throw new IllegalArgumentException("Validation failed");
        }
        return this;
    }

    public ProcessPipeline execute(Function<Process, Process> business) {
        business.apply(process);
        return this;
    }

    public Process complete(Function<Process, Process> responseHandler) {
        return responseHandler.apply(process);
    }
} 