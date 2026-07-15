package com.prototype.report;

import com.prototype.model.EvaluationMode;

public class PrototypeSummary {

    public void printSummary(EvaluationMode mode) {
        System.out.println("\n===== PROTOTYPE SUMMARY =====");
        System.out.println("Prototype Language       : Java 8");
        System.out.println("Static Analyzer          : SpotBugs");
        System.out.println("LLM Model                : gpt-4o-mini");
        if (mode == EvaluationMode.HYBRID) {
            System.out.println("Feedback Storage         : feedback.json");
        } else {
            System.out.println("Feedback Storage         : Disabled");
        }
        System.out.println("Ground Truth Source      : ground-truth-spark.csv");
        System.out.println("Evaluation Output        : prototype-evaluation.csv");
    }
}