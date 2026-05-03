package org.redsaberes.repository;

import org.redsaberes.model.LikeCurso;
import java.util.List;
import java.util.Optional;

public interface LikeCursoRepository extends GenericRepository<LikeCurso, Integer> {

    List<LikeCurso> findByCursoId(Integer cursoId);
    List<LikeCurso> findByUsuarioId(Integer usuarioId);
    List<LikeCurso> findByCursoPropietarioId(Integer propietarioId);
    boolean existsByUsuarioAndCurso(Integer usuarioId, Integer cursoId);
    Long countByCursoId(Integer cursoId);
    Long countByCursoPropietarioId(Integer propietarioId);
}

