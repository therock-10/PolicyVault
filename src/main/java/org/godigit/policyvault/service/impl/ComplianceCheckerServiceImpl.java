package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.ComplianceResponseDto;
import org.godigit.policyvault.service.ComplianceCheckerService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ComplianceCheckerServiceImpl implements ComplianceCheckerService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public ComplianceCheckerServiceImpl(ChatClient chatClient, ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public ComplianceResponseDto checkCompliance(String policyText) {
        try {
            String systemPrompt = """
                    You are a compliance checker for Indian labour laws.
                    
                    Analyze the given policy text and respond only in valid JSON that matches this exact format:
                    
                    {
                      "id": "UUID",
                      "compliant": true/false,
                      "reason": "short explanation",
                      "uploadedAt": "timestamp (e.g., 2025-08-24T16:14:04.685645500Z)",
                      "violations": ["list", "of", "violations"]
                    }
                    
                    - The 'id' should be a randomly generated UUID.
                    - Do not include any text outside the JSON.
                    - Ensure the JSON is valid and parsable.
                    """;

            String responseContent = chatClient.prompt()
                    .messages(List.of(
                            new SystemMessage(systemPrompt),
                            new UserMessage(policyText)
                    ))
                    .call()
                    .content(); // returns the raw string content

            String cleanedResponse = responseContent.trim();

            if (cleanedResponse.startsWith("```json")) {
                cleanedResponse = cleanedResponse.replaceFirst("```json", "").trim();
            }
            if (cleanedResponse.endsWith("```")) {
                cleanedResponse = cleanedResponse.substring(0, cleanedResponse.length() - 3).trim();
            }

            System.out.println("AI Response: " + cleanedResponse);

            return objectMapper.readValue(cleanedResponse, ComplianceResponseDto.class);

        } catch (Exception e) {
            e.printStackTrace();
            ComplianceResponseDto fallback = new ComplianceResponseDto();
            fallback.setCompliant(false);
            fallback.setReason("Error during compliance check: " + e.getMessage());
            fallback.setUploadedAt(OffsetDateTime.now());
            fallback.setViolations(List.of());
            return fallback;
        }
    }
}
