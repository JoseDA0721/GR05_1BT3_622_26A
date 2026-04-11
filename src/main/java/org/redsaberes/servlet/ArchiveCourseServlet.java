package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/archive-course")
public class ArchiveCourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CursoRepository cursoRepository = new CursoRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = session == null ? null : (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
            return;
        }

        Integer cursoId = parseInteger(request.getParameter("id"));
        if (cursoId == null) {
            response.sendRedirect(request.getContextPath() + "/my-courses?error=invalid_course");
            return;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || !isOwner(cursoOpt.get(), usuario)) {
            response.sendRedirect(request.getContextPath() + "/my-courses?error=course_not_found");
            return;
        }

        Curso curso = cursoOpt.get();
        curso.setEstado(EstadoCurso.ARCHIVADO);
        cursoRepository.update(curso);

        response.sendRedirect(request.getContextPath() + "/my-courses?success=archived");
    }

    private Integer parseInteger(String value) {
        try {
            return value == null ? null : Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean isOwner(Curso curso, Usuario usuario) {
        return curso != null
                && curso.getUsuario() != null
                && usuario != null
                && usuario.getId() != null
                && curso.getUsuario().getId() != null
                && curso.getUsuario().getId().equals(usuario.getId());
    }
}
