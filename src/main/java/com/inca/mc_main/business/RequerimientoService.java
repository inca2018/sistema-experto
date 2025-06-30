package com.inca.mc_main.business;

import com.inca.mc_main.dto.request.ActualizarRequerimientoRequest;
import com.inca.mc_main.dto.request.RegistrarRequerimientoRequest;
import com.inca.mc_main.dto.response.DetalleRequerimientoResponse;
import com.inca.mc_main.entity.catalogos.Requerimiento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RequerimientoService {
    Mono<Requerimiento> registrarRequerimiento(RegistrarRequerimientoRequest request);

    Mono<Requerimiento> actualizarRequerimiento(Integer id, ActualizarRequerimientoRequest request);

    Mono<DetalleRequerimientoResponse> obtenerPorId(Integer id);

    Mono<Requerimiento> cambiarEstado(Integer id, boolean nuevoEstado);

    Flux<DetalleRequerimientoResponse> listarTodos();

    Flux<DetalleRequerimientoResponse> buscarPorTexto(String texto);

    Flux<DetalleRequerimientoResponse> listarTodosActivos();
}
