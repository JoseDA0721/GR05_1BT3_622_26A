package org.redsaberes.service;

import org.redsaberes.service.dto.DashboardDataDto;

public interface DashboardService {

    DashboardDataDto buildDashboardData(Integer usuarioId, String nombreUsuario);
}

