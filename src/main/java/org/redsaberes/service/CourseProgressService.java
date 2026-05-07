package org.redsaberes.service;

public interface CourseProgressService {

    boolean updateProgress(Integer usuarioId, Integer cursoId, Integer progreso);

    Integer getSavedProgress(Integer usuarioId, Integer cursoId);//
}
