package org.redsaberes.service.validator;

import org.redsaberes.model.Curso;
import org.redsaberes.model.TipoNotificacion;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.NotificacionRepository;
import org.redsaberes.service.exception.ServiceValidationException;

public final class NotificationValidator {

    private NotificationValidator() {
    }

    public static void validateNotificationCreation(Usuario usuarioReceptor,
                                                    Usuario usuarioEmisor,
                                                    Curso curso,
                                                    TipoNotificacion tipoNotificacion,
                                                    NotificacionRepository notificacionRepository) throws ServiceValidationException {
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

        if(notificacionRepository.existsByUsuarioEmisorAndCursoAndTipo(usuarioEmisor.getId(), curso.getId(), tipoNotificacion)){
            throw new ServiceValidationException("No se puede duplicar notificaciones");
        }
    }
}
