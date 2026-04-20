package org.redsaberes.service.dto;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;

import java.util.List;

public class CourseMaterialViewDto {

    private final CourseMaterialViewOutcome outcome;
    private final Curso curso;
    private final List<Modulo> modulos;
    private final boolean accesoConcedido;

    public CourseMaterialViewDto(CourseMaterialViewOutcome outcome,
                                 Curso curso,
                                 List<Modulo> modulos,
                                 boolean accesoConcedido) {
        this.outcome = outcome;
        this.curso = curso;
        this.modulos = modulos;
        this.accesoConcedido = accesoConcedido;
    }

    public CourseMaterialViewOutcome getOutcome() {
        return outcome;
    }

    public Curso getCurso() {
        return curso;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public boolean isAccesoConcedido() {
        return accesoConcedido;
    }
}

