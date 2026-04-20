package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.MyCoursesService;
import org.redsaberes.service.impl.MyCoursesServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/my-courses")
public class MyCoursesServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MyCoursesServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final MyCoursesService myCoursesService = new MyCoursesServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
    ) throws ServletException, IOException {

        // Obtener la sesión del usuario
        HttpSession session = request.getSession(false);

        // Validar que el usuario esté logueado
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            List<Curso> cursos = myCoursesService.findMyCourses(usuario.getId());

            request.setAttribute("myCourses", cursos);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar cursos del usuario", e);
            request.setAttribute("error", "Error al cargar los cursos. Por favor, intenta más tarde.");
        }

        // Hacer forward a la JSP
        request.getRequestDispatcher("/WEB-INF/views/inc1/my-courses.jsp").forward(request, response);
    }
}
