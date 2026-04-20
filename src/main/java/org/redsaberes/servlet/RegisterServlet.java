package org.redsaberes.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.UsuarioService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.impl.UsuarioServiceImpl;
import org.redsaberes.util.EmailUtil;

import java.io.IOException;
import java.io.Serial;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

    @Serial
    private static final long serialVersionUID = 1L;
    private final UsuarioService usuarioService = new UsuarioServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String nombre = request.getParameter("name");
        String correo = request.getParameter("email");
        String contrasena = request.getParameter("password");
        String confirmarContrasena = request.getParameter("confirmPassword");
        String terms = request.getParameter("terms");

        try{
            Usuario usuarioRegistrado = usuarioService.registrarUsuario(
                    nombre,
                    correo,
                    contrasena,
                    confirmarContrasena,
                    terms != null
            );

            EmailUtil.enviarConfirmacion(
                    usuarioRegistrado.getCorreoElectronico(),
                    usuarioRegistrado.getNombre()
            );

            request.setAttribute("success", "Registro exitoso. Por favor revise su correo.");
            request.getRequestDispatcher("/WEB-INF/views/inc1/login.jsp").forward(request, response);

        } catch (ServiceValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al registrar usuario", e);
            request.setAttribute("error", "Error al registrar usuario: " + e.getMessage());
            try{
                request.getRequestDispatcher("/WEB-INF/views/inc1/register.jsp").forward(request, response);
            } catch (Exception erro) {
                LOGGER.log(Level.SEVERE, "Error al reenviar a la vista de registro", erro);
            }
        }

    }


    // GET → mostrar registro.jsp
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws IOException {
        try {
            req.getRequestDispatcher(
                            "/WEB-INF/views/inc1/register.jsp")
                    .forward(req, res);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al mostrar la vista de registro", e);
        }
    }
}
