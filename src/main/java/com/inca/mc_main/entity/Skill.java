package com.inca.mc_main.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("skills")
public class Skill {

    @Id
    @Column("id")
    private UUID id;

    @Column("postulante_id")
    private UUID postulanteId;

    private String nombre;

}
