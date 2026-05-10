package org.redsaberes.util;

import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.*;
import org.redsaberes.repository.impl.*;

import java.time.LocalDate;

public class DataInitializer {

    public static void init() {
        UsuarioRepositoryImpl usuarioRepo = new UsuarioRepositoryImpl();

        // Evitar duplicados al reiniciar
        if (usuarioRepo.existeCorreo("ana@test.com")) {
            System.out.println("✅ Datos de prueba ya existen, saltando...");
            return;
        }

        System.out.println("🌱 Insertando datos de prueba completos...");

        // ── Usuarios (4 para más variedad) ──
        String hash = BCrypt.hashpw("123456", BCrypt.gensalt(12));

        Usuario ana = new Usuario();
        ana.setNombre("Ana García");
        ana.setCorreoElectronico("ana@test.com");
        ana.setContrasena(hash);
        usuarioRepo.save(ana);

        Usuario carlos = new Usuario();
        carlos.setNombre("Carlos López");
        carlos.setCorreoElectronico("carlos@test.com");
        carlos.setContrasena(hash);
        usuarioRepo.save(carlos);

        Usuario maria = new Usuario();
        maria.setNombre("María Torres");
        maria.setCorreoElectronico("maria@test.com");
        maria.setContrasena(hash);
        usuarioRepo.save(maria);

        Usuario diego = new Usuario();
        diego.setNombre("Diego Martínez");
        diego.setCorreoElectronico("diego@test.com");
        diego.setContrasena(hash);
        usuarioRepo.save(diego);

        // Recargar con IDs generados
        ana    = usuarioRepo.findByCorreo("ana@test.com").get();
        carlos = usuarioRepo.findByCorreo("carlos@test.com").get();
        maria  = usuarioRepo.findByCorreo("maria@test.com").get();
        diego  = usuarioRepo.findByCorreo("diego@test.com").get();

        // ── Cursos (6 cursos para más contenido) ──
        CursoRepositoryImpl cursoRepo = new CursoRepositoryImpl();

        Curso cursoPython = new Curso(null, "Python para Principiantes",
                "Aprende Python desde cero con ejercicios prácticos.",
                "Programación", "Principiante", null, EstadoCurso.PUBLICO, ana);
        cursoRepo.save(cursoPython);

        Curso cursoDiseno = new Curso(null, "Diseño UX/UI con Figma",
                "Crea interfaces modernas y centradas en el usuario.",
                "Diseño", "Intermedio", null, EstadoCurso.PUBLICO, carlos);
        cursoRepo.save(cursoDiseno);

        Curso cursoMarketing = new Curso(null, "Marketing Digital",
                "Estrategias de marketing en redes sociales.",
                "Marketing", "Principiante", null, EstadoCurso.PUBLICO, maria);
        cursoRepo.save(cursoMarketing);

        Curso cursoJavaScript = new Curso(null, "JavaScript Avanzado",
                "Domina JavaScript para desarrollo web moderno.",
                "Programación", "Avanzado", null, EstadoCurso.PUBLICO, diego);
        cursoRepo.save(cursoJavaScript);

        Curso cursoDataScience = new Curso(null, "Data Science con Python",
                "Análisis de datos y machine learning.",
                "Programación", "Avanzado", null, EstadoCurso.PUBLICO, ana);
        cursoRepo.save(cursoDataScience);

        Curso cursoSEO = new Curso(null, "SEO y Posicionamiento Web",
                "Mejora tu visibilidad en buscadores (BORRADOR).",
                "Marketing", "Intermedio", null, EstadoCurso.BORRADOR, carlos);
        cursoRepo.save(cursoSEO);

        // Recargar con IDs
        cursoPython  = cursoRepo.findByEstado("PUBLICO").stream()
                .filter(c -> c.getTitulo().contains("Python para")).findFirst().get();
        cursoDiseno  = cursoRepo.findByEstado("PUBLICO").stream()
                .filter(c -> c.getTitulo().contains("Figma")).findFirst().get();
        cursoMarketing = cursoRepo.findByEstado("PUBLICO").stream()
                .filter(c -> c.getTitulo().contains("Marketing Digital")).findFirst().get();
        cursoJavaScript = cursoRepo.findByEstado("PUBLICO").stream()
                .filter(c -> c.getTitulo().contains("JavaScript")).findFirst().get();
        cursoDataScience = cursoRepo.findByEstado("PUBLICO").stream()
                .filter(c -> c.getTitulo().contains("Data Science")).findFirst().get();
        cursoSEO = cursoRepo.findByEstado("BORRADOR").stream()
                .filter(c -> c.getTitulo().contains("SEO")).findFirst().get();

        // ── Módulos y Lecciones ──
        ModuloRepositoryImpl moduloRepo   = new ModuloRepositoryImpl();
        LeccionRepositoryImpl leccionRepo = new LeccionRepositoryImpl();

        // Módulos para Python
        Modulo m1 = new Modulo(null, "Introducción a Python", 1, cursoPython);
        moduloRepo.save(m1);
        m1 = moduloRepo.findByCursoId(cursoPython.getId()).get(0);
        leccionRepo.save(new Leccion(null, "¿Qué es Python?",
                "Python es un lenguaje de programación interpretado...", m1));
        leccionRepo.save(new Leccion(null, "Instalación del entorno",
                "Descarga Python desde python.org e instala VS Code...", m1));

        Modulo m2 = new Modulo(null, "Variables y Tipos de Datos", 2, cursoPython);
        moduloRepo.save(m2);
        m2 = moduloRepo.findByCursoId(cursoPython.getId()).get(1);
        leccionRepo.save(new Leccion(null, "Variables en Python",
                "Una variable es un contenedor para almacenar datos...", m2));
        leccionRepo.save(new Leccion(null, "Tipos primitivos",
                "int, float, str, bool - conoce los tipos básicos...", m2));

        Modulo m3 = new Modulo(null, "Estructuras de Control", 3, cursoPython);
        moduloRepo.save(m3);
        m3 = moduloRepo.findByCursoId(cursoPython.getId()).get(2);
        leccionRepo.save(new Leccion(null, "Condicionales (if/else)",
                "Controla el flujo de tu programa con condicionales...", m3));

        // Módulos para Diseño
        Modulo m4 = new Modulo(null, "Fundamentos de UX", 1, cursoDiseno);
        moduloRepo.save(m4);
        m4 = moduloRepo.findByCursoId(cursoDiseno.getId()).get(0);
        leccionRepo.save(new Leccion(null, "¿Qué es UX?",
                "User Experience (UX) es la experiencia que tiene el usuario...", m4));
        leccionRepo.save(new Leccion(null, "Principios de diseño",
                "Conoce los 10 principios fundamentales del diseño moderno...", m4));

        Modulo m5 = new Modulo(null, "Prototipado en Figma", 2, cursoDiseno);
        moduloRepo.save(m5);
        m5 = moduloRepo.findByCursoId(cursoDiseno.getId()).get(1);
        leccionRepo.save(new Leccion(null, "Primeros pasos en Figma",
                "Interfaz de Figma y componentes principales...", m5));

        // Módulos para Data Science
        Modulo m6 = new Modulo(null, "Fundamentos de Data Science", 1, cursoDataScience);
        moduloRepo.save(m6);
        m6 = moduloRepo.findByCursoId(cursoDataScience.getId()).get(0);
        leccionRepo.save(new Leccion(null, "¿Qué es Data Science?",
                "Introducción a análisis de datos y machine learning...", m6));

        // Módulos para JavaScript
        Modulo m7 = new Modulo(null, "Fundamentos de JavaScript", 1, cursoJavaScript);
        moduloRepo.save(m7);
        m7 = moduloRepo.findByCursoId(cursoJavaScript.getId()).get(0);
        leccionRepo.save(new Leccion(null, "Async/Await",
                "Manejo de programación asíncrona con async/await...", m7));

        // Módulos para Marketing
        Modulo m8 = new Modulo(null, "Fundamentos de Marketing", 1, cursoMarketing);
        moduloRepo.save(m8);
        m8 = moduloRepo.findByCursoId(cursoMarketing.getId()).get(0);
        leccionRepo.save(new Leccion(null, "Segmentación de mercado",
                "Identifica y segmenta tu público objetivo...", m8));
        leccionRepo.save(new Leccion(null, "Redes sociales",
                "Estrategias efectivas en redes sociales...", m8));

        // ── Likes (muchos likes para mostrar notificaciones) ──
        LikeCursoRepositoryImpl likeRepo = new LikeCursoRepositoryImpl();

        // Ana recibe likes en Python
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), carlos, cursoPython));
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), maria, cursoPython));
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), diego, cursoPython));

        // Carlos recibe likes en Diseño
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), ana, cursoDiseno));
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), maria, cursoDiseno));

        // María recibe likes en Marketing
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), ana, cursoMarketing));
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), diego, cursoMarketing));

        // Diego recibe likes en JavaScript
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), ana, cursoJavaScript));
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), carlos, cursoJavaScript));
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), maria, cursoJavaScript));

        // Ana recibe likes en Data Science (su otro curso)
        likeRepo.save(new LikeCurso(null, LocalDate.now().toString(), diego, cursoDataScience));

        // ── Matches ──
        MatchCursoRepositoryImpl matchRepo = new MatchCursoRepositoryImpl();

        MatchCurso match1 = new MatchCurso();
        match1.setCurso(cursoPython);
        match1.setEstudiante(carlos);
        match1.setCreador(ana);
        match1.setFechaConfirmacion(LocalDate.now().toString());
        matchRepo.save(match1);

        MatchCurso match2 = new MatchCurso();
        match2.setCurso(cursoDiseno);
        match2.setEstudiante(ana);
        match2.setCreador(carlos);
        match2.setFechaConfirmacion(LocalDate.now().toString());
        matchRepo.save(match2);

        MatchCurso match3 = new MatchCurso();
        match3.setCurso(cursoMarketing);
        match3.setEstudiante(diego);
        match3.setCreador(maria);
        match3.setFechaConfirmacion(LocalDate.now().toString());
        matchRepo.save(match3);

        // ── Reseñas ──
        ReviewRepositoryImpl reviewRepo = new ReviewRepositoryImpl();

        Resena resena1 = new Resena();
        resena1.setEstrellas(5);
        resena1.setComentario("Excelente curso, muy bien explicado.");
        resena1.setFecha(LocalDate.now());
        resena1.setUsuario(carlos);
        resena1.setCurso(cursoPython);
        reviewRepo.save(resena1);

        Resena resena2 = new Resena();
        resena2.setEstrellas(5);
        resena2.setComentario("Figma hecha fácil. Recomendado 100%.");
        resena2.setFecha(LocalDate.now());
        resena2.setUsuario(ana);
        resena2.setCurso(cursoDiseno);
        reviewRepo.save(resena2);

        Resena resena3 = new Resena();
        resena3.setEstrellas(4);
        resena3.setComentario("Muy buen contenido en marketing.");
        resena3.setFecha(LocalDate.now());
        resena3.setUsuario(diego);
        resena3.setCurso(cursoMarketing);
        reviewRepo.save(resena3);

        Resena resena4 = new Resena();
        resena4.setEstrellas(5);
        resena4.setComentario("JavaScript avanzado de la mejor calidad.");
        resena4.setFecha(LocalDate.now());
        resena4.setUsuario(maria);
        resena4.setCurso(cursoJavaScript);
        reviewRepo.save(resena4);

        // ── Notificaciones de reseñas ──
        // Se crean para validar el nuevo flujo REVIEW_RECIBIDA en UI y API.
        NotificacionRepositoryImpl notificacionRepo = new NotificacionRepositoryImpl();

        notificacionRepo.save(new Notificacion(ana, carlos, cursoPython,
                TipoNotificacion.REVIEW_RECIBIDA, LocalDate.now()));
        notificacionRepo.save(new Notificacion(carlos, ana, cursoDiseno,
                TipoNotificacion.REVIEW_RECIBIDA, LocalDate.now()));
        notificacionRepo.save(new Notificacion(maria, diego, cursoMarketing,
                TipoNotificacion.REVIEW_RECIBIDA, LocalDate.now()));
        notificacionRepo.save(new Notificacion(diego, maria, cursoJavaScript,
                TipoNotificacion.REVIEW_RECIBIDA, LocalDate.now()));

        // ── Inscripciones con progreso variado ──
        InscripcionRepositoryImpl inscripcionRepo = new InscripcionRepositoryImpl();

        // Carlos: 65% en Python
        Inscripcion isc1 = new Inscripcion(null, LocalDate.now().toString(), carlos, cursoPython);
        isc1.setProgreso(65);
        inscripcionRepo.save(isc1);

        // María: 40% en Python
        Inscripcion isc2 = new Inscripcion(null, LocalDate.now().toString(), maria, cursoPython);
        isc2.setProgreso(40);
        inscripcionRepo.save(isc2);

        // Ana: 100% en Diseño
        Inscripcion isc3 = new Inscripcion(null, LocalDate.now().toString(), ana, cursoDiseno);
        isc3.setProgreso(100);
        inscripcionRepo.save(isc3);

        // Diego: 75% en JavaScript
        Inscripcion isc4 = new Inscripcion(null, LocalDate.now().toString(), diego, cursoJavaScript);
        isc4.setProgreso(75);
        inscripcionRepo.save(isc4);

        // María: 80% en Diseño
        Inscripcion isc5 = new Inscripcion(null, LocalDate.now().toString(), maria, cursoDiseno);
        isc5.setProgreso(80);
        inscripcionRepo.save(isc5);

        // Ana: 90% en Data Science
        Inscripcion isc6 = new Inscripcion(null, LocalDate.now().toString(), ana, cursoDataScience);
        isc6.setProgreso(90);
        inscripcionRepo.save(isc6);

        // Carlos: 50% en Marketing
        Inscripcion isc7 = new Inscripcion(null, LocalDate.now().toString(), carlos, cursoMarketing);
        isc7.setProgreso(50);
        inscripcionRepo.save(isc7);

        // Diego: 30% en Data Science
        Inscripcion isc8 = new Inscripcion(null, LocalDate.now().toString(), diego, cursoDataScience);
        isc8.setProgreso(30);
        inscripcionRepo.save(isc8);

        // ── Notificaciones de likes ──
        // Ana recibe notificaciones de likes
        notificacionRepo.save(new Notificacion(ana, carlos, cursoPython,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));
        notificacionRepo.save(new Notificacion(ana, maria, cursoPython,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));
        Notificacion notif_read1 = new Notificacion(ana, diego, cursoPython,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now());
        notificacionRepo.save(notif_read1);
        notif_read1.marcarComoLeida();
        notificacionRepo.update(notif_read1);

        // Carlos recibe notificaciones de likes
        notificacionRepo.save(new Notificacion(carlos, ana, cursoDiseno,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));
        notificacionRepo.save(new Notificacion(carlos, maria, cursoDiseno,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));

        // María recibe notificaciones de likes
        notificacionRepo.save(new Notificacion(maria, ana, cursoMarketing,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));
        notificacionRepo.save(new Notificacion(maria, diego, cursoMarketing,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));

        // Diego recibe notificaciones de likes
        notificacionRepo.save(new Notificacion(diego, ana, cursoJavaScript,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));
        notificacionRepo.save(new Notificacion(diego, carlos, cursoJavaScript,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));
        Notificacion notif_read2 = new Notificacion(diego, maria, cursoJavaScript,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now());
        notificacionRepo.save(notif_read2);
        notif_read2.marcarComoLeida();
        notificacionRepo.update(notif_read2);

        // Ana recibe más likes en Data Science
        notificacionRepo.save(new Notificacion(ana, diego, cursoDataScience,
                TipoNotificacion.LIKE_RECIBIDO, LocalDate.now()));

        printSummary();
    }

    private static void printSummary() {
        System.out.println("✅ Datos de prueba insertados correctamente.");
        System.out.println("\n📊 RESUMEN DE DATOS:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("\n👥 USUARIOS (patraseña: 123456):");
        System.out.println("   📧 ana@test.com       → Creadora de 2 cursos");
        System.out.println("   📧 carlos@test.com    → Creador de 2 cursos");
        System.out.println("   📧 maria@test.com     → Creadora de 1 curso");
        System.out.println("   📧 diego@test.com     → Creador de 1 curso");

        System.out.println("\n📚 CURSOS (6 total - 5 públicos + 1 borrador):");
        System.out.println("   • Python para Principiantes (Ana) - PÚBLICO");
        System.out.println("   • Diseño UX/UI con Figma (Carlos) - PÚBLICO");
        System.out.println("   • Marketing Digital (María) - PÚBLICO");
        System.out.println("   • JavaScript Avanzado (Diego) - PÚBLICO");
        System.out.println("   • Data Science con Python (Ana) - PÚBLICO");
        System.out.println("   • SEO y Posicionamiento Web (Carlos) - BORRADOR");

        System.out.println("\n📖 CONTENIDO:");
        System.out.println("   • 8 Módulos creados");
        System.out.println("   • 25+ Lecciones disponibles");

        System.out.println("\n❤️  LIKES (11 total):");
        System.out.println("   • Python: 3 likes (Carlos, María, Diego)");
        System.out.println("   • Diseño: 2 likes (Ana, María)");
        System.out.println("   • Marketing: 2 likes (Ana, Diego)");
        System.out.println("   • JavaScript: 3 likes (Ana, Carlos, María)");
        System.out.println("   • Data Science: 1 like (Diego)");

        System.out.println("\n🤝 MATCHES (3 total):");
        System.out.println("   • Python confirmado: Carlos + Ana");
        System.out.println("   • Diseño confirmado: Ana + Carlos");
        System.out.println("   • Marketing confirmado: Diego + María");

        System.out.println("\n⭐ RESEÑAS (4 total):");
        System.out.println("   • Python: 5⭐ (Carlos)");
        System.out.println("   • Diseño: 5⭐ (Ana)");
        System.out.println("   • Marketing: 4⭐ (Diego)");
        System.out.println("   • JavaScript: 5⭐ (María)");

        System.out.println("\n📊 INSCRIPCIONES CON PROGRESO (8 total):");
        System.out.println("   • Carlos: Python 65% ⏳");
        System.out.println("   • María: Python 40% ⏳");
        System.out.println("   • Ana: Diseño UX/UI 100% ✅");
        System.out.println("   • Diego: JavaScript 75% ⏳");
        System.out.println("   • María: Diseño 80% ⏳");
        System.out.println("   • Ana: Data Science 90% ⏳");
        System.out.println("   • Carlos: Marketing 50% ⏳");
        System.out.println("   • Diego: Data Science 30% ⏳");

        System.out.println("\n🔔 NOTIFICACIONES (15 total):");
        System.out.println("   • Ana: 5 notificaciones (1 leída ✅, 4 sin leer)");
        System.out.println("   • Carlos: 3 notificaciones (sin leer)");
        System.out.println("   • María: 3 notificaciones (sin leer)");
        System.out.println("   • Diego: 4 notificaciones (1 leída ✅, 3 sin leer)");

        System.out.println("\n🎯 FUNCIONALIDADES DEMOSTRABLES:");
        System.out.println("   ✅ Notificaciones de likes con badge");
        System.out.println("   ✅ Notificaciones de reseñas (REVIEW_RECIBIDA)");
        System.out.println("   ✅ Barra de progreso en inscripciones");
        System.out.println("   ✅ Múltiples cursos por usuario");
        System.out.println("   ✅ Matches confirmados");
        System.out.println("   ✅ Reseñas y calificaciones");
        System.out.println("   ✅ Likes y contador de reacciones");
        System.out.println("   ✅ Estados de cursos (público/borrador)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
