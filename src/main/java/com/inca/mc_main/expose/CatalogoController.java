package com.inca.mc_main.expose;

import com.inca.mc_main.business.CatalogoService;
import com.inca.mc_main.entity.catalogos.Empresa;
import com.inca.mc_main.entity.catalogos.NivelExperiencia;
import com.inca.mc_main.entity.catalogos.PerfilProfesional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService empresaService;

    @GetMapping("/empresas")
    public Flux<Empresa> listarEmpresas() {
        return empresaService.listarEmpresas();
    }

    @GetMapping("/perfiles")
    public Flux<PerfilProfesional> listarPerfiles() {
        return empresaService.listarPerfiles();
    }

    @GetMapping("/experiencia")
    public Flux<NivelExperiencia> listarNivelExperiencia() {
        return empresaService.listarNivelExperiencia();
    }
}
