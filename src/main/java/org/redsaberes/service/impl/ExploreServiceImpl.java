package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;
import org.redsaberes.service.ExploreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExploreServiceImpl implements ExploreService {

    private final CursoRepository cursoRepository;
    private final LikeCursoRepository likeCursoRepository;
    private final MatchCursoRepository matchCursoRepository;

    public ExploreServiceImpl() {
        this(new CursoRepositoryImpl(), new LikeCursoRepositoryImpl(), new MatchCursoRepositoryImpl());
    }

    ExploreServiceImpl(CursoRepository cursoRepository,
                       LikeCursoRepository likeCursoRepository,
                       MatchCursoRepository matchCursoRepository) {
        this.cursoRepository = cursoRepository;
        this.likeCursoRepository = likeCursoRepository;
        this.matchCursoRepository = matchCursoRepository;
    }

    @Override
    public List<Map<String, Object>> buildExploreCourses(Integer usuarioId,
                                                         String search,
                                                         String category) {
        String searchNorm = normalize(search);
        String categoryNorm = normalize(category);

        List<Curso> publicCourses = cursoRepository.findByEstado("PUBLICO");
        List<Map<String, Object>> courses = new ArrayList<>();

        for (Curso c : publicCourses) {
            if (isOwnCourse(c, usuarioId) || !matchesFilters(c, searchNorm, categoryNorm)) {
                continue;
            }

            boolean liked = likeCursoRepository.existsByUsuarioAndCurso(usuarioId, c.getId());
            boolean hasMatch = matchCursoRepository.existsByCursoAndUsuario(c.getId(), usuarioId);

            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("title", safe(c.getTitulo()));
            item.put("description", safe(c.getDescripcion()));
            item.put("category", safe(c.getCategoria()));
            item.put("level", safe(c.getNivelDificultad()));
            item.put("image", safe(c.getImagenPortada()));
            item.put("author", resolveAuthor(c));
            item.put("liked", liked);
            item.put("matched", hasMatch);
            courses.add(item);
        }

        return courses;
    }

    private boolean isOwnCourse(Curso curso, Integer usuarioId) {
        return curso.getUsuario() == null
            || curso.getUsuario().getId() == null
            || curso.getUsuario().getId().equals(usuarioId);
    }

    private String resolveAuthor(Curso curso) {
        if (curso.getUsuario() == null) {
            return "Autor";
        }
        String nombre = safe(curso.getUsuario().getNombre()).trim();
        return nombre.isEmpty() ? "Autor" : nombre;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean matchesFilters(Curso curso, String search, String category) {
        if (!search.isEmpty()) {
            String title = safe(curso.getTitulo()).toLowerCase(Locale.ROOT);
            String description = safe(curso.getDescripcion()).toLowerCase(Locale.ROOT);
            String term = search.toLowerCase(Locale.ROOT);
            if (!title.contains(term) && !description.contains(term)) {
                return false;
            }
        }

        if (!category.isEmpty() && !"Todas".equalsIgnoreCase(category)) {
            return safe(curso.getCategoria()).equalsIgnoreCase(category);
        }

        return true;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}

