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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CursoRepository cursoRepository = new CursoRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");

            // Fetch mínimo: solo cursos del usuario, sin relaciones pesadas.
            List<Curso> cursos = cursoRepository.findByUsuarioId(usuario.getId());

            Map<String, Object> dashboardStats = new HashMap<>();
            dashboardStats.put("coursesCreated", cursos.size());
            dashboardStats.put("likesReceived", 0);     // Se puede optimizar después con query agregada.
            dashboardStats.put("activeMatches", 0);
            dashboardStats.put("enrolledCourses", 0);

            // Adaptador ligero para mantener compatibilidad con dashboard.jsp actual.
            List<Map<String, Object>> userCourses = new ArrayList<>();
            for (Curso c : cursos) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", c.getId());
                item.put("title", c.getTitulo());
                item.put("imageUrl", c.getImagenPortada());
                item.put("status", c.getEstado() != null ? c.getEstado().name() : "BORRADOR");
                item.put("authorName", usuario.getNombre());
                item.put("likesCount", 0);
                item.put("matchesCount", 0);
                userCourses.add(item);
            }

            request.setAttribute("dashboardStats", dashboardStats);
            request.setAttribute("userCourses", userCourses);
            request.setAttribute("recentActivity", new ArrayList<>());

            request.getRequestDispatcher("/WEB-INF/views/inc1/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=1");
        }
    }
}
