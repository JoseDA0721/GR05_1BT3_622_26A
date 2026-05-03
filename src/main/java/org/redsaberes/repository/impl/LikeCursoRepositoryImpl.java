package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.redsaberes.model.LikeCurso;
import org.redsaberes.repository.LikeCursoRepository;

import java.util.List;

public class LikeCursoRepositoryImpl extends GenericRepositoryImpl<LikeCurso, Integer> implements LikeCursoRepository {
    
    public LikeCursoRepositoryImpl(){
        super(LikeCurso.class);
    }
    
    @Override
    public List<LikeCurso> findByCursoId(Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<LikeCurso> query = session.createQuery(
                    "SELECT l FROM LikeCurso l " +
                            "JOIN FETCH l.usuario " +
                            "JOIN FETCH l.curso " +
                            "WHERE l.curso.id = :cursoId",
                    LikeCurso.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        }
    }
    
    @Override
    public List<LikeCurso> findByUsuarioId(Integer usuarioId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<LikeCurso> query = session.createQuery(
                    "SELECT l FROM LikeCurso l " +
                            "JOIN FETCH l.curso " +
                            "WHERE l.usuario.id = :usuarioId",
                    LikeCurso.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        }
    }

    @Override
    public List<LikeCurso> findByCursoPropietarioId(Integer propietarioId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<LikeCurso> query = session.createQuery(
                    "SELECT l FROM LikeCurso l " +
                            "JOIN FETCH l.usuario " +
                            "JOIN FETCH l.curso c " +
                            "WHERE c.usuario.id = :propietarioId " +
                            "ORDER BY l.id DESC",
                    LikeCurso.class
            );
            query.setParameter("propietarioId", propietarioId);
            return query.getResultList();
        }
    }

    @Override
    public boolean existsByUsuarioAndCurso(Integer usuarioId, Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(l) FROM LikeCurso l WHERE l.usuario.id = :usuarioId AND l.curso.id = :cursoId",
                    Long.class
            );
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("cursoId", cursoId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public Long countByCursoId(Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(l) FROM LikeCurso l WHERE l.curso.id = :cursoId",
                    Long.class
            );
            query.setParameter("cursoId", cursoId);
            return query.uniqueResult();
        }
    }

    @Override
    public Long countByCursoPropietarioId(Integer propietarioId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(l) FROM LikeCurso l WHERE l.curso.usuario.id = :propietarioId",
                    Long.class
            );
            query.setParameter("propietarioId", propietarioId);
            return query.uniqueResult();
        }
    }
}

