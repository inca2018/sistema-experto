package com.inca.mc_main.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record RegistrarRequerimientoRequest(

        @NotBlank(message = "El título no puede estar vacío")
        String titulo,

        @NotNull(message = "El idPerfil es obligatorio")
        Integer idPerfil,

        @NotNull(message = "El idExperiencia es obligatorio")
        Integer idExperiencia,

        @NotBlank(message = "El resumen no puede estar vacío")
        String resumen,

        @NotNull(message = "El idEmpresa es obligatorio")
        Integer idEmpresa,

        @NotEmpty(message = "Debe incluir al menos una skill obligatoria")
        List<@NotNull Integer> skillsObligatorios,

        @NotNull(message = "La lista de skills deseados no puede ser nula")
        List<@NotNull Integer> skillsDeseados,

        @Future(message = "La fecha de expiración debe ser futura")
        @NotNull(message = "La fecha de expiración es obligatoria")
        LocalDateTime fechaExpiracion

) {}

