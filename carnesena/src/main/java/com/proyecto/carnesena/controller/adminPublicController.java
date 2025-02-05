package com.proyecto.carnesena.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.carnesena.model.AuthResponse;
import com.proyecto.carnesena.model.LoginRequest;
import com.proyecto.carnesena.model.RegisterRequest;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class adminPublicController {

  private final AuthService authService;
    
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> Login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> Register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }
}
