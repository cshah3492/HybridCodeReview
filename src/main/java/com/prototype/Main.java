package com.prototype;

import com.prototype.model.EvaluationMode;
import com.prototype.service.PrototypeRunner;

public class Main {

    public static void main(String[] args) {
        EvaluationMode mode = EvaluationMode.HYBRID;

        if (args.length > 0) {
            try {
                mode = EvaluationMode.valueOf(
                        args[0].trim().toUpperCase()
                );
            } catch (IllegalArgumentException e) {
                System.err.println(
                        "Invalid evaluation mode: " + args[0]
                );
                System.err.println(
                        "Valid modes: HYBRID or STANDALONE_LLM"
                );
                return;
            }
        }

        PrototypeRunner runner = new PrototypeRunner();
        runner.run(mode);
    }
}