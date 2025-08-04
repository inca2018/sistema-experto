package com.inca.mc_main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConteoOrigenDescripcionDTO {
    private Integer correlativo;
    private Integer idOrigenCV;
    private String descripcion;
    private Long total;
}

