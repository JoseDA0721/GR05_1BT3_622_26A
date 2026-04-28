package org.redsaberes.service.validator;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;

public class ReviewValidator {

    public boolean isValid(Integer estrellas, String comentario, Usuario usuario, Curso curso) {
        // Fase GREEN: Código funcional pero sin optimizar (todo en el mismo bloque)
        if (estrellas == null || estrellas < 1 || estrellas > 5) {
            return false;
        }
        if (usuario == null || usuario.getId() == null || usuario.getId() <= 0) {
            return false;
        }
        if (curso == null || curso.getId() == null || curso.getId() <= 0) {
            return false;
        }
        return true;
    }
}