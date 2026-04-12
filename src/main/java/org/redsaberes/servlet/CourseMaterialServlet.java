package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.ModuloRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;
import org.redsaberes.repository.impl.ModuloRepositoryImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebServlet("/course-material")
public class CourseMaterialServlet extends HttpServlet {

    private final CursoRepository cursoRepository = new CursoRepositoryImpl();
    private final MatchCursoRepository matchCursoRepository = new MatchCursoRepositoryImpl();
    private final ModuloRepository moduloRepository = new ModuloRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Integer courseId = parseInteger(request.getParameter("id"));
        if (courseId == null) {
            response.sendRedirect(request.getContextPath() + "/explore?error=invalid_course");
            return;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(courseId);
        if (cursoOpt.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/explore?error=course_not_found");
            return;
        }

        Curso curso = cursoOpt.get();
        boolean isOwner = curso.getUsuario() != null && curso.getUsuario().getId().equals(usuario.getId());
        boolean hasMatch = matchCursoRepository.existsByCursoAndUsuario(courseId, usuario.getId());
        boolean accessGranted = isOwner || hasMatch;

        List<Modulo> modulos = accessGranted
                ? moduloRepository.findByCursoIdWithLecciones(courseId)
                : Collections.emptyList();

        request.setAttribute("curso", curso);
        request.setAttribute("modulos", modulos);
        request.setAttribute("accesoConcedido", accessGranted);
        request.getRequestDispatcher("/WEB-INF/views/inc2/course-material.jsp").forward(request, response);
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

