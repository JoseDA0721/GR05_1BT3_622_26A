package org.redsaberes.service;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;

public interface ReviewService {
	void validateCommentLength(String comment);

	boolean containsOffensiveContent(String comment);
    void crearResena(Integer estrellas, String comentario, Usuario usuario, Curso curso);
}
