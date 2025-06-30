package com.inca.mc_main.expose;

import com.inca.mc_main.business.RequerimientoService;
import com.inca.mc_main.dto.request.ActualizarRequerimientoRequest;
import com.inca.mc_main.dto.request.RegistrarRequerimientoRequest;
import com.inca.mc_main.dto.response.DetalleRequerimientoResponse;
import com.inca.mc_main.entity.catalogos.Requerimiento;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/requerimientos")
@RequiredArgsConstructor
public class RequerimientoController {

    private final RequerimientoService requerimientoService;

    @PostMapping
    public Mono<Requerimiento> registrar(@Valid @RequestBody RegistrarRequerimientoRequest request) {
        return requerimientoService.registrarRequerimiento(request);
    }

    @PutMapping("/{id}")
    public Mono<Requerimiento> actualizar(
            @PathVariable Integer id,
            @RequestBody ActualizarRequerimientoRequest request
    ) {
        return requerimientoService.actualizarRequerimiento(id, request);
    }

    @GetMapping("/{id}")
    public Mono<DetalleRequerimientoResponse> obtenerRequerimiento(@PathVariable Integer id) {
        return requerimientoService.obtenerPorId(id);
    }

    @GetMapping
    public Flux<DetalleRequerimientoResponse> listarRequerimientos() {
        return requerimientoService.listarTodos();
    }

    @GetMapping("/buscar")
    public Flux<DetalleRequerimientoResponse> buscar(@RequestParam String texto) {
        return requerimientoService.buscarPorTexto(texto);
    }

    @PutMapping("/{id}/estado")
    public Mono<ResponseEntity<Requerimiento>> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam boolean estado) {
        return requerimientoService.cambiarEstado(id, estado)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/activos")
    public Flux<DetalleRequerimientoResponse> listarRequerimientosActivos() {
        return requerimientoService.listarTodosActivos();
    }

}