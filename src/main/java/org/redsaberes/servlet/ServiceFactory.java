package org.redsaberes.servlet;

import org.redsaberes.repository.*;
import org.redsaberes.repository.impl.*;
import org.redsaberes.service.*;
import org.redsaberes.service.impl.*;
import org.redsaberes.service.validator.ReviewValidator;

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
// ServiceFactory.java — versión completa
public final class ServiceFactory {

    // Repositorios como singletons (thread-safe, sin estado mutable)
    private static final UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
    private static final CursoRepository cursoRepo     = new CursoRepositoryImpl();
    private static final LikeCursoRepository likeRepo  = new LikeCursoRepositoryImpl();
    private static final MatchCursoRepository matchRepo = new MatchCursoRepositoryImpl();
    private static final ModuloRepository moduloRepo   = new ModuloRepositoryImpl();
    private static final LeccionRepository leccionRepo = new LeccionRepositoryImpl();
    private static final ReviewRepository reviewRepo   = new ReviewRepositoryImpl();

    // Servicios — todos construidos con los repos de arriba
    private static final LogoutService logoutService = new LogoutServiceImpl();
    private static final AuthService authService =
            new AuthServiceImpl(usuarioRepo);
    private static final UsuarioService usuarioService =
            new UsuarioServiceImpl(usuarioRepo);
    private static final CourseCreationService courseCreationService =
            new CourseCreationServiceImpl(cursoRepo);
    private static final MyCoursesService myCoursesService =
            new MyCoursesServiceImpl(cursoRepo);
    private static final DashboardService dashboardService =
            new DashboardServiceImpl(cursoRepo, likeRepo, matchRepo);
    private static final ExploreService exploreService =
            new ExploreServiceImpl(cursoRepo, likeRepo, matchRepo);
    private static final LikeCourseService likeCourseService =
            new LikeCourseServiceImpl(cursoRepo, likeRepo);
    private static final MatchesService matchesService =
            new MatchesServiceImpl(cursoRepo, likeRepo, matchRepo);
    private static final AcceptMatchService acceptMatchService =
            new AcceptMatchServiceImpl(cursoRepo, likeRepo, matchRepo);
    private static final ModuloManagementService moduloService =
            new ModuloManagementServiceImpl(cursoRepo, moduloRepo, leccionRepo);
    private static final CourseLifecycleService lifecycleService =
            new CourseLifecycleServiceImpl(cursoRepo, moduloRepo);
    private static final CourseOverviewService overviewService =
            new CourseOverviewServiceImpl(cursoRepo, matchRepo, moduloRepo, reviewRepo);
    private static final CourseMaterialServiceImpl courseMaterialService =
            new CourseMaterialServiceImpl(cursoRepo, matchRepo, moduloRepo);
    private static final ReviewService reviewService =
            new ReviewServiceImpl(reviewRepo, new ReviewValidator());
    private static final PasswordRecoveryService recoveryService =
            new PasswordRecoveryServiceImpl(usuarioRepo);
    private static final PreviewCourseService previewCourseService = new PreviewCourseServiceImpl();


    public static AuthService getAuthService()               { return authService; }
    public static UsuarioService getUsuarioService()          { return usuarioService; }
    public static CourseCreationService getCourseCreation()   { return courseCreationService; }
    public static MyCoursesService getMyCourses()             { return myCoursesService; }
    public static DashboardService getDashboard()             { return dashboardService; }
    public static ExploreService getExplore()                 { return exploreService; }
    public static LikeCourseService getLikeCourse()           { return likeCourseService; }
    public static MatchesService getMatches()                 { return matchesService; }
    public static AcceptMatchService getAcceptMatch()         { return acceptMatchService; }
    public static ModuloManagementService getModulos()        { return moduloService; }
    public static CourseLifecycleService getLifecycle()       { return lifecycleService; }
    public static CourseOverviewService getCourseOverview()   { return overviewService; }
    public static ReviewService getReview()                   { return reviewService; }
    public static CourseMaterialService getCourseMaterial() { return courseMaterialService; }
    public static PasswordRecoveryService getRecovery() { return recoveryService; }
    public static LogoutService getLogout() { return logoutService; }
    public static PreviewCourseService getPreviewCourse() { return previewCourseService; }

    private ServiceFactory() { throw new AssertionError(); }
}

