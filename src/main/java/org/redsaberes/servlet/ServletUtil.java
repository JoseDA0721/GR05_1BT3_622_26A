package org.redsaberes.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;

public class ServletUtil {

    public static Usuario getUsuarioSesion(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuario");
    }

    public static Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
