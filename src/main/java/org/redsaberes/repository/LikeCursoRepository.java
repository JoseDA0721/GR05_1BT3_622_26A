package org.redsaberes.repository;

import org.redsaberes.model.LikeCurso;
import java.util.List;
import java.util.Optional;

public interface LikeCursoRepository {
    
    void save(LikeCurso likeCurso);
    void delete(Integer id);
    Optional<LikeCurso> findById(Integer id);
    List<LikeCurso> findAll();
    List<LikeCurso> findByCursoId(Integer cursoId);
    List<LikeCurso> findByUsuarioId(Integer usuarioId);
    Long countByCursoId(Integer cursoId);
}

