package com.prototype.model;

public class Finding {
    private String bugType;
    private String category;
    private String priority;
    private String className;
    private String methodName;
    private String sourceFile;
    private int startLine;
    private int endLine;
    private String message;

    public String getBugType() { return bugType; }
    public void setBugType(String bugType) { this.bugType = bugType; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getSourceFile() { return sourceFile; }
    public void setSourceFile(String sourceFile) { this.sourceFile = sourceFile; }

    public int getStartLine() { return startLine; }
    public void setStartLine(int startLine) { this.startLine = startLine; }

    public int getEndLine() { return endLine; }
    public void setEndLine(int endLine) { this.endLine = endLine; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "Finding{" +
                "bugType='" + bugType + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", line=" + startLine +
                ", message='" + message + '\'' +
                '}';
    }
}