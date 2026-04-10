package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.Inscripcion;
import org.redsaberes.repository.InscripcionRepository;
import org.redsaberes.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class InscripcionRepositoryImpl implements InscripcionRepository {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    @Override
    public void save(Inscripcion inscripcion) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.persist(inscripcion);
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
            Inscripcion inscripcion = session.get(Inscripcion.class, id);
            if (inscripcion != null) {
                session.remove(inscripcion);
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
    public Optional<Inscripcion> findById(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            Inscripcion inscripcion = session.get(Inscripcion.class, id);
            return Optional.ofNullable(inscripcion);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Inscripcion> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query<Inscripcion> query = session.createQuery("FROM Inscripcion", Inscripcion.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Inscripcion> findByCursoId(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Inscripcion> query = session.createQuery(
                "FROM Inscripcion i WHERE i.curso.id = :cursoId",
                Inscripcion.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Inscripcion> findByUsuarioId(Integer usuarioId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Inscripcion> query = session.createQuery(
                "FROM Inscripcion i WHERE i.usuario.id = :usuarioId",
                Inscripcion.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public Long countByCursoId(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(i) FROM Inscripcion i WHERE i.curso.id = :cursoId",
                Long.class
            );
            query.setParameter("cursoId", cursoId);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}

