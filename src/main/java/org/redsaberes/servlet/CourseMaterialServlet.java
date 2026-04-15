package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.CourseMaterialService;
import org.redsaberes.service.dto.CourseMaterialViewDto;
import org.redsaberes.service.dto.CourseMaterialViewOutcome;
import org.redsaberes.service.impl.CourseMaterialServiceImpl;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/course-material")
public class CourseMaterialServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final CourseMaterialService courseMaterialService = new CourseMaterialServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Integer courseId = parseInteger(request.getParameter("id"));

        CourseMaterialViewDto viewData = courseMaterialService.buildCourseMaterialView(usuario.getId(), courseId);
        if (viewData.getOutcome() == CourseMaterialViewOutcome.INVALID_COURSE) {
            response.sendRedirect(request.getContextPath() + "/explore?error=invalid_course");
            return;
        }

        if (viewData.getOutcome() == CourseMaterialViewOutcome.COURSE_NOT_FOUND) {
            response.sendRedirect(request.getContextPath() + "/explore?error=course_not_found");
            return;
        }

        request.setAttribute("curso", viewData.getCurso());
        request.setAttribute("modulos", viewData.getModulos());
        request.setAttribute("accesoConcedido", viewData.isAccesoConcedido());
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

