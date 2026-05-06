package org.redsaberes.service.impl;

import jakarta.servlet.ServletException;
import org.redsaberes.model.*;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.service.LikeCourseService;
import org.redsaberes.service.NotificacionService;
import org.redsaberes.service.dto.LikeCourseOutcome;
import org.redsaberes.service.exception.ServiceValidationException;

import java.time.LocalDateTime;
import java.util.Optional;

public class LikeCourseServiceImpl implements LikeCourseService {

    private final CursoRepository cursoRepository;
    private final LikeCursoRepository likeCursoRepository;
    private final NotificacionService notificacionService;

    public LikeCourseServiceImpl(CursoRepository cursoRepository,
                                 LikeCursoRepository likeCursoRepository,
                                 NotificacionService notificacionService) {
        this.cursoRepository = cursoRepository;
        this.likeCursoRepository = likeCursoRepository;
        this.notificacionService = notificacionService;
    }

    // Backwards-compatible constructor
    public LikeCourseServiceImpl(CursoRepository cursoRepository,
                                 LikeCursoRepository likeCursoRepository) {
        this(cursoRepository, likeCursoRepository, null);
    }

    @Override
    public LikeCourseOutcome likeCourse(Usuario usuario, Integer cursoId) {
        if (usuario == null || usuario.getId() == null || cursoId == null) {
            return LikeCourseOutcome.INVALID_COURSE;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty()) {
            return LikeCourseOutcome.COURSE_NOT_FOUND;
        }

        Curso curso = cursoOpt.get();
        if (curso.getEstado() != EstadoCurso.PUBLICO
                || curso.getUsuario() == null
                || curso.getUsuario().getId() == null
                || curso.getUsuario().getId().equals(usuario.getId())) {
            return LikeCourseOutcome.FORBIDDEN;
        }

        if (!likeCursoRepository.existsByUsuarioAndCurso(usuario.getId(), cursoId)) {
            LikeCurso likeCurso = new LikeCurso();
            likeCurso.setCurso(curso);
            likeCurso.setUsuario(usuario);
            likeCurso.setFecha(LocalDateTime.now().toString());
            likeCursoRepository.save(likeCurso);
            try{
                System.out.println("Creando notificación para usuario " + curso.getUsuario().getNombre() + " por like de usuario " + usuario.getNombre() + " en curso " + curso.getTitulo());
                // Parámetros correctos: usuarioReceptor (dueño del curso), usuarioEmisor (quien dio like)
                notificacionService.createNotification(curso.getUsuario(), usuario, curso, TipoNotificacion.LIKE_RECIBIDO);
            } catch (ServiceValidationException e){
                // Loguear el error pero no interrumpir el flujo de la aplicación
                System.err.println("Error al crear notificación: " + e.getMessage());
            }
        }

        return LikeCourseOutcome.LIKED;
    }
}

