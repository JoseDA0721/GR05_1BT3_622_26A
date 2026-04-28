package org.redsaberes.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "resena")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer estrellas;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    //Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    public Resena() {
    }

    public Resena(Integer id, Integer estrellas, String comentario, LocalDate fecha, Usuario usuario, Curso curso) {
        this.id = id;
        this.estrellas = estrellas;
        this.comentario = comentario;
        this.fecha = fecha;
        this.usuario = usuario;
        this.curso = curso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(Integer estrellas) {
        this.estrellas = estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
