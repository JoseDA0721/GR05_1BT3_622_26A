package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.CourseOverviewService;
import org.redsaberes.service.ReviewService;
import org.redsaberes.service.dto.CourseMaterialViewOutcome;
import org.redsaberes.service.dto.CourseOverviewDto;
import org.redsaberes.service.dto.ReviewCreationOutcome;
import org.redsaberes.service.dto.ReviewCreationResult;

import java.io.IOException;
import java.io.Serial;
import java.util.Optional;
import java.util.logging.Logger;


@WebServlet("/course-overview")
public class CourseOverviewServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CourseOverviewServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private final CourseOverviewService courseOverviewService = ServiceFactory.getCourseOverview();
    private final ReviewService         reviewService         = ServiceFactory.getReview();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Optional<Usuario> usuarioOpt = getUsuario(request, response);
            if (usuarioOpt.isEmpty()) return;
            Usuario usuario = usuarioOpt.get();

            Integer cursoId = validateAndGetCursoId(request, response);
            if (cursoId == null) return;

            CourseOverviewDto viewData = loadCourseOverviewOrRedirect(request, response, usuario.getId(), cursoId);
            if (viewData == null) return;

            renderCourseOverviewView(request, response, viewData);

        } catch (RuntimeException e) {
            ServletErrorHandler.handleServerError(request, response, LOGGER, "Error al cargar presentación del curso", e);
        }
    }

    /**
     * Válida el acceso para crear una reseña:
     * - Outcome no sea OK
     * - accesoConcedido sea false
     * - yaReseno sea true
     *
     * @return dto. del curso cuando el acceso es válido; null si ya se redirigió
     */
    private CourseOverviewDto loadReviewCreationAccess(HttpServletRequest request, HttpServletResponse response,
                                                       Integer usuarioId, Integer cursoId) throws IOException {
        CourseOverviewDto access = courseOverviewService.buildCourseOverview(usuarioId, cursoId);

        if (access.outcome() != CourseMaterialViewOutcome.OK || !access.accesoConcedido()) {
            redirectToOverviewWithMessage(request, response, cursoId, ReviewCreationOutcome.UNAUTHORIZED.getMsgKey());
            return null;
        }

        if (access.yaReseno()) {
            redirectToOverviewWithMessage(request, response, cursoId, ReviewCreationOutcome.ALREADY_REVIEWED.getMsgKey());
            return null;
        }

        return access;
    }

    /**
     * ✅ Procesa la creación de reseña: extrae parámetros y delega al servicio.
     * Extrae responsabilidad de parsing de doPost().
     */
    private ReviewCreationResult requestReviewCreation(HttpServletRequest request, Usuario usuario, CourseOverviewDto access) {

        Integer estrellas = ServletUtil.parseInteger(request.getParameter("estrellas"));
        String comentario = request.getParameter("comentario");
        if (comentario != null) {
            comentario = comentario.trim();
        }

        return reviewService.crearResena(estrellas, comentario, usuario, access.curso());
    }

    private void mapCourseDataToRequest(HttpServletRequest request, CourseOverviewDto viewData) {
        request.setAttribute("curso",            viewData.curso());
        request.setAttribute("modulos",          viewData.modulos());
        request.setAttribute("totalLecciones",   viewData.totalLecciones());
        request.setAttribute("likesCount",       viewData.likeCount());
        request.setAttribute("inscritosCount",   viewData.inscritosCount());
        request.setAttribute("matchesCount",     viewData.matchesCount());
        request.setAttribute("promedioEstrellas", viewData.promedioEstrellas());
        request.setAttribute("resenas",          viewData.resenas());
        request.setAttribute("totalResenas",     viewData.resenas().size());
        request.setAttribute("accesoConcedido",  viewData.accesoConcedido());
        request.setAttribute("puedeResenar",     viewData.puedeResenar());
        request.setAttribute("yaReseno",         viewData.yaReseno());
        request.setAttribute("esPropietario",    viewData.esPropietario());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Optional<Usuario> usuarioOpt = getUsuario(request, response);
            if (usuarioOpt.isEmpty()) return;
            Usuario usuarioSession = usuarioOpt.get();

            Integer cursoId = validateAndGetCursoId(request, response);
            if (cursoId == null) return;

            CourseOverviewDto access = loadReviewCreationAccess(request, response, usuarioSession.getId(), cursoId);
            if (access == null) return;

            // Crear reseña
            ReviewCreationResult result = requestReviewCreation(request, usuarioSession, access);

            // Redirigir con mensaje
            String msg = result.getOutcome().getMsgKey();
            redirectToOverviewWithMessage(request, response, cursoId, msg);

        } catch (RuntimeException e) {
            ServletErrorHandler.handleServerError(request, response, LOGGER, "Error al guardar reseña", e);
        }
    }

    // ────────────────────────────── Helpers ──────────────────────────────

    private CourseOverviewDto loadCourseOverviewOrRedirect(HttpServletRequest request, HttpServletResponse response,
                                                           Integer usuarioId, Integer cursoId) throws IOException {
        CourseOverviewDto viewData = courseOverviewService.buildCourseOverview(usuarioId, cursoId);

        if (viewData.outcome() == CourseMaterialViewOutcome.INVALID_COURSE) {
            response.sendRedirect(request.getContextPath() + RouteConstants.exploreErrorUrl(RouteConstants.ERROR_INVALID_COURSE));
            return null;
        }

        if (viewData.outcome() == CourseMaterialViewOutcome.COURSE_NOT_FOUND) {
            response.sendRedirect(request.getContextPath() + RouteConstants.exploreErrorUrl(RouteConstants.ERROR_COURSE_NOT_FOUND));
            return null;
        }

        return viewData;
    }

    /**
     * ✅ Renderiza la vista del curso: carga datos, mapea atributos, aplica flash, forwarda.
     * Extrae la responsabilidad de rendering de doGet().
     */
    private void renderCourseOverviewView(HttpServletRequest request, HttpServletResponse response,
                                          CourseOverviewDto viewData) throws ServletException, IOException {
        mapCourseDataToRequest(request, viewData);
        applyFlash(request, request.getParameter(RouteConstants.PARAM_MSG));

        request.getRequestDispatcher(RouteConstants.VIEW_COURSE_OVERVIEW)
                .forward(request, response);
    }

    private void redirectToOverviewWithMessage(HttpServletRequest request, HttpServletResponse response,
                                               Integer cursoId, String msgKey) throws IOException {
        response.sendRedirect(request.getContextPath() + RouteConstants.courseOverviewUrl(cursoId, msgKey));
    }

    private void applyFlash(HttpServletRequest request, String msg) {
        ReviewCreationOutcome outcome = ReviewCreationOutcome.fromMsgKey(msg);
        if (outcome == null) return;

        switch (outcome) {
            case SUCCESS -> request.setAttribute("exito", "¡Reseña publicada correctamente!");
            case ALREADY_REVIEWED -> request.setAttribute("error", "Ya dejaste una reseña para este curso.");
            case INVALID_STARS -> request.setAttribute("error", "Debes seleccionar entre 1 y 5 estrellas.");
            case COMMENT_TOO_LONG -> request.setAttribute("error", "El comentario no puede superar 255 caracteres.");
            case COMMENT_OFFENSIVE -> request.setAttribute("error", "El comentario contiene contenido no permitido.");
            case UNAUTHORIZED -> request.setAttribute("error", "No tienes acceso para realizar esta acción.");
            case SAVE_ERROR -> request.setAttribute("error", "Error al guardar la reseña. Inténtalo de nuevo.");
        }
    }

    private Optional<Usuario> getUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = ServletUtil.getUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + RouteConstants.LOGIN);
            return Optional.empty();
        }
        return Optional.of(usuario);
    }

    private Integer validateAndGetCursoId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer cursoId = ServletUtil.parseInteger(request.getParameter(RouteConstants.PARAM_ID));
        if (cursoId == null) {
            response.sendRedirect(request.getContextPath() + RouteConstants.exploreErrorUrl(RouteConstants.ERROR_INVALID_COURSE));
        }
        return cursoId;
    }
}
