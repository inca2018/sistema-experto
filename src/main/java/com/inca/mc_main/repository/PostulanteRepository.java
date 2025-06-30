package com.inca.mc_main.repository;

import com.inca.mc_main.entity.Postulante;
import com.inca.mc_main.entity.Skill;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PostulanteRepository extends ReactiveCrudRepository<Postulante, UUID> {



}
