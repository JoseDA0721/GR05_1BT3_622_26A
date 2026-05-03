package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.redsaberes.model.Modulo;
import org.redsaberes.repository.ModuloRepository;

import java.util.List;

public class ModuloRepositoryImpl extends GenericRepositoryImpl<Modulo, Integer> implements ModuloRepository {
    
    public ModuloRepositoryImpl(){
        super(Modulo.class);
    }
    
    @Override
    public List<Modulo> findByCursoId(Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Modulo> query = session.createQuery(
                    "FROM Modulo m WHERE m.curso.id = :cursoId ORDER BY m.orden ASC",
                    Modulo.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        }
    }
    
    @Override
    public List<Modulo> findByCursoIdWithLecciones(Integer cursoId) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Modulo> query = session.createQuery(
                    "FROM Modulo m LEFT JOIN FETCH m.lecciones " +
                            "WHERE m.curso.id = :cursoId ORDER BY m.orden",
                    Modulo.class
            );
            query.setParameter("cursoId", cursoId);
            return query.getResultList();
        }
    }
}

