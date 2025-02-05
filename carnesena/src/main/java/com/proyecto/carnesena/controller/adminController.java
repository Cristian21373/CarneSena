package com.proyecto.carnesena.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class adminController {

    @PostMapping(value = "public/admin")
    public String Welcome(){
        return "welcome form secure endpoint";
    }
}
