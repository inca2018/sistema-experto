package com.inca.mc_main.entity.catalogos;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("nivel_experiencia")
public class NivelExperiencia {
    @Id
    private Integer id;
    private String codigo;
    private String descripcion;
}