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
import org.redsaberes.repository.impl.CursoRepositoryImpl;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/preview-course", "/view-course"})
public class PreviewCourseServlet extends HttpServlet {

    private final CursoRepository cursoRepository = new CursoRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer courseId = parseInteger(request.getParameter("id"));
        if (courseId == null) {
            response.sendRedirect(request.getContextPath() + "/my-courses?error=invalid_course");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Optional<Curso> cursoOpt = cursoRepository.findByIdWithRelations(courseId);

        if (cursoOpt.isEmpty() || !isOwner(cursoOpt.get(), usuario)) {
            response.sendRedirect(request.getContextPath() + "/my-courses?error=course_not_found");
            return;
        }

        request.setAttribute("curso", cursoOpt.get());
        request.getRequestDispatcher("/WEB-INF/views/preview-course.jsp").forward(request, response);
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isOwner(Curso curso, Usuario usuario) {
        return curso.getUsuario() != null
                && usuario != null
                && curso.getUsuario().getId() != null
                && curso.getUsuario().getId().equals(usuario.getId());
    }
}
