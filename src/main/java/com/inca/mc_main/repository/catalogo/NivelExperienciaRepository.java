package com.inca.mc_main.repository.catalogo;


import com.inca.mc_main.entity.catalogos.NivelExperiencia;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelExperienciaRepository extends ReactiveCrudRepository<NivelExperiencia, Integer> {
}
