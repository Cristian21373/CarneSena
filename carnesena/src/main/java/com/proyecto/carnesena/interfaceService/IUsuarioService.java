package com.proyecto.carnesena.interfaceService;

import java.util.List;
import java.util.Optional;

import com.proyecto.carnesena.model.usuario;

public interface IUsuarioService {
    public String save(usuario usuario);

    public List<usuario> findAll();

    public Optional<usuario> findOne(String id);

    public int delete(String id);

    public List<usuario> filtroUsuario(String filtro);

    public Optional<usuario> findByNis(int nis);

    int contarUsuariosPorFicha(String idFicha);

}
