package org.redsaberes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "El título es requerido")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column
    private String categoria;

    @Column(name = "nivel_dificultad")
    private String nivelDificultad;

    @Column(name = "imagen_portada")
    private String imagenPortada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCurso estado = EstadoCurso.BORRADOR;

    // ===== RELACIONES =====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Modulo> modulos = new ArrayList<>();

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LikeCurso> likes = new ArrayList<>();

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Curso() {
    }

    public Curso(Integer id, String titulo, String descripcion, String categoria, String nivelDificultad,
                 String imagenPortada, EstadoCurso estado) {
        this(id, titulo, descripcion, categoria, nivelDificultad, imagenPortada, estado, null);
    }

    public Curso(Integer id, String titulo, String descripcion, String categoria, String nivelDificultad,
                 String imagenPortada, EstadoCurso estado, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.nivelDificultad = nivelDificultad;
        this.imagenPortada = imagenPortada;
        this.estado = estado;
        this.usuario = usuario;
    }

    // ...existing getters and setters...
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public EstadoCurso getEstado() {
        return estado;
    }

    public void setEstado(EstadoCurso estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    public List<LikeCurso> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeCurso> likes) {
        this.likes = likes;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    // ===== MÉTODOS HELPER =====

    public Integer getModulosCount() {
        return modulos.size();
    }

    public Integer getLikesCount() {
        return likes.size();
    }

    public Integer getInscritosCount() {
        return inscripciones.size();
    }
}
