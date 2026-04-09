package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.MatchCurso;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class MatchCursoRepositoryImpl implements MatchCursoRepository {

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void save(MatchCurso matchCurso) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.persist(matchCurso);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            MatchCurso matchCurso = session.get(MatchCurso.class, id);
            if (matchCurso != null) {
                session.remove(matchCurso);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<MatchCurso> findById(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            MatchCurso matchCurso = session.get(MatchCurso.class, id);
            return Optional.ofNullable(matchCurso);
        } finally {
            session.close();
        }
    }

    @Override
    public List<MatchCurso> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query<MatchCurso> query = session.createQuery("FROM MatchCurso", MatchCurso.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    @Override
    public List<MatchCurso> findByCurso(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
            Query<MatchCurso> query = session.createQuery(
                "FROM MatchCurso m WHERE m.curso1.id = :cursoId OR m.curso2.id = :cursoId",
                MatchCurso.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    @Override
    public Long countByCurso(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM MatchCurso m WHERE m.curso1.id = :cursoId OR m.curso2.id = :cursoId",
                Long.class
            );
            query.setParameter("cursoId", cursoId);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}

