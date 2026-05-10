package org.redsaberes.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.service.exception.ServiceValidationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChangePasswordServiceImplTest {

    private UsuarioRepository usuarioRepository;
    private ChangePasswordServiceImpl service;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        service = new ChangePasswordServiceImpl(usuarioRepository);

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreoElectronico("usuario@test.com");
        usuario.setContrasena(BCrypt.hashpw("Actual123", BCrypt.gensalt(12)));
    }

    @Test
    void dadoContrasenaActualIncorrectaCuandoCambiarEntoncesDebeLanzarErrorYNoActualizar() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        ServiceValidationException exception = assertThrows(
                ServiceValidationException.class,
                () -> service.cambiarContrasena(
                        1,
                        "Incorrecta123",
                        "Nueva123",
                        "Nueva123"
                )
        );

        assertEquals("La contraseña actual no es correcta", exception.getMessage());
        verify(usuarioRepository, never()).update(any(Usuario.class));
    }

    @Test
    void dadoNuevaContrasenaDebilCuandoCambiarEntoncesDebeLanzarErrorYNoActualizar() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        ServiceValidationException exception = assertThrows(
                ServiceValidationException.class,
                () -> service.cambiarContrasena(
                        1,
                        "Actual123",
                        "nueva",
                        "nueva"
                )
        );

        assertEquals(
                "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número",
                exception.getMessage()
        );
        verify(usuarioRepository, never()).update(any(Usuario.class));
    }

    @Test
    void dadoConfirmacionDistintaCuandoCambiarEntoncesDebeLanzarErrorYNoActualizar() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        ServiceValidationException exception = assertThrows(
                ServiceValidationException.class,
                () -> service.cambiarContrasena(
                        1,
                        "Actual123",
                        "Nueva123",
                        "Otra1234"
                )
        );

        assertEquals("Las contraseñas no coinciden", exception.getMessage());
        verify(usuarioRepository, never()).update(any(Usuario.class));
    }

    @Test
    void dadoDatosValidosCuandoCambiarEntoncesDebeGuardarNuevoHashYRetornarTrue() throws ServiceValidationException {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        boolean resultado = service.cambiarContrasena(
                1,
                "Actual123",
                "Nueva123",
                "Nueva123"
        );

        assertTrue(resultado);
        assertNotEquals("Nueva123", usuario.getContrasena());
        assertTrue(BCrypt.checkpw("Nueva123", usuario.getContrasena()));
        verify(usuarioRepository).update(usuario);
    }

    @Test
    void dadoUsuarioInexistenteCuandoCambiarEntoncesDebeLanzarError() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        ServiceValidationException exception = assertThrows(
                ServiceValidationException.class,
                () -> service.cambiarContrasena(
                        99,
                        "Actual123",
                        "Nueva123",
                        "Nueva123"
                )
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, never()).update(any(Usuario.class));
    }
}
