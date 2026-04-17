package org.redsaberes.service.validator;

import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.service.exception.ServiceValidationException;

public final class UsuarioValidator {

    private static final int MAX_NOMBRE_LENGTH = 100;
    private static final int MIN_PASSWORD_LENGTH = 6;

    private UsuarioValidator() {
    }

    public static void validarRegistro(String nombre,
                                       String correo,
                                       String contrasena,
                                       String confirmarContrasena,
                                       boolean aceptaTerminos,
                                       UsuarioRepository usuarioRepository) throws ServiceValidationException {

        if (isBlank(nombre) || isBlank(correo) || isBlank(contrasena) || isBlank(confirmarContrasena)) {
            throw new ServiceValidationException("Todos los campos son obligatorios");
        }

        if (!aceptaTerminos) {
            throw new ServiceValidationException("Debes aceptar los términos y condiciones");
        }

        if (nombre.length() > MAX_NOMBRE_LENGTH) {
            throw new ServiceValidationException("El nombre no puede exceder 100 caracteres");
        }

        if (!contrasena.equals(confirmarContrasena)) {
            throw new ServiceValidationException("Las contraseñas no coinciden");
        }

        if (contrasena.length() < MIN_PASSWORD_LENGTH) {
            throw new ServiceValidationException("La contraseña debe tener al menos 6 caracteres");
        }

        if (!isValidEmail(correo)) {
            throw new ServiceValidationException("El formato del correo electrónico no es válido");
        }

        if (usuarioRepository.existeCorreo(correo)) {
            throw new ServiceValidationException("El correo ya está registrado");
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