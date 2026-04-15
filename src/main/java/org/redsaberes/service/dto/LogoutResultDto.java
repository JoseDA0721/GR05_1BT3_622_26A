package org.redsaberes.service.dto;

import jakarta.servlet.http.Cookie;

public class LogoutResultDto {

    private final Cookie cookieToDelete;

    public LogoutResultDto(Cookie cookieToDelete) {
        this.cookieToDelete = cookieToDelete;
    }

    public Cookie getCookieToDelete() {
        return cookieToDelete;
    }
}

