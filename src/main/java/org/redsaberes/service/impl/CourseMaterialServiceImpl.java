package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.ModuloRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;
import org.redsaberes.repository.impl.ModuloRepositoryImpl;
import org.redsaberes.service.CourseMaterialService;
import org.redsaberes.service.dto.CourseMaterialViewDto;
import org.redsaberes.service.dto.CourseMaterialViewOutcome;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CourseMaterialServiceImpl implements CourseMaterialService {

    private final CursoRepository cursoRepository;
    private final MatchCursoRepository matchCursoRepository;
    private final ModuloRepository moduloRepository;

    public CourseMaterialServiceImpl() {
        this(new CursoRepositoryImpl(), new MatchCursoRepositoryImpl(), new ModuloRepositoryImpl());
    }

    CourseMaterialServiceImpl(CursoRepository cursoRepository,
                              MatchCursoRepository matchCursoRepository,
                              ModuloRepository moduloRepository) {
        this.cursoRepository = cursoRepository;
        this.matchCursoRepository = matchCursoRepository;
        this.moduloRepository = moduloRepository;
    }

    @Override
    public CourseMaterialViewDto buildCourseMaterialView(Integer usuarioId, Integer cursoId) {
        if (usuarioId == null || cursoId == null) {
            return new CourseMaterialViewDto(CourseMaterialViewOutcome.INVALID_COURSE, null, Collections.emptyList(), false);
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty()) {
            return new CourseMaterialViewDto(CourseMaterialViewOutcome.COURSE_NOT_FOUND, null, Collections.emptyList(), false);
        }

        Curso curso = cursoOpt.get();
        boolean isOwner = curso.getUsuario() != null
                && curso.getUsuario().getId() != null
                && curso.getUsuario().getId().equals(usuarioId);
        boolean hasMatch = matchCursoRepository.existsByCursoAndUsuario(cursoId, usuarioId);
        boolean accessGranted = isOwner || hasMatch;

        List<Modulo> modulos = accessGranted
                ? moduloRepository.findByCursoIdWithLecciones(cursoId)
                : Collections.emptyList();

        return new CourseMaterialViewDto(CourseMaterialViewOutcome.OK, curso, modulos, accessGranted);
    }
}

