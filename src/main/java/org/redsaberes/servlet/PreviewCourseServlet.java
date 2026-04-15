package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.PreviewCourseService;
import org.redsaberes.service.dto.PreviewCourseViewDto;
import org.redsaberes.service.dto.PreviewCourseViewOutcome;
import org.redsaberes.service.impl.PreviewCourseServiceImpl;

import java.io.IOException;
import java.io.Serial;

@WebServlet(urlPatterns = {"/preview-course", "/view-course"})
public class PreviewCourseServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final PreviewCourseService previewCourseService = new PreviewCourseServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer courseId = parseInteger(request.getParameter("id"));
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        PreviewCourseViewDto viewData = previewCourseService.buildPreviewView(usuario.getId(), courseId);

        if (viewData.getOutcome() == PreviewCourseViewOutcome.INVALID_COURSE) {
            response.sendRedirect(request.getContextPath() + "/my-courses?error=invalid_course");
            return;
        }

        if (viewData.getOutcome() == PreviewCourseViewOutcome.COURSE_NOT_FOUND) {
            response.sendRedirect(request.getContextPath() + "/my-courses?error=course_not_found");
            return;
        }

        request.setAttribute("curso", viewData.getCurso());
        request.getRequestDispatcher("/WEB-INF/views/preview-course.jsp").forward(request, response);
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
