package com.proyecto.carnesena.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.carnesena.model.usuario;


@Repository
public interface Iusuario  extends CrudRepository<usuario,String> {
    // List<usuario>filtroUsuario(int filtro);
}
