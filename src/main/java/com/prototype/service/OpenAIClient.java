package com.prototype.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prototype.model.OpenAIResponse;
import com.prototype.model.TokenUsage;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.util.Properties;

public class OpenAIClient {

    private final ObjectMapper mapper = new ObjectMapper();
    private String apiKey;
    private String model;

    public OpenAIClient() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            Properties properties = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(input);

            apiKey = properties.getProperty("openai.api.key");
            model = properties.getProperty("openai.model", "gpt-4o-mini");

        } catch (Exception e) {
            throw new RuntimeException("Unable to load OpenAI config.properties", e);
        }
    }

    public OpenAIResponse analyzeFinding(String prompt) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
            post.setHeader("Authorization", "Bearer " + apiKey);
            post.setHeader("Content-Type", "application/json");

            String requestJson =
                    "{"
                            + "\"model\":\"" + model + "\","
                            + "\"temperature\":0,"
                            + "\"messages\":["
                            + "{"
                            + "\"role\":\"user\","
                            + "\"content\":" + mapper.writeValueAsString(prompt)
                            + "}"
                            + "]"
                            + "}";

            post.setEntity(new StringEntity(requestJson, "UTF-8"));

            try (CloseableHttpResponse response = client.execute(post)) {
                JsonNode root = mapper.readTree(response.getEntity().getContent());
                JsonNode usage = root.get("usage");

                TokenUsage tokenUsage = new TokenUsage();
                if (usage != null) {
                    tokenUsage.setPromptTokens(usage.path("prompt_tokens").asInt());
                    tokenUsage.setCompletionTokens(usage.path("completion_tokens").asInt());
                    tokenUsage.setTotalTokens(usage.path("total_tokens").asInt());
                }

                String content = root.get("choices").get(0).get("message").get("content").asText();

                return new OpenAIResponse(content, tokenUsage);
            }

        } catch (Exception e) {
            System.err.println("OpenAI API call failed: " + e.getMessage());

            TokenUsage tokenUsage = new TokenUsage();

            return new OpenAIResponse(
                    "{"
                            + "\"classification\":\"Manual Review Required\","
                            + "\"confidence\":0.0,"
                            + "\"reasoning\":\"LLM call failed or returned invalid response.\","
                            + "\"recommendation\":\"Review this finding manually.\""
                            + "}",
                    tokenUsage
            );
        }
    }
}