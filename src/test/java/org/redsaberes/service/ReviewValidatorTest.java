package org.redsaberes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.validator.ReviewValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ReviewValidatorTest {

    private ReviewValidator validator;
    private Usuario usuarioValido;
    private Curso cursoValido;

    @BeforeEach
    void setUp() {
        validator = new ReviewValidator();

        usuarioValido = new Usuario();
        usuarioValido.setId(1);

        cursoValido = new Curso();
        cursoValido.setId(1);
    }

    // TEST 1: Rango de Calificación
    @Test
    void debeFallarSiEstrellasEstanFueraDeRango() {
        assertFalse(validator.isValid(0, "Buen curso", usuarioValido, cursoValido), "Debe fallar con 0 estrellas");
        assertFalse(validator.isValid(6, "Buen curso", usuarioValido, cursoValido), "Debe fallar con 6 estrellas");
        assertFalse(validator.isValid(null, "Buen curso", usuarioValido, cursoValido), "Debe fallar si estrellas es null");
    }

    // TEST 2: Integridad de Identificadores
    @Test
    void debeFallarSiIdentificadoresSonNulosONegativos() {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setId(-1); // ID Negativo

        Curso cursoInvalido = new Curso();
        cursoInvalido.setId(null); // ID Nulo

        assertFalse(validator.isValid(5, "OK", usuarioInvalido, cursoValido), "Debe fallar si ID de usuario es negativo");
        assertFalse(validator.isValid(5, "OK", null, cursoValido), "Debe fallar si usuario es null");
        assertFalse(validator.isValid(5, "OK", usuarioValido, cursoInvalido), "Debe fallar si ID de curso es nulo");
    }
}