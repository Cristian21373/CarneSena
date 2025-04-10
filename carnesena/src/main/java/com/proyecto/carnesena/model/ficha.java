package com.proyecto.carnesena.model;



import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ficha {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_ficha", nullable = false, length = 36)
    private String id_ficha;

    @Column(name = "nombre_programa", nullable = false, length = 120)
    private String nombre_programa;

    @Column(name = "codigo_ficha", nullable = false, length = 7)
    private int codigo_ficha;

    @Column(name = "fecha_inicio", nullable = false)
    private Date fecha_inicio;

    @Column(name = "fecha_fin", nullable = false)
    private Date fecha_fin;

    @Enumerated(EnumType.STRING)
    private estado_ficha estado_ficha;
    
}
