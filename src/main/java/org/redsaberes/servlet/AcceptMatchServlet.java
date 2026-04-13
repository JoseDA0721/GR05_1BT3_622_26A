package org.redsaberes.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Curso;
import org.redsaberes.model.MatchCurso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet("/accept-match")
public class AcceptMatchServlet extends HttpServlet {

    private final CursoRepository cursoRepository = new CursoRepositoryImpl();
    private final LikeCursoRepository likeCursoRepository = new LikeCursoRepositoryImpl();
    private final MatchCursoRepository matchCursoRepository = new MatchCursoRepositoryImpl();

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

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || cursoOpt.get().getUsuario() == null
                || !creador.getId().equals(cursoOpt.get().getUsuario().getId())) {
            response.sendRedirect(request.getContextPath() + "/matches?error=forbidden");
            return;
        }

        if (!likeCursoRepository.existsByUsuarioAndCurso(usuarioObjetivoId, cursoId)) {
            response.sendRedirect(request.getContextPath() + "/matches?error=no_like");
            return;
        }

        if (!matchCursoRepository.existsByCursoAndUsuario(cursoId, usuarioObjetivoId)) {
            Usuario estudiante = new Usuario();
            estudiante.setId(usuarioObjetivoId);

            MatchCurso match = new MatchCurso();
            match.setCurso(cursoOpt.get());
            match.setCreador(creador);
            match.setEstudiante(estudiante);
            match.setFechaConfirmacion(LocalDateTime.now().toString());
            matchCursoRepository.save(match);
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

