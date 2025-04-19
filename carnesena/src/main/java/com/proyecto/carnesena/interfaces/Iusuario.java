package com.proyecto.carnesena.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.carnesena.model.usuario;

@Repository
public interface Iusuario extends CrudRepository<usuario, String> {
    @Query("SELECT u FROM usuario u WHERE "
            + "u.nombre LIKE %?1% OR "
            + "u.apellidos LIKE %?1%")
    List<usuario> filtroUsuario(String filtro);

    Optional<usuario> findByNis(int nis);
    

}
