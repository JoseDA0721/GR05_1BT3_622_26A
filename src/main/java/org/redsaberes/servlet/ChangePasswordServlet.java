package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.ChangePasswordService;
import org.redsaberes.service.exception.ServiceValidationException;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ChangePasswordServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private final ChangePasswordService changePasswordService = ServiceFactory.getChangePassword();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        request.setAttribute("usuarioNombre", usuario.getNombre());
        request.getRequestDispatcher("/WEB-INF/views/inc1/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            changePasswordService.cambiarContrasena(usuario.getId(), currentPassword, newPassword, confirmPassword);
            request.setAttribute("success", "Tu contraseña se actualizó correctamente");
            request.setAttribute("usuarioNombre", usuario.getNombre());
            request.getRequestDispatcher("/WEB-INF/views/inc1/change-password.jsp").forward(request, response);
        } catch (ServiceValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("usuarioNombre", usuario.getNombre());
            request.getRequestDispatcher("/WEB-INF/views/inc1/change-password.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cambiar contraseña", e);
            request.setAttribute("error", "No se pudo cambiar la contraseña. Intenta nuevamente.");
            request.setAttribute("usuarioNombre", usuario.getNombre());
            request.getRequestDispatcher("/WEB-INF/views/inc1/change-password.jsp").forward(request, response);
        }
    }
}

