package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.repository.impl.ReviewRepositoryImpl;
import org.redsaberes.service.ReviewService;
import org.redsaberes.service.dto.ReviewCreationOutcome;
import org.redsaberes.service.dto.ReviewCreationResult;
import org.redsaberes.service.validator.ReviewValidator;

import java.time.LocalDate;

public class ReviewServiceImpl implements ReviewService {
    private static final int MAX_COMMENT_LENGTH = 255;

    private final ReviewRepository reviewRepository;
    private final ReviewValidator reviewValidator;

    public ReviewServiceImpl() {
        this(new ReviewRepositoryImpl(), new ReviewValidator());
    }

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewValidator reviewValidator) {
        this.reviewRepository = reviewRepository;
        this.reviewValidator = reviewValidator;
    }

    @Override
    public ReviewCreationResult crearResena(Integer estrellas, String comentario, Usuario usuario, Curso curso) {
        ReviewCreationOutcome validationOutcome = reviewValidator.validateForCreation(estrellas, comentario, usuario, curso);
        if (validationOutcome != ReviewCreationOutcome.SUCCESS) {
            return ReviewCreationResult.fromOutcome(validationOutcome);
        }

        try {
            Resena resena = new Resena();
            resena.setEstrellas(estrellas);
            resena.setComentario(comentario != null && !comentario.trim().isEmpty() ? comentario.trim() : null);
            resena.setUsuario(usuario);
            resena.setFecha(LocalDate.now());
            resena.setCurso(curso);

            reviewRepository.save(resena);
            return ReviewCreationResult.success();

        } catch (Exception e) {
            // Si falla al guardar, devolver error genérico
            return ReviewCreationResult.saveError();
        }
    }

    @Override
    public void validateCommentLength(String commentText) {
        if (commentText != null && commentText.length() > MAX_COMMENT_LENGTH) {
            throw new IllegalArgumentException("El comentario no puede superar 255 caracteres");
        }
    }

    @Override
    public boolean containsOffensiveContent(String comment) {
        return reviewValidator.containsOffensiveContent(comment);
    }
}


