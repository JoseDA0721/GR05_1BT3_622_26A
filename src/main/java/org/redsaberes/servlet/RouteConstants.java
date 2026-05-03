package org.redsaberes.servlet;

/**
 * Constantes centralizadas para rutas de la aplicación.
 * Evita magic strings en servlets y facilita cambios de rutas.
 */
public final class RouteConstants {

    // ────────────────────────────── Servlets ──────────────────────────────
    public static final String EXPLORE = "/explore";
    public static final String COURSE_OVERVIEW = "/course-overview";
    public static final String LOGIN = "/login";

    // ────────────────────────────── JSP Views ──────────────────────────────
    public static final String VIEW_COURSE_OVERVIEW = "/WEB-INF/views/inc2/course-overview.jsp";

    // ────────────────────────────── Error Codes ──────────────────────────────
    public static final String ERROR_INVALID_COURSE = "invalid_course";
    public static final String ERROR_COURSE_NOT_FOUND = "course_not_found";
    public static final String ERROR_GENERIC = "1";

    // ────────────────────────────── Query Parameters ──────────────────────────────
    public static final String PARAM_ID = "id";
    public static final String PARAM_ERROR = "error";
    public static final String PARAM_MSG = "msg";

    // ────────────────────────────── URL Builders ──────────────────────────────

    /**
     * Construye URL de error para /explore
     * @param error código de error (ej: "invalid_course")
     * @return URL con parámetro error
     */
    public static String exploreErrorUrl(String error) {
        return EXPLORE + "?" + PARAM_ERROR + "=" + error;
    }

    /**
     * Construye URL de course-overview con ID y mensaje
     * @param courseId ID del curso
     * @param msg mensaje flash (ej: "review-saved")
     * @return URL con parámetros id y msg
     */
    public static String courseOverviewUrl(int courseId, String msg) {
        return COURSE_OVERVIEW + "?" + PARAM_ID + "=" + courseId + "&" + PARAM_MSG + "=" + msg;
    }

    /**
     * Construye URL de course-overview solo con ID
     * @param courseId ID del curso
     * @return URL con parámetro id
     */
    public static String courseOverviewUrl(int courseId) {
        return COURSE_OVERVIEW + "?" + PARAM_ID + "=" + courseId;
    }

    private RouteConstants() {
        // No instanciar
    }
}

