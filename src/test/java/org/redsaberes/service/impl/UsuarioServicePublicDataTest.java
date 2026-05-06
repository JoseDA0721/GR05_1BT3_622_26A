package org.redsaberes.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@DisplayName("UsuarioServiceImpl.buscarDatosPublicos")
class UsuarioServicePublicDataTest {

    @Test
    @DisplayName("debe exponer reputacion y ocultar campos sensibles del perfil público")
    void buscarDatosPublicos_debe_exponer_reputacion_y_ocultar_campos_sensibles() throws Exception {
        UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
        UsuarioServiceImpl service = new UsuarioServiceImpl(usuarioRepository);

        Usuario usuario = new Usuario();
        usuario.setId(7);
        usuario.setNombre("Perfil Publico");
        usuario.setCorreoElectronico("perfil.publico@redsaberes.test");
        usuario.setContrasena("secreto-no-visible");

        when(usuarioRepository.findById(7)).thenReturn(Optional.of(usuario));

        Method metodo;
        try {
            metodo = UsuarioServiceImpl.class.getMethod("buscarDatosPublicos", Integer.class);
        } catch (NoSuchMethodException e) {
            fail("Falta implementar el método buscarDatosPublicos(Integer) en UsuarioServiceImpl");
            return;
        }

        Object resultado = metodo.invoke(service, 7);

        assertAll(
                () -> assertNotNull(resultado, "El objeto de perfil público no debe ser nulo"),
                () -> assertTrue(tieneGetter(resultado, "getEstrellas"), "Debe exponer estrellas/reputación"),
                () -> assertTrue(tieneGetter(resultado, "getMatchesActivos"), "Debe exponer matches activos"),
                () -> assertFalse(tieneGetter(resultado, "getContrasena"), "No debe exponer password"),
                () -> assertFalse(tieneGetter(resultado, "getEmailPrivado"), "No debe exponer email_privado")
        );
    }

    private boolean tieneGetter(Object obj, String nombreMetodo) {
        try {
            obj.getClass().getMethod(nombreMetodo);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}

