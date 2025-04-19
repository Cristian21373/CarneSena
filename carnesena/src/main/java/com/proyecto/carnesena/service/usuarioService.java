package com.proyecto.carnesena.service;

import java.util.List;
import java.util.Optional;

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
        List<usuario> ListUsuario = (List<usuario>) data.findAll();
        return ListUsuario;
    }

    @Override
    public Optional<usuario> findOne(String id) {
        Optional<usuario> usuario = data.findById(id);
        return usuario;
    }

    @Override
    public int delete(String id) {
        data.deleteById(id);
        return 1;
    }

    @Override
    public List<usuario> filtroUsuario(String filtro) {
        List<usuario> ListUsuario = data.filtroUsuario(filtro);
        return ListUsuario;
    }

    @Override
    public Optional<usuario> findByNis(int nis) {
        return data.findByNis(nis);
    }

}
