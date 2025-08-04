package com.inca.mc_main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ArchivoProcesadoResponse {
    private String archivo;
    private String mensaje;
}
