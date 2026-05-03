package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.redsaberes.model.MatchCurso;
import org.redsaberes.repository.MatchCursoRepository;

import java.util.List;

public class MatchCursoRepositoryImpl extends GenericRepositoryImpl<MatchCurso, Integer> implements MatchCursoRepository {


    public MatchCursoRepositoryImpl() {
        super(MatchCurso.class);
    }

    @Override
    public List<MatchCurso> findByCursoId(Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<MatchCurso> query = session.createQuery(
                    "SELECT m FROM MatchCurso m " +
                            "JOIN FETCH m.curso " +
                            "JOIN FETCH m.estudiante " +
                            "JOIN FETCH m.creador " +
                            "WHERE m.curso.id = :cursoId " +
                            "ORDER BY m.id DESC",
                    MatchCurso.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        }
    }

    @Override
    public List<MatchCurso> findByCreadorId(Integer creadorId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<MatchCurso> query = session.createQuery(
                    "SELECT m FROM MatchCurso m " +
                            "JOIN FETCH m.curso " +
                            "JOIN FETCH m.estudiante " +
                            "JOIN FETCH m.creador " +
                            "WHERE m.creador.id = :creadorId " +
                            "ORDER BY m.id DESC",
                    MatchCurso.class
            );
            query.setParameter("creadorId", creadorId);
            return query.getResultList();
        }
    }

    @Override
    public List<MatchCurso> findByEstudianteId(Integer estudianteId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<MatchCurso> query = session.createQuery(
                    "SELECT m FROM MatchCurso m " +
                            "JOIN FETCH m.curso " +
                            "JOIN FETCH m.estudiante " +
                            "JOIN FETCH m.creador " +
                            "WHERE m.estudiante.id = :estudianteId " +
                            "ORDER BY m.id DESC",
                    MatchCurso.class
            );
            query.setParameter("estudianteId", estudianteId);
            return query.getResultList();
        }
    }

    @Override
    public boolean existsByCursoAndUsuario(Integer cursoId, Integer usuarioId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(m) FROM MatchCurso m WHERE m.curso.id = :cursoId AND m.estudiante.id = :usuarioId",
                    Long.class
            );
            query.setParameter("cursoId", cursoId);
            query.setParameter("usuarioId", usuarioId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public Long countByCursoId(Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(m) FROM MatchCurso m WHERE m.curso.id = :cursoId",
                    Long.class
            );
            query.setParameter("cursoId", cursoId);
            return query.uniqueResult();
        }
    }

    @Override
    public Long countByCreadorId(Integer creadorId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(m) FROM MatchCurso m WHERE m.creador.id = :creadorId",
                    Long.class
            );
            query.setParameter("creadorId", creadorId);
            return query.uniqueResult();
        }
    }

    @Override
    public Long countByEstudianteId(Integer estudianteId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(m) FROM MatchCurso m WHERE m.estudiante.id = :estudianteId",
                    Long.class
            );
            query.setParameter("estudianteId", estudianteId);
            return query.uniqueResult();
        }
    }
}

