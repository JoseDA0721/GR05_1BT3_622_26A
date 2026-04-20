package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.service.MyCoursesService;

import java.util.Collections;
import java.util.List;

public class MyCoursesServiceImpl implements MyCoursesService {

    private final CursoRepository cursoRepository;

    public MyCoursesServiceImpl() {
        this(new CursoRepositoryImpl());
    }

    MyCoursesServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    public List<Curso> findMyCourses(Integer usuarioId) {
        if (usuarioId == null) {
            return Collections.emptyList();
        }
        return cursoRepository.findByUsuarioIdWithRelations(usuarioId);
    }
}

