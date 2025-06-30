package com.inca.mc_main.entity.catalogos;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("requerimiento")
public class Requerimiento {
    @Id
    private Integer id;

    private String titulo;

    private Integer idPerfil;

    private Integer idExperiencia;

    private String resumen;

    private Integer idEmpresa;

    private LocalDateTime fechaCreacion;

    private Boolean estado;

    private LocalDateTime fechaExpiracion;

    private String proceso;


}
