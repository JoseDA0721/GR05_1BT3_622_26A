package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.repository.CursoRepository;

import java.util.List;
import java.util.Optional;

public class CursoRepositoryImpl extends GenericRepositoryImpl<Curso, Integer> implements CursoRepository {
    
    public CursoRepositoryImpl() {
        super(Curso.class);
    }
    
    @Override
    public List<Curso> findByUsuarioId(Integer usuarioId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Curso> query = session.createQuery(
                    "FROM Curso c WHERE c.usuario.id = :usuarioId ORDER BY c.id DESC",
                    Curso.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        }
    }
    
    @Override
    public List<Curso> findByCategoria(String categoria) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Curso> query = session.createQuery(
                    "FROM Curso c WHERE c.categoria = :categoria",
                    Curso.class
            );
            query.setParameter("categoria", categoria);
            return query.getResultList();
        }
    }
    
    @Override
    public List<Curso> findByEstado(String estado) {
        try (Session session = getSessionFactory().openSession()) {
            // Convertir string a enum
            EstadoCurso estadoEnum = EstadoCurso.valueOf(estado.toUpperCase());

            Query<Curso> query = session.createQuery(
                    "FROM Curso c WHERE c.estado = :estado",
                    Curso.class
            );
            query.setParameter("estado", estadoEnum);
            return query.getResultList();
        }
    }
    
    @Override
    public List<Curso> findByUsuarioIdWithRelations(Integer usuarioId) {
        try (Session session = getSessionFactory().openSession()) {
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
        }
    }
    
    @Override
    public Optional<Curso> findByIdWithRelations(Integer id) {
        try (Session session = getSessionFactory().openSession()) {
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
        }
    }
}
