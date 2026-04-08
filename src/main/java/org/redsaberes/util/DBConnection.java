package org.redsaberes.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String URL;

    static {
        Properties props = new Properties();
        // Cargamos el archivo desde el classpath
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("Error: No se encontró el archivo 'config.properties' en src/main/resources");
            }

            // Cargar las propiedades
            props.load(input);
            String dbPath = props.getProperty("db.path");

            if (dbPath == null || dbPath.isEmpty()) {
                throw new RuntimeException("Error: La propiedad 'db.path' no está definida en config.properties");
            }

            // Registrar el driver de SQLite
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("No se encontró el driver de SQLite en el proyecto", e);
            }

            // Resolver la ruta (relativa, absoluta o con variables de entorno)
            String expandedPath = expandPath(dbPath);
            File dbFile = new File(expandedPath);
            String resolvedPath = dbFile.getAbsolutePath();

            // Crear el directorio si no existe
            File dbDir = dbFile.getParentFile();
            if (dbDir != null && !dbDir.exists()) {
                dbDir.mkdirs();
            }

            // Construir la URL de conexión
            URL = "jdbc:sqlite:" + resolvedPath;
            System.out.println("=================================================");
            System.out.println("Base de datos SQLite configurada");
            System.out.println("Ubicación: " + resolvedPath);
            System.out.println("Carpeta: " + dbDir.getAbsolutePath());
            System.out.println("=================================================");

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de configuración", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Expande variables como ${user.home} en la ruta
     */
    private static String expandPath(String path) {
        // Reemplazar ${user.home}
        path = path.replace("${user.home}", System.getProperty("user.home"));

        // Reemplazar ${catalina.base} si está disponible (en Tomcat)
        String catalinaBase = System.getProperty("catalina.base");
        if (catalinaBase != null) {
            path = path.replace("${catalina.base}", catalinaBase);
        }

        // Reemplazar otras variables de entorno
        path = path.replace("${java.io.tmpdir}", System.getProperty("java.io.tmpdir"));

        return path;
    }
}
