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
import org.redsaberes.service.ReviewService;
import org.redsaberes.service.dto.CourseMaterialViewDto;
import org.redsaberes.service.dto.CourseMaterialViewOutcome;
import org.redsaberes.service.impl.CourseMaterialServiceImpl;
import org.redsaberes.service.impl.ReviewServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;

@WebServlet("/course-material")
public class CourseMaterialServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final CourseMaterialService courseMaterialService = new CourseMaterialServiceImpl();
    private final ReviewService reviewService = new ReviewServiceImpl();
    private final ReviewRepository reviewRepository = new ReviewRepositoryImpl();
    private final CursoRepository cursoRepository = new CursoRepositoryImpl();
    private final UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();

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
        request.setAttribute("comentarios", reviewRepository.findByCursoId(courseId));
        applyFlashMessage(request, request.getParameter("msg"));
        request.getRequestDispatcher("/WEB-INF/views/inc2/course-material.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        Integer courseId = parseInteger(request.getParameter("id"));

        CourseMaterialViewDto viewData = courseMaterialService.buildCourseMaterialView(usuarioSesion.getId(), courseId);
        if (viewData.getOutcome() == CourseMaterialViewOutcome.INVALID_COURSE) {
            response.sendRedirect(request.getContextPath() + "/explore?error=invalid_course");
            return;
        }

        if (viewData.getOutcome() == CourseMaterialViewOutcome.COURSE_NOT_FOUND) {
            response.sendRedirect(request.getContextPath() + "/explore?error=course_not_found");
            return;
        }

        if (!viewData.isAccesoConcedido()) {
            response.sendRedirect(request.getContextPath() + "/course-material?id=" + courseId + "&msg=comments-access-denied");
            return;
        }

        String comment = request.getParameter("comentario");
        if (comment != null) {
            comment = comment.trim();
        }

        if (comment == null || comment.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/course-material?id=" + courseId + "&msg=comment-empty");
            return;
        }

        try {
            reviewService.validateCommentLength(comment);
        } catch (IllegalArgumentException ex) {
            response.sendRedirect(request.getContextPath() + "/course-material?id=" + courseId + "&msg=comment-too-long");
            return;
        }

        if (reviewService.containsOffensiveContent(comment)) {
            response.sendRedirect(request.getContextPath() + "/course-material?id=" + courseId + "&msg=comment-offensive");
            return;
        }

        var cursoOpt = cursoRepository.findById(courseId);
        var usuarioOpt = usuarioRepository.findById(usuarioSesion.getId());
        if (cursoOpt.isEmpty() || usuarioOpt.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/course-material?id=" + courseId + "&msg=comment-save-error");
            return;
        }

        Resena resena = new Resena();
        resena.setComentario(comment);
        resena.setFecha(LocalDate.now());
        resena.setCurso(cursoOpt.get());
        resena.setUsuario(usuarioOpt.get());

        try {
            reviewRepository.save(resena);
            response.sendRedirect(request.getContextPath() + "/course-material?id=" + courseId + "&msg=comment-added");
        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/course-material?id=" + courseId + "&msg=comment-save-error");
        }
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

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

