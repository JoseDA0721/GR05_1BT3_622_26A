package org.redsaberes.repository;

import org.redsaberes.model.Inscripcion;
import java.util.List;
import java.util.Optional;

public interface InscripcionRepository {
    
    void save(Inscripcion inscripcion);
    void delete(Integer id);
    Optional<Inscripcion> findById(Integer id);
    List<Inscripcion> findAll();
    List<Inscripcion> findByCursoId(Integer cursoId);
    List<Inscripcion> findByUsuarioId(Integer usuarioId);
    Long countByCursoId(Integer cursoId);
}

