package com.proyecto.carnesena.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.carnesena.model.admin;


public interface Iadmin extends JpaRepository<admin, Integer>{

    Optional<admin> findByUsername(String username);

}
