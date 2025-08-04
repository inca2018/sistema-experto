package com.inca.mc_main.business.impl;

import com.inca.mc_main.business.CatalogoService;
import com.inca.mc_main.entity.catalogos.*;
import com.inca.mc_main.repository.catalogo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CatalogoServiceImpl implements CatalogoService {

    private final EmpresaRepository empresaRepository;
    private final PerfilProfesionalRepository perfilProfesionalRepository;
    private final NivelExperienciaRepository nivelExperienciaRepository;
    private final OrigenCvRepository origenCvRepository;

    private final SkillRepository  skillRepository;

    @Override
    public Flux<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    @Override
    public Flux<PerfilProfesional> listarPerfiles() {
        return perfilProfesionalRepository.findAll();
    }

    @Override
    public Flux<NivelExperiencia> listarNivelExperiencia() {
        return nivelExperienciaRepository.findAll();
    }


    @Override
    public Flux<Skill> listarSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Flux<Skill> findByNombreStartingWithIgnoreCase(String q) {
        return skillRepository.findByNombreStartingWithIgnoreCase(q);
    }

    @Override
    public Flux<OrigenCv> listarOrigenes() {
        return origenCvRepository.findAll();
    }

}
