package com.inca.mc_main.business.impl;

import com.inca.mc_main.business.PerfilProfesionalService;
import com.inca.mc_main.dto.CvExtraccionDTO;
import com.inca.mc_main.entity.catalogos.PerfilProfesional;
import com.inca.mc_main.repository.catalogo.PerfilProfesionalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerfilProfesionalServiceImpl implements PerfilProfesionalService {

    private final PerfilProfesionalRepository repository;

    public Mono<PerfilProfesional> insertarSiNoExiste(String descripcion) {
        return repository.findByDescripcion(descripcion)
                .switchIfEmpty(
                        repository.save(PerfilProfesional.builder()
                                        .descripcion(descripcion)
                                        .build())
                                .doOnNext(perfil ->
                                        log.info("Insertado nuevo Perfil Profesional: {}", descripcion)
                                )
                );
    }

    @Override
    public Mono<Void> procesarPerfilesDesdeDTO(CvExtraccionDTO dto) {
        List<String> perfiles = new ArrayList<>();
        perfiles.addAll(dto.getRolesTrabajados());
        perfiles.addAll(dto.getRolesSugeridos());

        return Flux.fromIterable(perfiles)
                .flatMap(this::insertarSiNoExiste)
                .then();
    }
}
