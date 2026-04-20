package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.CourseLifecycleService;
import org.redsaberes.service.dto.CourseLifecycleOutcome;
import org.redsaberes.service.impl.CourseLifecycleServiceImpl;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/archive-course")
public class ArchiveCourseServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private final CourseLifecycleService courseLifecycleService = new CourseLifecycleServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = session == null ? null : (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
            return;
        }

        Integer cursoId = parseInteger(request.getParameter("id"));
        if (cursoId == null) {
            response.sendRedirect(request.getContextPath() + "/my-courses?error=invalid_course");
            return;
        }

        CourseLifecycleOutcome outcome = courseLifecycleService.archiveCourse(cursoId, usuario.getId());
        if (outcome != CourseLifecycleOutcome.SUCCESS) {
            response.sendRedirect(request.getContextPath() + "/my-courses?error=course_not_found");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/my-courses?success=archived");
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
