package com.proyecto.carnesena.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.carnesena.model.authResponse;
import com.proyecto.carnesena.model.loginRequest;
import com.proyecto.carnesena.model.registerRequest;
import com.proyecto.carnesena.service.authService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class adminPublicController {


    
    @PostMapping(value = "login")
    public ResponseEntity<authResponse> Login(@RequestBody loginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<authResponse> Register(@RequestBody registerRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }
}
