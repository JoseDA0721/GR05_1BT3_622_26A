package org.redsaberes.util;

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
}
