package com.prototype.statistics;

import com.prototype.model.TokenUsage;

public class CostStatistics {

    private int totalPromptTokens;
    private int totalCompletionTokens;
    private int totalTokens;

    // Approximate GPT-4o-mini pricing per 1M tokens.
    // Adjust later if model/pricing changes.
    private static final double INPUT_COST_PER_1M = 0.15;
    private static final double OUTPUT_COST_PER_1M = 0.60;

    public void addUsage(TokenUsage usage) {
        if (usage == null) {
            return;
        }

        totalPromptTokens += usage.getPromptTokens();
        totalCompletionTokens += usage.getCompletionTokens();
        totalTokens += usage.getTotalTokens();
    }

    public void printStatistics() {
        double inputCost = (totalPromptTokens / 1000000.0) * INPUT_COST_PER_1M;
        double outputCost = (totalCompletionTokens / 1000000.0) * OUTPUT_COST_PER_1M;
        double totalCost = inputCost + outputCost;

        System.out.println("\n===== LLM TOKEN USAGE =====");
        System.out.println("Prompt Tokens     : " + totalPromptTokens);
        System.out.println("Completion Tokens : " + totalCompletionTokens);
        System.out.println("Total Tokens      : " + totalTokens);
        System.out.println("Estimated Cost    : $" + String.format("%.6f", totalCost));
    }
}