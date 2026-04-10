package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/my-courses")
public class MyCoursesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CursoRepository cursoRepository = new CursoRepositoryImpl();

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
            // Obtener los cursos del usuario con relaciones eager-loaded
            // Con Hibernate, ya traemos módulos y likes automáticamente
            List<Curso> cursos = cursoRepository.findByUsuarioIdWithRelations(usuario.getId());

            // Ahora podemos pasar los cursos directamente al JSP
            // Los helpers getModulosCount(), getLikesCount() etc. ya están en la clase Curso
            request.setAttribute("myCourses", cursos);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar los cursos. Por favor, intenta más tarde.");
        }

        // Hacer forward a la JSP
        request.getRequestDispatcher("/WEB-INF/views/inc1/my-courses.jsp").forward(request, response);
    }
}
