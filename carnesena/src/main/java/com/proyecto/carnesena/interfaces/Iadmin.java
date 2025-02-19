package com.proyecto.carnesena.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.carnesena.model.admin;

@Repository
public interface Iadmin extends JpaRepository<admin, String> {

    Optional<admin> findByUsername(String username);

}
