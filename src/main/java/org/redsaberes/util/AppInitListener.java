package org.redsaberes.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listener que se ejecuta cuando la aplicación inicia
 * Responsable de inicializar Hibernate y la base de datos
 */
@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 Inicializando aplicación RedSaberes...");

        try {
            // Inicializar base de datos con Hibernate
            DBInit.init();
            System.out.println("✅ Aplicación inicializada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error durante la inicialización de la aplicación");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("🛑 Cerrando aplicación RedSaberes...");

        try {
            // Cerrar Hibernate
            HibernateUtil.shutdown();
            System.out.println("✅ Aplicación cerrada correctamente");
        } catch (Exception e) {
            System.err.println("❌ Error al cerrar la aplicación");
            e.printStackTrace();
        }
    }
}
