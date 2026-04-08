package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.redsaberes.dao.UsuarioDAO;
import org.redsaberes.model.Usuario;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    //private static final int MAX_LOGIN_ATTEMPTS = 5;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        //Obtener parámetros del formulario
        String correo = request.getParameter("email");
        String contrasena = request.getParameter("password");
        String remember = request.getParameter("remember");

        //Validaciones
        try{
            //Validar campos completos
            if(correo == null || correo.isEmpty() || contrasena == null || contrasena.isEmpty()){
                request.setAttribute("error", "Todos los campos son obligatorios");
                request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);
                return;
            }

            //Validar formato de correo
            if(!isValidEmail(correo)){
                request.setAttribute("error", "Formato de correo inválido");
                request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);
                return;
            }

            //Verificar credenciales en la BD
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.validarAcceso(correo, contrasena);

            if(usuario != null){
                //Generar token
                String token = UsuarioDAO.generarToken();
                dao.guardarToken(usuario.getId(), token);
                usuario.setTokenSesion(token);

                //Permitir acceso
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("token", token);

                //Recordame
                if(remember != null){
                    Cookie cookie = new Cookie("email", correo);
                    cookie.setMaxAge(30 * 24 * 60 * 60);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }

                //Redirigir al dashboard
                response.sendRedirect("dashboard.jsp");
            } else {
                //Credenciales invalidadas
                request.setAttribute("error", "Correo o contraseña incorrectos");
                request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);
            }

        }catch(Exception e){
            e.printStackTrace();
            response.sendRedirect(
                    request.getContextPath() + "/WEB-INF/views/inc1/login.jsp?error=1"
            );
        }

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

}
