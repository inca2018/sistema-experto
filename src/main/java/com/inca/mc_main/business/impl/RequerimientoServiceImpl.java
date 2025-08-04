package com.inca.mc_main.business.impl;

import com.inca.mc_main.business.RequerimientoService;
import com.inca.mc_main.dto.request.ActualizarRequerimientoRequest;
import com.inca.mc_main.dto.request.RegistrarRequerimientoRequest;
import com.inca.mc_main.dto.response.DetalleRequerimientoResponse;
import com.inca.mc_main.entity.catalogos.Requerimiento;
import com.inca.mc_main.entity.catalogos.RequerimientoSkill;
import com.inca.mc_main.entity.catalogos.Skill;
import com.inca.mc_main.exception.ApiException;
import com.inca.mc_main.repository.catalogo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequerimientoServiceImpl implements RequerimientoService {
    private final RequerimientoRepository requerimientoRepository;
    private final RequerimientoSkillRepository requerimientoSkillRepository;
    private final EmpresaRepository empresaRepository;
    private final PerfilProfesionalRepository perfilProfesionalRepository;
    private final NivelExperienciaRepository nivelExperienciaRepository;
    private final SkillRepository skillRepository;
    @Override
    public Mono<Requerimiento> registrarRequerimiento(RegistrarRequerimientoRequest request) {
        return requerimientoRepository.existsByTituloAndEstadoTrue(request.titulo())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new ApiException(
                                false,
                                400,
                                "Ya existe un requerimiento con el mismo título.",
                                request // puedes pasar null o el objeto recibido como contexto adicional
                        ));
                    }

                    Requerimiento nuevo = new Requerimiento();
                    nuevo.setTitulo(request.titulo());
                    nuevo.setIdPerfil(request.idPerfil());
                    nuevo.setIdExperiencia(request.idExperiencia());
                    nuevo.setResumen(request.resumen());
                    nuevo.setIdEmpresa(request.idEmpresa());
                    nuevo.setFechaCreacion(LocalDateTime.now());
                    nuevo.setFechaExpiracion(request.fechaExpiracion());

                    return requerimientoRepository.save(nuevo)
                            .flatMapMany(saved -> {
                                List<RequerimientoSkill> obligatorios = request.skillsObligatorios().stream()
                                        .map(idSkill -> crearSkillLink(saved.getId(), idSkill, "O"))
                                        .toList();

                                List<RequerimientoSkill> deseados = request.skillsDeseados().stream()
                                        .map(idSkill -> crearSkillLink(saved.getId(), idSkill, "D"))
                                        .toList();

                                return requerimientoSkillRepository.saveAll(obligatorios)
                                        .thenMany(requerimientoSkillRepository.saveAll(deseados))
                                        .then(Mono.just(saved));
                            })
                            .single();
                });
    }

    @Override
    public Mono<Requerimiento> actualizarRequerimiento(Integer id, ActualizarRequerimientoRequest request) {
        return requerimientoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(false, 404, "Requerimiento no encontrado con id=" + id, request)))
                .flatMap(existing ->
                        requerimientoRepository.findByTituloAndEstadoTrue(request.titulo())
                                .flatMap(otro -> {
                                    if (!otro.getId().equals(id)) {
                                        // Ya existe otro con ese título => error
                                        return Mono.error(new ApiException(
                                                false,
                                                400,
                                                "Ya existe otro requerimiento con el título: " + request.titulo(),
                                                null
                                        ));
                                    }
                                    return Mono.empty(); // Es el mismo registro, continuar
                                })
                                .then(Mono.defer(() -> {
                                    // Actualizar campos
                                    existing.setTitulo(request.titulo());
                                    existing.setIdPerfil(request.idPerfil());
                                    existing.setIdExperiencia(request.idExperiencia());
                                    existing.setResumen(request.resumen());
                                    existing.setIdEmpresa(request.idEmpresa());
                                    existing.setFechaExpiracion(request.fechaExpiracion());

                                    return requerimientoRepository.save(existing)
                                            .flatMap(saved ->
                                                    requerimientoSkillRepository.findByIdRequerimiento(id)
                                                            .flatMap(requerimientoSkillRepository::delete)
                                                            .thenMany(
                                                                    guardarNuevosSkills(id, request.skillsObligatorios(), "O")
                                                                            .concatWith(guardarNuevosSkills(id, request.skillsDeseados(), "D"))
                                                            )
                                                            .then(Mono.just(saved))
                                            );
                                }))
                );
    }

    @Override
    public Mono<DetalleRequerimientoResponse> obtenerPorId(Integer id) {
        return requerimientoRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No existe requerimiento con id=" + id)))
                .flatMap(req ->
                        Mono.zip(
                                empresaRepository.findById(req.getIdEmpresa()),
                                perfilProfesionalRepository.findById(req.getIdPerfil()),
                                nivelExperienciaRepository.findById(req.getIdExperiencia()),
                                obtenerSkills(req.getId(), "O"),
                                obtenerSkills(req.getId(), "D")
                        ).map(tuple -> new DetalleRequerimientoResponse(
                                req.getId(),
                                req.getTitulo(),
                                req.getResumen(),
                                req.getFechaCreacion(),
                                tuple.getT1(), // Empresa
                                tuple.getT2(), // Perfil
                                tuple.getT3(), // Experiencia
                                tuple.getT4(), // Skills O
                                tuple.getT5(),  // Skills D
                                req.getEstado(),
                                req.getFechaExpiracion(),
                                req.getProceso()
                        ))
                );
    }

    @Override
    public Mono<Requerimiento> cambiarEstado(Integer id, boolean nuevoEstado) {
        return requerimientoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(false, 404, "No se encontró el requerimiento con id=" + id, null)))
                .flatMap(req -> {
                    req.setEstado(nuevoEstado);
                    return requerimientoRepository.save(req);
                });
    }

    @Override
    public Flux<DetalleRequerimientoResponse> listarTodos() {
        return requerimientoRepository.findAll()
                .sort(Comparator.comparing(Requerimiento::getId)) // orden ascendente por id
                .flatMap(req ->
                        Mono.zip(
                                empresaRepository.findById(req.getIdEmpresa()),
                                perfilProfesionalRepository.findById(req.getIdPerfil()),
                                nivelExperienciaRepository.findById(req.getIdExperiencia()),
                                obtenerSkills(req.getId(), "O"),
                                obtenerSkills(req.getId(), "D")
                        ).map(tuple -> new DetalleRequerimientoResponse(
                                req.getId(),
                                req.getTitulo(),
                                req.getResumen(),
                                req.getFechaCreacion(),
                                tuple.getT1(),
                                tuple.getT2(),
                                tuple.getT3(),
                                tuple.getT4(),
                                tuple.getT5(),
                                req.getEstado(),
                                req.getFechaExpiracion(),
                                req.getProceso()
                        ))
                );
    }

    @Override
    public Flux<DetalleRequerimientoResponse> buscarPorTexto(String texto) {
        String query = texto.toLowerCase();

        return requerimientoRepository.findAll()
               // .sort(Comparator.comparing(Requerimiento::getId))
                .flatMap(req ->
                        Mono.zip(
                                empresaRepository.findById(req.getIdEmpresa()),
                                perfilProfesionalRepository.findById(req.getIdPerfil()),
                                nivelExperienciaRepository.findById(req.getIdExperiencia()),
                                obtenerSkills(req.getId(), "O"),
                                obtenerSkills(req.getId(), "D")
                        ).flatMap(tuple -> {
                            var empresa = tuple.getT1();
                            var perfil = tuple.getT2();
                            var experiencia = tuple.getT3();
                            var obligatorios = tuple.getT4();
                            var deseados = tuple.getT5();

                            boolean matchPerfil = perfil.getDescripcion().toLowerCase().contains(query);
                            boolean matchEmpresa = empresa.getDescripcion().toLowerCase().contains(query);

                            return Flux.fromIterable(obligatorios)
                                    .concatWith(Flux.fromIterable(deseados))
                                    .filter(skill -> skill.getNombre().toLowerCase().contains(query))
                                    .hasElements()
                                    .map(matchSkills -> {
                                        int prioridad = matchPerfil ? 1 : matchEmpresa ? 2 : matchSkills ? 3 : 99;
                                        return new Object[] {
                                                prioridad,
                                                new DetalleRequerimientoResponse(
                                                        req.getId(),
                                                        req.getTitulo(),
                                                        req.getResumen(),
                                                        req.getFechaCreacion(),
                                                        empresa,
                                                        perfil,
                                                        experiencia,
                                                        obligatorios,
                                                        deseados,
                                                        req.getEstado(),
                                                        req.getFechaExpiracion(),
                                                        req.getProceso()
                                                )
                                        };
                                    });
                        })
                )
                .flatMap(Mono::just) // para convertir Flux<Object[]> a Flux<...>
                .filter(obj -> (int) obj[0] < 99)
                .sort((a, b) -> Integer.compare((int) a[0], (int) b[0]))
                .map(obj -> (DetalleRequerimientoResponse) obj[1]);
}

    @Override
    public Flux<DetalleRequerimientoResponse> listarTodosActivos() {

        return requerimientoRepository.findAllByEstadoTrue()
                .flatMap(req ->
                        Mono.zip(
                                empresaRepository.findById(req.getIdEmpresa()),
                                perfilProfesionalRepository.findById(req.getIdPerfil()),
                                nivelExperienciaRepository.findById(req.getIdExperiencia()),
                                obtenerSkills(req.getId(), "O"),
                                obtenerSkills(req.getId(), "D")
                        ).map(tuple -> new DetalleRequerimientoResponse(
                                req.getId(),
                                req.getTitulo(),
                                req.getResumen(),
                                req.getFechaCreacion(),
                                tuple.getT1(),
                                tuple.getT2(),
                                tuple.getT3(),
                                tuple.getT4(),
                                tuple.getT5(),
                                req.getEstado(),
                                req.getFechaExpiracion(),
                                req.getProceso()
                        ))
                );
    }

    @Override
    public Mono<Long> contarTodosActivos() {
        return requerimientoRepository.countByEstadoTrue();
    }


    private Mono<List<Skill>> obtenerSkills(Integer idReq, String tipo) {
        return requerimientoSkillRepository.findByIdRequerimiento(idReq)
                .filter(rs -> tipo.equals(rs.getTipo()))
                .flatMap(rs -> skillRepository.findById(rs.getIdSkill()))
                .collectList();
    }

    private Flux<RequerimientoSkill> guardarNuevosSkills(Integer idReq, List<Integer> skills, String tipo) {
        return requerimientoSkillRepository.saveAll(
                skills.stream()
                        .map(idSkill -> {
                            RequerimientoSkill rs = new RequerimientoSkill();
                            rs.setIdRequerimiento(idReq);
                            rs.setIdSkill(idSkill);
                            rs.setTipo(tipo);
                            return rs;
                        }).toList()
        );
    }


    private RequerimientoSkill crearSkillLink(Integer idReq, Integer idSkill, String tipo) {
        RequerimientoSkill rs = new RequerimientoSkill();
        rs.setIdRequerimiento(idReq);
        rs.setIdSkill(idSkill);
        rs.setTipo(tipo);
        return rs;
    }

}
