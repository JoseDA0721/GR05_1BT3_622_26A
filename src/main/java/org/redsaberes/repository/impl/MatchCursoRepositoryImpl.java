package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.Curso;
import org.redsaberes.model.MatchCurso;
import org.redsaberes.model.Usuario;
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
            if (matchCurso.getCurso() != null && matchCurso.getCurso().getId() != null) {
                Curso cursoRef = session.getReference(Curso.class, matchCurso.getCurso().getId());
                matchCurso.setCurso(cursoRef);
            }
            if (matchCurso.getEstudiante() != null && matchCurso.getEstudiante().getId() != null) {
                Usuario estudianteRef = session.getReference(Usuario.class, matchCurso.getEstudiante().getId());
                matchCurso.setEstudiante(estudianteRef);
            }
            if (matchCurso.getCreador() != null && matchCurso.getCreador().getId() != null) {
                Usuario creadorRef = session.getReference(Usuario.class, matchCurso.getCreador().getId());
                matchCurso.setCreador(creadorRef);
            }
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
            Query<MatchCurso> query = session.createQuery(
                "SELECT m FROM MatchCurso m " +
                    "JOIN FETCH m.curso " +
                    "JOIN FETCH m.estudiante " +
                    "JOIN FETCH m.creador " +
                    "ORDER BY m.id DESC",
                MatchCurso.class
            );
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    @Override
    public List<MatchCurso> findByCursoId(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
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
        } finally {
            session.close();
        }
    }

    @Override
    public List<MatchCurso> findByCreadorId(Integer creadorId) {
        Session session = sessionFactory.openSession();
        try {
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
        } finally {
            session.close();
        }
    }

    @Override
    public List<MatchCurso> findByEstudianteId(Integer estudianteId) {
        Session session = sessionFactory.openSession();
        try {
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
        } finally {
            session.close();
        }
    }

    @Override
    public boolean existsByCursoAndUsuario(Integer cursoId, Integer usuarioId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM MatchCurso m WHERE m.curso.id = :cursoId AND m.estudiante.id = :usuarioId",
                Long.class
            );
            query.setParameter("cursoId", cursoId);
            query.setParameter("usuarioId", usuarioId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }

    @Override
    public Long countByCursoId(Integer cursoId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM MatchCurso m WHERE m.curso.id = :cursoId",
                Long.class
            );
            query.setParameter("cursoId", cursoId);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public Long countByCreadorId(Integer creadorId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM MatchCurso m WHERE m.creador.id = :creadorId",
                Long.class
            );
            query.setParameter("creadorId", creadorId);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public Long countByEstudianteId(Integer estudianteId) {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM MatchCurso m WHERE m.estudiante.id = :estudianteId",
                Long.class
            );
            query.setParameter("estudianteId", estudianteId);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}

