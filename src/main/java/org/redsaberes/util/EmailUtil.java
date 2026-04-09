package org.redsaberes.util;

public class EmailUtil {
    public static void enviarConfirmacion(String correo, String nombre){
        System.out.println("=== CORREO DE CONFIRMACION ===");
        System.out.println("Para: " + correo);
        System.out.println("Hola " + nombre + ",");
        System.out.println("Tu cuenta en RedSaberes " +
                "fue creada exitosamente.");
        System.out.println("==============================");
    }

    public static void enviarEnlaceRecuperacion(
            String correo, String token,
            String baseUrl) {
        String enlace = baseUrl + "/reset-password?token="
                + token;
        System.out.println("=== ENLACE DE RECUPERACION ===");
        System.out.println("Para: " + correo);
        System.out.println("Enlace (expira en 24h): " + enlace);
        System.out.println("==============================");
    }
}
