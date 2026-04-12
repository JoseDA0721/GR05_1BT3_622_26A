package com.tarea2.webapp;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import logic.DatosEstudiante;
import logic.dao.EstudianteDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name ="JspServlet", urlPatterns = {"/JspServlet"})
public class JspServlet extends HttpServlet {

    EstudianteDAO dao = new EstudianteDAO();

    // LISTAR ESTUDIANTES (GET)
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<DatosEstudiante> listEstudiantes = dao.listar();

        HttpSession session = request.getSession();
        session.setAttribute("listEstudiantes", listEstudiantes);

        response.sendRedirect("MostrarEstudiantes.jsp");
    }

    // GUARDAR ESTUDIANTE (POST)
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        DatosEstudiante estudiante = new DatosEstudiante();

        estudiante.setCedula(request.getParameter("cedula"));
        estudiante.setNombre(request.getParameter("nombre"));
        estudiante.setApellido(request.getParameter("apellido"));
        estudiante.setEdad(request.getParameter("edad"));
        estudiante.setCarrera(request.getParameter("carrera"));
        estudiante.setUsuario(request.getParameter("usuario"));
        estudiante.setContrasena(request.getParameter("contrasena"));

        dao.guardar(estudiante);

        response.sendRedirect("JspServlet");
    }
}