package org.redsaberes.service;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.ReviewCreationResult;

public interface ReviewService {
	void validateCommentLength(String comment);

	boolean containsOffensiveContent(String comment);

	/**
	 * Crear una reseña con validación completa.
	 * @return ReviewCreationResult con outcome del proceso
	 */
	ReviewCreationResult crearResena(Integer estrellas, String comentario, Usuario usuario, Curso curso);
}


