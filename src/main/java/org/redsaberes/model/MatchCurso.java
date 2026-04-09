package org.redsaberes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "match_curso")
public class MatchCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_confirmacion")
    private String fechaConfirmacion;

    // ===== RELACIONES =====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario1_id")
    private Usuario usuario1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario2_id")
    private Usuario usuario2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso1_id")
    private Curso curso1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso2_id")
    private Curso curso2;

    // Constructores
    public MatchCurso() {
    }

    public MatchCurso(Integer id, String fechaConfirmacion, Usuario usuario1,
                     Usuario usuario2, Curso curso1, Curso curso2) {
        this.id = id;
        this.fechaConfirmacion = fechaConfirmacion;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.curso1 = curso1;
        this.curso2 = curso2;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(String fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    public Usuario getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(Usuario usuario2) {
        this.usuario2 = usuario2;
    }

    public Curso getCurso1() {
        return curso1;
    }

    public void setCurso1(Curso curso1) {
        this.curso1 = curso1;
    }

    public Curso getCurso2() {
        return curso2;
    }

    public void setCurso2(Curso curso2) {
        this.curso2 = curso2;
    }
}

