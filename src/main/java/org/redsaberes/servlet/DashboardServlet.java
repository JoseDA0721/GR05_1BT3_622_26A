package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.DashboardService;
import org.redsaberes.service.dto.DashboardDataDto;
import org.redsaberes.service.impl.DashboardServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DashboardServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final DashboardService dashboardService = new DashboardServiceImpl();

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

            DashboardDataDto dashboardData = dashboardService.buildDashboardData(
                usuario.getId(),
                usuario.getNombre()
            );

            request.setAttribute("dashboardStats", dashboardData.getDashboardStats());
            request.setAttribute("userCourses", dashboardData.getUserCourses());
            request.setAttribute("recentActivity", dashboardData.getRecentActivity());

            request.getRequestDispatcher("/WEB-INF/views/inc1/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar dashboard", e);
            response.sendRedirect(request.getContextPath() + "/login?error=1");
        }
    }
}
