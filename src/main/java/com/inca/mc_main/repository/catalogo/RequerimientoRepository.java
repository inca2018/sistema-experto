package com.inca.mc_main.repository.catalogo;


        import com.inca.mc_main.entity.catalogos.Requerimiento;
        import org.springframework.data.repository.reactive.ReactiveCrudRepository;
        import reactor.core.publisher.Flux;
        import reactor.core.publisher.Mono;

public interface RequerimientoRepository extends ReactiveCrudRepository<Requerimiento, Integer> {
    Mono<Boolean> existsByTituloAndEstadoTrue(String titulo);
    Mono<Requerimiento> findByTituloAndEstadoTrue(String titulo);

    Mono<Requerimiento> findById(Integer id);

    Flux<Requerimiento> findByProcesoNot(String proceso);


    Flux<Requerimiento> findAllByEstadoTrue();


    Mono<Long> countByEstadoTrue();
}