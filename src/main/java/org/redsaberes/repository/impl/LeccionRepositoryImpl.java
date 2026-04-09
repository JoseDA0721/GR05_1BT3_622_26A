package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.Leccion;
import org.redsaberes.repository.LeccionRepository;
import org.redsaberes.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class LeccionRepositoryImpl implements LeccionRepository {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    @Override
    public void save(Leccion leccion) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.persist(leccion);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    @Override
    public void update(Leccion leccion) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.merge(leccion);
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
            Leccion leccion = session.get(Leccion.class, id);
            if (leccion != null) {
                session.remove(leccion);
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
    public Optional<Leccion> findById(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            Leccion leccion = session.get(Leccion.class, id);
            return Optional.ofNullable(leccion);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Leccion> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query<Leccion> query = session.createQuery("FROM Leccion", Leccion.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Leccion> findByModuloId(Integer moduloId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Leccion> query = session.createQuery(
                "FROM Leccion l WHERE l.modulo.id = :moduloId",
                Leccion.class
            );
            query.setParameter("moduloId", moduloId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}

