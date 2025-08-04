package com.inca.mc_main.entity.catalogos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("origen_cv")
public class OrigenCv {
    @Id
    private Integer id;
    private String descripcion;
}
