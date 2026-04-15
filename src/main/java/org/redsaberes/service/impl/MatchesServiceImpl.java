package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.LikeCurso;
import org.redsaberes.model.MatchCurso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;
import org.redsaberes.service.MatchesService;
import org.redsaberes.service.dto.MatchesPageDataDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchesServiceImpl implements MatchesService {

    private final CursoRepository cursoRepository;
    private final LikeCursoRepository likeCursoRepository;
    private final MatchCursoRepository matchCursoRepository;

    public MatchesServiceImpl() {
        this(new CursoRepositoryImpl(), new LikeCursoRepositoryImpl(), new MatchCursoRepositoryImpl());
    }

    MatchesServiceImpl(CursoRepository cursoRepository,
                       LikeCursoRepository likeCursoRepository,
                       MatchCursoRepository matchCursoRepository) {
        this.cursoRepository = cursoRepository;
        this.likeCursoRepository = likeCursoRepository;
        this.matchCursoRepository = matchCursoRepository;
    }

    @Override
    public MatchesPageDataDto buildMatchesPageData(Integer usuarioId, Integer filterCourseId) {
        List<Curso> myCourses = cursoRepository.findByUsuarioId(usuarioId);
        List<Map<String, Object>> interested = buildInterested(myCourses, filterCourseId);
        List<Map<String, Object>> matches = buildMatches(usuarioId);

        return new MatchesPageDataDto(interested, matches, myCourses, filterCourseId);
    }

    private List<Map<String, Object>> buildInterested(List<Curso> myCourses, Integer filterCourseId) {
        List<Map<String, Object>> interested = new ArrayList<>();

        for (Curso course : myCourses) {
            if (filterCourseId != null && !filterCourseId.equals(course.getId())) {
                continue;
            }

            List<LikeCurso> likes = likeCursoRepository.findByCursoId(course.getId());
            for (LikeCurso like : likes) {
                Usuario interesado = like.getUsuario();
                if (interesado == null || interesado.getId() == null) {
                    continue;
                }
                if (matchCursoRepository.existsByCursoAndUsuario(course.getId(), interesado.getId())) {
                    continue;
                }

                Map<String, Object> row = new HashMap<>();
                row.put("userId", interesado.getId());
                row.put("name", safe(interesado.getNombre()));
                row.put("email", safe(interesado.getCorreoElectronico()));
                row.put("myCourse", safe(course.getTitulo()));
                row.put("myCourseId", course.getId());
                interested.add(row);
            }
        }

        return interested;
    }

    private List<Map<String, Object>> buildMatches(Integer usuarioId) {
        List<Map<String, Object>> matches = new ArrayList<>();

        List<MatchCurso> asCreator = matchCursoRepository.findByCreadorId(usuarioId);
        for (MatchCurso match : asCreator) {
            Map<String, Object> row = new HashMap<>();
            row.put("name", safe(match.getEstudiante() != null ? match.getEstudiante().getNombre() : null));
            row.put("email", safe(match.getEstudiante() != null ? match.getEstudiante().getCorreoElectronico() : null));
            row.put("course", safe(match.getCurso() != null ? match.getCurso().getTitulo() : null));
            row.put("courseId", match.getCurso() != null ? match.getCurso().getId() : null);
            row.put("matchDate", safe(match.getFechaConfirmacion()));
            row.put("ownerView", true);
            matches.add(row);
        }

        List<MatchCurso> asStudent = matchCursoRepository.findByEstudianteId(usuarioId);
        for (MatchCurso match : asStudent) {
            Map<String, Object> row = new HashMap<>();
            row.put("name", safe(match.getCreador() != null ? match.getCreador().getNombre() : null));
            row.put("email", safe(match.getCreador() != null ? match.getCreador().getCorreoElectronico() : null));
            row.put("course", safe(match.getCurso() != null ? match.getCurso().getTitulo() : null));
            row.put("courseId", match.getCurso() != null ? match.getCurso().getId() : null);
            row.put("matchDate", safe(match.getFechaConfirmacion()));
            row.put("ownerView", false);
            matches.add(row);
        }

        return matches;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}

