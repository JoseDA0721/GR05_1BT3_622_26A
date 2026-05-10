package org.redsaberes.repository;

import org.redsaberes.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends GenericRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByNombre(String nombre);

    Optional<Usuario> findByTokenSesion(String token);

    Optional<Usuario> findByTokenRecuperacion(String token);

    boolean existeCorreo(String correo);

    void actualizarToken(Integer usuarioId, String token);

    void actualizarTokenRecuperacion(String correo, String token, Long expiracion);
}

