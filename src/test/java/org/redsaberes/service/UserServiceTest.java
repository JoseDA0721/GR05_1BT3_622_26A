package org.redsaberes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.service.exception.NameAlreadyTakenException;
import org.redsaberes.service.impl.UsuarioServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserService - Test de validación de nombres duplicados")
class UserServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;

    /**
     * Configuración inicial para cada test.
     * Inicializa los mocks y crea una instancia del servicio.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Aquí se inicializa el UsuarioService con el mock del repositorio
        usuarioService = new UsuarioServiceImpl(usuarioRepository);
    }

    /**
     * Test que verifica que NameAlreadyTakenException es lanzada
     * cuando se intenta crear un usuario con un nombre que ya existe.
     *
     * Escenario:
     * - El repositorio devuelve un usuario existente cuando se busca por nombre
     * - El servicio debe lanzar NameAlreadyTakenException
     *
     * Este test FALLARÁ inicialmente porque el UsuarioService aún no está implementado.
     */
    @Test
    @DisplayName("Debe lanzar NameAlreadyTakenException cuando el nombre ya existe")
    void testCreateUserWithDuplicateNameThrowsException() {
        // ARRANGE - Preparar datos de prueba
        String nombreDuplicado = "jdoe98";
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setNombre(nombreDuplicado);
        usuarioExistente.setCorreoElectronico("john@example.com");

        // Configurar el mock del repositorio para devolver el usuario existente
        // cuando se busque por nombre
        when(usuarioRepository.findByNombre(nombreDuplicado))
                .thenReturn(Optional.of(usuarioExistente));

        // ACT & ASSERT - Ejecutar y verificar que se lanza la excepción esperada
        NameAlreadyTakenException exc = assertThrows(
                NameAlreadyTakenException.class,
                () -> usuarioService.registrarUsuarioConValidacion(nombreDuplicado, "newemail@example.com", "password123", "password123"),
                "Se esperaba que NameAlreadyTakenException fuera lanzada cuando el nombre ya existe"
        );

        // Verificar que el mensaje de la excepción contiene información relevante
        assertTrue(exc.getMessage().contains(nombreDuplicado),
                "El mensaje de excepción debe contener el nombre duplicado");

        // Verificar que el repositorio fue llamado exactamente una vez
        verify(usuarioRepository, times(1)).findByNombre(nombreDuplicado);
    }

    /**
     * Test que verifica que se puede crear un usuario cuando el nombre NO existe.
     * Este test también fallará inicialmente, demostrando que la implementación
     * aún no está completa.
     */
     @Test
     @DisplayName("Debe crear usuario exitosamente cuando el nombre no existe")
     void testCreateUserWithUniqueNameSucceeds() throws org.redsaberes.service.exception.ServiceValidationException {
         // ARRANGE
         String nombreNuevo = "newuser123";
         String email = "newuser@example.com";
         String contrasena = "SecurePass123!";

         // El repositorio devuelve Optional vacío (usuario no existe)
         when(usuarioRepository.findByNombre(nombreNuevo))
                 .thenReturn(Optional.empty());

         Usuario nuevoUsuario = new Usuario();
         nuevoUsuario.setId(2);
         nuevoUsuario.setNombre(nombreNuevo);
         nuevoUsuario.setCorreoElectronico(email);

         when(usuarioRepository.findById(2)).thenReturn(Optional.of(nuevoUsuario));

         // ACT
         Usuario usuarioCreado = usuarioService.registrarUsuarioConValidacion(nombreNuevo, email, contrasena, contrasena);

         // ASSERT
         assertNotNull(usuarioCreado, "El usuario creado no debe ser NULL");
         assertEquals(nombreNuevo, usuarioCreado.getNombre(), "El nombre del usuario debe coincidir");
         assertEquals(email, usuarioCreado.getCorreoElectronico(), "El email del usuario debe coincidir");

         // Verificar que el método save fue llamado en el repositorio
         verify(usuarioRepository, times(1)).findByNombre(nombreNuevo);
         verify(usuarioRepository, times(1)).save(any(Usuario.class));
     }

     /**
      * Test que verifica el comportamiento cuando el repositorio falla.
      * Demuestra que el test también puede validar comportamientos de error.
      */
     @Test
     @DisplayName("Debe manejar errores cuando el repositorio falla")
     void testHandleRepositoryException() {
         // ARRANGE
         String nombreUsuario = "user123";

         // Configurar el repositorio para lanzar una excepción
         when(usuarioRepository.findByNombre(nombreUsuario))
                 .thenThrow(new RuntimeException("Error de conexión a la base de datos"));

         // ACT & ASSERT
         assertThrows(RuntimeException.class,
                 () -> usuarioService.registrarUsuarioConValidacion(nombreUsuario, "user@example.com", "pass", "pass"),
                 "Se esperaba que RuntimeException fuera propagada cuando el repositorio falla"
         );

         verify(usuarioRepository, times(1)).findByNombre(nombreUsuario);
     }
}

