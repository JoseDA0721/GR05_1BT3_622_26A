package org.redsaberes.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.AcceptMatchService;
import org.redsaberes.service.dto.AcceptMatchOutcome;
import org.redsaberes.service.impl.AcceptMatchServiceImpl;

import java.io.IOException;

@WebServlet("/accept-match")
public class AcceptMatchServlet extends HttpServlet {

    private final AcceptMatchService acceptMatchService = new AcceptMatchServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario creador = (Usuario) session.getAttribute("usuario");
        Integer cursoId = parseInteger(request.getParameter("cursoId"));
        Integer usuarioObjetivoId = parseInteger(request.getParameter("usuarioObjetivoId"));

        if (cursoId == null || usuarioObjetivoId == null) {
            response.sendRedirect(request.getContextPath() + "/matches?error=invalid_data");
            return;
        }

        AcceptMatchOutcome outcome = acceptMatchService.acceptMatch(
                creador.getId(),
                cursoId,
                usuarioObjetivoId
        );

        if (outcome == AcceptMatchOutcome.INVALID_DATA) {
            response.sendRedirect(request.getContextPath() + "/matches?error=invalid_data");
            return;
        }

        if (outcome == AcceptMatchOutcome.FORBIDDEN) {
            response.sendRedirect(request.getContextPath() + "/matches?error=forbidden");
            return;
        }

        if (outcome == AcceptMatchOutcome.NO_LIKE) {
            response.sendRedirect(request.getContextPath() + "/matches?error=no_like");
            return;
        }


        response.sendRedirect(request.getContextPath() + "/matches?cursoId=" + cursoId + "&success=matched");
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

