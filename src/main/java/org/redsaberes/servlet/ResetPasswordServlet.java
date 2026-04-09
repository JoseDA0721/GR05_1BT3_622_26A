package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.dao.UsuarioDAO;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                       HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            request.getRequestDispatcher(
                            "/WEB-INF/views/inc1/reset-password.jsp")
                    .forward(request, response);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response
    ) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        //Obtener parámetros del formulario
        String token = request.getParameter("token");
        String contrasena = request.getParameter("password");
        String confirmarContraseña = request.getParameter("confirmPassword");

        //Validaciones basicas
        try {
            //Validar los campos completos
            if(token == null || token.trim().isEmpty()
            || contrasena == null || contrasena.trim().isEmpty()
            || confirmarContraseña == null || confirmarContraseña.trim().isEmpty())
            {
                request.setAttribute("error", "Todos los campos son obligatorios");
                request.getRequestDispatcher("/WEB-INF/views/inc1/reset-password.jsp?token=" + token).forward(request, response);
                return;
            }

            //Validar que las contraseñas coincidan
            if (!confirmarContraseña.equals(contrasena)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                request.getRequestDispatcher("/WEB-INF/views/inc1/reset-password.jsp?token=" + token).forward(request, response);
                return;
            }

            //Validar longitud de contraseña
            if (contrasena.length() < 6) {
                request.setAttribute("error", "Las contraseña debe tener al menos 6 caracteres");
                request.getRequestDispatcher("/WEB-INF/views/inc1/reset-password.jsp?token=" + token).forward(request, response);
                return;
            }

            //Verificar valides del token
            UsuarioDAO dao = new UsuarioDAO();
            boolean tokenValido = dao.tokenValido(token);
            if (!tokenValido) {
                request.setAttribute("error", "El enlace de recuperación no es válido o ha expirado");
                request.getRequestDispatcher("/WEB-INF/views/inc1/reset-password.jsp?token=" + token).forward(request, response);
                return;
            }

            String contrasenaHashed = hashedContrasena(contrasena);

            dao.actualizarContrasena(token, contrasenaHashed);

             // Redirigir al login con mensaje de éxito
             response.sendRedirect(request.getContextPath() + "/login?passwordReset=success");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error del servidor: " + e.getMessage());
            request.getRequestDispatcher("reset-password.jsp?token=" + token).forward(request, response);
        }
    }

    private String hashedContrasena(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(12));
    }
}
