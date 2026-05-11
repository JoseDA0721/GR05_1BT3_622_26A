package org.redsaberes.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redsaberes.model.*;
import org.redsaberes.repository.NotificacionRepository;
import org.redsaberes.service.exception.ServiceValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacionServiceImplTest {
    @Mock
    private NotificacionRepository notificacionRepository;

    private NotificacionServiceImpl notificacionService;
    private Usuario usuarioEmisor;
    private Usuario usuarioReceptor;
    private Curso curso;
    private Notificacion notificacion;
    private Notificacion notificacion2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificacionService = new NotificacionServiceImpl(notificacionRepository);

        //Usuarios para los test
        usuarioEmisor = new Usuario();
        usuarioEmisor.setId(1);
        usuarioEmisor.setNombre("Emisor");
        usuarioEmisor.setCorreoElectronico("emisor@test.com");

        usuarioReceptor = new Usuario();
        usuarioReceptor.setId(2);
        usuarioReceptor.setNombre("Receptor");
        usuarioReceptor.setCorreoElectronico("receptor@test.com");

        //Curso para los test
        curso = new Curso();
        curso.setId(1);
        curso.setTitulo("Curso de Test");
        curso.setUsuario(usuarioReceptor);
        curso.setEstado(EstadoCurso.PUBLICO);

        //Notificacion para los test
        notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setUsuarioReceptor(usuarioReceptor);
        notificacion.setUsuarioEmisor(usuarioEmisor);
        notificacion.setCurso(curso);
        notificacion.setTipo(TipoNotificacion.LIKE_RECIBIDO);
        notificacion.setEstado(EstadoNotificacion.NO_LEIDO);
        notificacion.setFechaCreacion(LocalDate.now());

        notificacion2 = new Notificacion();
        notificacion2.setId(2);
        notificacion2.setUsuarioReceptor(usuarioReceptor);
        notificacion2.setUsuarioEmisor(usuarioEmisor);
        notificacion2.setCurso(curso);
        notificacion2.setTipo(TipoNotificacion.REVIEW_RECIBIDA);
        notificacion2.setEstado(EstadoNotificacion.NO_LEIDO);
        notificacion2.setFechaCreacion(LocalDate.now());

        when(notificacionRepository
                .existsByUsuarioEmisorAndCursoAndTipo(
                        anyInt(), anyInt(), any(TipoNotificacion.class)))
                .thenReturn(false);
    }

    @Test
    void given_valid_like_when_notify_then_create_notification() throws ServiceValidationException {
        ArgumentCaptor<Notificacion> notificacionCaptor = ArgumentCaptor.forClass(Notificacion.class);

        notificacionService.createNotification(
                usuarioReceptor,
                usuarioEmisor,
                curso,
                TipoNotificacion.LIKE_RECIBIDO
        );

        verify(notificacionRepository, times(1)).save(notificacionCaptor.capture());
        Notificacion savedNotification = notificacionCaptor.getValue();
        assertEquals(usuarioReceptor.getId(), savedNotification.getUsuarioReceptor().getId());
        assertEquals(usuarioEmisor.getId(), savedNotification.getUsuarioEmisor().getId());
        assertEquals(curso.getId(), savedNotification.getCurso().getId());
        assertEquals(TipoNotificacion.LIKE_RECIBIDO, savedNotification.getTipo());
        assertEquals(EstadoNotificacion.NO_LEIDO, savedNotification.getEstado());
        assertNotNull(savedNotification.getFechaCreacion());
    }

    @Test
    void given_like_to_own_course_when_notify_then_throw_exception() {
        curso.setUsuario(usuarioEmisor);

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> notificacionService.createNotification(
                usuarioReceptor,
                usuarioEmisor,
                curso,
                TipoNotificacion.LIKE_RECIBIDO
        ));

        assertEquals("El usuario emisor no puede ser el dueño del curso", exception.getMessage());
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void given_duplicate_like_when_notify_then_do_not_create_second_notification() {
        when(notificacionRepository.existsByUsuarioEmisorAndCursoAndTipo(
                usuarioEmisor.getId(),
                curso.getId(),
                TipoNotificacion.LIKE_RECIBIDO
        )).thenReturn(true);

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () ->
                notificacionService.createNotification(
                        usuarioReceptor,
                        usuarioEmisor,
                        curso,
                        TipoNotificacion.LIKE_RECIBIDO
                )
        );

        assertEquals("No se puede duplicar notificaciones", exception.getMessage());
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void given_null_emisor_when_notify_then_throw_or_return_error() {
        usuarioEmisor = null;

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () ->
                notificacionService.createNotification(
                        usuarioReceptor,
                        usuarioEmisor,
                        curso,
                        TipoNotificacion.LIKE_RECIBIDO
                )
        );

        assertEquals("Usuario emisor no válido", exception.getMessage());
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void given_null_receptor_when_notify_then_throw_error() {
        usuarioReceptor = null;

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () ->
                notificacionService.createNotification(
                        usuarioReceptor,
                        usuarioEmisor,
                        curso,
                        TipoNotificacion.LIKE_RECIBIDO
                )
        );

        assertEquals("Usuario receptor no válido", exception.getMessage());
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void given_null_curso_when_notify_then_throw_or_return_error() {
        curso = null;

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () ->
                notificacionService.createNotification(
                        usuarioReceptor,
                        usuarioEmisor,
                        curso,
                        TipoNotificacion.LIKE_RECIBIDO
                )
        );

        assertEquals("Curso no válido", exception.getMessage());
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void given_curso_without_owner_when_notify_then_throw_error() {
        curso.setUsuario(null);

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () ->
                notificacionService.createNotification(
                        usuarioReceptor,
                        usuarioEmisor,
                        curso,
                        TipoNotificacion.LIKE_RECIBIDO
                )
        );

        assertEquals("Curso sin propietario", exception.getMessage());
        verify(notificacionRepository, never()).save(any(Notificacion.class));
    }

    @Test
    void given_notification_when_mark_as_read_then_estado_changes_to_leido() {
        assertEquals(EstadoNotificacion.NO_LEIDO, notificacion.getEstado());

        notificacionService.markAsRead(notificacion);

        assertEquals(EstadoNotificacion.LEIDO, notificacion.getEstado());
        verify(notificacionRepository, times(1)).update(notificacion);
    }

    @ParameterizedTest
    @MethodSource("provideUnreadNotificationsCases")
    void given_receptorId_when_getUnread_then_returns_only_unread_notifications(
            List<Notificacion> repoNotifications,
            int expectedUnreadNotifications
    ) {
        List<Notificacion> repoUnread = repoNotifications.stream()
                .filter(n -> n.getEstado() == EstadoNotificacion.NO_LEIDO)
                .collect(Collectors.toList());

        when(notificacionRepository.findUnreadByUsuarioReceptorId(usuarioReceptor.getId())).thenReturn(repoUnread);

        List<Notificacion> unreadNotifications = notificacionService.getUnread(usuarioReceptor.getId());

        assertEquals(expectedUnreadNotifications, unreadNotifications.size());
        assertTrue(unreadNotifications.stream()
                .allMatch(n -> n.getEstado() == EstadoNotificacion.NO_LEIDO));

        verify(notificacionRepository, times(1)).findUnreadByUsuarioReceptorId(usuarioReceptor.getId());
    }

    private static Stream<Arguments> provideUnreadNotificationsCases() {
        Usuario receptor = new Usuario();
        receptor.setId(2);

        Usuario emisor = new Usuario();
        emisor.setId(1);

        Curso curso = new Curso();
        curso.setId(1);

        Notificacion leida = new Notificacion();
        leida.setUsuarioReceptor(receptor);
        leida.setUsuarioEmisor(emisor);
        leida.setCurso(curso);
        leida.setTipo(TipoNotificacion.LIKE_RECIBIDO);
        leida.setEstado(EstadoNotificacion.LEIDO);
        leida.setFechaCreacion(LocalDate.now());

        Notificacion noLeida1 = new Notificacion();
        noLeida1.setUsuarioReceptor(receptor);
        noLeida1.setUsuarioEmisor(emisor);
        noLeida1.setCurso(curso);
        noLeida1.setTipo(TipoNotificacion.LIKE_RECIBIDO);
        noLeida1.setEstado(EstadoNotificacion.NO_LEIDO);
        noLeida1.setFechaCreacion(LocalDate.now());

        Notificacion noLeida2 = new Notificacion();
        noLeida2.setUsuarioReceptor(receptor);
        noLeida2.setUsuarioEmisor(emisor);
        noLeida2.setCurso(curso);
        noLeida2.setTipo(TipoNotificacion.LIKE_RECIBIDO);
        noLeida2.setEstado(EstadoNotificacion.NO_LEIDO);
        noLeida2.setFechaCreacion(LocalDate.now());

        return Stream.of(
                Arguments.of(List.of(leida, noLeida1), 1),
                Arguments.of(List.of(noLeida1, noLeida2), 2),
                Arguments.of(List.of(leida), 0)
        );
    }

    @Test
    void given_valid_review_when_notify_then_create_notification_review() throws ServiceValidationException {
        ArgumentCaptor<Notificacion> notificacionCaptor = ArgumentCaptor.forClass(Notificacion.class);

        notificacionService.createNotification(
                usuarioReceptor,   // receptor = dueño del curso
                usuarioEmisor,     // emisor = quien escribe la reseña
                curso,
                TipoNotificacion.REVIEW_RECIBIDA
        );

        verify(notificacionRepository, times(1)).save(notificacionCaptor.capture());
        Notificacion saved = notificacionCaptor.getValue();
        assertEquals(usuarioReceptor.getId(), saved.getUsuarioReceptor().getId());
        assertEquals(usuarioEmisor.getId(), saved.getUsuarioEmisor().getId());
        assertEquals(curso.getId(), saved.getCurso().getId());
        assertEquals(TipoNotificacion.REVIEW_RECIBIDA, saved.getTipo());
        assertEquals(EstadoNotificacion.NO_LEIDO, saved.getEstado());
        assertNotNull(saved.getFechaCreacion());
    }

    @Test
    void give_like_notification_when_get_description_then_ok(){
        String result = notificacionService.getDescripcion(notificacion);
        assertEquals("Emisor te dio like en tu curso 'Curso de Test'", result);
    }

    @Test
    void give_review_notification_when_get_description_then_ok() {
        String result = notificacionService.getDescripcion(notificacion2);
        assertEquals("Emisor te dejó una reseña en tu curso 'Curso de Test'", result);
    }

    // Test de centro de notificaciones

    private static Stream<Arguments> provideNotification() {
        Usuario receptor = new Usuario();
        receptor.setId(2);

        Usuario emisor = new Usuario();
        emisor.setId(1);

        Curso curso = new Curso();
        curso.setId(1);

        Notificacion leida = new Notificacion();
        leida.setUsuarioReceptor(receptor);
        leida.setUsuarioEmisor(emisor);
        leida.setCurso(curso);
        leida.setTipo(TipoNotificacion.LIKE_RECIBIDO);
        leida.setEstado(EstadoNotificacion.LEIDO);
        leida.setFechaCreacion(LocalDate.now());

        Notificacion noLeida1 = new Notificacion();
        noLeida1.setUsuarioReceptor(receptor);
        noLeida1.setUsuarioEmisor(emisor);
        noLeida1.setCurso(curso);
        noLeida1.setTipo(TipoNotificacion.LIKE_RECIBIDO);
        noLeida1.setEstado(EstadoNotificacion.NO_LEIDO);
        noLeida1.setFechaCreacion(LocalDate.now());

        Notificacion noLeida2 = new Notificacion();
        noLeida2.setUsuarioReceptor(receptor);
        noLeida2.setUsuarioEmisor(emisor);
        noLeida2.setCurso(curso);
        noLeida2.setTipo(TipoNotificacion.LIKE_RECIBIDO);
        noLeida2.setEstado(EstadoNotificacion.NO_LEIDO);
        noLeida2.setFechaCreacion(LocalDate.now());

        // Notificaciones de tipo REVIEW_RECIBIDA
        Notificacion reviewLeida = new Notificacion();
        reviewLeida.setUsuarioReceptor(receptor);
        reviewLeida.setUsuarioEmisor(emisor);
        reviewLeida.setCurso(curso);
        reviewLeida.setTipo(TipoNotificacion.REVIEW_RECIBIDA);
        reviewLeida.setEstado(EstadoNotificacion.LEIDO);
        reviewLeida.setFechaCreacion(LocalDate.now());

        Notificacion reviewNoLeida = new Notificacion();
        reviewNoLeida.setUsuarioReceptor(receptor);
        reviewNoLeida.setUsuarioEmisor(emisor);
        reviewNoLeida.setCurso(curso);
        reviewNoLeida.setTipo(TipoNotificacion.REVIEW_RECIBIDA);
        reviewNoLeida.setEstado(EstadoNotificacion.NO_LEIDO);
        reviewNoLeida.setFechaCreacion(LocalDate.now());

        return Stream.of(
                // Caso: una notificación LIKE leída
                Arguments.of(List.of(leida), 1),
                // Caso: dos notificaciones LIKE, una leída y una sin leer
                Arguments.of(List.of(leida, noLeida1), 2),
                // Caso: múltiples notificaciones de tipos distintos y estados distintos
                Arguments.of(List.of(leida, noLeida1, noLeida2, reviewLeida, reviewNoLeida), 5),
                // Caso: solo notificaciones sin leer de diferentes tipos
                Arguments.of(List.of(noLeida1, reviewNoLeida), 2),
                // Caso: vacío
                Arguments.of(List.of(), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNotification")
    void give_notification_when_get_all_then_return_list(
            List<Notificacion> allNotifications,
            int expectedNumberOfNotifications
    ) {
        when(notificacionRepository.findByUsuarioReceptorId(usuarioReceptor.getId()))
                .thenReturn(allNotifications);

        // Invocar el servicio
        allNotifications = notificacionService.getAllNotifications(usuarioReceptor.getId());

        // Verificar que devuelve exactamente las mismas (todas, sin filtrar)
        assertEquals(expectedNumberOfNotifications, allNotifications.size());
        verify(notificacionRepository, times(1)).findByUsuarioReceptorId(usuarioReceptor.getId());
    }
}