package org.redsaberes.service;

import org.redsaberes.service.dto.CourseOverviewDto;

public interface CourseOverviewService {
    CourseOverviewDto buildCourseOverview(Integer usuarioId, Integer courseId);
}
