package org.redsaberes.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility to centralize servlet error handling (logging + generic redirect).
 */
public final class ServletErrorHandler {

    private ServletErrorHandler() { /* utility */ }

    public static void handleServerError(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Logger logger,
                                         String message,
                                         Throwable t) {
        if (logger != null) {
            logger.log(Level.SEVERE, message, t);
        }
        try {
            response.sendRedirect(request.getContextPath() + RouteConstants.exploreErrorUrl(RouteConstants.ERROR_GENERIC));
        } catch (IOException e) {
            if (logger != null) {
                logger.log(Level.SEVERE, "Failed to redirect to generic error page", e);
            }
        }
    }
}

