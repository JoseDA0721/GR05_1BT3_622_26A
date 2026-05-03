package org.redsaberes.repository;

import org.redsaberes.model.Curso;
import java.util.List;
import java.util.Optional;

public interface CursoRepository extends GenericRepository<Curso, Integer> {
    // Búsquedas específicas
    List<Curso> findByUsuarioId(Integer usuarioId);
    List<Curso> findByCategoria(String categoria);
    List<Curso> findByEstado(String estado);
    
    // Con relaciones eager loaded
    List<Curso> findByUsuarioIdWithRelations(Integer usuarioId);
    Optional<Curso> findByIdWithRelations(Integer id);
}

