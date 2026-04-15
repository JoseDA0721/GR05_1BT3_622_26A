package org.redsaberes.service;

import org.redsaberes.service.dto.MatchesPageDataDto;

public interface MatchesService {

    MatchesPageDataDto buildMatchesPageData(Integer usuarioId, Integer filterCourseId);
}

