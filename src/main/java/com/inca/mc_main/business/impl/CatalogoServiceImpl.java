package com.inca.mc_main.business.impl;

import com.inca.mc_main.business.CatalogoService;
import com.inca.mc_main.entity.catalogos.Empresa;
import com.inca.mc_main.entity.catalogos.NivelExperiencia;
import com.inca.mc_main.entity.catalogos.PerfilProfesional;
import com.inca.mc_main.entity.catalogos.Skill;
import com.inca.mc_main.repository.catalogo.EmpresaRepository;
import com.inca.mc_main.repository.catalogo.NivelExperienciaRepository;
import com.inca.mc_main.repository.catalogo.PerfilProfesionalRepository;
import com.inca.mc_main.repository.catalogo.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CatalogoServiceImpl implements CatalogoService {

    private final EmpresaRepository empresaRepository;
    private final PerfilProfesionalRepository perfilProfesionalRepository;
    private final NivelExperienciaRepository nivelExperienciaRepository;

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

}
