package org.redsaberes.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.redsaberes.service.UserController;
import org.redsaberes.service.UsuarioService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    static Stream<UsuarioDtoValido> usuariosValidos() {
        return Stream.of(
                new UsuarioDtoValido("usuario1", "usuario1@test.com", "Pass123!", "Pass123!", "on"),
                new UsuarioDtoValido("usuario2", "usuario2@test.com", "Pass456!", "Pass456!", "on"),
                new UsuarioDtoValido("usuario3", "usuario3@test.com", "Pass789!", "Pass789!", "on")
        );
    }

    @ParameterizedTest(name = "Debe responder 200 para DTO válido #{index}: {0}")
    @MethodSource("usuariosValidos")
    void post_users_debe_validar_y_responder_ok(UsuarioDtoValido dto) throws Exception {
        UserController controller = new UserController();

        UsuarioService usuarioServiceMock = mock(UsuarioService.class);
        injectService(controller, usuarioServiceMock);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("name")).thenReturn(dto.nombre());
        when(request.getParameter("email")).thenReturn(dto.email());
        when(request.getParameter("password")).thenReturn(dto.password());
        when(request.getParameter("confirmPassword")).thenReturn(dto.confirmPassword());
        when(request.getParameter("terms")).thenReturn(dto.terms());

        invokeDoPost(controller, request, response);

        verify(usuarioServiceMock, times(1)).registrarUsuarioConValidacion(
                dto.nombre(),
                dto.email(),
                dto.password(),
                dto.confirmPassword()
        );

        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verifyNoMoreInteractions(usuarioServiceMock);
    }

    private void injectService(UserController controller, UsuarioService service) throws Exception {
        Field field = UserController.class.getDeclaredField("usuarioService");
        field.setAccessible(true);
        field.set(controller, service);
    }

    private void invokeDoPost(UserController controller,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        Method method = UserController.class.getDeclaredMethod(
                "doPost",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        method.setAccessible(true);
        method.invoke(controller, request, response);
    }

    record UsuarioDtoValido(String nombre, String email, String password, String confirmPassword, String terms) {}
}