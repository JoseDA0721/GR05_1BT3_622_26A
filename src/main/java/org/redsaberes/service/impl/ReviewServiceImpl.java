package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.repository.impl.ReviewRepositoryImpl;
import org.redsaberes.service.ReviewService;
import org.redsaberes.service.validator.ReviewValidator;

public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewValidator reviewValidator;

    public ReviewServiceImpl() {
        this(new ReviewRepositoryImpl(), new ReviewValidator());
    }

    ReviewServiceImpl(ReviewRepository reviewRepository, ReviewValidator reviewValidator) {
        this.reviewRepository = reviewRepository;
        this.reviewValidator = reviewValidator;
    }

    @Override
    public void crearResena(Integer estrellas, String comentario, Usuario usuario, Curso curso) {
        Resena resena = new Resena();

        if (!reviewValidator.isValid(estrellas, comentario, usuario, curso)) {
            return;
        }

        resena.setEstrellas(estrellas);
        resena.setComentario(comentario);
        resena.setUsuario(usuario);
        resena.setCurso(curso);

        reviewRepository.save(resena);

    }
}
