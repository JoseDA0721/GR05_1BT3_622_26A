package org.redsaberes.service;

import org.redsaberes.service.dto.CourseLifecycleOutcome;
import org.redsaberes.service.dto.PublishCourseViewDto;

public interface CourseLifecycleService {

    PublishCourseViewDto preparePublishView(Integer cursoId, Integer usuarioId);

    CourseLifecycleOutcome publishCourse(Integer cursoId, Integer usuarioId);

    CourseLifecycleOutcome archiveCourse(Integer cursoId, Integer usuarioId);
}

