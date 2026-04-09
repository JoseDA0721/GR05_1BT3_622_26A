package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            // Verificar que el usuario esté autenticado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
                return;
            }

            // Cargar datos básicos del dashboard
            // TODO: Cargar datos reales de la BD
            Map<String, Object> dashboardStats = new HashMap<>();
            dashboardStats.put("coursesCreated", 0);
            dashboardStats.put("likesReceived", 0);
            dashboardStats.put("activeMatches", 0);
            dashboardStats.put("enrolledCourses", 0);

            request.setAttribute("dashboardStats", dashboardStats);
            // userCourses, recentActivity se dejarán vacíos por ahora

            request.getRequestDispatcher("/WEB-INF/views/inc1/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?error=1");
        }
    }
}
