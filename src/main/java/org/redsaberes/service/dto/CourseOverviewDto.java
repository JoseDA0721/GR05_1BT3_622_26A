package org.redsaberes.service.dto;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Modulo;
import org.redsaberes.model.Resena;

import java.util.List;

public record CourseOverviewDto(CourseMaterialViewOutcome outcome, Curso curso, List<Modulo> modulos,
                                Integer totalLecciones, long likeCount, long inscritosCount, long matchesCount,
                                Double promedioEstrellas, List<Resena> resenas, boolean yaReseno, boolean puedeResenar,
                                boolean esPropietario, boolean accesoConcedido) {


}
