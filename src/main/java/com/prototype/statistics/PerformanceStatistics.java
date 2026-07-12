package com.prototype.statistics;

public class PerformanceStatistics {

    private long spotBugsTime;
    private long totalLlmTime;
    private long totalExecutionTime;
    private int llmCalls;

    public void setSpotBugsTime(long time) {
        this.spotBugsTime = time;
    }

    public void addLlmTime(long time) {
        totalLlmTime += time;
        llmCalls++;
    }

    public void setTotalExecutionTime(long time) {
        this.totalExecutionTime = time;
    }

    public void printStatistics() {

        System.out.println("\n===== PERFORMANCE METRICS =====");

        System.out.println("SpotBugs Analysis Time : "
                + spotBugsTime + " ms");

        if (llmCalls > 0) {
            System.out.println("Average LLM Latency    : "
                    + (totalLlmTime / llmCalls) + " ms");
        }

        System.out.println("Total Execution Time   : "
                + totalExecutionTime + " ms");
    }
}