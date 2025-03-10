package com.proyecto.carnesena.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/")
public class adminController {

    @GetMapping("profile/")
    public ResponseEntity<String> profile(@RequestBody String request) {
        return new ResponseEntity<String>("End-point private profile", HttpStatus.OK);
    }
}
