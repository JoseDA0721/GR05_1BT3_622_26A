package org.redsaberes.service;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Notificacion;
import org.redsaberes.model.TipoNotificacion;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.exception.ServiceValidationException;

import java.util.List;
import java.util.Optional;

public interface NotificacionService {
    Notificacion createNotification(
            Usuario usuarioReceptor,
            Usuario usuarioEmisor,
            Curso curso,
            TipoNotificacion tipo
    ) throws ServiceValidationException;

    void markAsRead(Notificacion notificacion) throws ServiceValidationException;

    List<Notificacion> getUnread(Integer usuarioReceptorId) throws ServiceValidationException;

    Optional<Notificacion> getNotificacionById(Integer notificacionId) throws ServiceValidationException;
}
