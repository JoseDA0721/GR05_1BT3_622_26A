package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Inscripcion;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.InscripcionRepository;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.InscripcionRepositoryImpl;
import org.redsaberes.repository.impl.UsuarioRepositoryImpl;
import org.redsaberes.service.CourseProgressService;

import java.time.LocalDate;
import java.util.Optional;

public class CourseProgressServiceImpl implements CourseProgressService {

    private final InscripcionRepository inscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public CourseProgressServiceImpl() {
        this(new InscripcionRepositoryImpl(), new UsuarioRepositoryImpl(), new CursoRepositoryImpl());
    }

    CourseProgressServiceImpl(InscripcionRepository inscripcionRepository,
                              UsuarioRepository usuarioRepository,
                              CursoRepository cursoRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @Override
    public boolean updateProgress(Integer usuarioId, Integer cursoId, Integer progreso) {
        if (usuarioId == null || cursoId == null || progreso == null) {
            return false;
        }

        int normalizedProgress = Math.max(0, Math.min(100, progreso));
        Optional<Inscripcion> existing = inscripcionRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId);

        if (existing.isPresent()) {
            Inscripcion inscripcion = existing.get();
            System.out.println("ID encontrado: " + inscripcion.getId());
            inscripcion.setProgreso(normalizedProgress);
            inscripcionRepository.update(inscripcion);
            return true;
        }

        System.out.println("No encontrado, creando nueva inscripción");
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        Optional<Curso> curso = cursoRepository.findById(cursoId);
        if (usuario.isEmpty() || curso.isEmpty()) {
            return false;
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUsuario(usuario.get());
        inscripcion.setCurso(curso.get());
        inscripcion.setFecha(LocalDate.now().toString());
        inscripcion.setProgreso(normalizedProgress);
        inscripcionRepository.save(inscripcion);
        return true;
    }

    @Override
    public Integer getSavedProgress(Integer usuarioId, Integer cursoId) {
        if (usuarioId == null || cursoId == null) {
            return 0;
        }

        return inscripcionRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId)
                .map(Inscripcion::getProgreso)
                .filter(value -> value != null && value > 0)
                .orElse(0);
    }
}
