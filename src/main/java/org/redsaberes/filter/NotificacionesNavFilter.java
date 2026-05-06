package org.redsaberes.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.NotificacionService;
import org.redsaberes.servlet.ServiceFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = {"/dashboard", "/explore", "/my-courses", "/matches", "/notificaciones"})
public class NotificacionesNavFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(NotificacionesNavFilter.class.getName());
    private final NotificacionService notificacionService = ServiceFactory.getNotificacionService();

    @Override
    public void doFilter(jakarta.servlet.ServletRequest req, jakarta.servlet.ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        List<?> noLeidas = Collections.emptyList();

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object usuarioObj = session.getAttribute("usuario");
                if (usuarioObj instanceof Usuario usuario && usuario.getId() != null) {
                    noLeidas = notificacionService.getUnread(usuario.getId());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No se pudieron cargar notificaciones para navbar", e);
        }

        request.setAttribute("notificacionesNoLeidas", noLeidas);
        request.setAttribute("totalNotificaciones", noLeidas.size());

        chain.doFilter(request, response);
    }
}