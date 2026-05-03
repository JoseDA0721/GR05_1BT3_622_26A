package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.redsaberes.model.Leccion;
import org.redsaberes.repository.LeccionRepository;

import java.util.List;

public class LeccionRepositoryImpl extends GenericRepositoryImpl<Leccion, Integer> implements LeccionRepository {
    
    public LeccionRepositoryImpl() {
        super(Leccion.class);
    }
    
    @Override
    public List<Leccion> findByModuloId(Integer moduloId) {
        Session session = getSessionFactory().openSession();
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

