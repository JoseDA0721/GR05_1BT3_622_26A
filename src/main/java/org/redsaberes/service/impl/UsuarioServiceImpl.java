package org.redsaberes.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.service.UsuarioService;
import org.redsaberes.service.dto.DatosPublicosUsuarioDto;
import org.redsaberes.service.exception.ServiceValidationException;
import org.redsaberes.service.exception.NameAlreadyTakenException;
import org.redsaberes.service.validator.UsuarioValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MatchCursoRepository matchCursoRepository;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this(usuarioRepository, null);
    }

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, MatchCursoRepository matchCursoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.matchCursoRepository = matchCursoRepository;
    }

    @Override
    public Usuario registrarUsuario(String nombre,
                                    String correo,
                                    String contrasena,
                                    String confirmarContrasena,
                                    boolean aceptaTerminos) throws ServiceValidationException {

        String nombreLimpio = normalizar(nombre);
        String correoLimpio = normalizar(correo);

        UsuarioValidator.validarRegistro(
                nombreLimpio,
                correoLimpio,
                contrasena,
                confirmarContrasena,
                aceptaTerminos,
                usuarioRepository
        );

        Usuario usuario = new Usuario();
        usuario.setNombre(nombreLimpio);
        usuario.setCorreoElectronico(correoLimpio);
        usuario.setContrasena(hashearContrasena(contrasena));

        usuarioRepository.save(usuario);
        return usuario;
    }

    @Override
    public Usuario registrarUsuarioConValidacion(String nombre, String email,
                                                 String contrasena, String confirmarContrasena)
            throws ServiceValidationException {

        String nombreLimpio = normalizar(nombre);
        String emailLimpio = normalizar(email);

        // Validar que el nombre no esté duplicado
        if (usuarioRepository.findByNombre(nombreLimpio).isPresent()) {
            throw new NameAlreadyTakenException(
                    "El nombre '" + nombreLimpio + "' ya existe en el sistema"
            );
        }

        // Validar que las contraseñas coincidan
        if (!contrasena.equals(confirmarContrasena)) {
            throw new ServiceValidationException("Las contraseñas no coinciden");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombreLimpio);
        usuario.setCorreoElectronico(emailLimpio);
        usuario.setContrasena(hashearContrasena(contrasena));

        // Guardar en repositorio
        usuarioRepository.save(usuario);

        return usuario;
    }

    @Override
    public Usuario actualizarPerfilBasico(Integer usuarioId,
                                          String nombre,
                                          String correo) throws ServiceValidationException {
        if (usuarioId == null) {
            throw new ServiceValidationException("Usuario inválido");
        }

        String nombreLimpio = normalizar(nombre);
        String correoLimpio = normalizar(correo);

        if (nombreLimpio == null || nombreLimpio.isBlank()) {
            throw new ServiceValidationException("El nombre es obligatorio");
        }
        if (nombreLimpio.length() > 100) {
            throw new ServiceValidationException("El nombre no puede exceder 100 caracteres");
        }
        if (correoLimpio == null || correoLimpio.isBlank()) {
            throw new ServiceValidationException("El correo es obligatorio");
        }
        if (!correoLimpio.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ServiceValidationException("El formato del correo electrónico no es válido");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ServiceValidationException("No se encontró el usuario"));

        Optional<Usuario> usuarioConMismoNombre = usuarioRepository.findByNombre(nombreLimpio);
        if (usuarioConMismoNombre.isPresent() && !usuarioConMismoNombre.get().getId().equals(usuarioId)) {
            throw new NameAlreadyTakenException("El nombre '" + nombreLimpio + "' ya existe en el sistema");
        }

        Optional<Usuario> usuarioConMismoCorreo = usuarioRepository.findByCorreo(correoLimpio);
        if (usuarioConMismoCorreo.isPresent() && !usuarioConMismoCorreo.get().getId().equals(usuarioId)) {
            throw new ServiceValidationException("El correo ya está registrado");
        }

        usuario.setNombre(nombreLimpio);
        usuario.setCorreoElectronico(correoLimpio);
        usuarioRepository.update(usuario);
        return usuario;
    }

    @Override
    public DatosPublicosUsuarioDto buscarDatosPublicos(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con id: " + usuarioId));

        double estrellas = calcularEstrellasPublicas(usuario);
        int matchesActivos = calcularMatchesActivos(usuario);

        return new DatosPublicosUsuarioDto(
                usuario.getId(),
                usuario.getNombre(),
                estrellas,
                matchesActivos
        );
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String hashearContrasena(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(12));
    }

    private double calcularEstrellasPublicas(Usuario usuario) {
        List<Curso> cursos = usuario.getCursos() == null ? List.of() : usuario.getCursos();
        List<Resena> resenas = new ArrayList<>();

        for (Curso curso : cursos) {
            if (curso != null && curso.getResenas() != null) {
                resenas.addAll(curso.getResenas());
            }
        }
        // Delegar el cálculo del promedio a un método reutilizable
        return calcularPromedio(resenas);
    }

    /**
     * Calcula el promedio de estrellas a partir de una colección de reseñas.
     * Método estático para permitir reuso desde otros componentes (p.ej. mappers)
     */
    public static double calcularPromedio(List<Resena> resenas) {
        if (resenas == null || resenas.isEmpty()) {
            return 0.0;
        }

        int suma = 0;
        int cantidad = 0;
        for (Resena resena : resenas) {
            if (resena != null && resena.getEstrellas() != null) {
                suma += resena.getEstrellas();
                cantidad++;
            }
        }

        return cantidad == 0 ? 0.0 : (double) suma / cantidad;
    }

    private int calcularMatchesActivos(Usuario usuario) {
        if (matchCursoRepository != null && usuario.getId() != null) {
            long comoCreador = Optional.ofNullable(matchCursoRepository.countByCreadorId(usuario.getId())).orElse(0L);
            long comoEstudiante = Optional.ofNullable(matchCursoRepository.countByEstudianteId(usuario.getId())).orElse(0L);
            return Math.toIntExact(comoCreador + comoEstudiante);
        }

        return 0;
    }
}