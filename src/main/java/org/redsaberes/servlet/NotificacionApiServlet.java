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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * API REST para consultas de notificaciones (usado por polling cada 30s)
 * Retorna JSON con contador y items de notificaciones no leídas
 */
@WebServlet("/api/notificaciones/unread")
public class NotificacionApiServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(NotificacionApiServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private final NotificacionService notificacionService = ServiceFactory.getNotificacionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Unauthorized\",\"total\":0}");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            List<Notificacion> unreadNotificaciones = notificacionService.getUnread(usuario.getId());

            // Construir JSON manualmente o con biblioteca JSON
            String jsonResponse = buildJsonResponse(unreadNotificaciones);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener notificaciones no leídas (API)", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Internal Server Error\",\"total\":0}");
        }
    }

    /**
     * Construye respuesta JSON con total e items de notificaciones no leídas
     * Formato: { "total": 2, "items": [ { "id": 1, "descripcion": "...", "fechaCreacion": "2026-05-06" }, ... ] }
     */
    private String buildJsonResponse(List<Notificacion> notificaciones) {
        StringBuilder json = new StringBuilder();
        json.append("{\"total\":").append(notificaciones.size()).append(",\"items\":[");

        for (int i = 0; i < notificaciones.size(); i++) {
            Notificacion n = notificaciones.get(i);
            json.append("{")
                    .append("\"id\":").append(n.getId()).append(",")
                    .append("\"descripcion\":\"").append(escapeJson(n.getDescripcion())).append("\",")
                    .append("\"fechaCreacion\":\"").append(n.getFechaCreacion()).append("\"")
                    .append("}");

            if (i < notificaciones.size() - 1) {
                json.append(",");
            }
        }

        json.append("]}");
        return json.toString();
    }

    /**
     * Escapa caracteres especiales en JSON para evitar inyecciones
     */
    private String escapeJson(String input) {
        if (input == null) return "";
        return input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

