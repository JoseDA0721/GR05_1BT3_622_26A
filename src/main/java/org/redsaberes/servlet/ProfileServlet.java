package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.impl.UsuarioRepositoryImpl;
import org.redsaberes.service.UsuarioService;
import org.redsaberes.service.dto.DatosPublicosUsuarioDto;
import org.redsaberes.service.dto.VistaPerfilPublicoViewDto;
import org.redsaberes.service.impl.VistaPerfilPublicoMapper;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/explore");
            return;
        }

        Integer userId;
        try {
            userId = Integer.valueOf(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/explore");
            return;
        }

        try {
            // Primero obtener la entidad Usuario y sus cursos (con relaciones).
            // Evitamos llamar a servicios que puedan lanzar si el usuario no existe.
            UsuarioRepositoryImpl repo = new UsuarioRepositoryImpl();
            Optional<Usuario> usuarioOpt = repo.findById(userId);
            if (usuarioOpt.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/explore?error=user_not_found");
                return;
            }

            Usuario usuario = usuarioOpt.get();

            // Obtener datos públicos (estrellas, matches) usando el servicio ahora que existe el usuario
            UsuarioService usuarioService = ServiceFactory.getUsuarioService();
            DatosPublicosUsuarioDto datosPublicos = usuarioService.buscarDatosPublicos(userId);

            // Cargar cursos y relaciones en la entidad usuario para evitar LazyInitialization
            org.redsaberes.repository.impl.CursoRepositoryImpl cursoRepo = new org.redsaberes.repository.impl.CursoRepositoryImpl();
            java.util.List<org.redsaberes.model.Curso> cursos = cursoRepo.findByUsuarioIdWithRelations(userId);
            usuario.setCursos(cursos == null ? java.util.List.of() : cursos);

            // Mapear vista pública completa
            VistaPerfilPublicoMapper mapper = new VistaPerfilPublicoMapper();
            VistaPerfilPublicoViewDto perfil = mapper.map(usuario, datosPublicos.getMatchesActivos());

            request.setAttribute("perfilPublico", perfil);
            request.getRequestDispatcher("/WEB-INF/views/inc2/profile-public.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar perfil público", e);
            // Para facilitar depuración en entorno de desarrollo, reenviamos a la vista de explore
            // mostrando un mensaje de error en lugar de perder el stack en un redirect.
            request.setAttribute("profileLoadError", true);
            request.setAttribute("profileLoadErrorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/inc2/explore.jsp").forward(request, response);
        }
    }
}

