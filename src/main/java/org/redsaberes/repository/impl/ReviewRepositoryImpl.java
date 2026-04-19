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

        Session session = sessionFactory.openSession();
        try {
            Query<Resena> query = session.createQuery(
                    "SELECT r FROM Resena r " +
                            "LEFT JOIN FETCH r.usuario " +
                            "WHERE r.curso.id = :cursoId " +
                            "ORDER BY r.fecha DESC, r.id DESC",
                    Resena.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}
