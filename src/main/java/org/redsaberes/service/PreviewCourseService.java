package org.redsaberes.service;

import org.redsaberes.service.dto.PreviewCourseViewDto;

public interface PreviewCourseService {

    PreviewCourseViewDto buildPreviewView(Integer usuarioId, Integer cursoId);
}

