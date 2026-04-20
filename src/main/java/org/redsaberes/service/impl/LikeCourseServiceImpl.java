package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.model.LikeCurso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.service.LikeCourseService;
import org.redsaberes.service.dto.LikeCourseOutcome;

import java.time.LocalDateTime;
import java.util.Optional;

public class LikeCourseServiceImpl implements LikeCourseService {

    private final CursoRepository cursoRepository;
    private final LikeCursoRepository likeCursoRepository;

    public LikeCourseServiceImpl() {
        this(new CursoRepositoryImpl(), new LikeCursoRepositoryImpl());
    }

    LikeCourseServiceImpl(CursoRepository cursoRepository,
                          LikeCursoRepository likeCursoRepository) {
        this.cursoRepository = cursoRepository;
        this.likeCursoRepository = likeCursoRepository;
    }

    @Override
    public LikeCourseOutcome likeCourse(Usuario usuario, Integer cursoId) {
        if (usuario == null || usuario.getId() == null || cursoId == null) {
            return LikeCourseOutcome.INVALID_COURSE;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty()) {
            return LikeCourseOutcome.COURSE_NOT_FOUND;
        }

        Curso curso = cursoOpt.get();
        if (curso.getEstado() != EstadoCurso.PUBLICO
                || curso.getUsuario() == null
                || curso.getUsuario().getId() == null
                || curso.getUsuario().getId().equals(usuario.getId())) {
            return LikeCourseOutcome.FORBIDDEN;
        }

        if (!likeCursoRepository.existsByUsuarioAndCurso(usuario.getId(), cursoId)) {
            LikeCurso likeCurso = new LikeCurso();
            likeCurso.setCurso(curso);
            likeCurso.setUsuario(usuario);
            likeCurso.setFecha(LocalDateTime.now().toString());
            likeCursoRepository.save(likeCurso);
        }

        return LikeCourseOutcome.LIKED;
    }
}

