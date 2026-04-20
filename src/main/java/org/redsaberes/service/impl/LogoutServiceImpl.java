package org.redsaberes.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.service.LogoutService;
import org.redsaberes.service.dto.LogoutResultDto;

public class LogoutServiceImpl implements LogoutService {

    @Override
    public LogoutResultDto logout(HttpSession session, Cookie[] cookies) {
        if (session != null) {
            session.invalidate();
        }

        Cookie cookieToDelete = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("email".equals(cookie.getName())) {
                    cookieToDelete = new Cookie("email", "");
                    cookieToDelete.setMaxAge(0);
                    cookieToDelete.setPath("/");
                    cookieToDelete.setHttpOnly(true);
                    break;
                }
            }
        }

        return new LogoutResultDto(cookieToDelete);
    }
}

