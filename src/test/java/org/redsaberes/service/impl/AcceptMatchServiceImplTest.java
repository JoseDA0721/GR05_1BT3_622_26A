package org.redsaberes.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.redsaberes.model.Curso;
import org.redsaberes.model.Usuario;
import org.redsaberes.model.MatchCurso;
import org.redsaberes.model.TipoNotificacion;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.service.NotificacionService;
import org.redsaberes.service.dto.AcceptMatchOutcome;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AcceptMatchServiceImplTest {

	@Test
	void acceptMatch_createsMatchAndNotifiesReceptor_whenValidInput() throws Exception {
		// Arrange
		Integer creadorId = 10;
		Integer cursoId = 100;
		Integer estudianteId = 20;

		Curso curso = new Curso();
		Usuario propietario = new Usuario();
		propietario.setId(creadorId);
		curso.setId(cursoId);
		curso.setUsuario(propietario);

		CursoRepository cursoRepo = mock(CursoRepository.class);
		LikeCursoRepository likeRepo = mock(LikeCursoRepository.class);
		MatchCursoRepository matchRepo = mock(MatchCursoRepository.class);
		NotificacionService notificacionService = mock(NotificacionService.class);

		when(cursoRepo.findById(cursoId)).thenReturn(Optional.of(curso));
		when(likeRepo.existsByUsuarioAndCurso(estudianteId, cursoId)).thenReturn(true);
		when(matchRepo.existsByCursoAndUsuario(cursoId, estudianteId)).thenReturn(false);

		AcceptMatchServiceImpl service = new AcceptMatchServiceImpl(
				cursoRepo, likeRepo, matchRepo, notificacionService
		);

		// Act
		AcceptMatchOutcome result = service.acceptMatch(creadorId, cursoId, estudianteId);

		// Assert
		assertEquals(AcceptMatchOutcome.MATCHED, result);

		// Verify that a match was saved
		verify(matchRepo, times(1)).save(any(MatchCurso.class));

		// Verify that notification was created with expected parameters
		ArgumentCaptor<Usuario> receptorCaptor = ArgumentCaptor.forClass(Usuario.class);
		ArgumentCaptor<Usuario> emisorCaptor = ArgumentCaptor.forClass(Usuario.class);
		ArgumentCaptor<Curso> cursoCaptor = ArgumentCaptor.forClass(Curso.class);
		ArgumentCaptor<TipoNotificacion> tipoCaptor = ArgumentCaptor.forClass(TipoNotificacion.class);

		verify(notificacionService, times(1)).createNotification(
				receptorCaptor.capture(), emisorCaptor.capture(), cursoCaptor.capture(), tipoCaptor.capture()
		);

		Usuario receptor = receptorCaptor.getValue();
		Usuario emisor = emisorCaptor.getValue();
		Curso cursoArg = cursoCaptor.getValue();
		TipoNotificacion tipo = tipoCaptor.getValue();

		assertEquals(estudianteId, receptor.getId());
		assertEquals(creadorId, emisor.getId());
		assertEquals(cursoId, cursoArg.getId());
		assertEquals(TipoNotificacion.MATCH_RECIBIDO, tipo);
	}

}