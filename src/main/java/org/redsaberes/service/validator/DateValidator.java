package org.redsaberes.service.validator;

import org.redsaberes.service.exception.ServiceValidationException;
import java.time.LocalDateTime;

public final class DateValidator {

    private DateValidator() {
    }

    public static void validarFechaRegistro(LocalDateTime fechaUsuario)
            throws ServiceValidationException {

        // ❌ No permite que el usuario envíe fecha
        if (fechaUsuario != null) {
            throw new ServiceValidationException(
                    "La fecha de registro no debe ser enviada por el usuario"
            );
        }
    }
}