package org.redsaberes.service;

import org.redsaberes.service.dto.CourseMaterialViewDto;

public interface CourseMaterialService {

    CourseMaterialViewDto buildCourseMaterialView(Integer usuarioId, Integer cursoId);
}

