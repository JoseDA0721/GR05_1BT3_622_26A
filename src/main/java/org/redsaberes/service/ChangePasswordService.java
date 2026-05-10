package org.redsaberes.service;

import org.redsaberes.service.exception.ServiceValidationException;

public interface ChangePasswordService {

    boolean cambiarContrasena(Integer usuarioId,
                              String contrasenaActual,
                              String nuevaContrasena,
                              String confirmarContrasena)
            throws ServiceValidationException;
}
