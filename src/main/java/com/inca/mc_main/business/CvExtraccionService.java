package com.inca.mc_main.business;

import com.inca.mc_main.dto.CvExtraccionDTO;
import reactor.core.publisher.Mono;

public interface CvExtraccionService {

    Mono<CvExtraccionDTO> guardarExtraccion(CvExtraccionDTO dto);

}
