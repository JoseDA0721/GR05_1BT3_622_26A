package org.redsaberes.service.dto;

import java.util.List;

public class VistaPerfilPublicoViewDto {

    private final String nombreUsuario;
    private final int cursosCreados;
    private final int matchesActivos;
    private final double calificacionPromedio;
    private final List<String> primerasResenas;

    public VistaPerfilPublicoViewDto(String nombreUsuario,
                                     int cursosCreados,
                                     int matchesActivos,
                                     double calificacionPromedio,
                                     List<String> primerasResenas) {
        this.nombreUsuario = nombreUsuario;
        this.cursosCreados = cursosCreados;
        this.matchesActivos = matchesActivos;
        this.calificacionPromedio = calificacionPromedio;
        this.primerasResenas = primerasResenas;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public int getCursosCreados() {
        return cursosCreados;
    }

    public int getMatchesActivos() {
        return matchesActivos;
    }

    public double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public List<String> getPrimerasResenas() {
        return primerasResenas;
    }
}

