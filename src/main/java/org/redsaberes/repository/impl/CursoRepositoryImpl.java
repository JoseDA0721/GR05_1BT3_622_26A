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
            // Hibernate no permite hacer fetch JOIN simultáneo de múltiples "bags" (List).
            // Evitamos el MultipleBagFetchException cargando modulos y likes en la primera
            // consulta y, si hay cursos, cargamos las reseñas en una segunda consulta
            // asignándolas manualmente a cada curso.
            Query<Curso> query = session.createQuery(
                    "SELECT DISTINCT c FROM Curso c " +
                            "LEFT JOIN FETCH c.modulos " +
                            "LEFT JOIN FETCH c.likes " +
                            "WHERE c.usuario.id = :usuarioId " +
                            "ORDER BY c.id DESC",
                    Curso.class
            );
            query.setParameter("usuarioId", usuarioId);
            List<Curso> cursos = query.getResultList();

            if (cursos == null || cursos.isEmpty()) {
                return cursos;
            }

            // Recopilar ids de cursos para consultar reseñas en batch
            java.util.List<Integer> cursoIds = new java.util.ArrayList<>();
            for (Curso c : cursos) {
                if (c != null && c.getId() != null) cursoIds.add(c.getId());
            }

            // Cargar reseñas en una segunda consulta y asociarlas a los cursos cargados
            Query<org.redsaberes.model.Resena> resQuery = session.createQuery(
                    "SELECT r FROM Resena r JOIN FETCH r.usuario u WHERE r.curso.id IN :ids ORDER BY r.fecha DESC",
                    org.redsaberes.model.Resena.class
            );
            resQuery.setParameter("ids", cursoIds);
            List<org.redsaberes.model.Resena> resenas = resQuery.getResultList();

            // Mapear id->curso para eficiencia
            java.util.Map<Integer, Curso> byId = new java.util.HashMap<>();
            for (Curso c : cursos) {
                byId.put(c.getId(), c);
            }

            for (org.redsaberes.model.Resena r : resenas) {
                if (r == null || r.getCurso() == null || r.getCurso().getId() == null) continue;
                Curso target = byId.get(r.getCurso().getId());
                if (target != null) {
                    // addResena establece la relación bidireccional correctamente
                    target.addResena(r);
                }
            }

            return cursos;
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
