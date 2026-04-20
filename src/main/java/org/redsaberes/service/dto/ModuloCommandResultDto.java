package org.redsaberes.service.dto;

public class ModuloCommandResultDto {

    private final ModuloCommandOutcome outcome;
    private final Integer moduloId;
    private final Integer cursoId;

    public ModuloCommandResultDto(ModuloCommandOutcome outcome,
                                  Integer moduloId,
                                  Integer cursoId) {
        this.outcome = outcome;
        this.moduloId = moduloId;
        this.cursoId = cursoId;
    }

    public ModuloCommandOutcome getOutcome() {
        return outcome;
    }

    public Integer getModuloId() {
        return moduloId;
    }

    public Integer getCursoId() {
        return cursoId;
    }
}

