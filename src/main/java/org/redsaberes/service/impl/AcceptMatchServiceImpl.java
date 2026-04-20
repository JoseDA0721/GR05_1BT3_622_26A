package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.MatchCurso;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LikeCursoRepository;
import org.redsaberes.repository.MatchCursoRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LikeCursoRepositoryImpl;
import org.redsaberes.repository.impl.MatchCursoRepositoryImpl;
import org.redsaberes.service.AcceptMatchService;
import org.redsaberes.service.dto.AcceptMatchOutcome;

import java.time.LocalDateTime;
import java.util.Optional;

public class AcceptMatchServiceImpl implements AcceptMatchService {

    private final CursoRepository cursoRepository;
    private final LikeCursoRepository likeCursoRepository;
    private final MatchCursoRepository matchCursoRepository;

    public AcceptMatchServiceImpl() {
        this(new CursoRepositoryImpl(), new LikeCursoRepositoryImpl(), new MatchCursoRepositoryImpl());
    }

    AcceptMatchServiceImpl(CursoRepository cursoRepository,
                           LikeCursoRepository likeCursoRepository,
                           MatchCursoRepository matchCursoRepository) {
        this.cursoRepository = cursoRepository;
        this.likeCursoRepository = likeCursoRepository;
        this.matchCursoRepository = matchCursoRepository;
    }

    @Override
    public AcceptMatchOutcome acceptMatch(Integer creadorId, Integer cursoId, Integer usuarioObjetivoId) {
        if (creadorId == null || cursoId == null || usuarioObjetivoId == null) {
            return AcceptMatchOutcome.INVALID_DATA;
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || cursoOpt.get().getUsuario() == null
                || cursoOpt.get().getUsuario().getId() == null
                || !creadorId.equals(cursoOpt.get().getUsuario().getId())) {
            return AcceptMatchOutcome.FORBIDDEN;
        }

        if (!likeCursoRepository.existsByUsuarioAndCurso(usuarioObjetivoId, cursoId)) {
            return AcceptMatchOutcome.NO_LIKE;
        }

        if (!matchCursoRepository.existsByCursoAndUsuario(cursoId, usuarioObjetivoId)) {
            Usuario creador = new Usuario();
            creador.setId(creadorId);

            Usuario estudiante = new Usuario();
            estudiante.setId(usuarioObjetivoId);

            MatchCurso match = new MatchCurso();
            match.setCurso(cursoOpt.get());
            match.setCreador(creador);
            match.setEstudiante(estudiante);
            match.setFechaConfirmacion(LocalDateTime.now().toString());
            matchCursoRepository.save(match);
        }

        return AcceptMatchOutcome.MATCHED;
    }
}

