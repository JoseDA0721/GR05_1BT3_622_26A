package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {

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
        Integer filterCourseId = parseInteger(request.getParameter("cursoId"));

        List<Curso> myCourses = cursoRepository.findByUsuarioId(usuario.getId());

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
                row.put("name", interesado.getNombre());
                row.put("email", interesado.getCorreoElectronico());
                row.put("myCourse", course.getTitulo());
                row.put("myCourseId", course.getId());
                interested.add(row);
            }
        }

        List<Map<String, Object>> matches = new ArrayList<>();
        List<MatchCurso> asCreator = matchCursoRepository.findByCreadorId(usuario.getId());
        for (MatchCurso match : asCreator) {
            Map<String, Object> row = new HashMap<>();
            row.put("name", match.getEstudiante().getNombre());
            row.put("email", match.getEstudiante().getCorreoElectronico());
            row.put("course", match.getCurso().getTitulo());
            row.put("courseId", match.getCurso().getId());
            row.put("matchDate", match.getFechaConfirmacion());
            row.put("ownerView", true);
            matches.add(row);
        }

        List<MatchCurso> asStudent = matchCursoRepository.findByEstudianteId(usuario.getId());
        for (MatchCurso match : asStudent) {
            Map<String, Object> row = new HashMap<>();
            row.put("name", match.getCreador().getNombre());
            row.put("email", match.getCreador().getCorreoElectronico());
            row.put("course", match.getCurso().getTitulo());
            row.put("courseId", match.getCurso().getId());
            row.put("matchDate", match.getFechaConfirmacion());
            row.put("ownerView", false);
            matches.add(row);
        }

        request.setAttribute("interested", interested);
        request.setAttribute("interestedCount", interested.size());
        request.setAttribute("matches", matches);
        request.setAttribute("matchesCount", matches.size());
        request.setAttribute("selectedCourseId", filterCourseId);
        request.setAttribute("myCourses", myCourses);
        request.getRequestDispatcher("/WEB-INF/views/inc2/matches.jsp").forward(request, response);
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

