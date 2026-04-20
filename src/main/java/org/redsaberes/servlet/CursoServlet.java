package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.CourseCreationService;
import org.redsaberes.service.dto.CourseCreationOutcome;
import org.redsaberes.service.dto.CourseCreationResultDto;
import org.redsaberes.service.impl.CourseCreationServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/create-course")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 5,        // 5MB
        maxRequestSize = 1024 * 1024 * 10     // 10MB
)
public class CursoServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CursoServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final CourseCreationService courseCreationService = new CourseCreationServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
    ) throws ServletException, IOException {

        // Verificar que el usuario está autenticado
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Usuario autenticado, mostrar formulario de crear curso
        request.getRequestDispatcher("/WEB-INF/views/inc1/create-course.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        String nivel = request.getParameter("nivelDificultad");

        try {
            String uploadDir = getServletContext()
                    .getRealPath("")
                    + "uploads" + java.io.File.separator
                    + "portadas";
            Part filePart = request.getPart("imagenPortada");

            CourseCreationResultDto result = courseCreationService.createDraftCourse(
                    usuario,
                    titulo,
                    descripcion,
                    categoria,
                    nivel,
                    filePart,
                    uploadDir
            );

            if (result.getOutcome() == CourseCreationOutcome.REDIRECT_LOGIN) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            if (result.getOutcome() == CourseCreationOutcome.FORWARD_WITH_ERROR) {
                request.setAttribute("error", result.getError());
                request.getRequestDispatcher("/WEB-INF/views/inc1/create-course.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath()
                    + "/my-courses?success=created");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al crear curso", e);
            request.setAttribute("error", "Error al crear el curso."
            + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/inc1/create-course.jsp").forward(request, response);
        }
    }
}
