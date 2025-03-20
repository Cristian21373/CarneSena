package com.proyecto.carnesena.service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<ficha> filtroFicha(int codigoFicha) {
        return data.filtroFicha(codigoFicha);
    }

    @Override
    public Optional<ficha> findOne(String id) {
        Optional<ficha> ficha=data.findById(id);
		return ficha;
    }

    @Override
    public int delete(String id) {
        data.deleteById(id);
		return 1;
    }

}
