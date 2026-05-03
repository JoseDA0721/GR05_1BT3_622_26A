package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.ModuloRepository;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;
import org.redsaberes.repository.impl.ModuloRepositoryImpl;
import org.redsaberes.repository.impl.ReviewRepositoryImpl;
import org.redsaberes.service.CourseOverviewService;
import org.redsaberes.service.dto.CourseMaterialViewOutcome;
import org.redsaberes.service.dto.CourseOverviewDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CourseOverviewServiceImpl implements CourseOverviewService {

    private final CursoRepository cursoRepository;
    private final MatchCursoRepository matchCursoRepository;
    private final ModuloRepository moduloRepository;
    private final ReviewRepository reviewRepository;

    public CourseOverviewServiceImpl() {
        this(new CursoRepositoryImpl(),
                new MatchCursoRepositoryImpl(),
                new ModuloRepositoryImpl(),
                new ReviewRepositoryImpl());
    }

    CourseOverviewServiceImpl(CursoRepository cursoRepository,
                              MatchCursoRepository matchCursoRepository,
                              ModuloRepository moduloRepository,
                              ReviewRepository reviewRepository) {
        this.cursoRepository = cursoRepository;
        this.matchCursoRepository = matchCursoRepository;
        this.moduloRepository = moduloRepository;
        this.reviewRepository = reviewRepository;
    }

     @Override
    public CourseOverviewDto buildCourseOverview(Integer usuarioId, Integer courseId) {
         if(usuarioId == null || courseId == null) {
             return new CourseOverviewDto(CourseMaterialViewOutcome.INVALID_COURSE,
                     null,
                     Collections.emptyList(),
                     null,
                     0,
                     0,
                     0,
                     0.0,
                     Collections.emptyList(),
                     false,
                     false,
                     false,
                     false);
         }

         Optional<Curso> cursoOpt = cursoRepository.findById(courseId);
         if(cursoOpt.isEmpty()) {
             return new CourseOverviewDto(CourseMaterialViewOutcome.INVALID_COURSE,
                     null,
                     Collections.emptyList(),
                     null,
                     0,
                     0,
                     0,
                     0.0,
                     Collections.emptyList(),
                     false,
                     false,
                     false,
                     false);
         }

         Curso curso = cursoOpt.get();

         boolean isOwner = curso.getUsuario() != null
                 && curso.getUsuario().getId() != null
                 && curso.getUsuario().getId().equals(usuarioId);
         boolean hasMatch = matchCursoRepository.existsByCursoAndUsuario(courseId, usuarioId);
         boolean accessGranted = isOwner || hasMatch;

         List<Modulo> modulos = accessGranted
                 ? moduloRepository.findByCursoIdWithLecciones(courseId)
                 : Collections.emptyList();

         Integer totalLecciones = modulos.stream()
                 .mapToInt(modulo -> modulo.getLecciones() != null ? modulo.getLecciones().size() : 0)
                 .sum();
         long likeCount = curso.getLikes() != null ? curso.getLikes().size() : 0;
         long inscritosCount = curso.getInscritosCount();
         long matchesCount = matchCursoRepository.countByCursoId(courseId);
         Double promedioEstrellas = reviewRepository.averageRatingByCursoId(courseId);
         boolean yaReseno = reviewRepository.existsReviewByUserIdAndCursoId(usuarioId, courseId);
         boolean puedeResenar = accessGranted && !yaReseno;
         return new CourseOverviewDto(CourseMaterialViewOutcome.OK, curso, modulos, totalLecciones, likeCount, inscritosCount, matchesCount, promedioEstrellas, reviewRepository.findByCursoId(courseId), yaReseno, puedeResenar, isOwner, accessGranted);
     }
}
