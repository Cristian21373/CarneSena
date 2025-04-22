package com.proyecto.carnesena.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class registerRequest {
    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private role role;
}
