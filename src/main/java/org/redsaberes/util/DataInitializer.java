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

        System.out.println("🌱 Insertando datos de prueba...");

        // ── Usuarios ──
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

        // Recargar con IDs generados
        ana    = usuarioRepo.findByCorreo("ana@test.com").get();
        carlos = usuarioRepo.findByCorreo("carlos@test.com").get();
        maria  = usuarioRepo.findByCorreo("maria@test.com").get();

        // ── Cursos ──
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
                "Marketing", "Principiante", null, EstadoCurso.BORRADOR, maria);
        cursoRepo.save(cursoMarketing);

        // Recargar con IDs
        cursoPython  = cursoRepo.findByEstado("PUBLICO").stream()
                .filter(c -> c.getTitulo().contains("Python")).findFirst().get();
        cursoDiseno  = cursoRepo.findByEstado("PUBLICO").stream()
                .filter(c -> c.getTitulo().contains("Figma")).findFirst().get();

        // ── Módulos y Lecciones ──
        ModuloRepositoryImpl moduloRepo   = new ModuloRepositoryImpl();
        LeccionRepositoryImpl leccionRepo = new LeccionRepositoryImpl();

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

        // Módulo para curso de Diseño
        Modulo m3 = new Modulo(null, "Fundamentos de UX", 1, cursoDiseno);
        moduloRepo.save(m3);
        m3 = moduloRepo.findByCursoId(cursoDiseno.getId()).get(0);
        leccionRepo.save(new Leccion(null, "¿Qué es UX?",
                "User Experience (UX) es la experiencia que tiene el usuario...", m3));

        // ── Likes ──
        LikeCursoRepositoryImpl likeRepo = new LikeCursoRepositoryImpl();

        LikeCurso like1 = new LikeCurso(null,
                LocalDate.now().toString(), carlos, cursoPython);
        likeRepo.save(like1);

        LikeCurso like2 = new LikeCurso(null,
                LocalDate.now().toString(), maria, cursoPython);
        likeRepo.save(like2);

        LikeCurso like3 = new LikeCurso(null,
                LocalDate.now().toString(), ana, cursoDiseno);
        likeRepo.save(like3);

        // ── Match ──
        MatchCursoRepositoryImpl matchRepo = new MatchCursoRepositoryImpl();

        MatchCurso match = new MatchCurso();
        match.setCurso(cursoPython);
        match.setEstudiante(carlos);
        match.setCreador(ana);
        match.setFechaConfirmacion(LocalDate.now().toString());
        matchRepo.save(match);

        // ── Reseña ──
        ReviewRepositoryImpl reviewRepo = new ReviewRepositoryImpl();

        Resena resena = new Resena();
        resena.setEstrellas(5);
        resena.setComentario("Excelente curso, muy bien explicado.");
        resena.setFecha(LocalDate.now());
        resena.setUsuario(carlos);
        resena.setCurso(cursoPython);
        reviewRepo.save(resena);

        System.out.println("✅ Datos de prueba insertados correctamente.");
        System.out.println("   📧 Usuarios: ana@test.com / carlos@test.com / maria@test.com");
        System.out.println("   🔑 Contraseña de todos: 123456");
    }
}