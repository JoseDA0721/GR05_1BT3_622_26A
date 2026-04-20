package org.redsaberes.service.dto;

import org.redsaberes.model.Curso;

public class PreviewCourseViewDto {

    private final PreviewCourseViewOutcome outcome;
    private final Curso curso;

    public PreviewCourseViewDto(PreviewCourseViewOutcome outcome, Curso curso) {
        this.outcome = outcome;
        this.curso = curso;
    }

    public PreviewCourseViewOutcome getOutcome() {
        return outcome;
    }

    public Curso getCurso() {
        return curso;
    }
}

