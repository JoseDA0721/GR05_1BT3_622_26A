package org.redsaberes.service;

import org.redsaberes.service.dto.AcceptMatchOutcome;

public interface AcceptMatchService {

    AcceptMatchOutcome acceptMatch(Integer creadorId, Integer cursoId, Integer usuarioObjetivoId);
}

