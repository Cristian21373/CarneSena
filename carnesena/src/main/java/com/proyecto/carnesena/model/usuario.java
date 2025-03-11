package com.proyecto.carnesena.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "usuario")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuario", nullable = true, length = 36)
    private String id_usuario;

    @Column(name = "foto", nullable = true, length = 50)
    private String foto;
    
    @Column(name = "nombre", nullable = true, length = 50)
    private String nombre;
    
    @Column(name = "apellidos", nullable = true, length = 50)
    private String apellidos;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true) 
    private tipo_documento tipo_documento;
    
    @Column(name="tipo_sangre", nullable = true, length = 3)
    private String tipo_sangre;
    
    @Column(name = "numero_documento", nullable = true, length = 12)
    private Integer numero_documento; 
    
    @Column(name = "nis", nullable = false, length = 8)
    private int nis; 
    
    @ManyToOne
    @JoinColumn(name = "ficha", nullable = true)
    private ficha ficha;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true) 
    private estado_user estado_user;
    
    public String getId() {
        return id_usuario;
    }

}
