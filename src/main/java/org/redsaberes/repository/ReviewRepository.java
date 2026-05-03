package org.redsaberes.repository;

import org.redsaberes.model.Resena;

import java.util.List;

public interface ReviewRepository extends GenericRepository<Resena, Integer> {
    List<Resena> findByCursoId(Integer cursoId);

    Double averageRatingByCursoId(Integer cursoId);

    boolean existsReviewByUserIdAndCursoId(Integer usuarioId, Integer cursoId);
}
