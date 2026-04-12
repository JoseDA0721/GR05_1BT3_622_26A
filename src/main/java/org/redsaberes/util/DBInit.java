package org.redsaberes.util;

import java.io.File;

/**
 * Inicialización de la base de datos
 * Con Hibernate, las tablas se crean automáticamente basadas en las anotaciones @Entity
 * Esta clase asegura que Hibernate esté correctamente inicializado
 */
public class DBInit {
    public static void init() {
        try {
            System.out.println("=================================================");
            System.out.println("Inicializando base de datos con Hibernat...");
            System.out.println("=================================================");

            String dbPath = resolveDbPath();
            System.setProperty("redsaberes.db.path", dbPath);
            System.out.println("Ruta SQLite: " + dbPath);

            // Inicializar SessionFactory de Hibernate
            // Esto cargará hibernat.cfg.xml y creará/actualizará las tablas automáticamente
            org.hibernate.SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            // Verificar que la conexión está activa
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                System.out.println("✅ Base de datos inicializada correctamente");
                System.out.println("✅ SessionFactory de Hibernat activo");
                System.out.println("✅ Esquema de BD creado/actualizado automáticamente");
                System.out.println("=================================================");
            } else {
                System.err.println("❌ Error: SessionFactory no está activo");
            }

        } catch (Exception e) {
            System.err.println("❌ Error al inicializar la base de datos");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String resolveDbPath() {
        String baseDir = System.getenv("LOCALAPPDATA");
        if (baseDir == null || baseDir.trim().isEmpty()) {
            String catalinaBase = System.getProperty("catalina.base");
            if (catalinaBase != null && !catalinaBase.trim().isEmpty()) {
                baseDir = catalinaBase;
            } else {
                baseDir = System.getProperty("user.home");
            }
        }

        File folder = new File(baseDir, "RedSaberes");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IllegalStateException("No se pudo crear directorio de BD: " + folder.getAbsolutePath());
        }

        return new File(folder, "redsaberes.db").getAbsolutePath();
    }
}
