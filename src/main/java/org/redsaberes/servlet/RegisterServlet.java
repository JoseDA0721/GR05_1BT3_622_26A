package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.dao.UsuarioDAO;
import org.redsaberes.model.Usuario;
import org.redsaberes.util.EmailUtil;

import java.io.IOException;

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        //Obtener parámetros del formulario
        Usuario usuario = new Usuario();

        String nombre = request.getParameter("name");
        String correo = request.getParameter("email");
        String contrasena = request.getParameter("password");
        String confirmarContrasena = request.getParameter("confirmPassword");
        String terms = request.getParameter("terms");


        System.out.println(nombre + correo + contrasena+confirmarContrasena+terms);
        //Validaciones del servidor
        try{
            //Validar blancos PRIMERO
            if (nombre == null || nombre.trim().isEmpty() ||
                    correo == null || correo.trim().isEmpty() ||
                    contrasena == null || contrasena.isEmpty() ||
                    confirmarContrasena == null || confirmarContrasena.isEmpty()) {

                request.setAttribute("error", "Todos los campos son obligatorios");
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
                return;
            }

            // Validar que aceptó términos y condiciones
            if (terms == null) {
                request.setAttribute("error", "Debes aceptar los términos y condiciones");
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
                return;
            }

            // Validar nombre (máximo 100 caracteres)
            if (nombre.length() > 100) {
                request.setAttribute("error", "El nombre no puede exceder 100 caracteres");
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
                return;
            }

            // Validar que las contraseñas coincidan
            if (!contrasena.equals(confirmarContrasena)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
                return;
            }

            // Validar longitud de contraseña
            if (contrasena.length() < 6) {
                request.setAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
                return;
            }

            // Validar formato de email
            if (!isValidEmail(correo)) {
                request.setAttribute("error", "El formato del correo electrónico no es válido");
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();

            //Verificar disponibilidad de correo
            boolean correoDisponible = dao.verificarCorreoUnico(correo);
            if(!correoDisponible) {
                request.setAttribute("error", "El correo ya está registrado");
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
                return;
            }

            //Hash de la contraseña
            String hashedContrasena = hashedContrasena(contrasena);

            //Registrar usuario
            dao.registrarUsuario(nombre, correo, hashedContrasena);
            EmailUtil.enviarConfirmacion(correo, nombre);

            request.setAttribute("success", "Registro exitoso. Por favor revise su correo.");
            request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar usuario: " + e.getMessage());
            try{
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
            } catch (Exception erro) {
                erro.printStackTrace();
            }
        }

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private String hashedContrasena(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(12));
    }

    // GET → mostrar registro.jsp (:PantallaRegistro)
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws IOException {
        try {
            req.getRequestDispatcher(
                            "/WEB-INF/views/inc1/register.jsp")
                    .forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
