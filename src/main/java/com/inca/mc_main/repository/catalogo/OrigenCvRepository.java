package com.inca.mc_main.repository.catalogo;

import com.inca.mc_main.entity.catalogos.Empresa;
import com.inca.mc_main.entity.catalogos.OrigenCv;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrigenCvRepository extends ReactiveCrudRepository<OrigenCv, Integer> {
}
