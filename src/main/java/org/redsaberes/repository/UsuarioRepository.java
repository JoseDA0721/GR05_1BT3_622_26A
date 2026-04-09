package org.redsaberes.repository;

import org.redsaberes.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    
    // CRUD Básico
    void save(Usuario usuario);
    void update(Usuario usuario);
    void delete(Integer id);
    Optional<Usuario> findById(Integer id);
    List<Usuario> findAll();
    
    // Búsquedas específicas
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByTokenSesion(String token);
    Optional<Usuario> findByTokenRecuperacion(String token);
    
    // Validaciones
    boolean existeCorreo(String correo);
    void actualizarToken(Integer usuarioId, String token);
    void actualizarTokenRecuperacion(String correo, String token, Long expiracion);
}

