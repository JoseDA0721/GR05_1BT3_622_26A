package org.redsaberes.repository;

import org.redsaberes.model.MatchCurso;
import java.util.List;
import java.util.Optional;

public interface MatchCursoRepository {

    void save(MatchCurso matchCurso);
    void delete(Integer id);
    Optional<MatchCurso> findById(Integer id);
    List<MatchCurso> findAll();
    List<MatchCurso> findByCurso(Integer cursoId);
    Long countByCurso(Integer cursoId);
}

