package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.model.Modulo;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.ModuloRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.ModuloRepositoryImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/publicar")
public class PublicarServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CursoRepository cursoRepository = new CursoRepositoryImpl();
    private ModuloRepository moduloRepository = new ModuloRepositoryImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response
    )throws ServletException, IOException{
        
        String idStr =  request.getParameter("id");
        if(idStr == null||idStr.trim().isEmpty()){
            response.sendRedirect(
                    request.getContextPath() + "/my-courses"
            );
            return;
        }
        
        try{
            int cursoId = Integer.parseInt(idStr);
            Usuario usuario = getUsuarioSesion(request);
            if(usuario == null){
                response.sendRedirect(
                        request.getContextPath() + "/login"
                );
                return;
            }
            
            Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
            
            //Verificar existencia y propiedad
            if(cursoOpt.isEmpty() ||
            !isOwner(cursoOpt.get(), usuario)){
                response.sendRedirect(
                        request.getContextPath() + "/my-courses"
                );
                return;
            }

            Curso curso = cursoOpt.get();

            //Ya publicado -> redirigir aviso
            if(curso.getEstado() == EstadoCurso.PUBLICO){
                response.sendRedirect(
                        request.getContextPath()
                        + "/edit-course?id" + cursoId
                        + "&msg=ya-publicado"
                );
                return;
            }

            List<Modulo> modulos = moduloRepository
                    .findByCursoIdWithLecciones(cursoId);

            boolean tieneContenido = !modulos.isEmpty()
                    && modulos.stream().anyMatch(
                            m -> !m.getLecciones().isEmpty());

            if(!tieneContenido){
                response.sendRedirect(
                        request.getContextPath()
                        + "/edit-course?id" + cursoId
                        + "&msg=sin-contenido"
                );
                return;
            }

            request.setAttribute("curso", curso);
            request.setAttribute("modulos", modulos);
            request.getRequestDispatcher(
                    "/WEB-INF/views/inc1/edit-course.jsp")
                    .forward(request, response);
            
        } catch (NumberFormatException e){
            response.sendRedirect(
                    request.getContextPath() + "/my-courses"
            );
        } catch(Exception e){
            e.printStackTrace();
            response.sendRedirect(
                    request.getContextPath() + "/my-courses");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String idStr = request.getParameter("cursoId");
        if(idStr == null || idStr.trim().isEmpty()){
            response.sendRedirect(
                    request.getContextPath() + "/my-courses"
            );
            return;
        }

        try{
            int cursoId = Integer.parseInt(idStr);
            Usuario usuario = getUsuarioSesion(request);

            if(usuario == null){
                response.sendRedirect(
                        request.getContextPath() + "/login"
                );
                return;
            }

            Optional<Curso> cursoOpt = cursoRepository.
                    findById(cursoId);

            //Verificar existencia y propiedad
            if(cursoOpt.isEmpty() ||
                !isOwner(cursoOpt.get(), usuario)){
                response.sendRedirect(
                        request.getContextPath() + "/my-courses"
                );
                return;
            }

            Curso curso = cursoOpt.get();

            List<Modulo> modulos = moduloRepository
                    .findByCursoIdWithLecciones(cursoId);

            boolean tieneContenido =
                    !modulos.isEmpty() &&
                            modulos.stream().anyMatch(
                                    m -> !m.getLecciones().isEmpty());

            if (!tieneContenido) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/edit-course?id=" + cursoId
                                + "&msg=sin-contenido");
                return;
            }

            // DS CU-07: publicarCurso()
            // → estadoActualizado(PUBLICADO)
            curso.setEstado(EstadoCurso.PUBLICO);
            cursoRepository.update(curso);

            System.out.println(
                    "=== CURSO PUBLICADO ===");
            System.out.println(
                    "Id: " + cursoId
                            + " | Titulo: " + curso.getTitulo());
            System.out.println(
                    "Estado: PUBLICO");
            System.out.println(
                    "======================");

            // DS CU-07: publicacionExitosa()
            // → mostrarMensaje("Publicación exitosa")
            response.sendRedirect(
                    request.getContextPath()
                            + "/my-courses?success=published");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(
                    request.getContextPath()
                            + "/my-courses?error=publicar");
        }
    }

    private Usuario getUsuarioSesion(
            HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        return (Usuario) session.getAttribute("usuario");
    }

    private boolean isOwner(Curso curso,
                            Usuario usuario) {
        return curso != null
                && curso.getUsuario() != null
                && usuario != null
                && usuario.getId() != null
                && curso.getUsuario().getId()
                .equals(usuario.getId());
    }
}
