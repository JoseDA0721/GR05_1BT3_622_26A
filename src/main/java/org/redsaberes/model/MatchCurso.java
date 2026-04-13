package org.redsaberes.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "match_curso",
    uniqueConstraints = @UniqueConstraint(columnNames = {"curso_id", "estudiante_id"})
)
public class MatchCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_confirmacion")
    private String fechaConfirmacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    // Constructores
    public MatchCurso() {
    }

    public MatchCurso(Integer id, String fechaConfirmacion, Curso curso, Usuario estudiante, Usuario creador) {
        this.id = id;
        this.fechaConfirmacion = fechaConfirmacion;
        this.curso = curso;
        this.estudiante = estudiante;
        this.creador = creador;
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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }
}

