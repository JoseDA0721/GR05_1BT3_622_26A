package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;
import org.redsaberes.service.DashboardService;
import org.redsaberes.service.dto.DashboardDataDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardServiceImpl implements DashboardService {

    private final CursoRepository cursoRepository;
    private final LikeCursoRepository likeCursoRepository;
    private final MatchCursoRepository matchCursoRepository;

    public DashboardServiceImpl() {
        this(new CursoRepositoryImpl(), new LikeCursoRepositoryImpl(), new MatchCursoRepositoryImpl());
    }

    DashboardServiceImpl(CursoRepository cursoRepository,
                         LikeCursoRepository likeCursoRepository,
                         MatchCursoRepository matchCursoRepository) {
        this.cursoRepository = cursoRepository;
        this.likeCursoRepository = likeCursoRepository;
        this.matchCursoRepository = matchCursoRepository;
    }

    @Override
    public DashboardDataDto buildDashboardData(Integer usuarioId, String nombreUsuario) {
        List<Curso> cursos = cursoRepository.findByUsuarioId(usuarioId);

        Map<String, Object> dashboardStats = new HashMap<>();
        dashboardStats.put("coursesCreated", cursos.size());
        dashboardStats.put("likesReceived", safeLong(likeCursoRepository.countByCursoPropietarioId(usuarioId)));
        dashboardStats.put("activeMatches", safeLong(matchCursoRepository.countByCreadorId(usuarioId)));
        dashboardStats.put("enrolledCourses", 0);

        List<Map<String, Object>> userCourses = new ArrayList<>();
        for (Curso c : cursos) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("title", c.getTitulo());
            item.put("imageUrl", c.getImagenPortada());
            item.put("status", c.getEstado() != null ? c.getEstado().name() : "BORRADOR");
            item.put("authorName", nombreUsuario);
            item.put("likesCount", safeLong(likeCursoRepository.countByCursoId(c.getId())));
            item.put("matchesCount", safeLong(matchCursoRepository.countByCursoId(c.getId())));
            userCourses.add(item);
        }

        return new DashboardDataDto(dashboardStats, userCourses, Collections.emptyList());
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }
}

