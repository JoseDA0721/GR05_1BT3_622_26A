package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.CourseProgressService;
import org.redsaberes.service.impl.CourseProgressServiceImpl;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/update-progress")
public class UpdateProgressServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final CourseProgressService courseProgressService = new CourseProgressServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"error\":\"session_expired\"}");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Integer cursoId = parseInteger(request.getParameter("cursoId"));
        Integer progreso = parseInteger(request.getParameter("progreso"));

        boolean updated = courseProgressService.updateProgress(usuario.getId(), cursoId, progreso);
        if (!updated) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"error\":\"invalid_progress\"}");
            return;
        }

        response.getWriter().write("{\"success\":true}");
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
