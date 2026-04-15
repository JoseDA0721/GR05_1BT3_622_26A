package org.redsaberes.service.dto;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;

import java.util.List;

public class ModuloPageDataDto {

    private final ModuloViewOutcome outcome;
    private final Curso curso;
    private final List<Modulo> modulos;
    private final Modulo moduloEditar;
    private final String accionModulo;
    private final boolean modoReordenar;

    public ModuloPageDataDto(ModuloViewOutcome outcome,
                             Curso curso,
                             List<Modulo> modulos,
                             Modulo moduloEditar,
                             String accionModulo,
                             boolean modoReordenar) {
        this.outcome = outcome;
        this.curso = curso;
        this.modulos = modulos;
        this.moduloEditar = moduloEditar;
        this.accionModulo = accionModulo;
        this.modoReordenar = modoReordenar;
    }

    public ModuloViewOutcome getOutcome() {
        return outcome;
    }

    public Curso getCurso() {
        return curso;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public Modulo getModuloEditar() {
        return moduloEditar;
    }

    public String getAccionModulo() {
        return accionModulo;
    }

    public boolean isModoReordenar() {
        return modoReordenar;
    }
}

