package com.prototype.model;

public class TriageResult {
    private String classification;
    private double confidence;
    private String reasoning;
    private String recommendation;

    public TriageResult() {}

    public TriageResult(String classification, double confidence, String reasoning, String recommendation) {
        this.classification = classification;
        this.confidence = confidence;
        this.reasoning = reasoning;
        this.recommendation = recommendation;
    }

    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    @Override
    public String toString() {
        return "TriageResult{" +
                "classification='" + classification + '\'' +
                ", confidence=" + confidence +
                ", reasoning='" + reasoning + '\'' +
                ", recommendation='" + recommendation + '\'' +
                '}';
    }
}