package org.redsaberes.service;

import org.junit.jupiter.api.Test;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.validator.DateValidator;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateValidatorTest {

    @Test
    void deberia_lanzar_excepcion_cuando_usuario_envia_fecha() {
        // Given (dato de entrada inválido)
        LocalDateTime fechaUsuario = crearFechaFalsa();

        // When & Then (validación)
        assertThrows(ServiceValidationException.class,
                () -> DateValidator.validarFechaRegistro(fechaUsuario));
    }

    @Test
    void no_deberia_lanzar_excepcion_cuando_fecha_es_null() {
        // Given
        LocalDateTime fechaUsuario = null;

        // When & Then
        assertDoesNotThrow(
                () -> DateValidator.validarFechaRegistro(fechaUsuario));
    }

    // 🔹 Método auxiliar para evitar duplicación
    private LocalDateTime crearFechaFalsa() {
        return LocalDateTime.of(2020, 1, 1, 0, 0);
    }
}