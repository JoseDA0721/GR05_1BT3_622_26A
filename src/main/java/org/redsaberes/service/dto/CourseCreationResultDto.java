package org.redsaberes.service.dto;

public class CourseCreationResultDto {

    private final CourseCreationOutcome outcome;
    private final String error;

    public CourseCreationResultDto(CourseCreationOutcome outcome, String error) {
        this.outcome = outcome;
        this.error = error;
    }

    public CourseCreationOutcome getOutcome() {
        return outcome;
    }

    public String getError() {
        return error;
    }
}

