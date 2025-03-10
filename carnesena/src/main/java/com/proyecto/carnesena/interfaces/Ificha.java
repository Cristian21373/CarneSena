package com.proyecto.carnesena.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.carnesena.model.ficha;

@Repository
public interface Ificha extends CrudRepository<ficha, String>{

    // List<ficha>filtroFicha(int filtro);

}
