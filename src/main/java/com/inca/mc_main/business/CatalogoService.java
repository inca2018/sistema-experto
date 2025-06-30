package com.inca.mc_main.business;

import com.inca.mc_main.entity.catalogos.Empresa;
import com.inca.mc_main.entity.catalogos.NivelExperiencia;
import com.inca.mc_main.entity.catalogos.PerfilProfesional;
import com.inca.mc_main.entity.catalogos.Skill;
import reactor.core.publisher.Flux;

public interface CatalogoService {
    Flux<Empresa> listarEmpresas();

    Flux<PerfilProfesional> listarPerfiles();

    Flux<NivelExperiencia> listarNivelExperiencia();

    Flux<Skill> listarSkills();

    Flux<Skill> findByNombreStartingWithIgnoreCase(String q);
}
