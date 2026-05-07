package org.redsaberes.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.File;

/**
 * Listener que se ejecuta cuando la aplicación inicia
 * Responsable de inicializar Hibernate y la base de datos
 */
@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Crear directorio de BD si no existe
            String dbPath = resolveDbPath();
            System.setProperty("redsaberes.db.path", dbPath);

            // Inicializar Hibernate (crea/actualiza tablas automáticamente)
            HibernateUtil.getSessionFactory();

            DataInitializer.init();
            System.out.println("✅ Aplicación inicializada. BD: " + dbPath);
            String publicUrl = System.getenv().getOrDefault("RED_SABERES_PUBLIC_URL", "http://localhost:8080/");
            System.out.println("✅ Aplicación inicializada. Accesible en " + publicUrl);
        } catch (Exception e) {
            throw new RuntimeException("Error crítico al inicializar", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
    }

    private String resolveDbPath() {
        String base = System.getenv("LOCALAPPDATA");
        if (base == null) base = System.getProperty("user.home");
        File folder = new File(base, "RedSaberes");
        if (!folder.exists() && !folder.mkdirs())
            throw new IllegalStateException("No se pudo crear: " + folder);
        return new File(folder, "redsaberes.db").getAbsolutePath();
    }
}