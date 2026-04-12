package com.tarea2.webapp;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.Curso;
import logic.DatosEstudiante;
import logic.LIKE_CURSO;
import logic.dao.CursoDAO;
import logic.dao.LikeCursoDAO;

import java.io.IOException;

@WebServlet(name ="LikeCursoServlet", urlPatterns = {"/LikeCursoServlet"})
public class LikeCursoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession();

        DatosEstudiante usuario =
                (DatosEstudiante) session.getAttribute("usuarioLogeado");
        if (usuario == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String idCursoParam = req.getParameter("idCurso");
        if (idCursoParam == null) {
            idCursoParam = req.getParameter("Id_curso");
        }

        if (idCursoParam == null || idCursoParam.isBlank()) {
            res.sendRedirect("index.jsp");
            return;
        }

        Integer idCurso = Integer.parseInt(idCursoParam);

        Curso curso = new CursoDAO().buscarPorId(idCurso);
        if (curso == null) {
            res.sendRedirect("index.jsp");
            return;
        }

        LikeCursoDAO dao = new LikeCursoDAO();

        if(!dao.existeLike(usuario.getId(), curso.getId_curso())){
            LIKE_CURSO like = new LIKE_CURSO();
            like.setId(usuario);
            like.setId_curso(curso);
            dao.guardar(like);
        }

        res.sendRedirect("index.jsp");
    }
}
