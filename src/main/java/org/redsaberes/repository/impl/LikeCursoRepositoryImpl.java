package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.LikeCurso;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class LikeCursoRepositoryImpl implements LikeCursoRepository {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    @Override
    public void save(LikeCurso likeCurso) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.persist(likeCurso);
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
            LikeCurso likeCurso = session.get(LikeCurso.class, id);
            if (likeCurso != null) {
                session.remove(likeCurso);
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
    public Optional<LikeCurso> findById(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            LikeCurso likeCurso = session.get(LikeCurso.class, id);
            return Optional.ofNullable(likeCurso);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<LikeCurso> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query<LikeCurso> query = session.createQuery("FROM LikeCurso", LikeCurso.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<LikeCurso> findByCursoId(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
            Query<LikeCurso> query = session.createQuery(
                "FROM LikeCurso l WHERE l.curso.id = :cursoId",
                LikeCurso.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<LikeCurso> findByUsuarioId(Integer usuarioId) {
        Session session = sessionFactory.openSession();
        try {
            Query<LikeCurso> query = session.createQuery(
                "FROM LikeCurso l WHERE l.usuario.id = :usuarioId",
                LikeCurso.class
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
                "SELECT COUNT(l) FROM LikeCurso l WHERE l.curso.id = :cursoId",
                Long.class
            );
            query.setParameter("cursoId", cursoId);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}

