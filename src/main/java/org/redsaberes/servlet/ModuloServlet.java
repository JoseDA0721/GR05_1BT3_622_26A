package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.ModuloManagementService;
import org.redsaberes.service.dto.ModuloCommandOutcome;
import org.redsaberes.service.dto.ModuloCommandResultDto;
import org.redsaberes.service.dto.ModuloCreateOutcome;
import org.redsaberes.service.dto.ModuloCreateResultDto;
import org.redsaberes.service.dto.ModuloPageDataDto;
import org.redsaberes.service.dto.ModuloViewOutcome;
import org.redsaberes.service.impl.ModuloManagementServiceImpl;

import java.io.IOException;
import java.io.Serial;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/edit-course")
public class ModuloServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ModuloServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;

    private final ModuloManagementService moduloManagementService =
        new ModuloManagementServiceImpl();

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
            LOGGER.log(Level.SEVERE, "Error al cargar gestión de módulos", e);
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
        Integer cursoId = (cursoIdStr == null || cursoIdStr.trim().isEmpty())
            ? null
            : Integer.parseInt(cursoIdStr);
        Usuario usuario = ServletUtil.getUsuarioSesion(req);
        ModuloPageDataDto data = moduloManagementService.buildListView(
            cursoId,
            usuario == null ? null : usuario.getId()
        );

        if (data.getOutcome() == ModuloViewOutcome.REDIRECT_LOGIN) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (data.getOutcome() != ModuloViewOutcome.OK) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        req.setAttribute("curso", data.getCurso());
        req.setAttribute("modulos", data.getModulos());

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
        Integer moduloId = (moduloIdStr == null || moduloIdStr.trim().isEmpty())
            ? null
            : Integer.parseInt(moduloIdStr);
        Usuario usuario = ServletUtil.getUsuarioSesion(req);
        ModuloPageDataDto data = moduloManagementService.buildEditView(
            moduloId,
            usuario == null ? null : usuario.getId()
        );

        if (data.getOutcome() == ModuloViewOutcome.REDIRECT_LOGIN) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (data.getOutcome() != ModuloViewOutcome.OK) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        req.setAttribute("curso", data.getCurso());
        req.setAttribute("modulos", data.getModulos());
        req.setAttribute("moduloEditar", data.getModuloEditar());
        req.setAttribute("accionModulo", data.getAccionModulo());
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

        Integer cursoId = (cursoIdStr == null || cursoIdStr.trim().isEmpty())
            ? null
            : Integer.parseInt(cursoIdStr);
        Usuario usuario = ServletUtil.getUsuarioSesion(req);
        ModuloPageDataDto data = moduloManagementService.buildReorderView(
            cursoId,
            usuario == null ? null : usuario.getId()
        );

        if (data.getOutcome() == ModuloViewOutcome.REDIRECT_LOGIN) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (data.getOutcome() != ModuloViewOutcome.OK) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        req.setAttribute("curso", data.getCurso());
        req.setAttribute("modulos", data.getModulos());
        req.setAttribute("modoReordenar", data.isModoReordenar());
        req.setAttribute("accionModulo", data.getAccionModulo());
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

        Integer cursoId = ServletUtil.parseInteger(cursoIdStr);
        Usuario usuario = ServletUtil.getUsuarioSesion(req);

        ModuloCreateResultDto result = moduloManagementService.createModulo(
            cursoId,
            usuario == null ? null : usuario.getId(),
            tituloModulo,
            tituloLeccion,
            contenidoLeccion
        );

        if (result.getOutcome() == ModuloCreateOutcome.REDIRECT_MY_COURSES) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        if (result.getOutcome() == ModuloCreateOutcome.FORWARD_WITH_ERROR) {
            req.setAttribute("error", result.getError());
            req.setAttribute("curso", result.getCurso());
            req.setAttribute("modulos", result.getModulos());
            req.getRequestDispatcher("/WEB-INF/views/inc1/edit-course.jsp")
                .forward(req, res);
            return;
        }

        res.sendRedirect(
            req.getContextPath()
            + "/edit-course?id=" + result.getCursoId()
            + "&msg=modulo-agregado"
        );
    }

    private void handleUpdatePost(HttpServletRequest req,
                                  HttpServletResponse res)
        throws IOException {

        String moduloIdStr = req.getParameter("moduloId");
        String tituloModulo = req.getParameter("tituloModulo");
        String ordenStr = req.getParameter("orden");

        Integer moduloId = (moduloIdStr == null || moduloIdStr.trim().isEmpty())
            ? null
            : Integer.parseInt(moduloIdStr);
        Usuario usuario = ServletUtil.getUsuarioSesion(req);

        ModuloCommandResultDto result = moduloManagementService.updateModulo(
            moduloId,
            usuario == null ? null : usuario.getId(),
            tituloModulo,
            ordenStr
        );

        if (result.getOutcome() == ModuloCommandOutcome.REDIRECT_MY_COURSES) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        res.sendRedirect(
            req.getContextPath()
            + "/edit-course?action=edit&id=" + result.getModuloId()
            + "&msg=modulo-actualizado"
        );
    }

    private void handleDeletePost(HttpServletRequest req,
                                  HttpServletResponse res)
        throws IOException {

        String moduloIdStr = req.getParameter("moduloId");
        Integer moduloId = (moduloIdStr == null || moduloIdStr.trim().isEmpty())
            ? null
            : Integer.parseInt(moduloIdStr);
        Usuario usuario = ServletUtil.getUsuarioSesion(req);

        ModuloCommandResultDto result = moduloManagementService.deleteModulo(
            moduloId,
            usuario == null ? null : usuario.getId()
        );

        if (result.getOutcome() == ModuloCommandOutcome.REDIRECT_MY_COURSES) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        res.sendRedirect(
            req.getContextPath()
            + "/edit-course?action=reorder&cursoId=" + result.getCursoId()
            + "&msg=modulo-eliminado"
        );
    }

    private void handleSaveOrderPost(HttpServletRequest req,
                                     HttpServletResponse res)
        throws IOException {

        String cursoIdStr = req.getParameter("cursoId");
        Integer cursoId = (cursoIdStr == null || cursoIdStr.trim().isEmpty())
            ? null
            : Integer.parseInt(cursoIdStr);
        Usuario usuario = ServletUtil.getUsuarioSesion(req);

        ModuloCommandResultDto result = moduloManagementService.saveModuloOrder(
            cursoId,
            usuario == null ? null : usuario.getId(),
            parseOrderedIds(req)
        );

        if (result.getOutcome() == ModuloCommandOutcome.REDIRECT_MY_COURSES) {
            res.sendRedirect(req.getContextPath() + "/my-courses");
            return;
        }

        String msg = result.getOutcome() == ModuloCommandOutcome.ORDER_EMPTY
            ? "orden-vacio"
            : "orden-guardado";

        res.sendRedirect(
            req.getContextPath()
            + "/edit-course?action=reorder&cursoId=" + result.getCursoId()
            + "&msg=" + msg
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


}
