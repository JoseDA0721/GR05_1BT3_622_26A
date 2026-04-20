package org.redsaberes.service;

import org.redsaberes.model.Usuario;
import org.redsaberes.service.exception.ServiceValidationException;

public interface AuthService {

    Usuario autenticar(String correo, String contrasena) throws ServiceValidationException;
}

