package com.prototype.model;

public class EvaluationRecord {

    private String bugType;
    private String className;
    private String methodName;
    private String groundTruth;
    private String llmPrediction;
    private double confidence;
    private String result;

    public String getBugType() { return bugType; }
    public void setBugType(String bugType) { this.bugType = bugType; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getGroundTruth() { return groundTruth; }
    public void setGroundTruth(String groundTruth) { this.groundTruth = groundTruth; }

    public String getLlmPrediction() { return llmPrediction; }
    public void setLlmPrediction(String llmPrediction) { this.llmPrediction = llmPrediction; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}