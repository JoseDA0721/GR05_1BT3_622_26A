package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.service.PasswordRecoveryService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.impl.PasswordRecoveryServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ForgotPasswordServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final PasswordRecoveryService passwordRecoveryService = new PasswordRecoveryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
    )throws ServletException, IOException {
        try {
            request.getRequestDispatcher(
                            "/WEB-INF/views/inc1/forgot-password.jsp")
                    .forward(request, response);
        } catch (Exception e) { LOGGER.log(Level.SEVERE, "Error al mostrar la vista de recuperación", e); }
    }
    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response
    )throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try {
            String correo = request.getParameter("email");
            String baseUrl = request.getScheme() + "://"
                    + request.getServerName() + ":"
                    + request.getServerPort()
                    + request.getContextPath();

            passwordRecoveryService.solicitarRestablecimiento(correo, baseUrl);

             HttpSession session = request.getSession();
             session.setAttribute("resetEmailSent", true);
             session.setAttribute("resetEmail",correo);

             response.sendRedirect(request.getContextPath() + "/forgot-password?sent=true&email=" +
                      java.net.URLEncoder.encode(correo, StandardCharsets.UTF_8));
        }catch (ServiceValidationException e) {
             request.setAttribute("error", e.getMessage());
             request.getRequestDispatcher("/WEB-INF/views/inc1/forgot-password.jsp").forward(request, response);
        }catch(Exception e){
             LOGGER.log(Level.SEVERE, "Error al enviar el correo de recuperación", e);
             request.setAttribute("error", "Error al enviar el correo");
             request.getRequestDispatcher("/WEB-INF/views/inc1/forgot-password.jsp").forward(request, response);
         }
    }


}
