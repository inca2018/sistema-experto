package com.inca.mc_main.business;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAiSkillExtractor {

    private static final Logger log = LoggerFactory.getLogger(OpenAiSkillExtractor.class);
    private final WebClient webClient;

    public OpenAiSkillExtractor(WebClient.Builder builder,
                                @Value("${openai.api.key}") String apiKey) {
        this.webClient = builder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Mono<List<String>> extraerSkillsDesdeTexto(String textoCV) {
        String prompt = "Del siguiente texto de un currículum, extrae únicamente las habilidades técnicas o tecnológicas en forma de lista (una por línea). No incluyas información personal ni experiencias laborales:\n\n" + textoCV;

        log.info("📄 Enviando prompt a OpenAI (tamaño: {} caracteres)", textoCV.length());

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", "gpt-3.5-turbo",
                        "messages", List.of(
                                Map.of("role", "user", "content", prompt)
                        ),
                        "temperature", 0.3
                ))
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("🛑 Error desde OpenAI: {}", errorBody);
                            return Mono.error(new RuntimeException("Error llamando a OpenAI: " + errorBody));
                        })
                )
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    try {
                        String content = json
                                .get("choices")
                                .get(0)
                                .get("message")
                                .get("content")
                                .asText();
                        log.info("✅ Respuesta recibida de OpenAI:\n{}", content);
                        return parsearSkillsDesdeContenido(content);
                    } catch (Exception e) {
                        log.error("❌ Error procesando respuesta de OpenAI", e);
                        return List.of();
                    }
                });
    }

    private List<String> parsearSkillsDesdeContenido(String texto) {
        return Arrays.stream(texto.split("\\r?\\n"))
                .map(String::trim)
                .filter(linea -> !linea.isBlank() && !linea.toLowerCase().contains("habilidades"))
                .map(linea -> linea.replaceAll("^[\\d\\-•\\*\\.]+\\s*", ""))
                .collect(Collectors.toList());
    }
}
