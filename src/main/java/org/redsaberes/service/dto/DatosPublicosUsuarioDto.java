package org.redsaberes.service.dto;

public class DatosPublicosUsuarioDto {

    private final Integer id;
    private final String nombreUsuario;
    private final double estrellas;
    private final int matchesActivos;

    public DatosPublicosUsuarioDto(Integer id,
                                   String nombreUsuario,
                                   double estrellas,
                                   int matchesActivos) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.estrellas = estrellas;
        this.matchesActivos = matchesActivos;
    }

    public Integer getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public double getEstrellas() {
        return estrellas;
    }

    public int getMatchesActivos() {
        return matchesActivos;
    }
}

