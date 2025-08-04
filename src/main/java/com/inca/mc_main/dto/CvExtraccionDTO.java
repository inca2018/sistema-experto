package com.inca.mc_main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CvExtraccionDTO {

    private String nombreCompleto;
    private Integer experienciaAnios;
    private List<String> skillsTecnicos;
    private List<String> rolesTrabajados;
    private List<String> rolesSugeridos;
    private String gradoExperiencia;
    private Integer scoreCV;
    private Integer idRequerimiento;
    private Integer idOrigenCV;
}