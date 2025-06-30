package com.inca.mc_main.expose;

import com.inca.mc_main.business.CatalogoService;
import com.inca.mc_main.entity.catalogos.Empresa;
import com.inca.mc_main.entity.catalogos.NivelExperiencia;
import com.inca.mc_main.entity.catalogos.PerfilProfesional;
import com.inca.mc_main.entity.catalogos.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService service;

    @GetMapping("/empresas")
    public Flux<Empresa> listarEmpresas() {
        return service.listarEmpresas();
    }

    @GetMapping("/perfiles")
    public Flux<PerfilProfesional> listarPerfiles() {
        return service.listarPerfiles();
    }

    @GetMapping("/experiencia")
    public Flux<NivelExperiencia> listarNivelExperiencia() {
        return service.listarNivelExperiencia();
    }

    @GetMapping("/skill")
    public Flux<Skill> listarSkills() {
        return service.listarSkills();
    }

    @GetMapping("/skill/search")
    public Flux<Skill> searchSkills(@RequestParam String q) {
        return service.findByNombreStartingWithIgnoreCase(q);
    }



}
