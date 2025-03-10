package com.proyecto.carnesena.interfaceService;

import java.util.List;

import com.proyecto.carnesena.model.ficha;



public interface IFichaService {
    public String save(ficha ficha);

    public List<ficha> findAll();

    // public List<ficha> filtroFicha(int filtro);
}
