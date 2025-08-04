package com.inca.mc_main.business;

import com.inca.mc_main.dto.CvExtraccionDTO;
import reactor.core.publisher.Mono;

public interface PerfilProfesionalService {
    Mono<Void> procesarPerfilesDesdeDTO(CvExtraccionDTO dto);
}
