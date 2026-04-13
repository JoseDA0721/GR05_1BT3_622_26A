package org.redsaberes.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.model.LikeCurso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet("/like-course")
public class LikeCourseServlet extends HttpServlet {

    private final CursoRepository cursoRepository = new CursoRepositoryImpl();
    private final LikeCursoRepository likeCursoRepository = new LikeCursoRepositoryImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Integer cursoId = parseInteger(request.getParameter("cursoId"));
        if (cursoId == null) {
            response.sendRedirect(request.getContextPath() + "/explore?error=invalid_course");
            return;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/explore?error=course_not_found");
            return;
        }

        Curso curso = cursoOpt.get();
        if (curso.getEstado() != EstadoCurso.PUBLICO
                || curso.getUsuario() == null
                || curso.getUsuario().getId().equals(usuario.getId())) {
            response.sendRedirect(request.getContextPath() + "/explore");
            return;
        }

        if (!likeCursoRepository.existsByUsuarioAndCurso(usuario.getId(), cursoId)) {
            LikeCurso likeCurso = new LikeCurso();
            likeCurso.setCurso(curso);
            likeCurso.setUsuario(usuario);
            likeCurso.setFecha(LocalDateTime.now().toString());
            likeCursoRepository.save(likeCurso);
        }

        response.sendRedirect(request.getContextPath() + "/explore?success=liked");
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

