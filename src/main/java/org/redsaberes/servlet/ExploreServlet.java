package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.ExploreService;
import org.redsaberes.service.impl.ExploreServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/explore")
public class ExploreServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ExploreServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final ExploreService exploreService = new ExploreServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            String search = request.getParameter("search");
            String category = request.getParameter("category");

            List<Map<String, Object>> courses = exploreService.buildExploreCourses(
                usuario.getId(),
                search,
                category
            );

            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/WEB-INF/views/inc2/explore.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar cursos de exploración", e);
            response.sendRedirect(request.getContextPath() + "/dashboard?error=1");
        }
    }
}

