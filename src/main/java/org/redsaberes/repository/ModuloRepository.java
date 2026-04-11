package org.redsaberes.repository;

import org.redsaberes.model.Modulo;
import java.util.List;
import java.util.Optional;

public interface ModuloRepository {
    
    void save(Modulo modulo);
    void update(Modulo modulo);
    void delete(Integer id);
    Optional<Modulo> findById(Integer id);
    List<Modulo> findAll();
    List<Modulo> findByCursoId(Integer cursoId);
    List<Modulo> findByCursoIdWithLecciones(Integer cursoId);


}

