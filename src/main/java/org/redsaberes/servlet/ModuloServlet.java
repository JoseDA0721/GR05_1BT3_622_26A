package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.redsaberes.model.*;
import org.redsaberes.repository.*;
import org.redsaberes.repository.impl.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/edit-course")
public class ModuloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CursoRepository cursoRepository =
        new CursoRepositoryImpl();
    private ModuloRepository moduloRepository =
        new ModuloRepositoryImpl();
    private LeccionRepository leccionRepository =
        new LeccionRepositoryImpl();

    // GET → cargar curso + módulos existentes → mostrar edit-course.jsp
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
        throws ServletException, IOException {

        String cursoIdStr = req.getParameter("id");

        // Validar que llegó el id del curso
        if (cursoIdStr == null || cursoIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        try {
            int cursoId = Integer.parseInt(cursoIdStr);

            // Verificar que el curso existe y pertenece al usuario en sesión
            Usuario usuario = (Usuario) req.getSession(false)
                .getAttribute("usuario");

            Optional<Curso> cursoOpt =
                cursoRepository.findById(cursoId);

            if (cursoOpt.isEmpty() ||
                !cursoOpt.get().getUsuario().getId()
                    .equals(usuario.getId())) {
                res.sendRedirect(
                    req.getContextPath() + "/my-courses");
                return;
            }

            // DS CU-05: cargar módulos con lecciones
            List<Modulo> modulos = moduloRepository
                .findByCursoIdWithLecciones(cursoId);

            req.setAttribute("curso", cursoOpt.get());
            req.setAttribute("modulos", modulos);

            // Pasar mensaje de éxito si viene en el parámetro
            String msg = req.getParameter("msg");
            if ("modulo-agregado".equals(msg)) {
                req.setAttribute("exito",
                    "Módulo y lección guardados correctamente.");
            }

            req.getRequestDispatcher(
                "/WEB-INF/views/inc1/edit-course.jsp")
               .forward(req, res);

        } catch (NumberFormatException e) {
            res.sendRedirect(
                req.getContextPath() + "/my-courses");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(
                req.getContextPath() + "/my-courses");
        }
    }

    // POST → procesarNuevoModulo(datosMódulo, datosLeccion)
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
        throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String cursoIdStr       = req.getParameter("cursoId");
        String tituloModulo     = req.getParameter("tituloModulo");
        String tituloLeccion    = req.getParameter("tituloLeccion");
        String contenidoLeccion = req.getParameter("contenidoLeccion");

        // Validar campos obligatorios (RF-C02)
        if (tituloModulo == null || tituloModulo.trim().isEmpty() ||
            tituloLeccion == null || tituloLeccion.trim().isEmpty()) {

            req.setAttribute("error",
                "El título del módulo y de la primera " +
                "lección son obligatorios.");
            recargarVista(req, res,
                Integer.parseInt(cursoIdStr));
            return;
        }

        try {
            int cursoId = Integer.parseInt(cursoIdStr);
            Optional<Curso> cursoOpt =
                cursoRepository.findById(cursoId);

            if (cursoOpt.isEmpty()) {
                res.sendRedirect(
                    req.getContextPath() + "/my-courses");
                return;
            }

            Curso curso = cursoOpt.get();

            // DS CU-05: calcular orden del nuevo módulo
            List<Modulo> modulosExistentes =
                moduloRepository.findByCursoId(cursoId);
            int nuevoOrden = modulosExistentes.size() + 1;

            // DS CU-05: agregarModulo(tituloModulo)
            // → instanciaModuloCreado
            Modulo modulo = new Modulo(
                null,
                tituloModulo.trim(),
                nuevoOrden,
                curso
            );
            moduloRepository.save(modulo);

            // DS CU-05: crearLeccion(tituloLeccion, contenido)
            // → vincularLeccion(instanciaLeccion)
            Leccion leccion = new Leccion(
                null,
                tituloLeccion.trim(),
                contenidoLeccion,
                modulo
            );
            leccionRepository.save(leccion);

            // DS CU-05: moduloAgregadoExitosamente()
            res.sendRedirect(
                req.getContextPath()
                + "/edit-course?id=" + cursoId
                + "&msg=modulo-agregado");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error",
                "Error al guardar el módulo: " + e.getMessage());
            recargarVista(req, res,
                Integer.parseInt(cursoIdStr));
        }
    }

    // Helper: recargar la vista con datos actualizados
    private void recargarVista(HttpServletRequest req,
                                HttpServletResponse res,
                                int cursoId)
        throws ServletException, IOException {

        Optional<Curso> cursoOpt =
            cursoRepository.findById(cursoId);
        List<Modulo> modulos =
            moduloRepository.findByCursoIdWithLecciones(cursoId);

        req.setAttribute("curso", cursoOpt.orElse(null));
        req.setAttribute("modulos", modulos);
        req.getRequestDispatcher(
            "/WEB-INF/views/inc1/edit-course.jsp")
           .forward(req, res);
    }
}
