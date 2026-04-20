package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.model.Modulo;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.ModuloRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.ModuloRepositoryImpl;
import org.redsaberes.service.CourseLifecycleService;
import org.redsaberes.service.dto.CourseLifecycleOutcome;
import org.redsaberes.service.dto.PublishCourseViewDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CourseLifecycleServiceImpl implements CourseLifecycleService {

    private final CursoRepository cursoRepository;
    private final ModuloRepository moduloRepository;

    public CourseLifecycleServiceImpl() {
        this(new CursoRepositoryImpl(), new ModuloRepositoryImpl());
    }

    CourseLifecycleServiceImpl(CursoRepository cursoRepository,
                               ModuloRepository moduloRepository) {
        this.cursoRepository = cursoRepository;
        this.moduloRepository = moduloRepository;
    }

    @Override
    public PublishCourseViewDto preparePublishView(Integer cursoId, Integer usuarioId) {
        if (cursoId == null || usuarioId == null) {
            return new PublishCourseViewDto(CourseLifecycleOutcome.NOT_FOUND_OR_FORBIDDEN, null, Collections.emptyList());
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || isNotOwner(cursoOpt.get(), usuarioId)) {
            return new PublishCourseViewDto(CourseLifecycleOutcome.NOT_FOUND_OR_FORBIDDEN, null, Collections.emptyList());
        }

        Curso curso = cursoOpt.get();
        if (curso.getEstado() == EstadoCurso.PUBLICO) {
            return new PublishCourseViewDto(CourseLifecycleOutcome.ALREADY_PUBLIC, curso, Collections.emptyList());
        }

        List<Modulo> modulos = moduloRepository.findByCursoIdWithLecciones(cursoId);
        if (lacksContent(modulos)) {
            return new PublishCourseViewDto(CourseLifecycleOutcome.NO_CONTENT, curso, modulos);
        }

        return new PublishCourseViewDto(CourseLifecycleOutcome.SUCCESS, curso, modulos);
    }

    @Override
    public CourseLifecycleOutcome publishCourse(Integer cursoId, Integer usuarioId) {
        if (cursoId == null || usuarioId == null) {
            return CourseLifecycleOutcome.NOT_FOUND_OR_FORBIDDEN;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || isNotOwner(cursoOpt.get(), usuarioId)) {
            return CourseLifecycleOutcome.NOT_FOUND_OR_FORBIDDEN;
        }

        List<Modulo> modulos = moduloRepository.findByCursoIdWithLecciones(cursoId);
        if (lacksContent(modulos)) {
            return CourseLifecycleOutcome.NO_CONTENT;
        }

        Curso curso = cursoOpt.get();
        curso.setEstado(EstadoCurso.PUBLICO);
        cursoRepository.update(curso);
        return CourseLifecycleOutcome.SUCCESS;
    }

    @Override
    public CourseLifecycleOutcome archiveCourse(Integer cursoId, Integer usuarioId) {
        if (cursoId == null || usuarioId == null) {
            return CourseLifecycleOutcome.NOT_FOUND_OR_FORBIDDEN;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || isNotOwner(cursoOpt.get(), usuarioId)) {
            return CourseLifecycleOutcome.NOT_FOUND_OR_FORBIDDEN;
        }

        Curso curso = cursoOpt.get();
        curso.setEstado(EstadoCurso.ARCHIVADO);
        cursoRepository.update(curso);
        return CourseLifecycleOutcome.SUCCESS;
    }

    private boolean lacksContent(List<Modulo> modulos) {
        return modulos.isEmpty() || modulos.stream().noneMatch(m -> m.getLecciones() != null && !m.getLecciones().isEmpty());
    }

    private boolean isNotOwner(Curso curso, Integer usuarioId) {
        return curso == null
                || curso.getUsuario() == null
                || curso.getUsuario().getId() == null
                || !curso.getUsuario().getId().equals(usuarioId);
    }
}


