package org.redsaberes.service.impl;

import org.redsaberes.model.Curso;
import org.redsaberes.model.Leccion;
import org.redsaberes.model.Modulo;
import org.redsaberes.repository.CursoRepository;
import org.redsaberes.repository.LeccionRepository;
import org.redsaberes.repository.ModuloRepository;
import org.redsaberes.repository.impl.CursoRepositoryImpl;
import org.redsaberes.repository.impl.LeccionRepositoryImpl;
import org.redsaberes.repository.impl.ModuloRepositoryImpl;
import org.redsaberes.service.ModuloManagementService;
import org.redsaberes.service.dto.ModuloCommandOutcome;
import org.redsaberes.service.dto.ModuloCommandResultDto;
import org.redsaberes.service.dto.ModuloCreateOutcome;
import org.redsaberes.service.dto.ModuloCreateResultDto;
import org.redsaberes.service.dto.ModuloPageDataDto;
import org.redsaberes.service.dto.ModuloViewOutcome;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ModuloManagementServiceImpl implements ModuloManagementService {

    private final CursoRepository cursoRepository;
    private final ModuloRepository moduloRepository;
    private final LeccionRepository leccionRepository;

    public ModuloManagementServiceImpl() {
        this(new CursoRepositoryImpl(), new ModuloRepositoryImpl(), new LeccionRepositoryImpl());
    }

    ModuloManagementServiceImpl(CursoRepository cursoRepository,
                                ModuloRepository moduloRepository,
                                LeccionRepository leccionRepository) {
        this.cursoRepository = cursoRepository;
        this.moduloRepository = moduloRepository;
        this.leccionRepository = leccionRepository;
    }

    @Override
    public ModuloPageDataDto buildListView(Integer cursoId, Integer usuarioId) {
        if (usuarioId == null) {
            return empty(ModuloViewOutcome.REDIRECT_LOGIN);
        }
        if (cursoId == null) {
            return empty(ModuloViewOutcome.REDIRECT_MY_COURSES);
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || isNotOwner(cursoOpt.get(), usuarioId)) {
            return empty(ModuloViewOutcome.REDIRECT_MY_COURSES);
        }

        List<Modulo> modulos = moduloRepository.findByCursoIdWithLecciones(cursoId);
        return new ModuloPageDataDto(ModuloViewOutcome.OK, cursoOpt.get(), modulos, null, null, false);
    }

    @Override
    public ModuloPageDataDto buildEditView(Integer moduloId, Integer usuarioId) {
        if (usuarioId == null) {
            return empty(ModuloViewOutcome.REDIRECT_LOGIN);
        }
        if (moduloId == null) {
            return empty(ModuloViewOutcome.REDIRECT_MY_COURSES);
        }

        Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
        if (moduloOpt.isEmpty() || moduloOpt.get().getCurso() == null) {
            return empty(ModuloViewOutcome.REDIRECT_MY_COURSES);
        }

        Curso curso = moduloOpt.get().getCurso();
        if (isNotOwner(curso, usuarioId)) {
            return empty(ModuloViewOutcome.REDIRECT_MY_COURSES);
        }

        List<Modulo> modulos = moduloRepository.findByCursoIdWithLecciones(curso.getId());
        return new ModuloPageDataDto(ModuloViewOutcome.OK, curso, modulos, moduloOpt.get(), "edit", false);
    }

    @Override
    public ModuloPageDataDto buildReorderView(Integer cursoId, Integer usuarioId) {
        if (usuarioId == null) {
            return empty(ModuloViewOutcome.REDIRECT_LOGIN);
        }
        if (cursoId == null) {
            return empty(ModuloViewOutcome.REDIRECT_MY_COURSES);
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || isNotOwner(cursoOpt.get(), usuarioId)) {
            return empty(ModuloViewOutcome.REDIRECT_MY_COURSES);
        }

        List<Modulo> modulos = moduloRepository.findByCursoId(cursoId);
        return new ModuloPageDataDto(ModuloViewOutcome.OK, cursoOpt.get(), modulos, null, "reorder", true);
    }

    @Override
    public ModuloCommandResultDto updateModulo(Integer moduloId,
                                               Integer usuarioId,
                                               String tituloModulo,
                                               String ordenStr) {
        if (moduloId == null || usuarioId == null) {
            return myCoursesResult();
        }

        Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
        if (moduloOpt.isEmpty() || moduloOpt.get().getCurso() == null
                || isNotOwner(moduloOpt.get().getCurso(), usuarioId)) {
            return myCoursesResult();
        }

        Modulo modulo = moduloOpt.get();
        if (tituloModulo != null && !tituloModulo.trim().isEmpty()) {
            modulo.setTitulo(tituloModulo.trim());
        }

        if (ordenStr != null && !ordenStr.trim().isEmpty()) {
            int nuevoOrden = Integer.parseInt(ordenStr.trim());
            if (nuevoOrden > 0) {
                modulo.setOrden(nuevoOrden);
            }
        }

        moduloRepository.update(modulo);
        return new ModuloCommandResultDto(ModuloCommandOutcome.UPDATED, modulo.getId(), null);
    }

    @Override
    public ModuloCommandResultDto deleteModulo(Integer moduloId,
                                               Integer usuarioId) {
        if (moduloId == null || usuarioId == null) {
            return myCoursesResult();
        }

        Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
        if (moduloOpt.isEmpty() || moduloOpt.get().getCurso() == null
                || isNotOwner(moduloOpt.get().getCurso(), usuarioId)) {
            return myCoursesResult();
        }

        Integer cursoId = moduloOpt.get().getCurso().getId();
        moduloRepository.delete(moduloId);
        return new ModuloCommandResultDto(ModuloCommandOutcome.DELETED, null, cursoId);
    }

    @Override
    public ModuloCommandResultDto saveModuloOrder(Integer cursoId,
                                                  Integer usuarioId,
                                                  List<Integer> orderedIds) {
        if (cursoId == null || usuarioId == null) {
            return myCoursesResult();
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || isNotOwner(cursoOpt.get(), usuarioId)) {
            return myCoursesResult();
        }

        if (orderedIds == null || orderedIds.isEmpty()) {
            return new ModuloCommandResultDto(ModuloCommandOutcome.ORDER_EMPTY, null, cursoId);
        }

        int orden = 1;
        for (Integer moduloId : orderedIds) {
            Optional<Modulo> moduloOpt = moduloRepository.findById(moduloId);
            if (moduloOpt.isEmpty() || moduloOpt.get().getCurso() == null
                    || !cursoId.equals(moduloOpt.get().getCurso().getId())) {
                continue;
            }
            Modulo modulo = moduloOpt.get();
            modulo.setOrden(orden++);
            moduloRepository.update(modulo);
        }

        return new ModuloCommandResultDto(ModuloCommandOutcome.ORDER_SAVED, null, cursoId);
    }

    @Override
    public ModuloCreateResultDto createModulo(Integer cursoId,
                                              Integer usuarioId,
                                              String tituloModulo,
                                              String tituloLeccion,
                                              String contenidoLeccion) {
        if (cursoId == null || usuarioId == null) {
            return new ModuloCreateResultDto(ModuloCreateOutcome.REDIRECT_MY_COURSES, null, null, null, Collections.emptyList());
        }

        if (isBlank(tituloModulo) || isBlank(tituloLeccion)) {
            return buildForwardError(cursoId, "El titulo del modulo y de la primera leccion son obligatorios.");
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty() || isNotOwner(cursoOpt.get(), usuarioId)) {
            return new ModuloCreateResultDto(ModuloCreateOutcome.REDIRECT_MY_COURSES, null, null, null, Collections.emptyList());
        }

        try {
            Curso curso = cursoOpt.get();
            List<Modulo> modulosExistentes = moduloRepository.findByCursoId(cursoId);
            int nuevoOrden = modulosExistentes.size() + 1;

            Modulo modulo = new Modulo(null, tituloModulo.trim(), nuevoOrden, curso);
            moduloRepository.save(modulo);

            Leccion leccion = new Leccion(null, tituloLeccion.trim(), contenidoLeccion, modulo);
            leccionRepository.save(leccion);

            return new ModuloCreateResultDto(ModuloCreateOutcome.CREATED, cursoId, null, null, Collections.emptyList());
        } catch (Exception e) {
            return buildForwardError(cursoId, "Error al guardar el modulo: " + e.getMessage());
        }
    }

    private ModuloPageDataDto empty(ModuloViewOutcome outcome) {
        return new ModuloPageDataDto(outcome, null, Collections.emptyList(), null, null, false);
    }

    private ModuloCommandResultDto myCoursesResult() {
        return new ModuloCommandResultDto(ModuloCommandOutcome.REDIRECT_MY_COURSES, null, null);
    }

    private ModuloCreateResultDto buildForwardError(Integer cursoId, String error) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        List<Modulo> modulos = moduloRepository.findByCursoIdWithLecciones(cursoId);
        return new ModuloCreateResultDto(
                ModuloCreateOutcome.FORWARD_WITH_ERROR,
                cursoId,
                error,
                cursoOpt.orElse(null),
                modulos
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isNotOwner(Curso curso, Integer usuarioId) {
        return curso == null
                || curso.getUsuario() == null
                || curso.getUsuario().getId() == null
                || !curso.getUsuario().getId().equals(usuarioId);
    }
}

