package com.proyecto.carnesena.interfaceService;

import java.util.List;
import java.util.Optional;

import com.proyecto.carnesena.model.ficha;

public interface IFichaService {
    public String save(ficha ficha);

    public List<ficha> findAll();

    public Optional<ficha> findOne(String id);

    public int delete(String id);

    public List<ficha> filtroFicha(int codigoFicha);
}
