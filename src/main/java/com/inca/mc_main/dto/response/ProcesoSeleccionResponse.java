package com.inca.mc_main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProcesoSeleccionResponse {

    private List<Proceso> listaProcesos;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class Proceso {
        private String id;
        private Datos datos;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class Datos {
        private Integer totalPostulantes;
        private List<ListadoOrigen> listadoOrigen;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class ListadoOrigen {
        private String origen;
        private Integer numeroCandidato;
        private String prioridad;
        private List<ResultadoCandidato> resultadoCandidato;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class ResultadoCandidato {
        private Integer orden;
        private String descripcion;
        private String nombreCandidato;
        private Integer experienciaAnios;
        private Integer scoreCV;
        private String gradoExperienciaIA;
        private boolean cumplimientoPerfil;
        private boolean cumplimientoExperiencia;
        private boolean cumplimientoSkill;
        private boolean admitido;
        private double porcentajeSkill;
    }
}
