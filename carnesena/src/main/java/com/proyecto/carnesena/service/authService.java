package com.proyecto.carnesena.service;

import java.util.List;
import java.util.Optional;

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
import com.proyecto.carnesena.model.role;

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
                .role(role.ADMIN)
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
    public boolean delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<admin> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    public authResponse login(loginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        admin admin = findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(admin);
        return authResponse
                .builder()
                .token(token)
                .build();
    }
}
