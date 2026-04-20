package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.CourseLifecycleService;
import org.redsaberes.service.dto.CourseLifecycleOutcome;
import org.redsaberes.service.dto.PublishCourseViewDto;
import org.redsaberes.service.impl.CourseLifecycleServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/publicar")
public class PublicarServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(PublicarServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final CourseLifecycleService courseLifecycleService = new CourseLifecycleServiceImpl();

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
            Integer cursoId = Integer.parseInt(idStr);
            Usuario usuario = getUsuarioSesion(request);
            if(usuario == null){
                response.sendRedirect(
                        request.getContextPath() + "/login"
                );
                return;
            }

            PublishCourseViewDto viewData = courseLifecycleService.preparePublishView(cursoId, usuario.getId());
            if (viewData.getOutcome() == CourseLifecycleOutcome.NOT_FOUND_OR_FORBIDDEN) {
                response.sendRedirect(
                        request.getContextPath() + "/my-courses"
                );
                return;
            }

            if (viewData.getOutcome() == CourseLifecycleOutcome.ALREADY_PUBLIC) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/edit-course?id=" + cursoId
                        + "&msg=ya-publicado"
                );
                return;
            }

            if (viewData.getOutcome() == CourseLifecycleOutcome.NO_CONTENT) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/edit-course?id=" + cursoId
                        + "&msg=sin-contenido"
                );
                return;
            }

            request.setAttribute("curso", viewData.getCurso());
            request.setAttribute("modulos", viewData.getModulos());
            request.getRequestDispatcher(
                    "/WEB-INF/views/inc1/edit-course.jsp")
                    .forward(request, response);
            
        } catch (NumberFormatException e){
            response.sendRedirect(
                    request.getContextPath() + "/my-courses"
            );
        } catch(Exception e){
            LOGGER.log(Level.SEVERE, "Error al preparar vista de publicación", e);
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
            Integer cursoId = Integer.parseInt(idStr);
            Usuario usuario = getUsuarioSesion(request);

            if(usuario == null){
                response.sendRedirect(
                        request.getContextPath() + "/login"
                );
                return;
            }

            CourseLifecycleOutcome outcome = courseLifecycleService.publishCourse(cursoId, usuario.getId());

            if (outcome == CourseLifecycleOutcome.NOT_FOUND_OR_FORBIDDEN) {
                response.sendRedirect(
                        request.getContextPath() + "/my-courses"
                );
                return;
            }

            if (outcome == CourseLifecycleOutcome.NO_CONTENT) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/edit-course?id=" + cursoId
                                + "&msg=sin-contenido");
                return;
            }

            // DS CU-07: publicacionExitosa()
            // → mostrarMensaje("Publicación exitosa")
            response.sendRedirect(
                    request.getContextPath()
                            + "/my-courses?success=published");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al publicar curso", e);
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
}
