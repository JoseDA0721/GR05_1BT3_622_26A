package org.redsaberes.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.service.dto.ReviewCreationOutcome;
import org.redsaberes.service.dto.ReviewCreationResult;
import org.redsaberes.service.validator.ReviewValidator;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

class ReviewServiceImplTest {

    @Test
    void give_a_review_valid_when_create_then_save_in_repository() {
        ReviewRepository repo = Mockito.mock(ReviewRepository.class);
        ReviewValidator validator = Mockito.mock(ReviewValidator.class);
        ReviewServiceImpl service = new ReviewServiceImpl(repo, validator);

        Usuario usuario = new Usuario();
        usuario.setId(1);

        Curso curso = new Curso();
        curso.setId(2);

        Integer estrellas = 4;
        String comentario = "comentario";

        when(validator.validateForCreation(estrellas, comentario, usuario, curso)).thenReturn(ReviewCreationOutcome.SUCCESS);

        ReviewCreationResult result = service.crearResena(estrellas, comentario, usuario, curso);

        assertTrue(result.isSuccess());
        Mockito.verify(validator).validateForCreation(estrellas, comentario, usuario, curso);
        Mockito.verify(repo).save(any(Resena.class));
    }

    @Test
    void give_a_review_not_valid_when_create_then_not_save_in_repository() {
        ReviewRepository repo = Mockito.mock(ReviewRepository.class);
        ReviewValidator validator = Mockito.mock(ReviewValidator.class);
        ReviewServiceImpl service = new ReviewServiceImpl(repo, validator);

        Usuario usuario = new Usuario();
        usuario.setId(1);

        Curso curso = new Curso();
        curso.setId(2);

        Integer estrellas = 8;
        String comentario = "comentario";

        when(validator.validateForCreation(estrellas, comentario, usuario, curso)).thenReturn(ReviewCreationOutcome.INVALID_STARS);

        ReviewCreationResult result = service.crearResena(estrellas, comentario, usuario, curso);

        assertEquals(ReviewCreationOutcome.INVALID_STARS, result.getOutcome());
        Mockito.verify(validator).validateForCreation(estrellas, comentario, usuario, curso);
        Mockito.verify(repo, never()).save(any());
    }

}