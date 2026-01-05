package com.romantic_messenger.romanticmessenger.claude.utils;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ClaudeConfig {

    @Value("${claude.model.version}")
    private String claudeModelVersion;

    @Value("${claude.api.key}")
    private String claudeApiKey;

    @Value("${claude.timeout.seconds}")
    private int timeoutSeconds;

    @Bean
    public AnthropicClient anthropicClient() {
        return AnthropicOkHttpClient.builder()
                .apiKey(claudeApiKey)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }
}
