package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.redsaberes.model.*;
import org.redsaberes.repository.*;
import org.redsaberes.repository.impl.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    // GET: list (default), edit, reorder
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
        throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "edit":
                    handleEditGet(req, res);
                    break;
                case "reorder":
                    handleReorderGet(req, res);
                    break;
                case "list":
                default:
                    handleListGet(req, res);
                    break;
            }
        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/my-courses");
        }
    }

    // POST: create (default CU-05), estructura lista para nuevos casos
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
        throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "create";
        }

        switch (action) {
            case "update":
                handleUpdatePost(req, res);
                break;
            case "delete":
                handleDeletePost(req, res);
                break;
            case "saveOrder":
                handleSaveOrderPost(req, res);
                break;
            case "create":
            default:
                handleCreatePost(req, res);
                break;
        }
    }

    private void handleListGet(HttpServletRequest req,
                               HttpServletResponse res)
        throws ServletException, IOException {

        String cursoIdStr = req.getParameter("id");
        if (cursoIdStr == null || cursoIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Integer cursoId = Integer.parseInt(cursoIdStr);
        Usuario usuario = getUsuarioSesion(req);
        if (usuario == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || !isOwner(cursoOpt.get(), usuario)) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        List<Modulo> modulos = moduloRepository.findByCursoIdWithLecciones(cursoId);
        req.setAttribute("curso", cursoOpt.get());
        req.setAttribute("modulos", modulos);

        String msg = req.getParameter("msg");
        applyFlashMessage(req, msg);

        req.getRequestDispatcher("/WEB-INF/views/inc1/edit-course.jsp")
            .forward(req, res);
    }

    // action=edit: carga modulo puntual para prellenar formulario de edicion
    private void handleEditGet(HttpServletRequest req,
                               HttpServletResponse res)
        throws ServletException, IOException {

        String moduloIdStr = req.getParameter("id");
        if (moduloIdStr == null || moduloIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Integer moduloId = Integer.parseInt(moduloIdStr);
        Usuario usuario = getUsuarioSesion(req);
        if (usuario == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
        if (moduloOpt.isEmpty() || moduloOpt.get().getCurso() == null) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Curso curso = moduloOpt.get().getCurso();
        if (!isOwner(curso, usuario)) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        List<Modulo> modulos = moduloRepository.findByCursoIdWithLecciones(curso.getId());
        req.setAttribute("curso", curso);
        req.setAttribute("modulos", modulos);
        req.setAttribute("moduloEditar", moduloOpt.get());
        req.setAttribute("accionModulo", "edit");
        applyFlashMessage(req, req.getParameter("msg"));

        req.getRequestDispatcher("/WEB-INF/views/inc1/manage-modules.jsp")
            .forward(req, res);
    }

    // action=reorder: envia lista de modulos para UI de reordenamiento
    private void handleReorderGet(HttpServletRequest req,
                                  HttpServletResponse res)
        throws ServletException, IOException {

        String cursoIdStr = req.getParameter("cursoId");
        if (cursoIdStr == null || cursoIdStr.trim().isEmpty()) {
            cursoIdStr = req.getParameter("id");
        }

        if (cursoIdStr == null || cursoIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Integer cursoId = Integer.parseInt(cursoIdStr);
        Usuario usuario = getUsuarioSesion(req);
        if (usuario == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || !isOwner(cursoOpt.get(), usuario)) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        List<Modulo> modulosOrdenables = moduloRepository.findByCursoId(cursoId);
        req.setAttribute("curso", cursoOpt.get());
        req.setAttribute("modulos", modulosOrdenables);
        req.setAttribute("modoReordenar", true);
        req.setAttribute("accionModulo", "reorder");
        applyFlashMessage(req, req.getParameter("msg"));

        req.getRequestDispatcher("/WEB-INF/views/inc1/manage-modules.jsp")
            .forward(req, res);
    }

    private void handleCreatePost(HttpServletRequest req,
                                  HttpServletResponse res)
        throws ServletException, IOException {

        String cursoIdStr = req.getParameter("cursoId");
        String tituloModulo = req.getParameter("tituloModulo");
        String tituloLeccion = req.getParameter("tituloLeccion");
        String contenidoLeccion = req.getParameter("contenidoLeccion");

        if (cursoIdStr == null || cursoIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        // Validar campos obligatorios (RF-C02)
        if (tituloModulo == null || tituloModulo.trim().isEmpty() ||
            tituloLeccion == null || tituloLeccion.trim().isEmpty()) {

            req.setAttribute("error", "El titulo del modulo y de la primera leccion son obligatorios.");
            recargarVista(req, res, Integer.parseInt(cursoIdStr));
            return;
        }

        try {
            int cursoId = Integer.parseInt(cursoIdStr);
            Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
            Usuario usuario = getUsuarioSesion(req);

            if (cursoOpt.isEmpty() || usuario == null || !isOwner(cursoOpt.get(), usuario)) {
                res.sendRedirect(req.getContextPath() + "/my-courses");
                return;
            }

            Curso curso = cursoOpt.get();

            List<Modulo> modulosExistentes = moduloRepository.findByCursoId(cursoId);
            int nuevoOrden = modulosExistentes.size() + 1;

            Modulo modulo = new Modulo(
                null,
                tituloModulo.trim(),
                nuevoOrden,
                curso
            );
            moduloRepository.save(modulo);

            Leccion leccion = new Leccion(
                null,
                tituloLeccion.trim(),
                contenidoLeccion,
                modulo
            );
            leccionRepository.save(leccion);

            res.sendRedirect(
                req.getContextPath()
                + "/edit-course?id=" + cursoId
                + "&msg=modulo-agregado"
            );

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error al guardar el modulo: " + e.getMessage());
            recargarVista(req, res, Integer.parseInt(cursoIdStr));
        }
    }

    private void handleUpdatePost(HttpServletRequest req,
                                  HttpServletResponse res)
        throws IOException {

        String moduloIdStr = req.getParameter("moduloId");
        String tituloModulo = req.getParameter("tituloModulo");
        String ordenStr = req.getParameter("orden");

        if (moduloIdStr == null || moduloIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Integer moduloId = Integer.parseInt(moduloIdStr);
        Usuario usuario = getUsuarioSesion(req);
        Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);

        if (usuario == null || moduloOpt.isEmpty() || moduloOpt.get().getCurso() == null
            || !isOwner(moduloOpt.get().getCurso(), usuario)) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Modulo modulo = moduloOpt.get();
        if (tituloModulo != null && !tituloModulo.trim().isEmpty()) {
            modulo.setTitulo(tituloModulo.trim());
        }
        if (ordenStr != null && !ordenStr.trim().isEmpty()) {
            int nuevoOrden = Integer.parseInt(ordenStr);
            if (nuevoOrden > 0) {
                modulo.setOrden(nuevoOrden);
            }
        }

        moduloRepository.update(modulo);

        res.sendRedirect(
            req.getContextPath()
            + "/edit-course?action=edit&id=" + modulo.getId()
            + "&msg=modulo-actualizado"
        );
    }

    private void handleDeletePost(HttpServletRequest req,
                                  HttpServletResponse res)
        throws IOException {

        String moduloIdStr = req.getParameter("moduloId");
        if (moduloIdStr == null || moduloIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Integer moduloId = Integer.parseInt(moduloIdStr);
        Usuario usuario = getUsuarioSesion(req);
        Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);

        if (usuario == null || moduloOpt.isEmpty() || moduloOpt.get().getCurso() == null
            || !isOwner(moduloOpt.get().getCurso(), usuario)) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Integer cursoId = moduloOpt.get().getCurso().getId();
        moduloRepository.delete(moduloId);

        res.sendRedirect(
            req.getContextPath()
            + "/edit-course?action=reorder&cursoId=" + cursoId
            + "&msg=modulo-eliminado"
        );
    }

    private void handleSaveOrderPost(HttpServletRequest req,
                                     HttpServletResponse res)
        throws IOException {

        String cursoIdStr = req.getParameter("cursoId");
        if (cursoIdStr == null || cursoIdStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        Integer cursoId = Integer.parseInt(cursoIdStr);
        Usuario usuario = getUsuarioSesion(req);
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);

        if (usuario == null || cursoOpt.isEmpty() || !isOwner(cursoOpt.get(), usuario)) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        List<Integer> orderedIds = parseOrderedIds(req);
        if (orderedIds.isEmpty()) {
            res.sendRedirect(
                req.getContextPath()
                + "/edit-course?action=reorder&cursoId=" + cursoId
                + "&msg=orden-vacio"
            );
            return;
        }

        int orden = 1;
        for (Integer moduloId : orderedIds) {
            Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
            if (moduloOpt.isEmpty() || moduloOpt.get().getCurso() == null
                || !cursoId.equals(moduloOpt.get().getCurso().getId())) {
                continue;
            }
            Modulo modulo = moduloOpt.get();
            modulo.setOrden(orden++);
            moduloRepository.update(modulo);
        }

        res.sendRedirect(
            req.getContextPath()
            + "/edit-course?action=reorder&cursoId=" + cursoId
            + "&msg=orden-guardado"
        );
    }

    private List<Integer> parseOrderedIds(HttpServletRequest req) {
        List<Integer> orderedIds = new java.util.ArrayList<>();

        String[] multiple = req.getParameterValues("moduloIds");
        if (multiple != null) {
            for (String idStr : multiple) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    orderedIds.add(Integer.parseInt(idStr.trim()));
                }
            }
            return orderedIds;
        }

        String orderedIdsCsv = req.getParameter("orderedIds");
        if (orderedIdsCsv != null && !orderedIdsCsv.trim().isEmpty()) {
            String[] parts = orderedIdsCsv.split(",");
            for (String part : parts) {
                if (part != null && !part.trim().isEmpty()) {
                    orderedIds.add(Integer.parseInt(part.trim()));
                }
            }
        }

        return orderedIds;
    }

    // 1. Definimos la estructura
    private static final Map<String, String[]> FLASH_MESSAGES = Map.of(
            "modulo-agregado", new String[]{"exito", "Modulo y leccion guardados correctamente."},
            "modulo-actualizado", new String[]{"exito", "Modulo actualizado correctamente."},
            "modulo-eliminado", new String[]{"exito", "Modulo eliminado correctamente."},
            "orden-guardado", new String[]{"exito", "Orden de modulos guardado correctamente."},
            "orden-vacio", new String[]{"error", "No se recibio ningun modulo para reordenar."},
            "sin-contenido", new String[]{"error", "El curso necesita al menos un módulo con una lección para publicarse."},
            "ya-publicado", new String[]{"exito", "Este curso ya está publicado."}
    );

    //2. Nuevo algoritmo
    private void applyFlashMessage(HttpServletRequest req, String msg) {
        if (msg == null || !FLASH_MESSAGES.containsKey(msg)) {
            return;
        }

        String[] config = FLASH_MESSAGES.get(msg);
        req.setAttribute(config[0], config[1]);
    }

    private Usuario getUsuarioSesion(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuario");
    }

    private boolean isOwner(Curso curso, Usuario usuario) {
        return curso != null
            && curso.getUsuario() != null
            && curso.getUsuario().getId() != null
            && usuario != null
            && usuario.getId() != null
            && curso.getUsuario().getId().equals(usuario.getId());
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
