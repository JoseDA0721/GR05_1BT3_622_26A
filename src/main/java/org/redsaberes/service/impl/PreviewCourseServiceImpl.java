package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.service.PreviewCourseService;
import org.redsaberes.service.dto.PreviewCourseViewDto;
import org.redsaberes.service.dto.PreviewCourseViewOutcome;

import java.util.Optional;

public class PreviewCourseServiceImpl implements PreviewCourseService {

    private final CursoRepository cursoRepository;

    public PreviewCourseServiceImpl() {
        this(new CursoRepositoryImpl());
    }

    PreviewCourseServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    public PreviewCourseViewDto buildPreviewView(Integer usuarioId, Integer cursoId) {
        if (usuarioId == null || cursoId == null) {
            return new PreviewCourseViewDto(PreviewCourseViewOutcome.INVALID_COURSE, null);
        }

        Optional<Curso> cursoOpt = cursoRepository.findByIdWithRelations(cursoId);
        if (cursoOpt.isEmpty() || isNotOwner(cursoOpt.get(), usuarioId)) {
            return new PreviewCourseViewDto(PreviewCourseViewOutcome.COURSE_NOT_FOUND, null);
        }

        return new PreviewCourseViewDto(PreviewCourseViewOutcome.OK, cursoOpt.get());
    }

    private boolean isNotOwner(Curso curso, Integer usuarioId) {
        return curso == null
                || curso.getUsuario() == null
                || curso.getUsuario().getId() == null
                || !curso.getUsuario().getId().equals(usuarioId);
    }
}

