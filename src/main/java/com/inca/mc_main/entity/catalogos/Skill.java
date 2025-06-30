package com.inca.mc_main.entity.catalogos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("skill")
public class Skill {
    @Id
    private Integer id;
    private String nombre;
    private String descripcion;

    public Skill withNombre(String nuevoNombre) {
        this.setNombre(nuevoNombre); // o crear un nuevo Skill si es inmutable
        return this;
    }
}