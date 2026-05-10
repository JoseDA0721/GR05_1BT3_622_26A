package org.redsaberes.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.impl.UsuarioRepositoryImpl;
import org.redsaberes.service.ChangePasswordService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.validator.PasswordValidator;

import java.util.Optional;

public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final UsuarioRepository usuarioRepository;

    public ChangePasswordServiceImpl() {
        this(new UsuarioRepositoryImpl());
    }

    public ChangePasswordServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean cambiarContrasena(Integer usuarioId,
                                    String contrasenaActual,
                                    String nuevaContrasena,
                                    String confirmarContrasena) throws ServiceValidationException {

        if (usuarioId == null) {
            throw new ServiceValidationException("Usuario inválido");
        }
        if (contrasenaActual == null || contrasenaActual.isBlank()
                || nuevaContrasena == null || nuevaContrasena.isBlank()
                || confirmarContrasena == null || confirmarContrasena.isBlank()) {
            throw new ServiceValidationException("Todos los campos son obligatorios");
        }
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            throw new ServiceValidationException("Las contraseñas no coinciden");
        }
        if (!PasswordValidator.esSegura(nuevaContrasena)) {
            throw new ServiceValidationException("La nueva contraseña no cumple con los requisitos de seguridad");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new ServiceValidationException("No se encontró el usuario");
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario.getContrasena() == null || !BCrypt.checkpw(contrasenaActual, usuario.getContrasena())) {
            throw new ServiceValidationException("La contraseña actual no es correcta");
        }

        usuario.setContrasena(BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt(12)));
        usuarioRepository.update(usuario);
        return true;
    }
}

