package org.redsaberes.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.LikeCourseService;
import org.redsaberes.service.dto.LikeCourseOutcome;
import org.redsaberes.service.impl.LikeCourseServiceImpl;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/like-course")
public class LikeCourseServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LikeCourseService likeCourseService = new LikeCourseServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Integer cursoId = parseInteger(request.getParameter("cursoId"));

        LikeCourseOutcome outcome = likeCourseService.likeCourse(usuario, cursoId);
        if (outcome == LikeCourseOutcome.INVALID_COURSE) {
            response.sendRedirect(request.getContextPath() + "/explore?error=invalid_course");
            return;
        }

        if (outcome == LikeCourseOutcome.COURSE_NOT_FOUND) {
            response.sendRedirect(request.getContextPath() + "/explore?error=course_not_found");
            return;
        }

        if (outcome == LikeCourseOutcome.FORBIDDEN) {
            response.sendRedirect(request.getContextPath() + "/explore");
            return;
        }


        response.sendRedirect(request.getContextPath() + "/explore?success=liked");
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

