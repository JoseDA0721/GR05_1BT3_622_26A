package org.redsaberes.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.redsaberes.model.*;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.service.NotificacionService;
import org.redsaberes.service.exception.ServiceValidationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeCourseServiceImplTest {

    @Mock
    private LikeCursoRepository likeCursoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private NotificacionService notificacionService;

    private LikeCourseServiceImpl likeCourseService;

    private Curso curso;
    private Usuario usuarioEmisor, usuarioReceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        likeCourseService = new LikeCourseServiceImpl(cursoRepository, likeCursoRepository, notificacionService);

        //Usuarios para los test
        usuarioEmisor = new Usuario();
        usuarioEmisor.setId(1);
        usuarioEmisor.setNombre("Emisor");
        usuarioEmisor.setCorreoElectronico("emisor@test.com");

        usuarioReceptor = new Usuario();
        usuarioReceptor.setId(2);
        usuarioReceptor.setNombre("Receptor");
        usuarioReceptor.setCorreoElectronico("receptor@test.com");

        //Curso para los test
        curso = new Curso();
        curso.setId(1);
        curso.setTitulo("Curso de Test");
        curso.setUsuario(usuarioReceptor);
        curso.setEstado(EstadoCurso.PUBLICO);

    }
    @Test
    void given_valid_like_when_likeCourse_then_create_notification() throws ServiceValidationException {
        // Arrange
        when(cursoRepository.findById(curso.getId())).thenReturn(Optional.of(curso));
        when(likeCursoRepository.existsByUsuarioAndCurso(usuarioEmisor.getId(), curso.getId())).thenReturn(false);

        // Act
        likeCourseService.likeCourse(usuarioEmisor, curso.getId());

        // Assert
        Mockito.verify(notificacionService, Mockito.times(1))
                .createNotification(
                    usuarioReceptor,
                    usuarioEmisor,
                    curso,
                    TipoNotificacion.LIKE_RECIBIDO
                );

        // Verify like was persisted with correct associations
        ArgumentCaptor<LikeCurso> likeCaptor = ArgumentCaptor.forClass(LikeCurso.class);
        Mockito.verify(likeCursoRepository, Mockito.times(1)).save(likeCaptor.capture());
        LikeCurso saved = likeCaptor.getValue();
        assertEquals(curso.getId(), saved.getCurso().getId());
        assertEquals(usuarioEmisor.getId(), saved.getUsuario().getId());

    }


}