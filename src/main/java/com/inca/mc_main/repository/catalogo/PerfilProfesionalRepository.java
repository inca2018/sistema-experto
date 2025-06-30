package com.inca.mc_main.repository.catalogo;

import com.inca.mc_main.entity.catalogos.PerfilProfesional;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilProfesionalRepository extends ReactiveCrudRepository<PerfilProfesional, Integer> {
}
