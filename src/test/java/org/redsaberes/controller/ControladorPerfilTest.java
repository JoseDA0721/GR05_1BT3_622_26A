package org.redsaberes.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.VistaPerfilPublicoViewDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ControladorPerfilTest {

    @Test
    void obtenerPerfilPublico_debe_calcular_promedio_de_tres_resenas() {
        // Mocks de reseñas con calificaciones 4, 5 y 3
        Resena r1 = Mockito.mock(Resena.class);
        Resena r2 = Mockito.mock(Resena.class);
        Resena r3 = Mockito.mock(Resena.class);

        when(r1.getEstrellas()).thenReturn(4);
        when(r2.getEstrellas()).thenReturn(5);
        when(r3.getEstrellas()).thenReturn(3);

        // Mock del curso que devuelve las tres reseñas
        Curso cursoMock = Mockito.mock(Curso.class);
        when(cursoMock.getResenas()).thenReturn(List.of(r1, r2, r3));

        // Usuario que tiene el curso mockeado
        Usuario usuario = new Usuario();
        usuario.setId(99);
        usuario.setNombre("Test User");
        usuario.setCursos(List.of(cursoMock));

        // Instancia del controlador (aún sin implementar la lógica)
        ControladorPerfil controlador = new ControladorPerfil();

        // Llamada al método bajo prueba
        VistaPerfilPublicoViewDto perfil = controlador.obtenerPerfilPublico(usuario);

        // Esperamos que el promedio sea 4.0 ( (4+5+3)/3 )
        assertEquals(4.0, perfil.getCalificacionPromedio(), 0.0001);
    }
}

