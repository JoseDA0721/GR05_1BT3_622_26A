package org.redsaberes.service;

import org.redsaberes.service.validator.PasswordValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {

    @Test
    void debeAceptarContrasenaConMinimoOchoCaracteresMayusculaYNumero() {
        assertTrue(PasswordValidator.esSegura("Password1"));
    }

    @Test
    void debeRechazarContrasenaConMenosDeOchoCaracteres() {
        assertFalse(PasswordValidator.esSegura("Pass1"));
    }

    @Test
    void debeRechazarContrasenaSinMayuscula() {
        assertFalse(PasswordValidator.esSegura("password1"));
    }

    @Test
    void debeRechazarContrasenaSinNumero() {
        assertFalse(PasswordValidator.esSegura("Password"));
    }

    @Test
    void debeRechazarContrasenaNulaOVacia() {
        assertFalse(PasswordValidator.esSegura(null));
        assertFalse(PasswordValidator.esSegura(""));
        assertFalse(PasswordValidator.esSegura("   "));
    }
}

