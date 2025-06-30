package com.inca.mc_main.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PerfilRutaRequestDto {
    private String ruta;
    private List<String> requerimientos; // igual que antes
    // getters y setters
}