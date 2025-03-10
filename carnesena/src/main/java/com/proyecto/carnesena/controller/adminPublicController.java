package com.proyecto.carnesena.controller;

import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/user/")
public class adminPublicController {

    private final authService authService;

    @PostMapping("login/")
    public ResponseEntity<authResponse> login(@RequestBody loginRequest request) {
        authResponse response = authService.login(request);
        return new ResponseEntity<authResponse>(response, HttpStatus.OK);
    }

    @PostMapping("register/")
    public ResponseEntity<authResponse> register(@RequestBody registerRequest request) {
        authResponse response = authService.register(request);
        return new ResponseEntity<authResponse>(response, HttpStatus.OK);
    }
}
