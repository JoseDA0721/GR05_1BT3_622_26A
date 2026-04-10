package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class CursoRepositoryImpl implements CursoRepository {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    @Override
    public void save(Curso curso) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.persist(curso);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    @Override
    public void update(Curso curso) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.merge(curso);
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
            Curso curso = session.get(Curso.class, id);
            if (curso != null) {
                session.remove(curso);
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
    public Optional<Curso> findById(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            Curso curso = session.get(Curso.class, id);
            return Optional.ofNullable(curso);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Curso> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query<Curso> query = session.createQuery("FROM Curso", Curso.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Curso> findByUsuarioId(Integer usuarioId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Curso> query = session.createQuery(
                "FROM Curso c WHERE c.usuario.id = :usuarioId ORDER BY c.id DESC",
                Curso.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Curso> findByCategoria(String categoria) {
        Session session = sessionFactory.openSession();
        try {
            Query<Curso> query = session.createQuery(
                "FROM Curso c WHERE c.categoria = :categoria",
                Curso.class
            );
            query.setParameter("categoria", categoria);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Curso> findByEstado(String estado) {
        Session session = sessionFactory.openSession();
        try {
            // Convertir string a enum
            EstadoCurso estadoEnum = EstadoCurso.valueOf(estado.toUpperCase());

            Query<Curso> query = session.createQuery(
                "FROM Curso c WHERE c.estado = :estado",
                Curso.class
            );
            query.setParameter("estado", estadoEnum);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Curso> findByUsuarioIdWithRelations(Integer usuarioId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Curso> query = session.createQuery(
                "SELECT DISTINCT c FROM Curso c " +
                "LEFT JOIN FETCH c.modulos " +
                "LEFT JOIN FETCH c.likes " +
                "WHERE c.usuario.id = :usuarioId " +
                "ORDER BY c.id DESC",
                Curso.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public Optional<Curso> findByIdWithRelations(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            Query<Curso> query = session.createQuery(
                "SELECT DISTINCT c FROM Curso c " +
                "LEFT JOIN FETCH c.modulos " +
                "LEFT JOIN FETCH c.likes " +
                "WHERE c.id = :id",
                Curso.class
            );
            query.setParameter("id", id);
            List<Curso> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            session.close();
        }
    }
}
