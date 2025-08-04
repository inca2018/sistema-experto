package com.inca.mc_main.business;

import com.inca.mc_main.dto.CvExtraccionDTO;
import com.inca.mc_main.dto.request.RegistrarRequerimientoRequest;
import com.inca.mc_main.entity.catalogos.Requerimiento;
import com.inca.mc_main.entity.catalogos.Skill;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SkillService {
    Flux<Skill> listarSkills();

    Mono<Skill> busqueda(Skill skill);


    Mono<Void> procesarSkillsDesdeDTO(CvExtraccionDTO dto);
}
