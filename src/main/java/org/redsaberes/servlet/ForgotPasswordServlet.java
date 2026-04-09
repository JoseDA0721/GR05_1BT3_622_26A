package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.dao.UsuarioDAO;
import org.redsaberes.util.EmailUtil;

import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response
    )throws ServletException, IOException {
        try {
            request.getRequestDispatcher(
                            "/WEB-INF/views/inc1/forgot-password.jsp")
                    .forward(request, response);
        } catch (Exception e) { e.printStackTrace(); }
    }
    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response
    )throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String correo = request.getParameter("email");

        // Validaciones basicas
        try {
            //Validar que el correo no este vacio
             if(correo==null || correo.trim().isEmpty()){
                 request.setAttribute("error", "El correo es obligatorio");
                 request.getRequestDispatcher("/WEB-INF/views/inc1/forgot-password.jsp").forward(request, response);
                 return;
             }

             //Validar formato de correo
             if(!isValidEmail(correo)){
                 request.setAttribute("error", "El correo no es válido");
                 request.getRequestDispatcher("/WEB-INF/views/inc1/forgot-password.jsp").forward(request, response);
                 return;
             }

            //Verificar si el correo existe
            UsuarioDAO dao = new UsuarioDAO();

            boolean correoExiste = dao.existeCorreo(correo);
            if(correoExiste){
                String token = UsuarioDAO.generarToken();

                dao.guardarTokenRecuperacion(correo, token);

                String baseUrl = request.getScheme() + "://"
                        + request.getServerName() + ":"
                        + request.getServerPort()
                        + request.getContextPath();
                EmailUtil.enviarEnlaceRecuperacion(correo, token, baseUrl);
            }
             HttpSession session = request.getSession();
             session.setAttribute("resetEmailSent", true);
             session.setAttribute("resetEmail",correo);

             response.sendRedirect(request.getContextPath() + "/forgot-password?sent=true&email=" +
                     java.net.URLEncoder.encode(correo, "UTF-8"));
        }catch(Exception e){
             e.printStackTrace();
             request.setAttribute("error", "Error al enviar el correo");
             request.getRequestDispatcher("/WEB-INF/views/inc1/forgot-password.jsp").forward(request, response);
         }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

}
