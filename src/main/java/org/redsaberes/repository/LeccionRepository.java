package org.redsaberes.repository;

import org.redsaberes.model.Leccion;
import java.util.List;
import java.util.Optional;

public interface LeccionRepository extends GenericRepository<Leccion, Integer>{

    List<Leccion> findByModuloId(Integer moduloId);
}

