package com.inca.mc_main.entity.catalogos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("requerimiento_skill")
public class RequerimientoSkill {
    @Id
    private Integer id;
    private Integer idRequerimiento;
    private Integer idSkill;
    private String tipo; // 'O' = Obligatorio, 'D' = Deseado
}