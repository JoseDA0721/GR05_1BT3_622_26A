package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/explore")
public class ExploreServlet extends HttpServlet {

    private final CursoRepository cursoRepository = new CursoRepositoryImpl();
    private final LikeCursoRepository likeCursoRepository = new LikeCursoRepositoryImpl();
    private final MatchCursoRepository matchCursoRepository = new MatchCursoRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String search = normalize(request.getParameter("search"));
        String category = normalize(request.getParameter("category"));

        List<Curso> publicCourses = cursoRepository.findByEstado("PUBLICO");
        List<Map<String, Object>> courses = new ArrayList<>();

        for (Curso c : publicCourses) {
            if (c.getUsuario() == null || c.getUsuario().getId() == null || c.getUsuario().getId().equals(usuario.getId())) {
                continue;
            }
            if (!matchesFilters(c, search, category)) {
                continue;
            }

            boolean liked = likeCursoRepository.existsByUsuarioAndCurso(usuario.getId(), c.getId());
            boolean hasMatch = matchCursoRepository.existsByCursoAndUsuario(c.getId(), usuario.getId());

            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("title", c.getTitulo());
            item.put("description", c.getDescripcion());
            item.put("category", c.getCategoria());
            item.put("level", c.getNivelDificultad());
            item.put("image", c.getImagenPortada());
            item.put("author", c.getUsuario().getNombre());
            item.put("liked", liked);
            item.put("matched", hasMatch);
            courses.add(item);
        }

        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/WEB-INF/views/inc2/explore.jsp").forward(request, response);
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

