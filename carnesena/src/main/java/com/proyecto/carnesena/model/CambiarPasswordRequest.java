package com.proyecto.carnesena.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPasswordRequest {
    private String passwordActual;
    private String nuevaPassword;
    private String confirmarPassword;
}
