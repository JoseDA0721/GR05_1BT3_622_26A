package com.tarea2.webapp;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.Curso;
import logic.DatosEstudiante;
import logic.dao.CursoDAO;

import java.io.IOException;
@WebServlet(name ="CrearCursoServlet", urlPatterns = {"/CrearCursoServlet"})
public class CrearCursoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        // 🔥 Obtener usuario desde la sesión
        DatosEstudiante usuario =
                (DatosEstudiante) session.getAttribute("usuarioLogeado");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String nombre = request.getParameter("nombre_curso");

        Curso curso = new Curso();
        curso.setNombre_curso(nombre);

        // 🔥 asignar creador automáticamente
        curso.setId(usuario);

        new CursoDAO().guardar(curso);

        response.sendRedirect("index.jsp");
    }
}
