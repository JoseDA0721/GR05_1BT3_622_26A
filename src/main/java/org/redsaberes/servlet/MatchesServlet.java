package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.MatchesService;
import org.redsaberes.service.dto.MatchesPageDataDto;
import org.redsaberes.service.impl.MatchesServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MatchesServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final MatchesService matchesService = new MatchesServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            Integer filterCourseId = parseInteger(request.getParameter("cursoId"));

            MatchesPageDataDto data = matchesService.buildMatchesPageData(usuario.getId(), filterCourseId);

            request.setAttribute("interested", data.getInterested());
            request.setAttribute("interestedCount", data.getInterested().size());
            request.setAttribute("matches", data.getMatches());
            request.setAttribute("matchesCount", data.getMatches().size());
            request.setAttribute("selectedCourseId", data.getSelectedCourseId());
            request.setAttribute("myCourses", data.getMyCourses());
            request.getRequestDispatcher("/WEB-INF/views/inc2/matches.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar matches", e);
            response.sendRedirect(request.getContextPath() + "/dashboard?error=1");
        }
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

