package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.util.ImagenUtil;

import java.io.IOException;

@WebServlet("/create-course")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 5,        // 5MB
        maxRequestSize = 1024 * 1024 * 10     // 10MB
)
public class CursoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CursoRepository cursoRepository = new CursoRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
    ) throws ServletException, IOException {

        // Verificar que el usuario está autenticado
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Usuario autenticado, mostrar formulario de crear curso
        request.getRequestDispatcher("/WEB-INF/views/inc1/create-course.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        //Obtener usuario logeado
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        //Obtener parámetros del formulario
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        String nivel = request.getParameter("nivelDificultad");

        if (titulo == null || titulo.trim().isEmpty() || descripcion == null || descripcion.trim().isEmpty()){
            request.setAttribute("error", "El título y la descripción son obligatorios.");
            request.getRequestDispatcher("/WEB-INF/views/inc1/create-course.jsp").forward(request, response);
            return;
        }

        if (titulo.length() > 100){
            request.setAttribute("error", "El título no puede exceder los 100 caracteres.");
            request.getRequestDispatcher("/WEB-INF/views/inc1/create-course.jsp").forward(request, response);
            return;
        }

        try {
            String uploadDir = getServletContext()
                    .getRealPath("")
                    + "uploads" + java.io.File.separator
                    + "portadas";
            Part filePart = request.getPart("imagenPortada");
            String fileName = ImagenUtil.guardarImagen(filePart, uploadDir);

            Curso curso = new Curso(
                    null,
                    titulo.trim(),
                    descripcion,
                    categoria,
                    nivel,
                    fileName,
                    EstadoCurso.BORRADOR,
                    usuario
            );

            cursoRepository.save(curso);

            response.sendRedirect(request.getContextPath()
                    + "/my-courses?success=created");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al crear el curso."
            + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/inc1/create-course.jsp").forward(request, response);
        }
    }
}
