package com.proyecto.carnesena.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.carnesena.interfaces.Iadmin;
import com.proyecto.carnesena.model.Admin;
import com.proyecto.carnesena.model.AuthResponse;
import com.proyecto.carnesena.model.LoginRequest;
import com.proyecto.carnesena.model.RegisterRequest;
import com.proyecto.carnesena.model.role;
import com.proyecto.carnesena.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Iadmin iadmin;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
       UserDetails admin =   iadmin.findByUsername(request.getUsername()).orElseThrow();
       String token = jwtService.getToken(admin);
       return AuthResponse.builder()
       .token(token)
       .build();
    }

    public AuthResponse register(RegisterRequest request) {
        Admin admin = Admin.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .lastname(request.getLastname())
        .role(role.USER)
        .build();

        iadmin.save(admin);

        return AuthResponse.builder()
        .token(jwtService.getToken(admin))
        .build();
    }

}
