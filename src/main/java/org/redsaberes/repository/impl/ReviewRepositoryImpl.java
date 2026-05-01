package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.Resena;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.util.HibernateUtil;

import java.util.Collections;
import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void save(Resena resena) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.merge(resena);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Resena> findByCursoId(Integer cursoId) {
        if (cursoId == null) {
            return Collections.emptyList();
        }

        try (Session session = sessionFactory.openSession()) {
            Query<Resena> query = session.createQuery(
                    "SELECT r FROM Resena r " +
                            "LEFT JOIN FETCH r.usuario " +
                            "WHERE r.curso.id = :cursoId " +
                            "ORDER BY r.fecha DESC, r.id DESC",
                    Resena.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        }
    }

    @Override
    public Double averageRatingByCursoId(Integer cursoId) {
        if (cursoId == null) return null;
        try (Session session = sessionFactory.openSession()) {
            Query<Double> query = session.createQuery(
                    "SELECT AVG(CAST(r.estrellas AS double)) FROM Resena r " +
                            "WHERE r.curso.id = :cursoId",
                    Double.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getSingleResult();
        }
    }

    @Override
    public boolean existsReviewByUserIdAndCursoId(Integer usuarioId, Integer cursoId) {
        if(usuarioId == null || cursoId == null) return false;
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(r) FROM Resena r " +
                            "WHERE r.usuario.id = :usuarioId AND r.curso.id = :cursoId",
                    Long.class
            );
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("cursoId", cursoId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }
}
