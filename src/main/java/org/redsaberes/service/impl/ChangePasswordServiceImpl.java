package org.redsaberes.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.impl.UsuarioRepositoryImpl;
import org.redsaberes.service.ChangePasswordService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.validator.PasswordValidator;

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
                                     String confirmarContrasena)
            throws ServiceValidationException {

        validarCampos(usuarioId, contrasenaActual, nuevaContrasena, confirmarContrasena);
        validarConfirmacion(nuevaContrasena, confirmarContrasena);
        validarSeguridad(nuevaContrasena);

        Usuario usuario = buscarUsuario(usuarioId);
        validarContrasenaActual(contrasenaActual, usuario);

        actualizarContrasena(usuario, nuevaContrasena);
        return true;
    }

    private void validarCampos(Integer usuarioId,
                               String contrasenaActual,
                               String nuevaContrasena,
                               String confirmarContrasena)
            throws ServiceValidationException {

        if (usuarioId == null) {
            throw new ServiceValidationException("Usuario inválido");
        }

        if (isBlank(contrasenaActual) || isBlank(nuevaContrasena) || isBlank(confirmarContrasena)) {
            throw new ServiceValidationException("Todos los campos son obligatorios");
        }
    }

    private void validarConfirmacion(String nuevaContrasena,
                                     String confirmarContrasena)
            throws ServiceValidationException {

        if (!nuevaContrasena.equals(confirmarContrasena)) {
            throw new ServiceValidationException("Las contraseñas no coinciden");
        }
    }

    private void validarSeguridad(String nuevaContrasena)
            throws ServiceValidationException {

        if (!PasswordValidator.esSegura(nuevaContrasena)) {
            throw new ServiceValidationException(
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número"
            );
        }
    }

    private Usuario buscarUsuario(Integer usuarioId)
            throws ServiceValidationException {

        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ServiceValidationException("Usuario no encontrado"));
    }

    private void validarContrasenaActual(String contrasenaActual,
                                         Usuario usuario)
            throws ServiceValidationException {

        if (usuario.getContrasena() == null || !BCrypt.checkpw(contrasenaActual, usuario.getContrasena())) {
            throw new ServiceValidationException("La contraseña actual no es correcta");
        }
    }

    private void actualizarContrasena(Usuario usuario,
                                      String nuevaContrasena) {

        usuario.setContrasena(BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt(12)));
        usuarioRepository.update(usuario);
    }

    private boolean isBlank(String valor) {
        return valor == null || valor.isBlank();
    }
}
