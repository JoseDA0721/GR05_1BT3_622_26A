package org.redsaberes.repository;

import org.redsaberes.model.Modulo;
import java.util.List;

public interface ModuloRepository extends GenericRepository<Modulo, Integer> {

    List<Modulo> findByCursoId(Integer cursoId);
    List<Modulo> findByCursoIdWithLecciones(Integer cursoId);


}

