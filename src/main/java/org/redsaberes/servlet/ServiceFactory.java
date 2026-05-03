package org.redsaberes.servlet;

import org.redsaberes.service.CourseOverviewService;
import org.redsaberes.service.ReviewService;
import org.redsaberes.service.impl.CourseOverviewServiceImpl;
import org.redsaberes.service.impl.ReviewServiceImpl;

/**
 * ServiceFactory: Centraliza la creación e inyección de dependencias.
 *
 * Ventajas:
 * - Desacopla los servlets de las implementaciones concretas (Impl)
 * - Punto único para cambiar tipos de implementación o agregar mocking en tests
 * - Si en futuro migran a Spring, adaptar aquí es trivial
 *
 * Patrón: Service Locator / Factory
 */
public final class ServiceFactory {

    private static final CourseOverviewService courseOverviewServiceInstance = new CourseOverviewServiceImpl();
    private static final ReviewService reviewServiceInstance = new ReviewServiceImpl();

    // Private constructor to prevent instantiation
    private ServiceFactory() {
        throw new UnsupportedOperationException("ServiceFactory cannot be instantiated");
    }

    /**
     * Obtiene la instancia singleton de CourseOverviewService.
     * @return instancia de CourseOverviewService
     */
    public static CourseOverviewService getCourseOverviewService() {
        return courseOverviewServiceInstance;
    }

    /**
     * Obtiene la instancia singleton de ReviewService.
     * @return instancia de ReviewService
     */
    public static ReviewService getReviewService() {
        return reviewServiceInstance;
    }
}

