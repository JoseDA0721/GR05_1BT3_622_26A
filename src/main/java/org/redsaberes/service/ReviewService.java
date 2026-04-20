package org.redsaberes.service;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;

public interface ReviewService {
    void crearResena(Integer estrellas, String comentario, Usuario usuario, Curso curso);
}
