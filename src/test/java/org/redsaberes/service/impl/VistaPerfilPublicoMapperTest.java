package org.redsaberes.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.redsaberes.model.Curso;
import org.redsaberes.model.EstadoCurso;
import org.redsaberes.model.Resena;
import org.redsaberes.model.Usuario;
import org.redsaberes.service.dto.VistaPerfilPublicoViewDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("VistaPerfilPublico")
class VistaPerfilPublicoMapperTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("casosVistaPerfilPublico")
    @DisplayName("debe renderizar nombre, cursos, matches, calificacion y tres reseñas")
    void debe_renderizar_la_vista_del_perfil_publico(String caso,
                                                     PerfilPublicoInput input,
                                                     VistaPerfilPublicoEsperada expected) {
        VistaPerfilPublicoViewDto renderizado = new VistaPerfilPublicoMapper().map(input.usuario(), input.matchesActivos());

        assertAll(
                () -> assertEquals(expected.nombreUsuario(), renderizado.getNombreUsuario(), "nombre del usuario"),
                () -> assertEquals(expected.cursosCreados(), renderizado.getCursosCreados(), "cursos creados"),
                () -> assertEquals(expected.matchesActivos(), renderizado.getMatchesActivos(), "matches activos"),
                () -> assertEquals(expected.calificacionPromedio(), renderizado.getCalificacionPromedio(), 0.01, "calificacion por estrellas"),
                () -> assertEquals(expected.primerasResenas(), renderizado.getPrimerasResenas(), "primeras reseñas visibles")
        );
    }

    private static Stream<Arguments> casosVistaPerfilPublico() {
        return Stream.of(
                Arguments.of(
                        "perfil completo",
                        nuevoPerfilCompleto(),
                        new VistaPerfilPublicoEsperada(
                                "Ana Torres",
                                2,
                                5,
                                4.50,
                                List.of("Excelente ritmo", "Bien explicado", "Muy claro")
                        )
                ),
                Arguments.of(
                        "perfil sin cursos",
                        nuevoPerfilSinCursos(),
                        new VistaPerfilPublicoEsperada(
                                "Luis Gómez",
                                0,
                                2,
                                0.00,
                                List.of()
                        )
                ),
                Arguments.of(
                        "perfil sin reseñas",
                        nuevoPerfilSinResenas(),
                        new VistaPerfilPublicoEsperada(
                                "María Pérez",
                                3,
                                1,
                                0.00,
                                List.of()
                        )
                )
        );
    }

    private static PerfilPublicoInput nuevoPerfilCompleto() {
        Usuario autor = crearUsuario(1, "Ana Torres");
        List<Curso> cursos = new ArrayList<>();

        Curso cursoJava = crearCurso(10, "Java desde cero", autor);
        Curso cursoSQL = crearCurso(11, "SQL práctico", autor);

        cursoJava.addResena(crearResena(100, 5, "Excelente ritmo", LocalDate.of(2026, 4, 1), crearUsuario(20, "Reviewer 1")));
        cursoJava.addResena(crearResena(101, 4, "Bien explicado", LocalDate.of(2026, 4, 2), crearUsuario(21, "Reviewer 2")));
        cursoSQL.addResena(crearResena(102, 5, "Muy claro", LocalDate.of(2026, 4, 3), crearUsuario(22, "Reviewer 3")));
        cursoSQL.addResena(crearResena(103, 4, "Contenido útil", LocalDate.of(2026, 4, 4), crearUsuario(23, "Reviewer 4")));

        cursos.add(cursoJava);
        cursos.add(cursoSQL);
        autor.setCursos(cursos);

        return new PerfilPublicoInput(autor, 5);
    }

    private static PerfilPublicoInput nuevoPerfilSinCursos() {
        Usuario usuario = crearUsuario(2, "Luis Gómez");
        usuario.setCursos(new ArrayList<>());

        return new PerfilPublicoInput(usuario, 2);
    }

    private static PerfilPublicoInput nuevoPerfilSinResenas() {
        Usuario autor = crearUsuario(3, "María Pérez");
        List<Curso> cursos = List.of(
                crearCurso(30, "Diseño UX", autor),
                crearCurso(31, "Figma para principiantes", autor),
                crearCurso(32, "Accesibilidad web", autor)
        );

        autor.setCursos(new ArrayList<>(cursos));

        return new PerfilPublicoInput(autor, 1);
    }

    private static Usuario crearUsuario(Integer id, String nombre) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setCorreoElectronico(nombre.toLowerCase().replace(" ", ".") + "@redsaberes.test");
        usuario.setContrasena("secret");
        return usuario;
    }

    private static Curso crearCurso(Integer id, String titulo, Usuario autor) {
        Curso curso = new Curso();
        curso.setId(id);
        curso.setTitulo(titulo);
        curso.setDescripcion("Descripcion de " + titulo);
        curso.setCategoria("General");
        curso.setNivelDificultad("Básico");
        curso.setImagenPortada("portada-" + id + ".png");
        curso.setEstado(EstadoCurso.PUBLICO);
        curso.setUsuario(autor);
        curso.setResenas(new ArrayList<>());
        return curso;
    }

    private static Resena crearResena(Integer id,
                                      Integer estrellas,
                                      String comentario,
                                      LocalDate fecha,
                                      Usuario revisor) {
        Resena resena = new Resena();
        resena.setId(id);
        resena.setEstrellas(estrellas);
        resena.setComentario(comentario);
        resena.setFecha(fecha);
        resena.setUsuario(revisor);
        return resena;
    }

    private record PerfilPublicoInput(Usuario usuario, int matchesActivos) {
    }

    private record VistaPerfilPublicoEsperada(String nombreUsuario,
                                              int cursosCreados,
                                              int matchesActivos,
                                              double calificacionPromedio,
                                              List<String> primerasResenas) {
    }
}
