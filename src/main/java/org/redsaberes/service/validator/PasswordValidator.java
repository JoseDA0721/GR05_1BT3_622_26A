package org.redsaberes.service.validator;

public final class PasswordValidator {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    private PasswordValidator() {}

    public static boolean esSegura(String contrasena) {
        return contrasena != null && contrasena.matches(PASSWORD_REGEX);
    }
}
