package org.redsaberes.repository;

import org.redsaberes.model.Leccion;
import java.util.List;
import java.util.Optional;

public interface LeccionRepository {
    
    void save(Leccion leccion);
    void update(Leccion leccion);
    void delete(Integer id);
    Optional<Leccion> findById(Integer id);
    List<Leccion> findAll();
    List<Leccion> findByModuloId(Integer moduloId);
}

