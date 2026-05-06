package org.redsaberes.repository;

import org.redsaberes.model.Notificacion;

import java.util.List;

public interface NotificacionRepository extends GenericRepository<Notificacion, Integer>{
    List<Notificacion> findByUsuarioReceptorId(Integer usuarioId);
    boolean existsByUsuarioEmisorAndCurso(Integer id, Integer id1);
}
