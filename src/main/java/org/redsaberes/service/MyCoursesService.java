package org.redsaberes.service;

import org.redsaberes.model.Curso;

import java.util.List;

public interface MyCoursesService {

    List<Curso> findMyCourses(Integer usuarioId);
}

