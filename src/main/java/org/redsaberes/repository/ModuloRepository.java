package org.redsaberes.repository;

import org.redsaberes.model.Modulo;
import java.util.List;
import java.util.Optional;

public interface ModuloRepository extends GenericRepository<Modulo, Integer> {

    List<Modulo> findByCursoId(Integer cursoId);
    List<Modulo> findByCursoIdWithLecciones(Integer cursoId);


}

