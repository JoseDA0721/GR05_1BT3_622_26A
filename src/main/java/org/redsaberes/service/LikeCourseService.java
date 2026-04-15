package org.redsaberes.service;

import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.LikeCourseOutcome;

public interface LikeCourseService {

    LikeCourseOutcome likeCourse(Usuario usuario, Integer cursoId);
}

