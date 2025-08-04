package com.inca.mc_main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ResumenProcesamientoResponse {
    private String mensaje;
    private List<ArchivoProcesadoResponse> archivosProcesados;
    private List<ArchivoProcesadoResponse> archivosNoProcesados;
}
