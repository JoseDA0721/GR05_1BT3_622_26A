package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.redsaberes.model.EstadoNotificacion;
import org.redsaberes.model.Notificacion;
import org.redsaberes.model.TipoNotificacion;
import org.redsaberes.repository.NotificacionRepository;

import java.util.List;

public class NotificacionRepositoryImpl extends GenericRepositoryImpl<Notificacion, Integer> implements NotificacionRepository {
    public NotificacionRepositoryImpl() {
        super(Notificacion.class);
    }

    @Override
    public List<Notificacion> findByUsuarioReceptorId(Integer usuarioId) {
        try(Session session = getSessionFactory().openSession()){
            Query<Notificacion> query = session.createQuery(
                    "FROM Notificacion n WHERE n.usuarioReceptor.id = :usuarioId", Notificacion.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        }
    }

    @Override
    public boolean existsByUsuarioEmisorAndCursoAndTipo(Integer usuarioEmisorId, Integer cursoId, TipoNotificacion tipoNotificacion) {
        try(Session session = getSessionFactory().openSession()){
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(n) " +
                            "FROM Notificacion n " +
                            "WHERE n.usuarioEmisor.id = :usuarioEmisorId " +
                            "AND n.curso.id = :cursoId " +
                            "AND n.tipo = :tipoNotificacion",
                    Long.class);
            query.setParameter("usuarioEmisorId", usuarioEmisorId);
            query.setParameter("cursoId", cursoId);
            query.setParameter("tipoNotificacion", tipoNotificacion);
            Long count = query.uniqueResult();
            return count > 0;
        }
    }

    @Override
    public boolean existsByUsuarioEmisorAndCurso(Integer usuarioEmisorId, Integer cursoId) {
        try(Session session = getSessionFactory().openSession()){
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(n) FROM Notificacion n WHERE n.usuarioEmisor.id = :usuarioEmisorId AND n.curso.id = :cursoId",
                    Long.class);
            query.setParameter("usuarioEmisorId", usuarioEmisorId);
            query.setParameter("cursoId", cursoId);
            Long count = query.uniqueResult();
            return count > 0;
        }
    }

    @Override
    public List<Notificacion> findUnreadByUsuarioReceptorId(Integer usuarioId) {
        try(Session session = getSessionFactory().openSession()){
            Query<Notificacion> query = session.createQuery(
                    "FROM Notificacion n WHERE n.usuarioReceptor.id = :usuarioId AND n.estado = :estado",
                    Notificacion.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("estado", EstadoNotificacion.NO_LEIDO);
            return query.getResultList();
        }
    }

}
