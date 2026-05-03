package org.redsaberes.repository;

import org.redsaberes.model.MatchCurso;
import java.util.List;
import java.util.Optional;

public interface MatchCursoRepository extends GenericRepository<MatchCurso, Integer> {

    List<MatchCurso> findByCursoId(Integer cursoId);
    List<MatchCurso> findByCreadorId(Integer creadorId);
    List<MatchCurso> findByEstudianteId(Integer estudianteId);
    boolean existsByCursoAndUsuario(Integer cursoId, Integer usuarioId);
    Long countByCursoId(Integer cursoId);
    Long countByCreadorId(Integer creadorId);
    Long countByEstudianteId(Integer estudianteId);
}

