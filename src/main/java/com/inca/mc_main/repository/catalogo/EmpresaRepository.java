package com.inca.mc_main.repository.catalogo;

import com.inca.mc_main.entity.catalogos.Empresa;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmpresaRepository extends ReactiveCrudRepository<Empresa, Integer> {
}
