package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.InscripcionRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.ModuloRepository;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.InscripcionRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;
import org.redsaberes.repository.impl.ModuloRepositoryImpl;
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
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/course-overview")
public class CourseOverviewServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CourseOverviewServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private final CourseMaterialService courseMaterialService = new CourseMaterialServiceImpl();
    private final ReviewService         reviewService         = new ReviewServiceImpl();
    private final ReviewRepository      reviewRepository      = new ReviewRepositoryImpl();
    private final CursoRepository       cursoRepository       = new CursoRepositoryImpl();
    private final ModuloRepository      moduloRepository      = new ModuloRepositoryImpl();
    private final LikeCursoRepository   likeCursoRepository   = new LikeCursoRepositoryImpl();
    private final InscripcionRepository inscripcionRepository = new InscripcionRepositoryImpl();
    private final MatchCursoRepository  matchCursoRepository  = new MatchCursoRepositoryImpl();
    private final UsuarioRepository     usuarioRepository     = new UsuarioRepositoryImpl();

    // ────────────────────────────── GET ──────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Usuario usuario = getUsuario(request);
            if (usuario == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Integer cursoId = parseInteger(request.getParameter("id"));
            if (cursoId == null) {
                response.sendRedirect(request.getContextPath() + "/explore?error=invalid_course");
                return;
            }

            // Verificar acceso (match o propietario)
            CourseMaterialViewDto access =
                    courseMaterialService.buildCourseMaterialView(usuario.getId(), cursoId);

            if (access.getOutcome() == CourseMaterialViewOutcome.INVALID_COURSE) {
                response.sendRedirect(request.getContextPath() + "/explore?error=invalid_course");
                return;
            }
            if (access.getOutcome() == CourseMaterialViewOutcome.COURSE_NOT_FOUND) {
                response.sendRedirect(request.getContextPath() + "/explore?error=course_not_found");
                return;
            }

            populateRequest(request, usuario, cursoId, access);
            applyFlash(request, request.getParameter("msg"));
            request.getRequestDispatcher("/WEB-INF/views/inc2/course-overview.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar presentación del curso", e);
            response.sendRedirect(request.getContextPath() + "/explore?error=1");
        }
    }

    // ────────────────────────────── POST (reseña) ──────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Usuario usuarioSesion = getUsuario(request);
            if (usuarioSesion == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Integer cursoId = parseInteger(request.getParameter("id"));
            if (cursoId == null) {
                response.sendRedirect(request.getContextPath() + "/explore?error=invalid_course");
                return;
            }

            String base = request.getContextPath() + "/course-overview?id=" + cursoId;

            // Verificar acceso
            CourseMaterialViewDto access =
                    courseMaterialService.buildCourseMaterialView(usuarioSesion.getId(), cursoId);
            if (access.getOutcome() != CourseMaterialViewOutcome.OK) {
                response.sendRedirect(base + "&msg=access-denied");
                return;
            }
            if (!access.isAccesoConcedido()) {
                response.sendRedirect(base + "&msg=access-denied");
                return;
            }

            // Ya dejó reseña?
            if (reviewRepository.existsReviewByUserIdAndCursoId(usuarioSesion.getId(), cursoId)) {
                response.sendRedirect(base + "&msg=already-reviewed");
                return;
            }

            // Validar estrellas
            Integer estrellas = parseInteger(request.getParameter("estrellas"));
            if (estrellas == null || estrellas < 1 || estrellas > 5) {
                response.sendRedirect(base + "&msg=invalid-stars");
                return;
            }

            // Validar comentario
            String comentario = request.getParameter("comentario");
            if (comentario != null) comentario = comentario.trim();

            if (comentario != null && !comentario.isEmpty()) {
                try {
                    reviewService.validateCommentLength(comentario);
                } catch (IllegalArgumentException ex) {
                    response.sendRedirect(base + "&msg=comment-too-long");
                    return;
                }
                if (reviewService.containsOffensiveContent(comentario)) {
                    response.sendRedirect(base + "&msg=comment-offensive");
                    return;
                }
            }

            // Guardar reseña
            Optional<Curso>   cursoOpt   = cursoRepository.findById(cursoId);
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioSesion.getId());

            if (cursoOpt.isEmpty() || usuarioOpt.isEmpty()) {
                response.sendRedirect(base + "&msg=save-error");
                return;
            }

            Resena resena = new Resena();
            resena.setEstrellas(estrellas);
            resena.setComentario(comentario != null && !comentario.isEmpty() ? comentario : null);
            resena.setFecha(LocalDate.now());
            resena.setCurso(cursoOpt.get());
            resena.setUsuario(usuarioOpt.get());

            reviewRepository.save(resena);
            response.sendRedirect(base + "&msg=review-saved");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar reseña", e);
            response.sendRedirect(request.getContextPath() + "/explore?error=1");
        }
    }

    // ────────────────────────────── Helpers ──────────────────────────────

    /**
     * Carga todos los atributos que necesita la vista.
     */
    private void populateRequest(HttpServletRequest request,
                                 Usuario usuario,
                                 Integer cursoId,
                                 CourseMaterialViewDto access) {

        Curso curso = access.getCurso();

        // Módulos con lecciones para el plan de estudios
        List<Modulo> modulos = moduloRepository.findByCursoIdWithLecciones(cursoId);

        // Conteo total de lecciones
        int totalLecciones = modulos.stream()
                .mapToInt(m -> m.getLecciones() == null ? 0 : m.getLecciones().size())
                .sum();

        // Estadísticas
        long    likesCount       = safe(likeCursoRepository.countByCursoId(cursoId));
        long    inscritosCount   = safe(inscripcionRepository.countByCursoId(cursoId));
        long    matchesCount     = safe(matchCursoRepository.countByCursoId(cursoId));
        Double  promedioEstrellas = reviewRepository.averageRatingByCursoId(cursoId);
        List<Resena> resenas     = reviewRepository.findByCursoId(cursoId);

        // ¿El usuario ya dejó reseña?
        boolean yaReseno = reviewRepository.existsReviewByUserIdAndCursoId(usuario.getId(), cursoId);

        // ¿Puede dejar reseña? Solo si tiene acceso concedido y no ha reseñado aún
        boolean puedeResenar = access.isAccesoConcedido() && !yaReseno;

        // ¿Es el propietario?
        boolean esPropietario = curso.getUsuario() != null
                && curso.getUsuario().getId() != null
                && curso.getUsuario().getId().equals(usuario.getId());

        request.setAttribute("curso",            curso);
        request.setAttribute("modulos",          modulos);
        request.setAttribute("totalModulos",     modulos.size());
        request.setAttribute("totalLecciones",   totalLecciones);
        request.setAttribute("likesCount",       likesCount);
        request.setAttribute("inscritosCount",   inscritosCount);
        request.setAttribute("matchesCount",     matchesCount);
        request.setAttribute("promedioEstrellas", promedioEstrellas);
        request.setAttribute("resenas",          resenas);
        request.setAttribute("totalResenas",     resenas.size());
        request.setAttribute("accesoConcedido",  access.isAccesoConcedido());
        request.setAttribute("puedeResenar",     puedeResenar);
        request.setAttribute("yaReseno",         yaReseno);
        request.setAttribute("esPropietario",    esPropietario);
    }

    private void applyFlash(HttpServletRequest request, String msg) {
        if (msg == null) return;
        switch (msg) {
            case "review-saved"    -> request.setAttribute("exito", "¡Reseña publicada correctamente!");
            case "already-reviewed"-> request.setAttribute("error", "Ya dejaste una reseña para este curso.");
            case "invalid-stars"   -> request.setAttribute("error", "Debes seleccionar entre 1 y 5 estrellas.");
            case "comment-too-long"-> request.setAttribute("error", "El comentario no puede superar 255 caracteres.");
            case "comment-offensive"->request.setAttribute("error", "El comentario contiene contenido no permitido.");
            case "access-denied"   -> request.setAttribute("error", "No tienes acceso para realizar esta acción.");
            case "save-error"      -> request.setAttribute("error", "Error al guardar la reseña. Inténtalo de nuevo.");
        }
    }

    private Usuario getUsuario(HttpServletRequest request) {
        return ServletUtil.getUsuarioSesion(request);
    }

    private Integer parseInteger(String value) {
        try { return value == null ? null : Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private long safe(Long value) {
        return value == null ? 0L : value;
    }
}