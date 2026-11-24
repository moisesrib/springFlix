package com.spring.flix.ia.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Cria o cliente do modelo de linguagem (LangChain4j + OpenAI).
 * O bean só existe quando a variável OPENAI_API_KEY está preenchida; sem ela
 * os serviços de IA respondem em modo simulado.
 */
@Slf4j
@Configuration
public class IaConfig {

    @Bean
    @ConditionalOnExpression("!'${ia.openai.api-key:}'.isBlank()")
    public ChatModel chatModel(@Value("${ia.openai.api-key}") String apiKey,
                               @Value("${ia.openai.model-name}") String modelName,
                               @Value("${ia.openai.base-url}") String baseUrl,
                               @Value("${ia.openai.temperature}") Double temperature,
                               @Value("${ia.openai.timeout-seconds}") long timeoutSeconds,
                               @Value("${ia.openai.log-requests}") boolean logRequests,
                               @Value("${ia.openai.log-responses}") boolean logResponses) {
        log.info("IA habilitada com o modelo {} em {}", modelName, baseUrl);
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .baseUrl(baseUrl)
                .temperature(temperature)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .logRequests(logRequests)
                .logResponses(logResponses)
                .build();
    }
}
