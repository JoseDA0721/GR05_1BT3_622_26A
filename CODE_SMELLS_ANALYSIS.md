# Análisis de Code Smells - RedSaberes

## Introducción

Este documento identifica **5 tipos de Code Smells** encontrados en el proyecto RedSaberes tras la refactorización a la capa de servicios. El análisis se enfoca en patrones de código que, aunque funcionales, podrían mejorarse para aumentar la mantenibilidad, reutilización y claridad.

---

## 1. COMPOSING METHODS

### 1.1 Extract Method

**Definición:** Métodos demasiado largos que realizan múltiples responsabilidades y deberían dividirse en métodos más pequeños y enfocados.

#### 1.1.1 `ModuloServlet.java` - Métodos `doGet()` y `doPost()`

**Ubicación:** Líneas 34-62 y 66-92

**Problema:**
```java
protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String action = req.getParameter("action");
    if (action == null || action.trim().isEmpty()) {
        action = "list";
    }
    
    try {
        switch (action) {
            case "edit":
                handleEditGet(req, res);
                break;
            case "reorder":
                handleReorderGet(req, res);
                break;
            case "list":
            default:
                handleListGet(req, res);
                break;
        }
    } catch (NumberFormatException e) {
        res.sendRedirect(req.getContextPath() + "/my-courses");
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error al cargar gestión de módulos", e);
        res.sendRedirect(req.getContextPath() + "/my-courses");
    }
}
```

**Por qué es un smell:**
- El método contiene lógica de normalización de parámetros (`action == null || action.trim().isEmpty()`)
- Manejo de excepciones genérico que podría ser extraído
- La validación de acción se repite en `doPost()`

**Métodos privados que podrían crearse:**
- `String normalizeAction(String action)` - Extrae la lógica de normalización
- `void handleError(HttpServletResponse res, HttpServletRequest req, Exception e)` - Manejo centralizado de errores

#### 1.1.2 `ModuloServlet.java` - Métodos `parseOrderedIds()`

**Ubicación:** Líneas 334-358

**Problema:**
```java
private List<Integer> parseOrderedIds(HttpServletRequest req) {
    List<Integer> orderedIds = new java.util.ArrayList<>();
    
    String[] multiple = req.getParameterValues("moduloIds");
    if (multiple != null) {
        for (String idStr : multiple) {
            if (idStr != null && !idStr.trim().isEmpty()) {
                orderedIds.add(Integer.parseInt(idStr.trim()));
            }
        }
        return orderedIds;
    }
    
    String orderedIdsCsv = req.getParameter("orderedIds");
    if (orderedIdsCsv != null && !orderedIdsCsv.trim().isEmpty()) {
        String[] parts = orderedIdsCsv.split(",");
        for (String part : parts) {
            if (part != null && !part.trim().isEmpty()) {
                orderedIds.add(Integer.parseInt(part.trim()));
            }
        }
    }
    
    return orderedIds;
}
```

**Por qué es un smell:**
- El método maneja dos formatos de entrada diferentes (array y CSV)
- La lógica de parsing se repite dos veces
- Duplicación de validaciones

**Métodos privados que podrían crearse:**
- `List<Integer> parseIdArray(String[] values)` - Procesa array de strings
- `List<Integer> parseIdsCsv(String csv)` - Procesa string CSV

#### 1.1.3 `ExploreServiceImpl.java` - Método `buildExploreCourses()`

**Ubicación:** Líneas 37-68

**Problema:**
```java
public List<Map<String, Object>> buildExploreCourses(Integer usuarioId,
                                                     String search,
                                                     String category) {
    String searchNorm = normalize(search);
    String categoryNorm = normalize(category);

    List<Curso> publicCourses = cursoRepository.findByEstado("PUBLICO");
    List<Map<String, Object>> courses = new ArrayList<>();

    for (Curso c : publicCourses) {
        if (isOwnCourse(c, usuarioId) || !matchesFilters(c, searchNorm, categoryNorm)) {
            continue;
        }

        boolean liked = likeCursoRepository.existsByUsuarioAndCurso(usuarioId, c.getId());
        boolean hasMatch = matchCursoRepository.existsByCursoAndUsuario(c.getId(), usuarioId);

        Map<String, Object> item = new HashMap<>();
        item.put("id", c.getId());
        // ... más de 5 put() repetitivos
        courses.add(item);
    }

    return courses;
}
```

**Por qué es un smell:**
- La creación del `Map` con múltiples `put()` podría extraerse
- La validación y transformación de cada curso está dispersa

**Métodos privados que podrían crearse:**
- `Map<String, Object> buildCourseMap(Curso c, boolean liked, boolean hasMatch)` - Crea la representación del curso

---

### 1.2 Replace Temp with Query

**Definición:** Variables temporales que almacenan resultados de consultas deberían ser reemplazadas por llamadas directo al método que genera esa consulta.

#### 1.2.1 `DashboardServiceImpl.java` - Método `buildDashboardData()`

**Ubicación:** Líneas 38-60

**Problema:**
```java
@Override
public DashboardDataDto buildDashboardData(Integer usuarioId, String nombreUsuario) {
    List<Curso> cursos = cursoRepository.findByUsuarioId(usuarioId);  // ← Variable temporal
    
    Map<String, Object> dashboardStats = new HashMap<>();
    dashboardStats.put("coursesCreated", cursos.size());
    dashboardStats.put("likesReceived", safeLong(likeCursoRepository.countByCursoPropietarioId(usuarioId)));
    dashboardStats.put("activeMatches", safeLong(matchCursoRepository.countByCreadorId(usuarioId)));
    // ...
    
    List<Map<String, Object>> userCourses = new ArrayList<>();
    for (Curso c : cursos) {  // ← Se itera sobre la variable temporal
        // ...
    }
    
    return new DashboardDataDto(dashboardStats, userCourses, Collections.emptyList());
}
```

**Por qué es un smell:**
- La variable `cursos` se usa solo una vez para `cursos.size()` y luego para iteración
- Podría reemplazarse con:
  - `cursoRepository.countByUsuarioId(usuarioId)` para el count
  - Una consulta optimizada que devuelva ambos datos

#### 1.2.2 `ExploreServiceImpl.java` - Método `buildExploreCourses()`

**Ubicación:** Líneas 40-41

**Problema:**
```java
String searchNorm = normalize(search);
String categoryNorm = normalize(category);

List<Curso> publicCourses = cursoRepository.findByEstado("PUBLICO");  // ← Variable temporal

for (Curso c : publicCourses) {
    if (!matchesFilters(c, searchNorm, categoryNorm)) {
        continue;
    }
    // ...
}
```

**Por qué es un smell:**
- `searchNorm` y `categoryNorm` se asignan pero podrían pasar directamente a `matchesFilters()`
- La consulta `publicCourses` se carga completamente en memoria

#### 1.2.3 `CourseCreationServiceImpl.java` - Método `createDraftCourse()`

**Ubicación:** Líneas 26-74

**Problema:**
```java
String fileName = ImagenUtil.guardarImagen(filePart, uploadDir);  // ← Temp variable

Curso curso = new Curso(
    null,
    titulo.trim(),
    descripcion,
    categoria,
    nivel,
    fileName,  // ← Se usa una sola vez
    EstadoCurso.BORRADOR,
    usuario
);
```

**Por qué es un smell:**
- `fileName` se asigna pero se podría usar directamente en el constructor
- No es reutilizada

---

### 1.3 Substitute Algorithm

**Definición:** Algoritmos que podrían reemplazarse por algoritmos más simples, claros o eficientes.

#### 1.3.1 `ModuloServlet.java` - Método `applyFlashMessage()`

**Ubicación:** Líneas 360-392

**Problema:**
```java
private void applyFlashMessage(HttpServletRequest req, String msg) {
    if (msg == null) {
        return;
    }
    switch (msg) {
        case "modulo-agregado":
            req.setAttribute("exito", "Modulo y leccion guardados correctamente.");
            break;
        case "modulo-actualizado":
            req.setAttribute("exito", "Modulo actualizado correctamente.");
            break;
        case "modulo-eliminado":
            req.setAttribute("exito", "Modulo eliminado correctamente.");
            break;
        case "orden-guardado":
            req.setAttribute("exito", "Orden de modulos guardado correctamente.");
            break;
        case "orden-vacio":
            req.setAttribute("error", "No se recibio ningun modulo para reordenar.");
            break;
        // ... más casos
        default:
            break;
    }
}
```

**Por qué es un smell:**
- Switch statement muy largo con lógica repetitiva
- Cada mensaje está hardcodeado
- Difícil de mantener y extender

**Algoritmo alternativo (usando Map):**
```java
private static final Map<String, FlashMessage> MESSAGES = Map.ofEntries(
    Map.entry("modulo-agregado", new FlashMessage("exito", "Modulo y leccion guardados correctamente.")),
    // ...
);
```

#### 1.3.2 `ExploreServiceImpl.java` - Método `buildExploreCourses()`

**Ubicación:** Líneas 88-103

**Problema:**
```java
private boolean matchesFilters(Curso curso, String search, String category) {
    if (!search.isEmpty()) {
        String title = safe(curso.getTitulo()).toLowerCase(Locale.ROOT);
        String description = safe(curso.getDescripcion()).toLowerCase(Locale.ROOT);
        String term = search.toLowerCase(Locale.ROOT);
        if (!title.contains(term) && !description.contains(term)) {
            return false;
        }
    }

    if (!category.isEmpty() && !"Todas".equalsIgnoreCase(category)) {
        return safe(curso.getCategoria()).equalsIgnoreCase(category);
    }

    return true;
}
```

**Por qué es un smell:**
- Lógica condicional anidada innecesaria
- Múltiples conversiones a lowercase
- Podría simplificarse usando Stream API o alguna otra estructura

#### 1.3.3 `CourseLifecycleServiceImpl.java` - Validación de propietario

**Ubicación:** Líneas 100-105

**Problema:**
```java
private boolean isNotOwner(Curso curso, Integer usuarioId) {
    return curso == null
            || curso.getUsuario() == null
            || curso.getUsuario().getId() == null
            || !curso.getUsuario().getId().equals(usuarioId);
}
```

**Por qué es un smell:**
- Validaciones nulas anidadas que se repiten en múltiples métodos
- Debería extraerse a una clase de validación general
- El algoritmo es defensivo pero redundante

---

## 2. MOVING FEATURES BETWEEN OBJECTS

### 2.1 Move Method

**Definición:** Métodos que están en la clase equivocada y deberían moverse a otra clase para mejor cohesión.

#### 2.1.1 `ModuloServlet.java` - Métodos de validación y parseamiento

**Ubicación:** Líneas 394-408

**Problema:**
```java
private Usuario getUsuarioSesion(HttpServletRequest req) {
    HttpSession session = req.getSession(false);
    if (session == null) {
        return null;
    }
    return (Usuario) session.getAttribute("usuario");
}

private Integer parseInteger(String value) {
    try {
        return value == null ? null : Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
        return null;
    }
}
```

**Por qué es un smell:**
- Estos métodos son **utilitarios generales** que se repiten en otros servlets
- No son específicos de la gestión de módulos
- Deberían estar en una clase `ServletUtil` o `RequestUtil` compartida

**Propuesta:** Crear clase `org.redsaberes.util.ServletUtil`

#### 2.1.2 `ExploreServiceImpl.java` - Métodos de normalización

**Ubicación:** Líneas 84-107

**Problema:**
```java
private String normalize(String value) {
    return value == null ? "" : value.trim();
}

private String safe(String value) {
    return value == null ? "" : value;
}
```

**Por qué es un smell:**
- Estos métodos de utilidad de strings se repiten en **múltiples servicios**
- No son específicos de ExploreService
- Deberían estar en `org.redsaberes.util.StringUtil`

#### 2.1.3 `DashboardServiceImpl.java` - Método `safeLong()`

**Ubicación:** Líneas 63-65

**Problema:**
```java
private long safeLong(Long value) {
    return value == null ? 0L : value;
}
```

**Por qué es un smell:**
- Método genérico de conversión segura
- Se podría reutilizar en otros servicios
- Debería estar en `org.redsaberes.util.ConversionUtil`

#### 2.1.4 `ModuloServlet.java` - Método `applyFlashMessage()`

**Ubicación:** Líneas 360-392

**Por qué es un smell:**
- Manejo de mensajes flash debería estar en un servicio `FlashMessageService`
- El servlet solo debería llamar al servicio para obtener el mensaje
- La lógica de mapeo de mensajes no pertenece al servlet

**Propuesta:** Crear servicio `org.redsaberes.service.FlashMessageService`

---

### 2.2 Extract Class

**Definición:** Clases que tienen demasiadas responsabilidades y deberían dividirse en varias clases más enfocadas.

#### 2.2.1 `ModuloServlet.java` - Responsabilidades múltiples

**Ubicación:** Todo el archivo (410 líneas)

**Problema:**
```
ModuloServlet (410 líneas)
├── Responsabilidad 1: Enrutar acciones HTTP (GET/POST)
├── Responsabilidad 2: Parsear parámetros de request
├── Responsabilidad 3: Validar sesión de usuario
├── Responsabilidad 4: Mapear mensajes flash
├── Responsabilidad 5: Manejar redirecciones
└── Responsabilidad 6: Procesar órdenes de módulos
```

**Por qué es un smell:**
- **Violación del Single Responsibility Principle (SRP)**
- Demasiadas líneas de código
- Difícil de testear (muchas responsabilidades)
- Cambios en una responsabilidad afectan a las otras

**Métodos que podrían extraerse a clases:**
1. **`ServletParameterExtractor`** - Métodos de parseamiento:
   - `parseInteger()`
   - `parseOrderedIds()`
   - `normalizeAction()`

2. **`SessionManager`** - Gestión de sesión:
   - `getUsuarioSesion()`
   - Validación de sesión

3. **`ModuloRequestProcessor`** - Procesamiento de requests:
   - Lógica de cada `handle*()` podría estar aquí

#### 2.2.2 `DashboardServiceImpl.java` - Construcción de datos complejos

**Ubicación:** Líneas 37-60

**Problema:**
```java
// En buildDashboardData() hay dos responsabilidades:
// 1. Recolectar datos de múltiples repositorios
// 2. Construir mapas de estadísticas y cursos
```

**Por qué es un smell:**
- El método mezcla lógica de consulta con lógica de presentación
- Los mapas se construyen manualmente con múltiples `put()`

**Clases que podrían crearse:**
1. **`DashboardStatsBuilder`** - Construcción de estadísticas
2. **`UserCourseMapper`** - Transformación de Curso a Map

#### 2.2.3 `ExploreServiceImpl.java` - Lógica de filtrado y transformación

**Ubicación:** Líneas 37-107

**Problema:**
```java
// Responsabilidades mezcladas:
// 1. Consultar cursos públicos
// 2. Filtrar por propietario
// 3. Filtrar por búsqueda/categoría
// 4. Construir representación
// 5. Consultar likes y matches
```

**Por qué es un smell:**
- Múltiples niveles de filtrado en un único método
- Lógica de transformación Curso → Map

**Clases que podrían crearse:**
1. **`CourseFilterChain`** - Aplicar filtros secuencialmente
2. **`CourseTransformer`** - Transformar Curso a representación

---

## 3. RESUMEN TABULAR

| Code Smell | Archivo | Líneas | Severidad | Tipo |
|-----------|---------|--------|-----------|------|
| **Extract Method** | ModuloServlet.java | 34-62 | Alto | doGet() muy largo |
| **Extract Method** | ModuloServlet.java | 66-92 | Alto | doPost() muy largo |
| **Extract Method** | ModuloServlet.java | 334-358 | Medio | parseOrderedIds() duplica lógica |
| **Extract Method** | ExploreServiceImpl.java | 37-68 | Medio | buildExploreCourses() construye mapas manualmente |
| **Replace Temp** | DashboardServiceImpl.java | 39 | Bajo | Variable cursos temporal |
| **Replace Temp** | ExploreServiceImpl.java | 40-41 | Bajo | searchNorm, categoryNorm innecesarias |
| **Replace Temp** | CourseCreationServiceImpl.java | 53 | Bajo | fileName variable temporal |
| **Substitute Algorithm** | ModuloServlet.java | 360-392 | Alto | applyFlashMessage() switch gigante |
| **Substitute Algorithm** | ExploreServiceImpl.java | 88-103 | Medio | matchesFilters() lógica compleja |
| **Substitute Algorithm** | CourseLifecycleServiceImpl.java | 100-105 | Bajo | isNotOwner() validaciones anidadas |
| **Move Method** | ModuloServlet.java | 394-408 | Alto | getUsuarioSesion(), parseInteger() generales |
| **Move Method** | ExploreServiceImpl.java | 84-107 | Medio | normalize(), safe() generales |
| **Move Method** | DashboardServiceImpl.java | 63-65 | Bajo | safeLong() general |
| **Move Method** | ModuloServlet.java | 360-392 | Alto | applyFlashMessage() lógica de negocio |
| **Extract Class** | ModuloServlet.java | 1-410 | Alto | 410 líneas, 6 responsabilidades |
| **Extract Class** | DashboardServiceImpl.java | 37-60 | Medio | Construcción de datos complejos |
| **Extract Class** | ExploreServiceImpl.java | 37-107 | Medio | Filtrado + Transformación mixto |

---

## 4. IMPACTO Y PRIORIDADES

### Prioritarios (Impacto Alto)

1. **ModuloServlet - Reducir tamaño**
   - Afecta: Testabilidad, mantenibilidad
   - Esfuerzo: Alto
   - Beneficio: Código más limpio y reutilizable

2. **Centralizar utilidades generales**
   - Crear `ServletUtil`, `StringUtil`, etc.
   - Afecta: Duplicación de código
   - Esfuerzo: Medio
   - Beneficio: DRY principle

3. **applyFlashMessage() - Usar Map**
   - Afecta: Mantenibilidad
   - Esfuerzo: Bajo
   - Beneficio: Fácil de extender

### Medianos (Impacto Medio)

4. **Extractores de mapas**
   - `DashboardStatsBuilder`, `UserCourseMapper`
   - Afecta: Testabilidad
   - Esfuerzo: Medio

5. **Simplificar algoritmos de filtrado**
   - Afecta: Performance, claridad
   - Esfuerzo: Medio

### Bajos (Impacto Bajo)

6. **Replace temp variables**
   - Afecta: Readabilidad
   - Esfuerzo: Bajo

---

## 5. CONCLUSIÓN

El proyecto RedSaberes **está en buen camino** con la separación a capa de servicios. Sin embargo, existen **oportunidades claras de mejora**:

- ✅ La estructura de servicios es correcta
- ❌ Algunos servicios aún mezclan responsabilidades
- ❌ Hay código genérico disperso que debería centralizarse
- ❌ Algunos métodos son demasiado largos

**Recomendación:** Aplicar estas mejoras **antes de escribir tests unitarios** con JUnit y Mocks, ya que reducirán la complejidad ciclomática y harán el código más testeable.

---

**Documento generado:** 2026-04-15
**Versión de análisis:** 1.0
**Estado del proyecto:** Post-refactorización a capa SERVICE

