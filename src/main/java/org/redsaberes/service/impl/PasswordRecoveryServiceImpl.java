package org.redsaberes.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.impl.UsuarioRepositoryImpl;
import org.redsaberes.service.PasswordRecoveryService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.util.EmailUtil;

import java.util.Optional;
import java.util.UUID;

public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final long EXPIRACION_24_HORAS_MS = 24L * 60L * 60L * 1000L;

    private final UsuarioRepository usuarioRepository;

    public PasswordRecoveryServiceImpl() {
        this(new UsuarioRepositoryImpl());
    }

    PasswordRecoveryServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void solicitarRestablecimiento(String correo, String baseUrl) throws ServiceValidationException {
        String correoLimpio = correo == null ? null : correo.trim();

        validarCorreo(correoLimpio);

        if (!usuarioRepository.existeCorreo(correoLimpio)) {
            return;
        }

        String token = UUID.randomUUID().toString();
        Long expiracion = System.currentTimeMillis() + EXPIRACION_24_HORAS_MS;
        usuarioRepository.actualizarTokenRecuperacion(correoLimpio, token, expiracion);
        EmailUtil.enviarEnlaceRecuperacion(correoLimpio, token, baseUrl);
    }

    @Override
    public void restablecerContrasena(String token,
                                      String contrasena,
                                      String confirmarContrasena) throws ServiceValidationException {
        String tokenLimpio = token == null ? null : token.trim();
        String contrasenaLimpia = contrasena == null ? null : contrasena.trim();
        String confirmarLimpia = confirmarContrasena == null ? null : confirmarContrasena.trim();

        validarCamposRestablecimiento(tokenLimpio, contrasenaLimpia, confirmarLimpia);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByTokenRecuperacion(tokenLimpio);
        if (usuarioOpt.isEmpty()) {
            throw new ServiceValidationException("El enlace de recuperación no es válido o ha expirado");
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario.getExpiracionToken() != null && usuario.getExpiracionToken() < System.currentTimeMillis()) {
            throw new ServiceValidationException("El enlace de recuperación ha expirado");
        }

        usuario.setContrasena(hashearContrasena(contrasenaLimpia));
        usuario.setTokenRecuperacion(null);
        usuario.setExpiracionToken(null);
        usuarioRepository.update(usuario);
    }

    private void validarCorreo(String correo) throws ServiceValidationException {
        if (isBlank(correo)) {
            throw new ServiceValidationException("El correo es obligatorio");
        }

        if (!isValidEmail(correo)) {
            throw new ServiceValidationException("El correo no es válido");
        }
    }

    private void validarCamposRestablecimiento(String token,
                                               String contrasena,
                                               String confirmarContrasena) throws ServiceValidationException {
        if (isBlank(token) || isBlank(contrasena) || isBlank(confirmarContrasena)) {
            throw new ServiceValidationException("Todos los campos son obligatorios");
        }

        if (!contrasena.equals(confirmarContrasena)) {
            throw new ServiceValidationException("Las contraseñas no coinciden");
        }

        if (contrasena.length() < MIN_PASSWORD_LENGTH) {
            throw new ServiceValidationException("La contraseña debe tener al menos 6 caracteres");
        }
    }

    private String hashearContrasena(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(12));
    }

    private boolean isBlank(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
}

