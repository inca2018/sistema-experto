package com.inca.mc_main.expose;

import com.inca.mc_main.business.CatalogoService;
import com.inca.mc_main.entity.catalogos.*;
import com.inca.mc_main.exception.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CatalogoController {

    private final CatalogoService service;

    @GetMapping("/empresas")
    public Mono<BasicResponse<List<Empresa>>> listarEmpresas() {
        return service.listarEmpresas()
                .collectList()
                .map(data -> BasicResponse.<List<Empresa>>builder()
                        .status(true)
                        .code(200)
                        .message("Listado de empresas")
                        .data(data)
                        .build());
    }

    @GetMapping("/perfiles")
    public Mono<BasicResponse<List<PerfilProfesional>>> listarPerfiles() {
        return service.listarPerfiles()
                .collectList()
                .map(data -> BasicResponse.<List<PerfilProfesional>>builder()
                        .status(true)
                        .code(200)
                        .message("Listado de perfiles profesionales")
                        .data(data)
                        .build());
    }

    @GetMapping("/experiencia")
    public Mono<BasicResponse<List<NivelExperiencia>>> listarNivelExperiencia() {
        return service.listarNivelExperiencia()
                .collectList()
                .map(data -> BasicResponse.<List<NivelExperiencia>>builder()
                        .status(true)
                        .code(200)
                        .message("Listado de niveles de experiencia")
                        .data(data)
                        .build());
    }

    @GetMapping("/skill")
    public Mono<BasicResponse<List<Skill>>> listarSkills() {
        return service.listarSkills()
                .collectList()
                .map(data -> BasicResponse.<List<Skill>>builder()
                        .status(true)
                        .code(200)
                        .message("Listado de skills")
                        .data(data)
                        .build());
    }

    @GetMapping("/origen-cv")
    public Mono<BasicResponse<List<OrigenCv>>> listarOrigenes() {
        return service.listarOrigenes()
                .collectList()
                .map(data -> BasicResponse.<List<OrigenCv>>builder()
                        .status(true)
                        .code(200)
                        .message("Listado de orígenes de CV")
                        .data(data)
                        .build());
    }

    @GetMapping("/skill/search")
    public Mono<BasicResponse<List<Skill>>> searchSkills(@RequestParam String q) {
        return service.findByNombreStartingWithIgnoreCase(q)
                .collectList()
                .map(data -> BasicResponse.<List<Skill>>builder()
                        .status(true)
                        .code(200)
                        .message("Búsqueda de skills por nombre")
                        .data(data)
                        .build());
    }

}
