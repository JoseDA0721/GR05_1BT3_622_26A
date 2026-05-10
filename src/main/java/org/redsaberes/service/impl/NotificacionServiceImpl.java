package org.redsaberes.service.impl;

import org.redsaberes.model.*;
import org.redsaberes.repository.NotificacionRepository;
import org.redsaberes.service.NotificacionService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.validator.NotificationValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public Notificacion createNotification(
            Usuario usuarioReceptor,
            Usuario usuarioEmisor,
            Curso curso,
            TipoNotificacion tipo) throws ServiceValidationException {
        // Validaciones
        NotificationValidator.validateNotificationCreation(usuarioReceptor, usuarioEmisor, curso, tipo, notificacionRepository);

        // Crear la notificación
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioReceptor(usuarioReceptor);
        notificacion.setUsuarioEmisor(usuarioEmisor);
        notificacion.setCurso(curso);
        notificacion.setTipo(tipo);
        notificacion.setEstado(EstadoNotificacion.NO_LEIDO);
        notificacion.setFechaCreacion(LocalDate.now());
        notificacionRepository.save(notificacion);
        return notificacion;
    }


    @Override
    public void markAsRead(Notificacion notificacion){
        notificacion.setEstado(EstadoNotificacion.LEIDO);
        notificacionRepository.update(notificacion);
    }

    @Override
    public List<Notificacion> getUnread(Integer usuarioReceptorId){
        return notificacionRepository.findUnreadByUsuarioReceptorId(usuarioReceptorId);
    }

    @Override
    public Optional<Notificacion> getNotificacionById(Integer notificacionId) throws ServiceValidationException {
        return notificacionRepository.findById(notificacionId);
    }

    @Override
    public String getDescripcion(Notificacion notificacion) {
        return notificacion.getDescripcion();
    }

    @Override
    public List<Notificacion> getAllNotifications(Integer usuarioReceptorId) {
        return notificacionRepository.findByUsuarioReceptorId(usuarioReceptorId);
    }


}
