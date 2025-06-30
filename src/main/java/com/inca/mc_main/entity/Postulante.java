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
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table("postulantes")
public class Postulante {
    @Id
    @Column("id")
    private UUID id;

    private String nombre;
    private String texto;
    private Double score;

    @Transient
    private List<Skill> skills;
}
