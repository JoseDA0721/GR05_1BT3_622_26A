package org.redsaberes.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.impl.UsuarioRepositoryImpl;
import org.redsaberes.service.AuthService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.validator.AuthValidator;

import java.util.Optional;
import java.util.UUID;

public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthServiceImpl() {
        this(new UsuarioRepositoryImpl());
    }

    AuthServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String correo, String contrasena) throws ServiceValidationException {
        String correoLimpio = correo == null ? null : correo.trim();
        String contrasenaLimpia = contrasena == null ? null : contrasena.trim();

        AuthValidator.validarLogin(correoLimpio, contrasenaLimpia);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correoLimpio);
        if (usuarioOpt.isEmpty()) {
            throw new ServiceValidationException("Correo o contraseña incorrectos");
        }

        Usuario usuario = usuarioOpt.get();
        if (!BCrypt.checkpw(contrasenaLimpia, usuario.getContrasena())) {
            throw new ServiceValidationException("Correo o contraseña incorrectos");
        }

        String token = UUID.randomUUID().toString();
        usuario.setTokenSesion(token);
        usuarioRepository.actualizarToken(usuario.getId(), token);

        return usuario;
    }
}

