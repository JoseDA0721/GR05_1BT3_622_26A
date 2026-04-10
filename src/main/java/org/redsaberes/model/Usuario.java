package org.redsaberes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true)
    @Email(message = "El correo debe ser válido")
    @NotBlank(message = "El correo es requerido")
    private String correoElectronico;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;

    @Column(name = "token_sesion")
    private String tokenSesion;

    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "expiracion_token")
    private Long expiracionToken;

    // ===== RELACIONES =====

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Curso> cursos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LikeCurso> likes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Integer id, String nombre, String correoElectronico, String contrasena, String tokenSesion) {
        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.tokenSesion = tokenSesion;
    }

    // ...existing getters and setters...
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTokenSesion() {
        return tokenSesion;
    }

    public void setTokenSesion(String tokenSesion) {
        this.tokenSesion = tokenSesion;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public Long getExpiracionToken() {
        return expiracionToken;
    }

    public void setExpiracionToken(Long expiracionToken) {
        this.expiracionToken = expiracionToken;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
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
}
