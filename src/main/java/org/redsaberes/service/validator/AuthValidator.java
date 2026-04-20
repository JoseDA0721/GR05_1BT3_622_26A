package org.redsaberes.service.validator;

import org.redsaberes.service.exception.ServiceValidationException;

public final class AuthValidator {

    private AuthValidator() {
    }

    public static void validarLogin(String correo, String contrasena) throws ServiceValidationException {
        if (isBlank(correo) || isBlank(contrasena)) {
            throw new ServiceValidationException("Todos los campos son obligatorios");
        }

        if (!isValidEmail(correo)) {
            throw new ServiceValidationException("Formato de correo inválido");
        }
    }

    private static boolean isBlank(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
}

