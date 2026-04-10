package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.Modulo;
import org.redsaberes.repository.ModuloRepository;
import org.redsaberes.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class ModuloRepositoryImpl implements ModuloRepository {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    @Override
    public void save(Modulo modulo) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.persist(modulo);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    @Override
    public void update(Modulo modulo) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.merge(modulo);
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
            Modulo modulo = session.get(Modulo.class, id);
            if (modulo != null) {
                session.remove(modulo);
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
    public Optional<Modulo> findById(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            Modulo modulo = session.get(Modulo.class, id);
            return Optional.ofNullable(modulo);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Modulo> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query<Modulo> query = session.createQuery("FROM Modulo ORDER BY orden", Modulo.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Modulo> findByCursoId(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Modulo> query = session.createQuery(
                "FROM Modulo m WHERE m.curso.id = :cursoId ORDER BY m.orden ASC",
                Modulo.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Modulo> findByCursoIdWithLecciones(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Modulo> query = session.createQuery(
                "FROM Modulo m LEFT JOIN FETCH m.lecciones " +
                "WHERE m.curso.id = :cursoId ORDER BY m.orden",
                Modulo.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}

