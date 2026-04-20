package org.redsaberes.service.impl;

import jakarta.servlet.http.Part;
import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.service.CourseCreationService;
import org.redsaberes.service.dto.CourseCreationOutcome;
import org.redsaberes.service.dto.CourseCreationResultDto;
import org.redsaberes.util.ImagenUtil;

public class CourseCreationServiceImpl implements CourseCreationService {

    private final CursoRepository cursoRepository;

    public CourseCreationServiceImpl() {
        this(new CursoRepositoryImpl());
    }

    CourseCreationServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    public CourseCreationResultDto createDraftCourse(Usuario usuario,
                                                     String titulo,
                                                     String descripcion,
                                                     String categoria,
                                                     String nivel,
                                                     Part filePart,
                                                     String uploadDir) {
        if (usuario == null || usuario.getId() == null) {
            return new CourseCreationResultDto(CourseCreationOutcome.REDIRECT_LOGIN, null);
        }

        if (isBlank(titulo) || isBlank(descripcion)) {
            return new CourseCreationResultDto(
                    CourseCreationOutcome.FORWARD_WITH_ERROR,
                    "El título y la descripción son obligatorios."
            );
        }

        if (titulo.length() > 100) {
            return new CourseCreationResultDto(
                    CourseCreationOutcome.FORWARD_WITH_ERROR,
                    "El título no puede exceder los 100 caracteres."
            );
        }

        try {
            String fileName = ImagenUtil.guardarImagen(filePart, uploadDir);

            Curso curso = new Curso(
                    null,
                    titulo.trim(),
                    descripcion,
                    categoria,
                    nivel,
                    fileName,
                    EstadoCurso.BORRADOR,
                    usuario
            );

            cursoRepository.save(curso);
            return new CourseCreationResultDto(CourseCreationOutcome.CREATED, null);
        } catch (Exception e) {
            return new CourseCreationResultDto(
                    CourseCreationOutcome.FORWARD_WITH_ERROR,
                    "Error al crear el curso." + e.getMessage()
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

