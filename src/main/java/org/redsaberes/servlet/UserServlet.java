package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redsaberes.service.UsuarioService;
import org.redsaberes.service.exception.ServiceValidationException;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UsuarioService usuarioService;

    public UserServlet() {
        this(ServiceFactory.getUsuarioService());
    }

    public UserServlet(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String nombre = request.getParameter("name");
        String correo = request.getParameter("email");
        String contrasena = request.getParameter("password");
        String confirmarContrasena = request.getParameter("confirmPassword");

        try {
            usuarioService.registrarUsuarioConValidacion(
                    nombre,
                    correo,
                    contrasena,
                    confirmarContrasena
            );

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}