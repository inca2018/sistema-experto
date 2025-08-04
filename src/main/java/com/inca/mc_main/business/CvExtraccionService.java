package com.inca.mc_main.business;

import com.inca.mc_main.dto.CvExtraccionDTO;
import com.inca.mc_main.dto.response.ConteoOrigenDescripcionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CvExtraccionService {

    Mono<CvExtraccionDTO> guardarExtraccion(CvExtraccionDTO dto);

    Mono<Boolean> existsByNombreCompletoAndIdRequerimiento(String nombreCompleto, Integer idRequerimiento);

    Flux<ConteoOrigenDescripcionDTO> obtenerConteoPorOrigenConDescripcion(Integer idRequerimiento);
}
