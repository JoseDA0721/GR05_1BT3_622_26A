package org.redsaberes.service;

import org.redsaberes.service.dto.ModuloCommandResultDto;
import org.redsaberes.service.dto.ModuloCreateResultDto;
import org.redsaberes.service.dto.ModuloPageDataDto;

import java.util.List;

public interface ModuloManagementService {

    ModuloPageDataDto buildListView(Integer cursoId, Integer usuarioId);

    ModuloPageDataDto buildEditView(Integer moduloId, Integer usuarioId);

    ModuloPageDataDto buildReorderView(Integer cursoId, Integer usuarioId);

    ModuloCommandResultDto updateModulo(Integer moduloId,
                                        Integer usuarioId,
                                        String tituloModulo,
                                        String ordenStr);

    ModuloCommandResultDto deleteModulo(Integer moduloId,
                                        Integer usuarioId);

    ModuloCommandResultDto saveModuloOrder(Integer cursoId,
                                           Integer usuarioId,
                                           List<Integer> orderedIds);

    ModuloCreateResultDto createModulo(Integer cursoId,
                                       Integer usuarioId,
                                       String tituloModulo,
                                       String tituloLeccion,
                                       String contenidoLeccion);
}

