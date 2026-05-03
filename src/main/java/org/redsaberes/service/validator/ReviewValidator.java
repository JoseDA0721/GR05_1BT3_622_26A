package org.redsaberes.service.validator;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.ReviewCreationOutcome;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;

public class ReviewValidator {
    private static final int MAX_COMMENT_LENGTH = 255;

    private static final Set<String> OFFENSIVE_WORDS = Set.of(
            "insulto1",
            "spam_link",
            "puto",
            "cabron",
            "tonto",
            "idiota",
            "estupido",
            "imbecil",
            "payaso",
            "inutil",
            "basura",
            "mentiroso",
            "tramposo"
    );

    public boolean isValid(Integer estrellas, String comentario, Usuario usuario, Curso curso) {
        return validateForCreation(estrellas, comentario, usuario, curso) == ReviewCreationOutcome.SUCCESS;
    }

    public ReviewCreationOutcome validateForCreation(Integer estrellas, String comentario, Usuario usuario, Curso curso) {
        if (!esEstrellasValida(estrellas)) {
            return ReviewCreationOutcome.INVALID_STARS;
        }
        if (!esReferenciaValida(usuario) || !esReferenciaValida(curso)) {
            return ReviewCreationOutcome.UNAUTHORIZED;
        }
        if (comentario != null && !comentario.trim().isEmpty()) {
            if (isCommentTooLong(comentario)) {
                return ReviewCreationOutcome.COMMENT_TOO_LONG;
            }
            if (containsOffensiveContent(comentario)) {
                return ReviewCreationOutcome.COMMENT_OFFENSIVE;
            }
        }
        return ReviewCreationOutcome.SUCCESS;
    }

    private boolean esEstrellasValida(Integer estrellas) {
        return estrellas != null && estrellas >= 1 && estrellas <= 5;
    }

    public boolean isCommentTooLong(String comentario) {
        return comentario != null && comentario.length() > MAX_COMMENT_LENGTH;
    }

    public boolean containsOffensiveContent(String comentario) {
        if (comentario == null) {
            return false;
        }

        String normalizedComment = normalizarTexto(comentario);

        for (String offensiveWord : OFFENSIVE_WORDS) {
            if (normalizedComment.contains(offensiveWord)) {
                return true;
            }
        }

        return false;
    }

    private String normalizarTexto(String text) {
        String lowerCaseText = text.toLowerCase(Locale.ROOT);
        String normalized = Normalizer.normalize(lowerCaseText, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }

    private boolean esReferenciaValida(Usuario usuario) {
        return usuario != null && idValido(usuario.getId());
    }

    private boolean esReferenciaValida(Curso curso) {
        return curso != null && idValido(curso.getId());
    }

    private boolean idValido(Integer id) {
        return id != null && id > 0;
    }
}