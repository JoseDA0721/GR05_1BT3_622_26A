package org.redsaberes.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.impl.UsuarioRepositoryImpl;
import org.redsaberes.service.UsuarioService;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.validator.UsuarioValidator;

public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl() {
        this(new UsuarioRepositoryImpl());
    }

    UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrarUsuario(String nombre,
                                    String correo,
                                    String contrasena,
                                    String confirmarContrasena,
                                    boolean aceptaTerminos) throws ServiceValidationException {

        String nombreLimpio = normalizar(nombre);
        String correoLimpio = normalizar(correo);

        UsuarioValidator.validarRegistro(
                nombreLimpio,
                correoLimpio,
                contrasena,
                confirmarContrasena,
                aceptaTerminos,
                usuarioRepository
        );

        Usuario usuario = new Usuario();
        usuario.setNombre(nombreLimpio);
        usuario.setCorreoElectronico(correoLimpio);
        usuario.setContrasena(hashearContrasena(contrasena));

        usuarioRepository.save(usuario);
        return usuario;
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String hashearContrasena(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(12));
    }
}

