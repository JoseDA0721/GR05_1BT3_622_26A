package org.redsaberes.service;

import org.redsaberes.model.Usuario;
import org.redsaberes.service.exception.ServiceValidationException;

public interface UsuarioService {

    Usuario registrarUsuario(String nombre,
                             String correo,
                             String contrasena,
                             String confirmarContrasena,
                             boolean aceptaTerminos) throws ServiceValidationException;
}

