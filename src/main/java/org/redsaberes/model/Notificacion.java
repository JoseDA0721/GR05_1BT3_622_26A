package org.redsaberes.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Usuario que RECIBE la notificación (propietario del curso).
     * Es el usuario sobre el que recae la notificación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_receptor_id", nullable = false)
    private Usuario usuarioReceptor;

    /**
     * Usuario que CAUSA/EMITE la notificación (quien dio el like).
     * Es la fuente del evento que genera la notificación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_emisor_id", nullable = false)
    private Usuario usuarioEmisor;

    /**
     * Curso al que se le notifica sobre el like.
     * Link al contexto del evento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    /**
     * Tipo de notificación (ej: LIKE_RECIBIDO).
     * Define qué tipo de evento generó esta notificación.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacion tipo;

    /**
     * Estado de la notificación (LEIDO o NO_LEIDO).
     * Indica si el usuario la ha visto.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoNotificacion estado = EstadoNotificacion.NO_LEIDO;

    /**
     * Fecha en que se creó la notificación.
     */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    // ===== CONSTRUCTORES =====

    public Notificacion() {
    }

    public Notificacion(Usuario usuarioReceptor, Usuario usuarioEmisor, Curso curso,
                        TipoNotificacion tipo, LocalDate fechaCreacion) {
        this.usuarioReceptor = usuarioReceptor;
        this.usuarioEmisor = usuarioEmisor;
        this.curso = curso;
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
    }

    // ===== GETTERS Y SETTERS =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuarioReceptor() {
        return usuarioReceptor;
    }

    public void setUsuarioReceptor(Usuario usuarioReceptor) {
        this.usuarioReceptor = usuarioReceptor;
    }

    public Usuario getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(Usuario usuarioEmisor) {
        this.usuarioEmisor = usuarioEmisor;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public EstadoNotificacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoNotificacion estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // ===== MÉTODOS HELPER =====

    /**
     * Marca la notificación como leída.
     */
    public void marcarComoLeida() {
        this.estado = EstadoNotificacion.LEIDO;
    }

    /**
     * Verifica si la notificación aún no ha sido leída.
     */
    public boolean esNoLeida() {
        return this.estado == EstadoNotificacion.NO_LEIDO;
    }

    /**
     * Obtiene la descripción legible de la notificación para mostrar en UI.
     * Ej: "Juan García te dio like en tu curso 'React Avanzado'"
     */
    public String getDescripcion() {
        return switch(this.tipo) {
            case LIKE_RECIBIDO ->
                    String.format("%s te dio like en tu curso '%s'",
                            usuarioEmisor.getNombre(),
                            curso.getTitulo());

            case MATCH_RECIBIDO ->
                    String.format("%s te aceptó en su curso '%s'",
                            usuarioEmisor.getNombre(),
                            curso.getTitulo());
            case REVIEW_RECIBIDA ->
                    String.format("%s te dejó una reseña en tu curso '%s'",
                            usuarioEmisor.getNombre(),
                            curso.getTitulo());
        };
    }
}
