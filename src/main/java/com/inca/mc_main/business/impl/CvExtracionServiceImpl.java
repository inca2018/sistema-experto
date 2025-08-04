package com.inca.mc_main.business.impl;

import com.inca.mc_main.business.CvExtraccionService;
import com.inca.mc_main.dto.CvExtraccionDTO;
import com.inca.mc_main.entity.CvExtraccion;
import com.inca.mc_main.mapper.CvExtraccionMapper;
import com.inca.mc_main.repository.CvExtraccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
}
