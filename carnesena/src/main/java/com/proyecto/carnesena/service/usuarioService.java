package com.proyecto.carnesena.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.carnesena.interfaceService.IUsuarioService;
import com.proyecto.carnesena.interfaces.Iusuario;
import com.proyecto.carnesena.model.usuario;

@Service
public class usuarioService implements IUsuarioService {
    
    @Autowired
    private Iusuario data;

    @Override
	public String save(usuario usuario) {
		data.save(usuario);
        return usuario.getId_usuario(); 
	}

    @Override
    public List<usuario> findAll() {
        List<usuario> ListUsuario=(List<usuario>) data.findAll();
        return ListUsuario;
    }


    // @Override
    // public List<usuario> filtrousuario(int filtro) {
    //     List <usuario> listUsuario=data.filtroUsuario(filtro);
    //     return listUsuario;
    // }

}
