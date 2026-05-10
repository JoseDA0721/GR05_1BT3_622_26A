package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.ReviewRepositoryImpl;
import org.redsaberes.repository.impl.UsuarioRepositoryImpl;
import org.redsaberes.service.CourseMaterialService;
import org.redsaberes.service.CourseProgressService;
import org.redsaberes.service.ReviewService;
import org.redsaberes.service.dto.CourseMaterialViewDto;
import org.redsaberes.service.dto.CourseMaterialViewOutcome;
import org.redsaberes.service.impl.CourseMaterialServiceImpl;
import org.redsaberes.service.impl.CourseProgressServiceImpl;
import org.redsaberes.service.impl.ReviewServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/course-material", "/course-content"})
public class CourseMaterialServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final CourseMaterialService courseMaterialService = ServiceFactory.getCourseMaterial();
    private final CourseProgressService courseProgressService = new CourseProgressServiceImpl();

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

        loadViewAttributes(request, viewData, courseId, usuario.getId());
        applyFlashMessage(request, request.getParameter("msg"));

        String view = "/course-content".equals(request.getServletPath())
                ? "/WEB-INF/views/inc2/course-content.jsp"
                : "/WEB-INF/views/inc2/course-material.jsp";
        request.getRequestDispatcher(view).forward(request, response);
    }

    private void applyFlashMessage(HttpServletRequest request, String msg) {
        if (msg == null) {
            return;
        }

        switch (msg) {
            case "comment-added":
                request.setAttribute("exito", "Comentario publicado correctamente.");
                break;
            case "comment-empty":
                request.setAttribute("error", "El comentario no puede estar vacio.");
                break;
            case "comment-too-long":
                request.setAttribute("error", "El comentario no puede superar 255 caracteres.");
                break;
            case "comment-offensive":
                request.setAttribute("error", "El comentario contiene contenido no permitido.");
                break;
            case "comment-save-error":
                request.setAttribute("error", "No se pudo guardar el comentario. Intentalo nuevamente.");
                break;
            case "comments-access-denied":
                request.setAttribute("error", "Necesitas acceso por match para comentar en este curso.");
                break;
            default:
                break;
        }
    }

    private void loadViewAttributes(HttpServletRequest request, CourseMaterialViewDto viewData, Integer courseId, Integer usuarioId) {
        request.setAttribute("curso", viewData.getCurso());
        request.setAttribute("modulos", viewData.getModulos());
        request.setAttribute("accesoConcedido", viewData.isAccesoConcedido());
        request.setAttribute("progresoGuardado", courseProgressService.getSavedProgress(usuarioId, courseId));
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
