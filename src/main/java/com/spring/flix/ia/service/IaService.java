package com.spring.flix.ia.service;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.PromptTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * Ponto único de acesso ao modelo de linguagem. Monta o prompt a partir de um
 * template em resources/prompts e envia para o ChatModel, se ele estiver configurado.
 */
@Slf4j
@Service
public class IaService {

    private final ObjectProvider<ChatModel> chatModelProvider;
    private final String modelName;

    public IaService(ObjectProvider<ChatModel> chatModelProvider,
                     @Value("${ia.openai.model-name}") String modelName) {
        this.chatModelProvider = chatModelProvider;
        this.modelName = modelName;
    }

    public boolean habilitada() {
        return chatModelProvider.getIfAvailable() != null;
    }

    public String modelo() {
        return habilitada() ? modelName : "simulado";
    }

    /**
     * Executa o template e devolve a resposta do modelo. Retorna vazio quando a IA
     * não está configurada ou quando a chamada falha (sem crédito, timeout etc.).
     */
    public Optional<String> executar(String template, Map<String, Object> variaveis) {
        ChatModel model = chatModelProvider.getIfAvailable();
        if (model == null) {
            return Optional.empty();
        }
        try {
            String prompt = PromptTemplate.from(lerTemplate(template)).apply(variaveis).text();
            return Optional.ofNullable(model.chat(prompt));
        } catch (RuntimeException e) {
            log.warn("Falha ao chamar a IA ({}): {}", template, e.getMessage());
            return Optional.empty();
        }
    }

    private String lerTemplate(String nome) {
        try {
            return new ClassPathResource("prompts/" + nome).getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Template de prompt não encontrado: " + nome, e);
        }
    }
}
