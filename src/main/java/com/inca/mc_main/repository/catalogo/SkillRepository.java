package com.inca.mc_main.repository.catalogo;

import com.inca.mc_main.entity.catalogos.Skill;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface SkillRepository extends ReactiveCrudRepository<Skill, Integer> {

    Mono<Skill> findByNombreIgnoreCase(String nombre);

    Flux<Skill> findByNombreContainingIgnoreCase(String nombreParte);
    Flux<Skill> findByNombreStartingWithIgnoreCase(String q);

}
