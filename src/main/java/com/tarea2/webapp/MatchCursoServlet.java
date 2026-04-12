package com.tarea2.webapp;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;
import logic.Curso;
import logic.DatosEstudiante;
import logic.dao.CursoDAO;
import logic.dao.LikeCursoDAO;
import logic.dao.MatchCursoDAO;

import java.io.IOException;

@WebServlet(name = "MatchCursoServlet", urlPatterns = {"/MatchCursoServlet"})
public class MatchCursoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);
        DatosEstudiante usuarioLogeado = session == null
                ? null
                : (DatosEstudiante) session.getAttribute("usuarioLogeado");

        if (usuarioLogeado == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String idCursoParam = req.getParameter("idCurso");
        String idUsuarioObjetivoParam = req.getParameter("idUsuarioObjetivo");
        if (idCursoParam == null || idUsuarioObjetivoParam == null
                || idCursoParam.isBlank() || idUsuarioObjetivoParam.isBlank()) {
            res.sendRedirect("index.jsp");
            return;
        }

        int idCurso;
        int idUsuarioObjetivo;
        try {
            idCurso = Integer.parseInt(idCursoParam);
            idUsuarioObjetivo = Integer.parseInt(idUsuarioObjetivoParam);
        } catch (NumberFormatException e) {
            res.sendRedirect("index.jsp");
            return;
        }

        Curso curso = new CursoDAO().buscarPorId(idCurso);
        if (curso == null || curso.getId() != usuarioLogeado.getId()) {
            res.sendRedirect("index.jsp");
            return;
        }

        LikeCursoDAO likeDao = new LikeCursoDAO();
        if (!likeDao.existeLike(idUsuarioObjetivo, idCurso)) {
            res.sendRedirect("VerLikesCursoServlet?idCurso=" + idCurso);
            return;
        }

        MatchCursoDAO matchDao = new MatchCursoDAO();
        if (!matchDao.existeMatch(idCurso, idUsuarioObjetivo)) {
            matchDao.guardar(idCurso, idUsuarioObjetivo, usuarioLogeado.getId());
        }

        res.sendRedirect("VerLikesCursoServlet?idCurso=" + idCurso);
    }
}
