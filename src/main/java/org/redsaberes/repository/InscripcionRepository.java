package org.redsaberes.repository;

import org.redsaberes.model.Inscripcion;
import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends GenericRepository<Inscripcion, Integer> {
    List<Inscripcion> findByCursoId(Integer cursoId);
    List<Inscripcion> findByUsuarioId(Integer usuarioId);
    Long countByCursoId(Integer cursoId);
}

