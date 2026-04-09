package org.redsaberes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "leccion")
public class Leccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "El título de la lección es requerido")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    // ===== RELACIONES =====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    // Constructores
    public Leccion() {
    }

    public Leccion(Integer id, String titulo, String contenido, Modulo modulo) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.modulo = modulo;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }
}

