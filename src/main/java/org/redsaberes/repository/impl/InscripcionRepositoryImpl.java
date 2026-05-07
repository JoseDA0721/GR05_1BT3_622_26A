package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.redsaberes.model.Inscripcion;
import org.redsaberes.repository.InscripcionRepository;

import java.util.List;
import java.util.Optional;

public class InscripcionRepositoryImpl extends GenericRepositoryImpl<Inscripcion, Integer> implements InscripcionRepository {
    
    public InscripcionRepositoryImpl() {
        super(Inscripcion.class);
    }
    
    @Override
    public List<Inscripcion> findByCursoId(Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Inscripcion> query = session.createQuery(
                    "FROM Inscripcion i WHERE i.curso.id = :cursoId",
                    Inscripcion.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        }
    }
    
    @Override
    public List<Inscripcion> findByUsuarioId(Integer usuarioId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Inscripcion> query = session.createQuery(
                    "FROM Inscripcion i WHERE i.usuario.id = :usuarioId",
                    Inscripcion.class
            );
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        }
    }
    
    @Override
    public Long countByCursoId(Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(i) FROM Inscripcion i WHERE i.curso.id = :cursoId",
                    Long.class
            );
            query.setParameter("cursoId", cursoId);
            return query.uniqueResult();
        }
    }

    @Override
    public Optional<Inscripcion> findByUsuarioIdAndCursoId(Integer usuarioId, Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Inscripcion> query = session.createQuery(
                    "FROM Inscripcion i WHERE i.usuario.id = :usuarioId AND i.curso.id = :cursoId",
                    Inscripcion.class
            );
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("cursoId", cursoId);
            return query.uniqueResultOptional();
        }
    }
}
