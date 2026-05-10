package org.redsaberes.util;

import java.util.regex.Pattern;

public class EmailValidator {

    // Regex estándar práctica para validar correos de formato usuario@dominio.tld
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public boolean isValid(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
