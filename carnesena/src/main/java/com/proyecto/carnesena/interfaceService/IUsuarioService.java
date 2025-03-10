package com.proyecto.carnesena.interfaceService;

import java.util.List;

import com.proyecto.carnesena.model.usuario;

public interface IUsuarioService {
    public String save(usuario usuario);    
    public List<usuario> findAll();
    // public List<usuario> filtrousuario(int filtro);

}
