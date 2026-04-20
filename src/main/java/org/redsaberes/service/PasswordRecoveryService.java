package org.redsaberes.service;

import org.redsaberes.service.exception.ServiceValidationException;

public interface PasswordRecoveryService {

    void solicitarRestablecimiento(String correo, String baseUrl) throws ServiceValidationException;

    void restablecerContrasena(String token,
                               String contrasena,
                               String confirmarContrasena) throws ServiceValidationException;
}

