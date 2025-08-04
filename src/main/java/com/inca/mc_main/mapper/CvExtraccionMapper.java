package com.inca.mc_main.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inca.mc_main.dto.CvExtraccionDTO;
import com.inca.mc_main.entity.CvExtraccion;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class CvExtraccionMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static CvExtraccionDTO convertirJsonNodeADTO(JsonNode jsonNode) {
        try {
            return CvExtraccionDTO.builder()
                    .nombreCompleto(jsonNode.path("nombreCompleto").asText(null))
                    .experienciaAnios(jsonNode.path("experienciaAnios").asInt(0))
                    .skillsTecnicos(jsonArrayToList(jsonNode.path("skillsTecnicos")))
                    .rolesTrabajados(jsonArrayToList(jsonNode.path("rolesTrabajados")))
                    .rolesSugeridos(jsonArrayToList(jsonNode.path("rolesSugeridos")))
                    .gradoExperiencia(jsonNode.path("gradoExperiencia").asText(null))
                    .scoreCV(jsonNode.path("scoreCV").asInt(0))
                    .build();
        } catch (Exception e) {
            log.error(" Error convirtiendo JsonNode a CvExtraccionDTO", e);
            throw new RuntimeException("Error mapeando respuesta de OpenAI a DTO", e);
        }
    }

    private static List<String> jsonArrayToList(JsonNode arrayNode) {
        List<String> result = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode node : arrayNode) {
                result.add(node.asText());
            }
        }
        return result;
    }

    public static CvExtraccion fromDto(CvExtraccionDTO dto) {
        try {
            return CvExtraccion.builder()
                    .nombreCompleto(dto.getNombreCompleto())
                    .experienciaAnios(dto.getExperienciaAnios())
                    .skillsTecnicosJson(objectMapper.writeValueAsString(dto.getSkillsTecnicos()))
                    .rolesTrabajadosJson(objectMapper.writeValueAsString(dto.getRolesTrabajados()))
                    .rolesSugeridosJson(objectMapper.writeValueAsString(dto.getRolesSugeridos()))
                    .gradoExperiencia(dto.getGradoExperiencia())
                    .scoreCV(dto.getScoreCV())
                    .idRequerimiento(dto.getIdRequerimiento())
                    .idOrigenCV(dto.getIdOrigenCV())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error serializando JSON", e);
        }
    }

    public static CvExtraccionDTO toDto(CvExtraccion entity) {
        try {
            return CvExtraccionDTO.builder()
                    .nombreCompleto(entity.getNombreCompleto())
                    .experienciaAnios(entity.getExperienciaAnios())
                    .skillsTecnicos(objectMapper.readValue(entity.getSkillsTecnicosJson(), new TypeReference<>() {}))
                    .rolesTrabajados(objectMapper.readValue(entity.getRolesTrabajadosJson(), new TypeReference<>() {}))
                    .rolesSugeridos(objectMapper.readValue(entity.getRolesSugeridosJson(), new TypeReference<>() {}))
                    .gradoExperiencia(entity.getGradoExperiencia())
                    .scoreCV(entity.getScoreCV())
                    .idRequerimiento(entity.getIdRequerimiento())
                    .idOrigenCV(entity.getIdOrigenCV())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error deserializando JSON", e);
        }
    }
}