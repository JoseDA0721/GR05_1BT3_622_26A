package org.redsaberes.util;

import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Paths;

public class ImagenUtil {
    public static String guardarImagen(Part filePart,
                                       String uploadDir
                                       ) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null; // No se subió ningún archivo
        }

        //Validar imagen
        String contentType = filePart.getContentType();
        if(contentType == null
                || !contentType.startsWith("image/")) {
            throw new IOException("" +
                    "El archivo debe ser una imagen");
        }

        //Generar nombre unico
        String fileName = Paths.get(
                filePart.getSubmittedFileName())
                .getFileName().toString();
        String extension = fileName.contains(".")
                ? fileName.substring(
                        fileName.lastIndexOf("."))
                : ".jpg";
        String uniqueFileName = System.currentTimeMillis()
                + "." + extension;

        //Crear directorio si no existe

        File dir  = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        //Guardar archivo
        String rutaCompleta = uploadDir
                + File.separator + uniqueFileName;
        try (InputStream input = filePart.getInputStream();
            OutputStream output = new FileOutputStream(
                    rutaCompleta))
        {
            input.transferTo(output);
        }

        System.out.println(
                "=== IMAGEN GUARDADA ===");
        System.out.println("Ruta: " + rutaCompleta);
        System.out.println(
                "======================");

        return uniqueFileName;

    }

}
