package com.inca.mc_main.business;

import com.inca.mc_main.entity.catalogos.*;
import reactor.core.publisher.Flux;

public interface CatalogoService {
    Flux<Empresa> listarEmpresas();

    Flux<PerfilProfesional> listarPerfiles();

    Flux<NivelExperiencia> listarNivelExperiencia();

    Flux<Skill> listarSkills();

    Flux<Skill> findByNombreStartingWithIgnoreCase(String q);

    Flux<OrigenCv> listarOrigenes();
}
