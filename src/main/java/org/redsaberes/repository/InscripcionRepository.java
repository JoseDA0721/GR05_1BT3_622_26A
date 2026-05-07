package org.redsaberes.repository;

import org.redsaberes.model.Inscripcion;
import java.util.List;
import java.util.Optional;

public interface InscripcionRepository {

    void save(Inscripcion inscripcion);
    void update(Inscripcion inscripcion);
    void delete(Integer id);
    Optional<Inscripcion> findById(Integer id);
    Optional<Inscripcion> findByUsuarioIdAndCursoId(Integer usuarioId, Integer cursoId);
    List<Inscripcion> findAll();
    List<Inscripcion> findByCursoId(Integer cursoId);
    List<Inscripcion> findByUsuarioId(Integer usuarioId);
    Long countByCursoId(Integer cursoId);
}

