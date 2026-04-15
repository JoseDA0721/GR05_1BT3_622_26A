package org.redsaberes.service.dto;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;

import java.util.List;

public class PublishCourseViewDto {

    private final CourseLifecycleOutcome outcome;
    private final Curso curso;
    private final List<Modulo> modulos;

    public PublishCourseViewDto(CourseLifecycleOutcome outcome,
                                Curso curso,
                                List<Modulo> modulos) {
        this.outcome = outcome;
        this.curso = curso;
        this.modulos = modulos;
    }

    public CourseLifecycleOutcome getOutcome() {
        return outcome;
    }

    public Curso getCurso() {
        return curso;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }
}

