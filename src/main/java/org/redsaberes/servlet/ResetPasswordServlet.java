package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redsaberes.service.PasswordRecoveryService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.impl.PasswordRecoveryServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ResetPasswordServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final PasswordRecoveryService passwordRecoveryService = new PasswordRecoveryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                       HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            request.getRequestDispatcher(
                            "/WEB-INF/views/inc1/reset-password.jsp")
                    .forward(request, response);
        } catch (Exception e) { LOGGER.log(Level.SEVERE, "Error al mostrar la vista de restablecimiento", e); }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response
    ) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try {
            String token = request.getParameter("token");
            String contrasena = request.getParameter("password");
            String confirmarContrasena = request.getParameter("confirmPassword");

            passwordRecoveryService.restablecerContrasena(token, contrasena, confirmarContrasena);

             // Redirigir al login con mensaje de éxito
             response.sendRedirect(request.getContextPath() + "/login?passwordReset=success");

        } catch (ServiceValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/inc1/reset-password.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al restablecer la contraseña", e);
            request.setAttribute("error", "Error del servidor: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/inc1/reset-password.jsp").forward(request, response);
        }
    }
}
