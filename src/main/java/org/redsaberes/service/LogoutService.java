package org.redsaberes.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.redsaberes.service.dto.LogoutResultDto;

public interface LogoutService {

    LogoutResultDto logout(HttpSession session, Cookie[] cookies);
}

