package com.proyecto.carnesena.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.carnesena.model.Admin;


public interface Iadmin extends JpaRepository<Admin, Integer>{

    Optional<Admin> findByUsername(String username);

}
