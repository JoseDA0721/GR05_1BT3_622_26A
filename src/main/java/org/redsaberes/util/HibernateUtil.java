package org.redsaberes.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.File;

/**
 * Utility class para inicializar y gestionar la SessionFactory de Hibernate.
 * Esta clase proporciona una instancia única (singleton) de SessionFactory
 * que se utiliza en todo el proyecto.
 */
public class HibernateUtil {
    static {
        // Asegurar que el directorio de la BD existe ANTES de inicializar Hibernat
        ensureDatabaseDirectoryExists();
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Asegura que el directorio para la base de datos SQLite existe
     */
    private static void ensureDatabaseDirectoryExists() {
        try {
            String userHome = System.getProperty("user.home");
            File dbDir = new File(userHome, ".redsaberes");

            if (!dbDir.exists()) {
                if (dbDir.mkdirs()) {
                    System.out.println("✅ Directorio de BD creado: " + dbDir.getAbsolutePath());
                } else {
                    System.err.println("⚠️ No se pudo crear el directorio: " + dbDir.getAbsolutePath());
                }
            } else {
                System.out.println("✅ Directorio de BD existe: " + dbDir.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al verificar/crear directorio de BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Construye la SessionFactory desde el archivo de configuración hibernate.cfg.xml
     *
     * @return SessionFactory configurada
     * @throws ExceptionInInitializerError si hay error al inicializar
     */
    private static SessionFactory buildSessionFactory() {
        try {
            System.out.println("=================================================");
            System.out.println("Inicializando Hibernate SessionFactory...");
            System.out.println("=================================================");

            // Crear SessionFactory desde hibernate.cfg.xml
            SessionFactory factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            System.out.println("✅ Hibernate SessionFactory inicializado correctamente");
            System.out.println("=================================================");

            return factory;
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar SessionFactory");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(
                "Error fatal: No se pudo inicializar Hibernat. " + e.getMessage()
            );
        }
    }

    /**
     * Obtiene la instancia única de SessionFactory
     *
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Cierra la SessionFactory
     * Debe llamarse al finalizar la aplicación
     */
    public static void shutdown() {
        try {
            if (sessionFactory != null) {
                System.out.println("=================================================");
                System.out.println("Cerrando Hibernat SessionFactory...");
                sessionFactory.close();
                System.out.println("✅ Hibernat SessionFactory cerrado correctamente");
                System.out.println("=================================================");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cerrar SessionFactory");
            e.printStackTrace();
        }
    }
}

