package org.redsaberes.repository;

import org.redsaberes.model.Notificacion;
import org.redsaberes.model.TipoNotificacion;

import java.util.List;

public interface NotificacionRepository extends GenericRepository<Notificacion, Integer>{
    List<Notificacion> findByUsuarioReceptorId(Integer usuarioId);
    boolean existsByUsuarioEmisorAndCursoAndTipo(Integer id, Integer id1, TipoNotificacion tipoNotificacion);

    boolean existsByUsuarioEmisorAndCurso(Integer usuarioEmisorId, Integer cursoId);

    List<Notificacion> findUnreadByUsuarioReceptorId(Integer usuarioId);

}
