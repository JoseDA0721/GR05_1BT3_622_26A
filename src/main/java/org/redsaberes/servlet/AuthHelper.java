package org.redsaberes.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;

import java.io.IOException;

/**
 * Helper utilities for authentication/session checks used by servlets.
 */
public final class AuthHelper {

    private AuthHelper() {
        // utility
    }

    /**
     * Obtains the current session user or redirects to login if session expired/missing.
     *
     * @param request  http request
     * @param response http response (used for redirect)
     * @return Usuario from session or null if redirected
     * @throws IOException if sendRedirect fails
     */
    public static Usuario getSessionUserOrRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
            return null;
        }
        return (Usuario) session.getAttribute("usuario");
    }
}

