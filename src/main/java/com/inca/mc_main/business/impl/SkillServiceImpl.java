package com.inca.mc_main.business.impl;

import com.inca.mc_main.business.SkillService;
import com.inca.mc_main.dto.CvExtraccionDTO;
import com.inca.mc_main.entity.catalogos.Skill;
import com.inca.mc_main.repository.catalogo.SkillRepository;
import com.inca.mc_main.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public Flux<Skill> listarSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Mono<Skill> busqueda(Skill skill) {
        String nombreNormalizado = Util.normalizarNombre(skill.getNombre());

        return skillRepository.findByNombreIgnoreCase(nombreNormalizado)
                .switchIfEmpty(
                        skillRepository.save(skill.withNombre(nombreNormalizado))
                                .doOnSuccess(s -> log.info("Nuevo skill registrado: {}", s.getNombre()))
                );
    }


    public Mono<Skill> insertarSiNoExiste(String nombre) {
        return skillRepository.findByNombreIgnoreCase(nombre)
                .switchIfEmpty(
                        skillRepository.save(Skill.builder()
                                        .nombre(nombre)
                                        .build())
                                .doOnNext(skill ->
                                        log.info("Insertado nuevo Skill: {}", nombre)
                                )
                );
    }

    @Override
    public Mono<Void> procesarSkillsDesdeDTO(CvExtraccionDTO dto) {
        return Flux.fromIterable(dto.getSkillsTecnicos())
                .flatMap(this::insertarSiNoExiste)
                .then();
    }

}
