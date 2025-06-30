package com.inca.mc_main.repository.catalogo;

import com.inca.mc_main.entity.catalogos.RequerimientoSkill;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RequerimientoSkillRepository extends ReactiveCrudRepository<RequerimientoSkill, Integer> {
    Flux<RequerimientoSkill> findByIdRequerimiento(Integer idRequerimiento);
}
