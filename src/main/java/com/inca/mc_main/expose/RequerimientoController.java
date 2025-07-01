package com.inca.mc_main.expose;

import com.inca.mc_main.business.RequerimientoService;
import com.inca.mc_main.dto.ListadoResponse;
import com.inca.mc_main.dto.Paginacion;
import com.inca.mc_main.dto.request.ActualizarRequerimientoRequest;
import com.inca.mc_main.dto.request.RegistrarRequerimientoRequest;
import com.inca.mc_main.dto.response.DetalleRequerimientoResponse;
import com.inca.mc_main.entity.catalogos.Requerimiento;
import com.inca.mc_main.exception.response.BasicResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/requerimientos")
@RequiredArgsConstructor
public class RequerimientoController {

    private final RequerimientoService requerimientoService;

    @PostMapping
    public Mono<BasicResponse<Requerimiento>> registrar(@Valid @RequestBody RegistrarRequerimientoRequest request) {
        return requerimientoService.registrarRequerimiento(request)
                .map(data -> BasicResponse.<Requerimiento>builder()
                        .status(true)
                        .code(201)
                        .message("Registro exitoso")
                        .data(data)
                        .build());
    }

    @PutMapping("/{id}")
    public Mono<BasicResponse<Requerimiento>> actualizar(
            @PathVariable Integer id,
            @RequestBody ActualizarRequerimientoRequest request
    ) {
        return requerimientoService.actualizarRequerimiento(id, request)
                .map(data -> BasicResponse.<Requerimiento>builder()
                        .status(true)
                        .code(200)
                        .message("Actualización exitosa")
                        .data(data)
                        .build());
    }

    @GetMapping("/{id}")
    public Mono<BasicResponse<DetalleRequerimientoResponse>> obtenerRequerimiento(@PathVariable Integer id) {
        return requerimientoService.obtenerPorId(id)
                .map(data -> BasicResponse.<DetalleRequerimientoResponse>builder()
                        .status(true)
                        .code(200)
                        .message("Consulta exitosa")
                        .data(data)
                        .build());
    }

    @GetMapping
    public Mono<BasicResponse<ListadoResponse>> listarRequerimientos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        long skip = (long) page * size;

        Mono<Long> totalMono = requerimientoService.contarTodosActivos(); // Debes implementar este método
        Mono<List<DetalleRequerimientoResponse>> datosMono = requerimientoService.listarTodos()
                .skip(skip)
                .take(size)
                .collectList();

        return Mono.zip(totalMono, datosMono)
                .map(tuple -> {
                    Long total = tuple.getT1();
                    List<DetalleRequerimientoResponse> lista = tuple.getT2();

                    Paginacion paginacion = Paginacion.builder()
                            .totalElements(total)
                            .currentPage(page)
                            .pageSize(size)
                            .totalPages((total + size - 1) / size)
                            .build();

                    ListadoResponse responseData = ListadoResponse.builder()
                            .elementos(lista)
                            .paginacion(paginacion)
                            .build();

                    return BasicResponse.<ListadoResponse>builder()
                            .status(true)
                            .code(200)
                            .message("Consulta exitosa")
                            .data(responseData)
                            .build();
                });
    }



    @GetMapping("/buscar")
    public Mono<BasicResponse<ListadoResponse>> buscar(
            @RequestParam String texto,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 1) {
            return Mono.just(BasicResponse.<ListadoResponse>builder()
                    .status(false)
                    .code(400)
                    .message("El número de página debe ser mayor o igual a 1")
                    .build());
        }

        long skip = (long) (page - 1) * size;

        Mono<Long> totalMono = requerimientoService.contarTodosActivos();
        Mono<List<DetalleRequerimientoResponse>> datosMono = requerimientoService.buscarPorTexto(texto)
                .skip(skip)
                .take(size)
                .collectList();

        return Mono.zip(totalMono, datosMono)
                .map(tuple -> {
                    Long total = tuple.getT1();
                    List<DetalleRequerimientoResponse> lista = tuple.getT2();

                    Paginacion paginacion = Paginacion.builder()
                            .totalElements(total)
                            .currentPage(page)
                            .pageSize(size)
                            .totalPages((total + size - 1) / size)
                            .build();

                    ListadoResponse responseData = ListadoResponse.builder()
                            .elementos(lista)
                            .paginacion(paginacion)
                            .build();

                    return BasicResponse.<ListadoResponse>builder()
                            .status(true)
                            .code(200)
                            .message("Consulta exitosa")
                            .data(responseData)
                            .build();
                });
    }



    @PutMapping("/{id}/estado")
    public Mono<ResponseEntity<BasicResponse<Requerimiento>>> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam boolean estado) {

        return requerimientoService.cambiarEstado(id, estado)
                .map(data -> ResponseEntity.ok(BasicResponse.<Requerimiento>builder()
                        .status(true)
                        .code(200)
                        .message("Estado actualizado")
                        .data(data)
                        .build()));
    }


    @GetMapping("/activos")
    public Mono<BasicResponse<ListadoResponse>> listarRequerimientosActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        long skip = (long) page * size;

        Mono<Long> totalMono = requerimientoService.contarTodosActivos(); // Debes implementar este método
        Mono<List<DetalleRequerimientoResponse>> datosMono = requerimientoService.listarTodosActivos()
                .skip(skip)
                .take(size)
                .collectList();

        return Mono.zip(totalMono, datosMono)
                .map(tuple -> {
                    Long total = tuple.getT1();
                    List<DetalleRequerimientoResponse> lista = tuple.getT2();

                    Paginacion paginacion = Paginacion.builder()
                            .totalElements(total)
                            .currentPage(page)
                            .pageSize(size)
                            .totalPages((total + size - 1) / size)
                            .build();

                    ListadoResponse responseData = ListadoResponse.builder()
                            .elementos(lista)
                            .paginacion(paginacion)
                            .build();

                    return BasicResponse.<ListadoResponse>builder()
                            .status(true)
                            .code(200)
                            .message("Consulta exitosa")
                            .data(responseData)
                            .build();
                });
    }




}