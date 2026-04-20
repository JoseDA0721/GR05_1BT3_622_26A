package org.redsaberes.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.validator.ReviewValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ReviewValidatorTest {

    private ReviewValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ReviewValidator();
    }

    // 1.1 Test Unitario 2: Integridad de Referencias
    @Test
    void debeFallarSiIdentificadoresDeUsuarioOCursoSonInvalidos() {
        Usuario usuarioValido = new Usuario(); usuarioValido.setId(1);
        Curso cursoValido = new Curso(); cursoValido.setId(1);

        Usuario usuarioIdNulo = new Usuario(); usuarioIdNulo.setId(null);
        Usuario usuarioIdNegativo = new Usuario(); usuarioIdNegativo.setId(-1);

        Curso cursoIdNulo = new Curso(); cursoIdNulo.setId(null);
        Curso cursoIdNegativo = new Curso(); cursoIdNegativo.setId(-1);

        // Pruebas de Usuario
        assertFalse(validator.isValid(5, "OK", usuarioIdNulo, cursoValido), "Falla si usuario ID es nulo");
        assertFalse(validator.isValid(5, "OK", usuarioIdNegativo, cursoValido), "Falla si usuario ID es negativo");
        assertFalse(validator.isValid(5, "OK", null, cursoValido), "Falla si el objeto usuario es nulo");

        // Pruebas de Curso
        assertFalse(validator.isValid(5, "OK", usuarioValido, cursoIdNulo), "Falla si curso ID es nulo");
        assertFalse(validator.isValid(5, "OK", usuarioValido, cursoIdNegativo), "Falla si curso ID es negativo");
        assertFalse(validator.isValid(5, "OK", usuarioValido, null), "Falla si el objeto curso es nulo");
    }
}