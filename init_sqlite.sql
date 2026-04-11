-- ============================================
-- Script de inicialización para SQLite
-- WebApp - Estudiantes, Cursos, Likes y Match
-- ============================================

-- Limpiar tablas existentes (opcional)
-- DROP TABLE IF EXISTS MATCH_CURSO;
-- DROP TABLE IF EXISTS LIKE_CURSO;
-- DROP TABLE IF EXISTS Curso;
-- DROP TABLE IF EXISTS DatosEstudiante;

-- ============================================
-- Crear tabla DatosEstudiante
-- ============================================
CREATE TABLE IF NOT EXISTS DatosEstudiante (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cedula TEXT,
    nombre TEXT,
    apellido TEXT,
    edad TEXT,
    carrera TEXT,
    usuario TEXT NOT NULL UNIQUE,
    contrasena TEXT
);


-- ============================================
-- Crear tabla Curso
-- ============================================
CREATE TABLE IF NOT EXISTS Curso (
    Id_curso INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_curso TEXT NOT NULL,
    id_creador INTEGER NOT NULL,
    FOREIGN KEY (id_creador) REFERENCES DatosEstudiante(id) ON DELETE CASCADE
);

-- ============================================
-- Crear tabla LIKE_CURSO
-- ============================================
CREATE TABLE IF NOT EXISTS LIKE_CURSO (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_estudiante INTEGER NOT NULL,
    id_curso INTEGER NOT NULL,
    FOREIGN KEY (id_estudiante) REFERENCES DatosEstudiante(id) ON DELETE CASCADE,
    FOREIGN KEY (id_curso) REFERENCES Curso(Id_curso) ON DELETE CASCADE,
    UNIQUE(id_estudiante, id_curso)
);

-- ============================================
-- Crear tabla MATCH_CURSO
-- ============================================
CREATE TABLE IF NOT EXISTS MATCH_CURSO (
    id_match INTEGER PRIMARY KEY AUTOINCREMENT,
    id_curso INTEGER NOT NULL,
    id_estudiante INTEGER NOT NULL,
    id_creador INTEGER NOT NULL,
    FOREIGN KEY (id_curso) REFERENCES Curso(Id_curso) ON DELETE CASCADE,
    FOREIGN KEY (id_estudiante) REFERENCES DatosEstudiante(id) ON DELETE CASCADE,
    FOREIGN KEY (id_creador) REFERENCES DatosEstudiante(id) ON DELETE CASCADE,
    UNIQUE(id_curso, id_estudiante)
);

-- ============================================
-- Crear índices para optimizar queries
-- ============================================
CREATE INDEX IF NOT EXISTS idx_like_estudiante
    ON LIKE_CURSO(id_estudiante);

CREATE INDEX IF NOT EXISTS idx_like_curso
    ON LIKE_CURSO(id_curso);

CREATE INDEX IF NOT EXISTS idx_match_curso
    ON MATCH_CURSO(id_curso);

CREATE INDEX IF NOT EXISTS idx_match_estudiante
    ON MATCH_CURSO(id_estudiante);

CREATE INDEX IF NOT EXISTS idx_curso_creador
    ON Curso(id_creador);

CREATE INDEX IF NOT EXISTS idx_estudiante_usuario
    ON DatosEstudiante(usuario);

-- ============================================
-- Datos de prueba
-- ============================================

-- Insertar estudiantes
INSERT OR IGNORE INTO DatosEstudiante
    (id, cedula, nombre, apellido, edad, carrera, usuario, contrasena)
VALUES
    (1, '12345678', 'Juan', 'Perez', '20', 'Ingenieria Informatica', 'juan123', 'pass123'),
    (2, '87654321', 'Maria', 'Garcia', '21', 'Administracion', 'maria456', 'pass456'),
    (3, '11223344', 'Carlos', 'Lopez', '22', 'Ingenieria Informatica', 'carlos789', 'pass789'),
    (4, '55667788', 'Ana', 'Martinez', '19', 'Contabilidad', 'ana999', 'pass999');

-- Insertar cursos
INSERT OR IGNORE INTO Curso
    (Id_curso, nombre_curso, id_creador)
VALUES
    (1, 'Python Avanzado', 1),
    (2, 'Gestion de Empresas', 2),
    (3, 'Base de Datos SQL', 1),
    (4, 'Finanzas Corporativas', 3);

-- Insertar algunos likes de prueba
INSERT OR IGNORE INTO LIKE_CURSO
    (id_estudiante, id_curso)
VALUES
    (2, 1),
    (3, 2),
    (4, 1),
    (1, 2);

-- Insertar un match de prueba (creador del curso 1 autoriza al usuario 2)
INSERT OR IGNORE INTO MATCH_CURSO
    (id_curso, id_estudiante, id_creador)
VALUES
    (1, 2, 1);

-- ============================================
-- Verificación: mostrar todas las tablas
-- ============================================
SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;
