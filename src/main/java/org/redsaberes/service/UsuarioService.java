package org.redsaberes.service;

import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.DatosPublicosUsuarioDto;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.exception.NameAlreadyTakenException;

public interface UsuarioService {

    Usuario registrarUsuario(String nombre,
                             String correo,
                             String contrasena,
                             String confirmarContrasena,
                             boolean aceptaTerminos) throws ServiceValidationException;

    /**
     * Registra un nuevo usuario con validación de nombre duplicado.
     *
     * Este método valida que el nombre no esté duplicado lanzando NameAlreadyTakenException
     * si ya existe. Puede ser usado en contextos donde la validación de nombre duplicado
     * es crítica.
     *
     * @param nombre El nombre del nuevo usuario
     * @param email El correo electrónico del usuario
     * @param contrasena La contraseña del usuario
     * @param confirmarContrasena Confirmación de la contraseña
     * @return El usuario creado
     * @throws org.redsaberes.service.exception.NameAlreadyTakenException Si el nombre ya existe
     * @throws ServiceValidationException Si hay otro error de validación
     */
    Usuario registrarUsuarioConValidacion(String nombre, String email,
                                         String contrasena, String confirmarContrasena)
            throws ServiceValidationException;

    Usuario actualizarPerfilBasico(Integer usuarioId,
                                   String nombre,
                                   String correo) throws ServiceValidationException;

    DatosPublicosUsuarioDto buscarDatosPublicos(Integer usuarioId);
}

