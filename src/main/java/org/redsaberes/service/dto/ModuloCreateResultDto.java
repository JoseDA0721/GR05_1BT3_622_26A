package org.redsaberes.service.dto;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;

import java.util.List;

public class ModuloCreateResultDto {

    private final ModuloCreateOutcome outcome;
    private final Integer cursoId;
    private final String error;
    private final Curso curso;
    private final List<Modulo> modulos;

    public ModuloCreateResultDto(ModuloCreateOutcome outcome,
                                 Integer cursoId,
                                 String error,
                                 Curso curso,
                                 List<Modulo> modulos) {
        this.outcome = outcome;
        this.cursoId = cursoId;
        this.error = error;
        this.curso = curso;
        this.modulos = modulos;
    }

    public ModuloCreateOutcome getOutcome() {
        return outcome;
    }

    public Integer getCursoId() {
        return cursoId;
    }

    public String getError() {
        return error;
    }

    public Curso getCurso() {
        return curso;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }
}

