package com.inca.mc_main.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiSkillExtractorService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiSkillExtractorService.class);
    private final WebClient webClient;

    public OpenAiSkillExtractorService(WebClient.Builder builder,
                                       @Value("${openai.api.key}") String apiKey) {
        this.webClient = builder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }


    public Mono<JsonNode> extraerInformacionDesdeTextoCV(String textoCV) {
        String prompt = """
        A partir del siguiente texto, analiza y extrae la siguiente información en formato JSON:
        
        - nombre completo,
        - experiencia en años (según fechas),
        - tecnologías dominadas (skills),
        - roles desempeñados,
        - sugiere 5 roles coherentes y específicos con su experiencia técnica principal,
        - determina el grado de experiencia (Junior, Middle, Senior) considerando la cantidad de años de experiencia y número de empresas mencionadas,
        - asigna un score de CV entre 0 y 100 considerando su experiencia, tecnologías actuales, y seniority,
        - indica si el texto tiene formato de Currículum Vitae (CV) con un valor booleano (esFormatoCV: true/false):
          Considera que esFormatoCV será true solo si el texto cumple simultáneamente las siguientes condiciones:
            1. Nombre completo identificable.
            2. Al menos una experiencia laboral (rol o fechas de trabajo).
            3. Información técnica o personal relevante (skills, estudios, certificaciones, etc.).
          Si alguno de estos elementos no está presente, esFormatoCV debe ser false.
          
        Evita sugerencias genéricas o alejadas del enfoque dominante del perfil (por ejemplo, no incluir 'Ingeniero de Datos' si no hay evidencia clara de Big Data, ETL o ML).
        
        Criterios para definir el grado de experiencia:
        - Junior: Menos de 2 años de experiencia o ha trabajado en solo 1 empresa.
        - Middle: Entre 2 y 5 años de experiencia, y ha trabajado en al menos 2 empresas.
        - Senior: Más de 5 años de experiencia y experiencia en 3 o más empresas.
        Si hay casos ambiguos, elige el grado que mejor represente el peso de su experiencia técnica.
        
        Ejemplo de respuesta:
        
        {
          "nombreCompleto": "Juan Pérez",
          "experienciaAnios": 7,
          "skillsTecnicos": ["Java", "Spring Boot", "Docker"],
          "rolesTrabajados": ["Backend Developer", "Team Lead"],
          "rolesSugeridos": ["Arquitecto Backend", "Senior Backend Developer"],
          "gradoExperiencia": "Senior",
          "scoreCV": 91,
          "esFormatoCV": true
        }
        
        Texto a analizar:
        """ + textoCV;

        log.info("Enviando prompt a OpenAI (tamaño: {} caracteres)", textoCV.length());

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", "gpt-3.5-turbo",
                        "messages", List.of(
                                Map.of("role", "user", "content", prompt)
                        ),
                        "temperature", 0.2
                ))
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("Error desde OpenAI: {}", errorBody);
                            return Mono.error(new RuntimeException("Error llamando a OpenAI: " + errorBody));
                        })
                )
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    try {
                        String content = json.get("choices").get(0).get("message").get("content").asText();
                        log.info("JSON de respuesta OpenAI:\n{}", content);
                        return new ObjectMapper().readTree(content);
                    } catch (Exception e) {
                        log.error("Error procesando JSON de OpenAI", e);
                        return NullNode.getInstance();
                    }
                });
    }


}
