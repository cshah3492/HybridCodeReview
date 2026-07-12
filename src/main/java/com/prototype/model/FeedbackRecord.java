package com.prototype.model;

public class FeedbackRecord {

    private String bugType;
    private String className;
    private String methodName;

    private String llmClassification;
    private double llmConfidence;

    private String developerDecision;
    private String notes;

    public FeedbackRecord() {
    }

    public String getBugType() {
        return bugType;
    }

    public void setBugType(String bugType) {
        this.bugType = bugType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getLlmClassification() {
        return llmClassification;
    }

    public void setLlmClassification(String llmClassification) {
        this.llmClassification = llmClassification;
    }

    public double getLlmConfidence() {
        return llmConfidence;
    }

    public void setLlmConfidence(double llmConfidence) {
        this.llmConfidence = llmConfidence;
    }

    public String getDeveloperDecision() {
        return developerDecision;
    }

    public void setDeveloperDecision(String developerDecision) {
        this.developerDecision = developerDecision;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "FeedbackRecord{" +
                "bugType='" + bugType + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", llmClassification='" + llmClassification + '\'' +
                ", llmConfidence=" + llmConfidence +
                ", developerDecision='" + developerDecision + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}