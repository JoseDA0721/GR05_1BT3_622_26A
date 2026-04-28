package org.redsaberes.service.validator;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;

public class ReviewValidator {

    public boolean isValid(Integer estrellas, String comentario, Usuario usuario, Curso curso) {
        // Fase REFACTOR: Código limpio utilizando métodos privados reutilizables
        return esEstrellasValida(estrellas) && esReferenciaValida(usuario) && esReferenciaValida(curso);
    }

    private boolean esEstrellasValida(Integer estrellas) {
        return estrellas != null && estrellas >= 1 && estrellas <= 5;
    }

    private boolean esReferenciaValida(Usuario usuario) {
        return usuario != null && idValido(usuario.getId());
    }

    private boolean esReferenciaValida(Curso curso) {
        return curso != null && idValido(curso.getId());
    }

    private boolean idValido(Integer id) {
        return id != null && id > 0;
    }
}