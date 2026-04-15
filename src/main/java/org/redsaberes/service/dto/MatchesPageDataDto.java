package org.redsaberes.service.dto;

import org.redsaberes.model.Curso;

import java.util.List;
import java.util.Map;

public class MatchesPageDataDto {

    private final List<Map<String, Object>> interested;
    private final List<Map<String, Object>> matches;
    private final List<Curso> myCourses;
    private final Integer selectedCourseId;

    public MatchesPageDataDto(List<Map<String, Object>> interested,
                              List<Map<String, Object>> matches,
                              List<Curso> myCourses,
                              Integer selectedCourseId) {
        this.interested = interested;
        this.matches = matches;
        this.myCourses = myCourses;
        this.selectedCourseId = selectedCourseId;
    }

    public List<Map<String, Object>> getInterested() {
        return interested;
    }

    public List<Map<String, Object>> getMatches() {
        return matches;
    }

    public List<Curso> getMyCourses() {
        return myCourses;
    }

    public Integer getSelectedCourseId() {
        return selectedCourseId;
    }
}

