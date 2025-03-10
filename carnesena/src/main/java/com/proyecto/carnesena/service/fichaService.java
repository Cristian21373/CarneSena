package com.proyecto.carnesena.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.carnesena.interfaceService.IFichaService;
import com.proyecto.carnesena.interfaces.Ificha;
import com.proyecto.carnesena.model.ficha;

@Service
public class fichaService implements IFichaService {

    @Autowired
    private Ificha data;

    @Override
    public String save(ficha ficha) {
        data.save(ficha);
        return ficha.getId_ficha();
    }

    @Override
    public List<ficha> findAll() {
        List<ficha> ListFicha = (List<ficha>) data.findAll();
        return ListFicha;
    }

    // @Override
    // public List<ficha> filtroFicha(int filtro) {
    //     return data.findByCodigo_ficha(filtro);
    // }

}
