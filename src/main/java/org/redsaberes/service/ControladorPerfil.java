package org.redsaberes.service;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.VistaPerfilPublicoViewDto;

import java.util.ArrayList;
import java.util.List;

public class ControladorPerfil {

    /**
     * Obtiene la vista del perfil público de un usuario.
     * Calcula el promedio de calificación basado en las reseñas de todos sus cursos.
     */
    public VistaPerfilPublicoViewDto obtenerPerfilPublico(Usuario usuario) {
        if (usuario == null) {
            return new VistaPerfilPublicoViewDto(null, 0, 0, 0.0, List.of());
        }

        String nombreUsuario = usuario.getNombre();
        List<Curso> cursos = usuario.getCursos() == null ? List.of() : usuario.getCursos();

        int cursosCreados = cursos.size();
        double calificacionPromedio = calcularPromedioCalificacion(cursos);
        int matchesActivos = 0;
        List<String> primerasResenas = new ArrayList<>();

        return new VistaPerfilPublicoViewDto(
                nombreUsuario,
                cursosCreados,
                matchesActivos,
                calificacionPromedio,
                primerasResenas
        );
    }

    /**
     * Calcula el promedio de calificación a partir de todas las reseñas
     * de los cursos del usuario.
     */
    private double calcularPromedioCalificacion(List<Curso> cursos) {
        if (cursos.isEmpty()) {
            return 0.0;
        }

        List<Resena> todasLasResenas = new ArrayList<>();

        // Recolectar todas las reseñas de todos los cursos
        for (Curso curso : cursos) {
            if (curso != null && curso.getResenas() != null) {
                todasLasResenas.addAll(curso.getResenas());
            }
        }

        // Si no hay reseñas, retornar 0.0
        if (todasLasResenas.isEmpty()) {
            return 0.0;
        }

        // Calcular suma de estrellas
        int sumaEstrellas = 0;
        int cantidadResenas = 0;
        for (Resena resena : todasLasResenas) {
            if (resena != null && resena.getEstrellas() != null) {
                sumaEstrellas += resena.getEstrellas();
                cantidadResenas++;
            }
        }

        return cantidadResenas == 0 ? 0.0 : (double) sumaEstrellas / cantidadResenas;
    }
}


