package com.inca.mc_main;

import com.inca.mc_main.repository.catalogo.RequerimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequerimientoScheduler {

    private final RequerimientoRepository requerimientoRepository;

    // Ejecuta cada 5 minutos
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void verificarYActualizarExpirados() {
        log.info("⏳ Iniciando verificación de requerimientos expirados...");

        requerimientoRepository.findByProcesoNot("Expirado")
                .filter(req -> {
                    boolean expirado = req.getFechaExpiracion() != null &&
                            LocalDateTime.now().isAfter(req.getFechaExpiracion());
                    if (expirado) {
                        log.info("📌 Requerimiento vencido detectado (ID={}): Expira en {}", req.getId(), req.getFechaExpiracion());
                    }
                    return expirado;
                })
                .flatMap(req -> {
                    req.setProceso("Expirado");
                    return requerimientoRepository.save(req)
                            .doOnSuccess(saved -> log.info("✅ Requerimiento actualizado a 'Expirado': ID={}", saved.getId()))
                            .doOnError(err -> log.error("❌ Error actualizando requerimiento ID={}", req.getId(), err));
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        r -> {}, // Ignorado en onNext
                        error -> log.error("💥 Error general en el scheduler de expiración", error),
                        () -> log.info("✔️ Verificación completada.")
                );
    }
}