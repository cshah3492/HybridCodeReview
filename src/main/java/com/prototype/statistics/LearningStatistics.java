package com.prototype.statistics;

public class LearningStatistics {

    private int totalFindingsAnalyzed;
    private int totalFeedbackRecords;
    private int findingsUsingPriorFeedback;

    public void incrementTotalFindingsAnalyzed() {
        totalFindingsAnalyzed++;
    }

    public void incrementFindingsUsingPriorFeedback() {
        findingsUsingPriorFeedback++;
    }

    public void setTotalFeedbackRecords(int totalFeedbackRecords) {
        this.totalFeedbackRecords = totalFeedbackRecords;
    }

    public void printStatistics() {
        double reuseRate = 0.0;

        if (totalFindingsAnalyzed > 0) {
            reuseRate = ((double) findingsUsingPriorFeedback / totalFindingsAnalyzed) * 100;
        }

        System.out.println("\n===== FEEDBACK LEARNING STATISTICS =====");
        System.out.println("Total Findings Analyzed: " + totalFindingsAnalyzed);
        System.out.println("Developer Feedback Records: " + totalFeedbackRecords);
        System.out.println("Findings Reusing Prior Feedback: " + findingsUsingPriorFeedback);
        System.out.println("Feedback Reuse Rate: " + String.format("%.2f", reuseRate) + "%");
    }
}