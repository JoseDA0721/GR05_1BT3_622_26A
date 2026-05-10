package org.redsaberes.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {

    @Test
    void shouldReturnTrueForValidEmail() {
        EmailValidator validator = new EmailValidator();

        boolean result = validator.isValid("test@gmail.com");

        assertTrue(result);
    }
}
