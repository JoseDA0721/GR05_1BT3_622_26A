package org.redsaberes.service;

import jakarta.servlet.http.Part;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.CourseCreationResultDto;

public interface CourseCreationService {

    CourseCreationResultDto createDraftCourse(Usuario usuario,
                                              String titulo,
                                              String descripcion,
                                              String categoria,
                                              String nivel,
                                              Part filePart,
                                              String uploadDir);
}

