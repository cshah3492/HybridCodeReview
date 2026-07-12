package com.prototype.service;

import java.util.List;

import com.prototype.model.Finding;

public class PromptBuilder {

    public String buildPrompt(Finding finding, List<String> priorFeedback) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an AI-assisted Java code review engine.\n\n");

        prompt.append("Static Analyzer Finding:\n");
        prompt.append("Bug Type: ").append(finding.getBugType()).append("\n");
        prompt.append("Category: ").append(finding.getCategory()).append("\n");
        prompt.append("Priority: ").append(finding.getPriority()).append("\n");
        prompt.append("Class: ").append(finding.getClassName()).append("\n");
        prompt.append("Method: ").append(finding.getMethodName()).append("\n");
        prompt.append("Source File: ").append(finding.getSourceFile()).append("\n");
        prompt.append("Start Line: ").append(finding.getStartLine()).append("\n");
        prompt.append("Message: ").append(finding.getMessage()).append("\n\n");

        if (priorFeedback != null && !priorFeedback.isEmpty()) {
            prompt.append("Relevant Prior Developer Feedback:\n");
            for (String feedback : priorFeedback) {
                prompt.append("- ").append(feedback).append("\n");
            }
            prompt.append("\n");
        }

        prompt.append("Task:\n");
        prompt.append("Classify whether this finding is likely a True Positive or False Positive.\n");
        prompt.append("Explain the reasoning and recommend remediation.\n\n");

        prompt.append("Return only JSON in this format:\n");
        prompt.append("{\n");
        prompt.append("  \"classification\": \"True Positive or False Positive\",\n");
        prompt.append("  \"confidence\": 0.0,\n");
        prompt.append("  \"reasoning\": \"short explanation\",\n");
        prompt.append("  \"recommendation\": \"recommended fix\"\n");
        prompt.append("}\n");

        return prompt.toString();
    }
}