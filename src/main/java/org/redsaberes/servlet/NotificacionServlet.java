package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Notificacion;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.NotificacionService;

import java.io.IOException;
import java.io.Serial;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/notificaciones")
public class NotificacionServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(NotificacionServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private final NotificacionService notificacionService = ServiceFactory.getNotificacionService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
    ) throws ServletException, IOException {
        try{
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");

            // Obtener todas las notificaciones del usuario (historial completo)
            var todasNotificaciones = notificacionService.getAllNotifications(usuario.getId());

            request.setAttribute("todasNotificaciones", todasNotificaciones);
            request.setAttribute("totalNotificaciones", todasNotificaciones.size());
            request.getRequestDispatcher("/WEB-INF/views/inc2/notificaciones.jsp").forward(request, response);

        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar notificaciones", e);
            response.sendRedirect(request.getContextPath() + "/login?error=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
                return;
            }

            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

            String notificacionIdParam = request.getParameter("notificacionId");
            if (notificacionIdParam == null || notificacionIdParam.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/notificaciones?error=invalid_id");
                return;
            }

            Integer notificacionId;
            try {
                notificacionId = Integer.parseInt(notificacionIdParam);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "ID de notificacion invalido: {0}", notificacionIdParam);
                response.sendRedirect(request.getContextPath() + "/notificaciones?error=invalid_format");
                return;
            }

            Optional<Notificacion> notificacionOpt = notificacionService.getNotificacionById(notificacionId);
            if (notificacionOpt.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/notificaciones?error=not_found");
                return;
            }

            Notificacion notificacion = notificacionOpt.get();

            // Seguridad: impedir marcar notificaciones de otro usuario
            if (notificacion.getUsuarioReceptor() == null
                    || notificacion.getUsuarioReceptor().getId() == null
                    || !notificacion.getUsuarioReceptor().getId().equals(usuarioSesion.getId())) {
                response.sendRedirect(request.getContextPath() + "/notificaciones?error=forbidden");
                return;
            }

            notificacionService.markAsRead(notificacion);

            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isBlank()) {
                response.sendRedirect(referer);
            } else {
                response.sendRedirect(request.getContextPath() + "/notificaciones");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al marcar notificacion como leida", e);
            response.sendRedirect(request.getContextPath() + "/notificaciones?error=1");
        }
    }

}
