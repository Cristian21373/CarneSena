package com.proyecto.carnesena.interfaceService;

import java.util.List;
import java.util.Optional;

import com.proyecto.carnesena.model.admin;
import com.proyecto.carnesena.model.authResponse;
import com.proyecto.carnesena.model.registerRequest;

public interface IAdminServices {
    public authResponse register(registerRequest request);

    public Optional<admin> findByUsername(String username);

    public int deleteById(String id);

    public List<admin> findAll();
}
