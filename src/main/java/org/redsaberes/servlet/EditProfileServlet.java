package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.UsuarioService;
import org.redsaberes.service.exception.ServiceValidationException;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/profile/edit")
public class EditProfileServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EditProfileServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private final UsuarioService usuarioService = ServiceFactory.getUsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        populateForm(request, usuario);
        request.getRequestDispatcher("/WEB-INF/views/inc1/edit-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        String nombre = request.getParameter("name");
        String correo = request.getParameter("email");

        // Campos presentes en UI por requerimiento, sin funcionalidad de cambio de clave por ahora.
        request.getParameter("password");
        request.getParameter("confirmPassword");

        try {
            Usuario actualizado = usuarioService.actualizarPerfilBasico(usuarioSesion.getId(), nombre, correo);
            session.setAttribute("usuario", actualizado);
            request.setAttribute("success", "Perfil actualizado correctamente");
            populateForm(request, actualizado);
            request.getRequestDispatcher("/WEB-INF/views/inc1/edit-profile.jsp").forward(request, response);
        } catch (ServiceValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("formName", nombre);
            request.setAttribute("formEmail", correo);
            request.getRequestDispatcher("/WEB-INF/views/inc1/edit-profile.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar perfil", e);
            request.setAttribute("error", "No se pudo actualizar el perfil. Intenta nuevamente.");
            request.setAttribute("formName", nombre);
            request.setAttribute("formEmail", correo);
            request.getRequestDispatcher("/WEB-INF/views/inc1/edit-profile.jsp").forward(request, response);
        }
    }

    private void populateForm(HttpServletRequest request, Usuario usuario) {
        request.setAttribute("formName", usuario != null ? usuario.getNombre() : "");
        request.setAttribute("formEmail", usuario != null ? usuario.getCorreoElectronico() : "");
    }
}

