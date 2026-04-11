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
import logic.dao.MatchCursoDAO;

import java.io.IOException;

@WebServlet(name = "MaterialCursoServlet", urlPatterns = {"/MaterialCursoServlet"})
public class MaterialCursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        DatosEstudiante usuario = session == null
                ? null
                : (DatosEstudiante) session.getAttribute("usuarioLogeado");

        if (usuario == null) {
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

        boolean accesoConcedido = curso.getId() == usuario.getId() ||
                new MatchCursoDAO().puedeVerMaterial(idCurso, usuario.getId());

        req.setAttribute("curso", curso);
        req.setAttribute("accesoConcedido", accesoConcedido);
        req.getRequestDispatcher("materiales.jsp").forward(req, res);
    }
}

