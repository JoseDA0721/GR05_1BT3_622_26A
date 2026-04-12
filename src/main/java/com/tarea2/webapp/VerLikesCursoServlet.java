package com.tarea2.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.Curso;
import logic.DatosEstudiante;
import logic.dao.CursoDAO;
import logic.dao.LikeCursoDAO;
import logic.dao.MatchCursoDAO;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "VerLikesCursoServlet", urlPatterns = {"/VerLikesCursoServlet"})
public class VerLikesCursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        DatosEstudiante usuarioLogeado = session == null
                ? null
                : (DatosEstudiante) session.getAttribute("usuarioLogeado");

        if (usuarioLogeado == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String idCursoParam = req.getParameter("idCurso");
        if (idCursoParam == null || idCursoParam.isBlank()) {
            res.sendRedirect("index.jsp");
            return;
        }

        int idCurso;
        try {
            idCurso = Integer.parseInt(idCursoParam);
        } catch (NumberFormatException e) {
            res.sendRedirect("index.jsp");
            return;
        }

        Curso curso = new CursoDAO().buscarPorId(idCurso);
        if (curso == null) {
            res.sendRedirect("index.jsp");
            return;
        }

        List<DatosEstudiante> usuariosLike = new LikeCursoDAO().listarUsuariosPorCurso(idCurso);
        boolean esCreador = curso.getId() == usuarioLogeado.getId();
        Set<Integer> idsMatch = new MatchCursoDAO().listarIdsUsuariosMatchPorCurso(idCurso);

        req.setAttribute("curso", curso);
        req.setAttribute("usuariosLike", usuariosLike);
        req.setAttribute("esCreador", esCreador);
        req.setAttribute("idsMatch", idsMatch);
        req.getRequestDispatcher("likesCurso.jsp").forward(req, res);
    }
}
