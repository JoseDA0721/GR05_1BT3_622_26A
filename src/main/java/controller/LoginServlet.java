package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

import logic.DatosEstudiante;
import logic.dao.EstudianteDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        EstudianteDAO dao = new EstudianteDAO();

        DatosEstudiante estudiante =
                dao.login(usuario, contrasena);

        // LOGIN CORRECTO
        if(estudiante != null){

            HttpSession session = request.getSession();

            session.setAttribute("usuarioLogeado", estudiante);

            response.sendRedirect("index.jsp");

        }else{

            request.setAttribute("error",
                    "Usuario o contraseña incorrectos");

            request.getRequestDispatcher("login.jsp")
                    .forward(request,response);
        }
    }
}