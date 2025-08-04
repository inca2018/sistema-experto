package com.inca.mc_main.repository;

import com.inca.mc_main.dto.response.ConteoOrigenDescripcionDTO;
import com.inca.mc_main.entity.CvExtraccion;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CvExtraccionRepository extends ReactiveCrudRepository<CvExtraccion, UUID> {

    Mono<Boolean> existsByNombreCompletoAndIdRequerimiento(String nombreCompleto, Integer idRequerimiento);

    @Query("SELECT oc.id AS idOrigenCV, oc.descripcion AS descripcion, COUNT(ce.id) AS total " +
            "FROM cv_extraccion ce " +
            "JOIN origen_cv oc ON ce.idOrigenCV = oc.id " +
            "WHERE ce.idRequerimiento = :idRequerimiento " +
            "GROUP BY oc.id, oc.descripcion " +
            "ORDER BY total DESC")
    Flux<ConteoOrigenDescripcionDTO> contarPorOrigenConDescripcionOrdenado(Integer idRequerimiento);
}
