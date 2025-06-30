package com.inca.mc_main.dto.response;

import com.inca.mc_main.entity.catalogos.Empresa;
import com.inca.mc_main.entity.catalogos.NivelExperiencia;
import com.inca.mc_main.entity.catalogos.PerfilProfesional;
import com.inca.mc_main.entity.catalogos.Skill;

import java.time.LocalDateTime;
import java.util.List;

public record DetalleRequerimientoResponse(
        Integer id,
        String titulo,
        String resumen,
        LocalDateTime fechaCreacion,
        Empresa empresa,
        PerfilProfesional perfil,
        NivelExperiencia experiencia,
        List<Skill> skillsObligatorios,
        List<Skill> skillsDeseados,
        boolean estado,
        LocalDateTime fechaExpiracion,
        String proceso
) {}
