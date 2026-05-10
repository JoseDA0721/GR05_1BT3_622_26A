package org.redsaberes.service.exception;

/**
 * Excepción lanzada cuando se intenta registrar un usuario con un nombre que ya existe en el sistema.
 */
public class NameAlreadyTakenException extends ServiceValidationException {

    public NameAlreadyTakenException(String message) {
        super(message);
    }

    public NameAlreadyTakenException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}

