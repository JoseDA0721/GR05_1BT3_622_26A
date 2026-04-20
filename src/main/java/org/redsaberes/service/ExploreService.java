package org.redsaberes.service;

import java.util.List;
import java.util.Map;

public interface ExploreService {

    List<Map<String, Object>> buildExploreCourses(Integer usuarioId,
                                                  String search,
                                                  String category);
}

