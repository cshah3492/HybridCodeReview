package com.prototype.model;

public class OpenAIResponse {

    private String content;
    private TokenUsage tokenUsage;

    public OpenAIResponse(String content, TokenUsage tokenUsage) {
        this.content = content;
        this.tokenUsage = tokenUsage;
    }

    public String getContent() {
        return content;
    }

    public TokenUsage getTokenUsage() {
        return tokenUsage;
    }
}