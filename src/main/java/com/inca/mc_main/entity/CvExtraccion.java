package com.inca.mc_main.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table("cv_extraccion")
public class CvExtraccion {

    @Id
    @Column("id")
    private Long  id;

    @Column("nombre_completo")
    private String nombreCompleto;

    @Column("experiencia_anios")
    private Integer experienciaAnios;

    @Column("skills_tecnicos")
    private String skillsTecnicosJson;

    @Column("roles_trabajados")
    private String rolesTrabajadosJson;

    @Column("roles_sugeridos")
    private String rolesSugeridosJson;

    @Column("grado_experiencia")
    private String gradoExperiencia;

    @Column("score_cv")
    private Integer scoreCV;

    @Column("fecha_registro")
    private String fechaRegistro;

    @Transient
    private List<String> skillsTecnicos;

    @Transient
    private List<String> rolesTrabajados;

    @Transient
    private List<String> rolesSugeridos;

    @Column("idRequerimiento")
    private Integer idRequerimiento;

    @Column("idOrigenCV")
    private Integer idOrigenCV;
}
