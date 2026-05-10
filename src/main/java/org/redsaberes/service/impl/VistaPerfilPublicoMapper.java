package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.VistaPerfilPublicoViewDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VistaPerfilPublicoMapper {

    public VistaPerfilPublicoViewDto map(Usuario usuario, int matchesActivos) {
        if (usuario == null) {
            return new VistaPerfilPublicoViewDto(null, 0, matchesActivos, 0.0, List.of());
        }

        List<Curso> cursos = usuario.getCursos() == null ? List.of() : usuario.getCursos();
        List<Resena> resenas = new ArrayList<>();

        for (Curso curso : cursos) {
            if (curso != null && curso.getResenas() != null) {
                resenas.addAll(curso.getResenas());
            }
        }

        double promedio = UsuarioServiceImpl.calcularPromedio(resenas);
        List<String> primerasResenas = resenas.stream()
                .filter(resena -> resena != null && resena.getComentario() != null)
                .sorted(Comparator
                        .comparing(Resena::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Resena::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(3)
                .map(Resena::getComentario)
                .toList();

        return new VistaPerfilPublicoViewDto(
                usuario.getNombre(),
                cursos.size(),
                matchesActivos,
                promedio,
                primerasResenas
        );
    }

    // calcularPromedio fue movido a UsuarioServiceImpl para evitar duplicación y permitir reutilización
}

