package org.redsaberes.util;

public class DBInit {
    public static void init() {
        String sql = """
      CREATE TABLE IF NOT EXISTS usuario (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre TEXT NOT NULL,
        correo TEXT UNIQUE NOT NULL,
        contrasena TEXT NOT NULL,
        token_sesion TEXT
      );
      CREATE TABLE IF NOT EXISTS curso (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        titulo TEXT NOT NULL,
        descripcion TEXT,
        categoria TEXT,
        nivel_dificultad TEXT,
        imagen_portada TEXT,
        estado TEXT DEFAULT 'BORRADOR',
        usuario_id INTEGER,
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
      );
      CREATE TABLE IF NOT EXISTS modulo (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        titulo TEXT NOT NULL,
        orden INTEGER,
        curso_id INTEGER,
        FOREIGN KEY (curso_id) REFERENCES curso(id)
      );
      CREATE TABLE IF NOT EXISTS leccion (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        titulo TEXT NOT NULL,
        contenido TEXT,
        modulo_id INTEGER,
        FOREIGN KEY (modulo_id) REFERENCES modulo(id)
      );
      CREATE TABLE IF NOT EXISTS like_curso (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        fecha TEXT,
        usuario_id INTEGER,
        curso_id INTEGER
      );
      CREATE TABLE IF NOT EXISTS inscripcion (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        fecha TEXT,
        usuario_id INTEGER,
        curso_id INTEGER
      );
      CREATE TABLE IF NOT EXISTS match_curso (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        fecha_confirmacion TEXT,
        usuario1_id INTEGER,
        usuario2_id INTEGER,
        curso1_id INTEGER,
        curso2_id INTEGER
      );
    """;
        try (var conn = DBConnection.getConnection();
             var stmt = conn.createStatement()) {
            for (String s : sql.split(";"))
                if (!s.isBlank()) stmt.execute(s);
            System.out.println("BD inicializada correctamente");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
