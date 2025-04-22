package com.proyecto.carnesena.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.carnesena.interfaceService.IAdminServices;
import com.proyecto.carnesena.interfaces.Iadmin;
import com.proyecto.carnesena.model.admin;
import com.proyecto.carnesena.model.authResponse;
import com.proyecto.carnesena.model.loginRequest;
import com.proyecto.carnesena.model.registerRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class authService implements IAdminServices {

    private final Iadmin dataAdmin;
    private final jwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public authResponse register(registerRequest request) {
        admin adminData = admin.builder()
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .role(request.getRole())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        dataAdmin.save(adminData);
        return authResponse.builder()
                .token(jwtService.getToken(adminData))
                .build();
    }

    @Override
    public Optional<admin> findByUsername(String username) {
        return dataAdmin.findByUsername(username);
    }


    @Override
    public List<admin> findAll() {
        List<admin> ListAdmin = (List<admin>) dataAdmin.findAll();
        return ListAdmin;
    }

    public authResponse login(loginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        admin admin = findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(admin);
        String id = admin.getId();  // Obtén el ID del administrador
        return authResponse
                .builder()
                .token(token)
                .id(id)
                .build();
    }

    public String generarNuevaContrasena() {
        int longitud = 12;
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
    
        for (int i = 0; i < longitud; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return sb.toString();
    }
    

    public void save(admin admin) {
        dataAdmin.save(admin);
    }


    @Override
    public int deleteById(String id) {
        dataAdmin.deleteById(id);
        return 1;
    }

}
