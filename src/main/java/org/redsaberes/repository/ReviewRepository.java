package org.redsaberes.repository;

import org.redsaberes.model.Resena;

import java.util.List;

public interface ReviewRepository {
    void save (Resena resena);

    List<Resena> findByCursoId(Integer cursoId);
}
