package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.redsaberes.service.LogoutService;
import org.redsaberes.service.dto.LogoutResultDto;
import org.redsaberes.service.impl.LogoutServiceImpl;

import java.io.IOException;
import java.io.Serial;

@WebServlet ("/logout")
public class LogoutServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private final LogoutService logoutService = new LogoutServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processLogout(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processLogout(request,response);
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        LogoutResultDto result = logoutService.logout(session, request.getCookies());

        if (result.getCookieToDelete() != null) {
            response.addCookie(result.getCookieToDelete());
        }

        // Redirigir a la página de login con mensaje de éxito
        response.sendRedirect(request.getContextPath() + "/login?logout=success");
    }
}
