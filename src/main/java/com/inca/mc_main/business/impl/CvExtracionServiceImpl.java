package com.inca.mc_main.business.impl;

import com.inca.mc_main.business.CvExtraccionService;
import com.inca.mc_main.dto.CvExtraccionDTO;
import com.inca.mc_main.dto.response.ConteoOrigenDescripcionDTO;
import com.inca.mc_main.entity.CvExtraccion;
import com.inca.mc_main.mapper.CvExtraccionMapper;
import com.inca.mc_main.repository.CvExtraccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CvExtracionServiceImpl implements CvExtraccionService {

    private final CvExtraccionRepository repository;


    @Override
    public Mono<CvExtraccionDTO> guardarExtraccion(CvExtraccionDTO dto) {
        CvExtraccion entity = CvExtraccionMapper.fromDto(dto);
        return repository.save(entity)
                .map(CvExtraccionMapper::toDto);
    }
    @Override
    public Mono<Boolean> existsByNombreCompletoAndIdRequerimiento(String nombreCompleto, Integer idRequerimiento) {
        return repository.existsByNombreCompletoAndIdRequerimiento(nombreCompleto,idRequerimiento);
    }

    @Override
    public Flux<ConteoOrigenDescripcionDTO> obtenerConteoPorOrigenConDescripcion(Integer idRequerimiento) {
        return repository.contarPorOrigenConDescripcionOrdenado(idRequerimiento)
                .zipWith(Flux.range(1, Integer.MAX_VALUE), (dto, index) -> {
                    return new ConteoOrigenDescripcionDTO(
                            index, // Correlativo empezando en 1
                            dto.getIdOrigenCV(),
                            dto.getDescripcion(),
                            dto.getTotal()
                    );
                });
    }
}
