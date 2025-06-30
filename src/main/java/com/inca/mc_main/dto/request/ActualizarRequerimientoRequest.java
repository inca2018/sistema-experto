package com.inca.mc_main.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record ActualizarRequerimientoRequest(
        String titulo,
        Integer idPerfil,
        Integer idExperiencia,
        String resumen,
        Integer idEmpresa,
        List<Integer> skillsObligatorios,
        List<Integer> skillsDeseados,
        LocalDateTime fechaExpiracion
) {}