package com.inca.mc_main.repository;

import com.inca.mc_main.entity.CvExtraccion;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CvExtraccionRepository extends ReactiveCrudRepository<CvExtraccion, UUID> {
}
