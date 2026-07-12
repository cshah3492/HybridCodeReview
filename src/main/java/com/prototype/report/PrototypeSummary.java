package com.prototype.report;

public class PrototypeSummary {

    public void printSummary() {
        System.out.println("\n===== PROTOTYPE SUMMARY =====");
        System.out.println("Prototype Language       : Java 8");
        System.out.println("Static Analyzer          : SpotBugs");
        System.out.println("LLM Model                : gpt-4o-mini");
        System.out.println("Feedback Storage         : feedback.json");
        System.out.println("Ground Truth Source      : ground-truth-v2.csv");
        System.out.println("Evaluation Output        : prototype-evaluation.csv");
    }
}