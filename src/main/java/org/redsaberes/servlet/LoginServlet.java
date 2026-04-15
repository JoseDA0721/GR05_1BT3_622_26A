package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.AuthService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.impl.AuthServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final AuthService authService = new AuthServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);
        } catch(Exception e){
            LOGGER.log(Level.SEVERE, "Error al mostrar la vista de login", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try{
            String correo = request.getParameter("email");
            String contrasena = request.getParameter("password");
            String remember = request.getParameter("remember");

            Usuario usuario = authService.autenticar(correo, contrasena);

            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("token", usuario.getTokenSesion());

            if(remember != null){
                Cookie cookie = new Cookie("email", correo);
                cookie.setMaxAge(30 * 24 * 60 * 60);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }

            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (ServiceValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);

        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "Error al autenticar usuario", e);
            request.setAttribute("error", "Error del servidor");
            request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);
        }

    }


}
