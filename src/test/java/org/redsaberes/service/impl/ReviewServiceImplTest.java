package org.redsaberes.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.TipoNotificacion;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.service.NotificacionService;
import org.redsaberes.service.dto.ReviewCreationOutcome;
import org.redsaberes.service.dto.ReviewCreationResult;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.validator.ReviewValidator;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private ReviewValidator reviewValidator;

    private ReviewServiceImpl service;
    private Curso curso;
    private Usuario reviewer;
    private Usuario owner;
    private Resena resena;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ReviewServiceImpl(reviewRepository, reviewValidator ,notificacionService);

        reviewer = new Usuario();
        reviewer.setId(10);
        owner = new Usuario();
        owner.setId(20);

       curso = new Curso();
        curso.setId(100); curso.setUsuario(owner);

        resena = new Resena();
        resena.setUsuario(reviewer);
        resena.setCurso(curso);
        resena.setEstrellas(4);
        resena.setComentario("Buen curso");
    }

    @Test
    void give_a_review_valid_when_create_then_save_in_repository() {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        Curso curso = new Curso();
        curso.setId(2);
        curso.setUsuario(new Usuario());
        curso.getUsuario().setId(99);

        Integer estrellas = 4;
        String comentario = "comentario";

        when(reviewValidator.validateForCreation(estrellas, comentario, usuario, curso))
                .thenReturn(ReviewCreationOutcome.SUCCESS);

        ReviewCreationResult result = service.crearResena(estrellas, comentario, usuario, curso);

        assertTrue(result.isSuccess());
        verify(reviewValidator).validateForCreation(estrellas, comentario, usuario, curso);
        verify(reviewRepository).save(any(Resena.class));
    }

    @Test
    void give_a_review_not_valid_when_create_then_not_save_in_repository() {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        Curso curso = new Curso();
        curso.setId(2);

        Integer estrellas = 8;
        String comentario = "comentario";

        when(reviewValidator.validateForCreation(estrellas, comentario, usuario, curso))
                .thenReturn(ReviewCreationOutcome.INVALID_STARS);

        ReviewCreationResult result = service.crearResena(estrellas, comentario, usuario, curso);

        assertEquals(ReviewCreationOutcome.INVALID_STARS, result.getOutcome());
        verify(reviewValidator).validateForCreation(estrellas, comentario, usuario, curso);
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void when_create_valid_review_then_call_notificacion_service() throws ServiceValidationException {
        when(reviewValidator.validateForCreation(4, "Comentario válido", reviewer, curso))
                .thenReturn(ReviewCreationOutcome.SUCCESS);

        service.crearResena(4, "Comentario válido", reviewer, curso);

        verify(reviewRepository, times(1)).save(any(Resena.class));
        verify(notificacionService, times(1)).createNotification(
                eq(owner),
                eq(reviewer),
                eq(curso),
                eq(TipoNotificacion.REVIEW_RECIBIDA)
        );
    }
}