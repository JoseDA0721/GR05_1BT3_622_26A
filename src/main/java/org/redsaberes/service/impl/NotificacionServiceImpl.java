package org.redsaberes.service.impl;

import org.redsaberes.model.*;
import org.redsaberes.repository.NotificacionRepository;
import org.redsaberes.service.NotificacionService;
import org.redsaberes.service.exception.ServiceValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
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
        if (usuarioEmisor == null) {
            throw new ServiceValidationException("Usuario emisor no válido");
        }
        if  (usuarioReceptor == null) {
            throw new ServiceValidationException("Usuario receptor no válido");
        }

        if(curso == null) {
            throw new ServiceValidationException("Curso no válido");
        }

        if(curso.getUsuario()==null){
            throw new ServiceValidationException("Curso sin propietario");
        }

        if(curso.getUsuario().equals(usuarioEmisor)){
            throw new ServiceValidationException("El usuario emisor no puede ser el dueño del curso");
        }

        if(notificacionRepository.existsByUsuarioEmisorAndCurso(usuarioEmisor.getId(), curso.getId())){
            throw new ServiceValidationException("No se puede duplicar notificaciones");
        }

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
        List<Notificacion> allNotificaciones = notificacionRepository.findByUsuarioReceptorId(usuarioReceptorId);
        List<Notificacion> unreadNotificaciones = new ArrayList<>();
        for(Notificacion notificacion : allNotificaciones){
            if(notificacion.getEstado().equals(EstadoNotificacion.NO_LEIDO)){
                unreadNotificaciones.add(notificacion);
            }
        }
        return unreadNotificaciones;
    }

    @Override
    public Optional<Notificacion> getNotificacionById(Integer notificacionId) throws ServiceValidationException {
        return notificacionRepository.findById(notificacionId);
    }

}
