<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Curso - RedSaberes</title>

    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Lucide Icons -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <style>
        .bg-gradient-main {
            background: linear-gradient(to bottom right, #eef2ff, #ffffff, #faf5ff);
        }
        .bg-gradient-brand {
            background: linear-gradient(to right, #4f46e5, #9333ea);
        }
        .text-gradient-brand {
            background: linear-gradient(to right, #4f46e5, #9333ea);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to   { opacity: 1; transform: translateY(0); }
        }
        .fade-in { animation: fadeIn 0.5s ease-out; }
        .backdrop-blur-md {
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
        }
        .hover-scale {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .hover-scale:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px -5px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<!-- Navigation Header -->
<nav class="bg-white/80 backdrop-blur-md border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">

            <a href="${pageContext.request.contextPath}/dashboard"
               class="flex items-center gap-2">
                <div class="w-10 h-10 bg-gradient-brand rounded-xl flex items-center justify-center">
                    <i data-lucide="book-open" class="w-6 h-6 text-white"></i>
                </div>
                <span class="text-2xl font-bold text-gradient-brand">RedSaberes</span>
            </a>

            <div class="flex items-center gap-4">
                <!-- Badge de estado del curso -->
                <c:choose>
                    <c:when test="${curso.estado == 'BORRADOR'}">
                        <span class="px-3 py-1 bg-yellow-100 text-yellow-700
                                     text-xs font-semibold rounded-full">
                            Borrador
                        </span>
                    </c:when>
                    <c:when test="${curso.estado == 'PUBLICO'}">
                        <span class="px-3 py-1 bg-green-100 text-green-700
                                     text-xs font-semibold rounded-full">
                            Publicado
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="px-3 py-1 bg-gray-100 text-gray-600
                                     text-xs font-semibold rounded-full">
                            Archivado
                        </span>
                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/my-courses"
                   class="flex items-center gap-2 px-4 py-2 text-gray-600
                          hover:text-gray-900 transition text-sm">
                    <i data-lucide="arrow-left" class="w-4 h-4"></i>
                    Mis Cursos
                </a>
            </div>
        </div>
    </div>
</nav>

<div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

    <!-- Header del curso -->
    <div class="mb-8 fade-in">
        <p class="text-sm text-indigo-600 font-medium mb-1">Editando curso</p>
        <h1 class="text-3xl font-bold text-gray-900 mb-2">${curso.titulo}</h1>
        <p class="text-gray-500">
            Agrega módulos y lecciones. Necesitas al menos uno para publicar el curso.
        </p>
    </div>

    <!-- Mensajes -->
    <c:if test="${not empty exito}">
        <div class="mb-6 p-4 bg-green-50 border border-green-200 text-green-700
                    rounded-lg flex items-center gap-2 fade-in">
            <i data-lucide="check-circle-2" class="w-5 h-5 flex-shrink-0"></i>
            <p class="text-sm">${exito}</p>
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700
                    rounded-lg flex items-center gap-2 fade-in">
            <i data-lucide="alert-circle" class="w-5 h-5 flex-shrink-0"></i>
            <p class="text-sm">${error}</p>
        </div>
    </c:if>

    <div class="grid lg:grid-cols-3 gap-8">

        <!-- Columna izquierda: formulario agregar módulo -->
        <div class="lg:col-span-1">
            <div class="bg-white rounded-xl shadow-sm border border-gray-100
                        p-6 sticky top-24 fade-in">
                <h2 class="text-lg font-bold text-gray-900 mb-5 flex items-center gap-2">
                    <i data-lucide="plus-circle" class="w-5 h-5 text-indigo-600"></i>
                    Agregar Módulo
                </h2>

                <!--
                    DS CU-05: procesarNuevoModulo(datosMódulo, datosLeccion)
                    Trazabilidad: ingresarDatosModulo() → ingresarDatosLeccion()
                    → clicGuardarModuloYLeccion()
                -->
                <form method="post"
                      action="${pageContext.request.contextPath}/edit-course"
                      class="space-y-4">

                    <!-- cursoId como campo oculto -->
                    <input type="hidden" name="cursoId" value="${curso.id}" />

                    <!-- DS CU-05: tituloModulo -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">
                            Título del módulo *
                        </label>
                        <input type="text"
                               name="tituloModulo"
                               required
                               maxlength="150"
                               class="w-full px-3 py-2 border border-gray-300 rounded-lg
                                      text-sm focus:ring-2 focus:ring-indigo-600
                                      focus:border-transparent transition"
                               placeholder="Ej: Introducción al tema" />
                    </div>

                    <!-- DS CU-05: tituloLeccion (obligatoria, RF-C02) -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">
                            Título de la primera lección *
                        </label>
                        <input type="text"
                               name="tituloLeccion"
                               required
                               maxlength="150"
                               class="w-full px-3 py-2 border border-gray-300 rounded-lg
                                      text-sm focus:ring-2 focus:ring-indigo-600
                                      focus:border-transparent transition"
                               placeholder="Ej: ¿Qué es y para qué sirve?" />
                    </div>

                    <!-- DS CU-05: contenidoLeccion -->
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">
                            Contenido de la lección
                        </label>
                        <textarea name="contenidoLeccion"
                                  rows="5"
                                  class="w-full px-3 py-2 border border-gray-300 rounded-lg
                                         text-sm focus:ring-2 focus:ring-indigo-600
                                         focus:border-transparent transition resize-none"
                                  placeholder="Escribe el contenido de la lección..."></textarea>
                    </div>

                    <!-- DS CU-05: clicGuardarModuloYLeccion() -->
                    <button type="submit"
                            class="w-full flex items-center justify-center gap-2
                                   py-2.5 bg-gradient-brand text-white rounded-lg
                                   font-semibold hover:shadow-lg transition text-sm">
                        <i data-lucide="save" class="w-4 h-4"></i>
                        Guardar módulo
                    </button>
                </form>
            </div>
        </div>

        <!-- Columna derecha: lista de módulos existentes -->
        <div class="lg:col-span-2">
            <div class="flex items-center justify-between mb-4">
                <h2 class="text-lg font-bold text-gray-900">
                    Módulos del curso
                    <span class="ml-2 px-2 py-0.5 bg-indigo-100 text-indigo-700
                                 text-xs font-semibold rounded-full">
                        ${modulos.size()}
                    </span>
                </h2>
            </div>

            <!-- Estado vacío -->
            <c:if test="${empty modulos}">
                <div class="bg-white rounded-xl border-2 border-dashed border-gray-300
                            p-12 text-center fade-in">
                    <i data-lucide="layers" class="w-14 h-14 text-gray-300 mx-auto mb-4"></i>
                    <h3 class="text-lg font-semibold text-gray-700 mb-2">
                        Sin módulos todavía
                    </h3>
                    <p class="text-gray-500 text-sm">
                        Agrega al menos un módulo con una lección
                        para poder publicar el curso.
                    </p>
                </div>
            </c:if>

            <!-- Lista de módulos -->
            <c:if test="${not empty modulos}">
                <div class="space-y-4">
                    <c:forEach var="modulo" items="${modulos}" varStatus="st">
                        <div class="bg-white rounded-xl shadow-sm border border-gray-100
                                    overflow-hidden hover-scale fade-in">

                            <!-- Header del módulo -->
                            <div class="flex items-center justify-between
                                        px-6 py-4 border-b border-gray-100">
                                <div class="flex items-center gap-3">
                                    <span class="w-8 h-8 bg-indigo-100 text-indigo-700
                                                 rounded-full flex items-center
                                                 justify-center text-sm font-bold
                                                 flex-shrink-0">
                                            ${st.index + 1}
                                    </span>
                                    <div>
                                        <h3 class="font-semibold text-gray-900">
                                                ${modulo.titulo}
                                        </h3>
                                        <p class="text-xs text-gray-500 mt-0.5">
                                                ${modulo.lecciones.size()} lección(es)
                                        </p>
                                    </div>
                                </div>
                                <span class="text-xs text-gray-400">
                                    Orden: ${modulo.orden}
                                </span>
                            </div>

                            <!-- Lecciones del módulo -->
                            <div class="px-6 py-4">
                                <c:choose>
                                    <c:when test="${not empty modulo.lecciones}">
                                        <div class="space-y-3">
                                            <c:forEach var="leccion"
                                                       items="${modulo.lecciones}"
                                                       varStatus="lst">
                                                <div class="flex items-start gap-3
                                                            pl-4 border-l-2
                                                            border-indigo-200">
                                                    <div class="flex-1 min-w-0">
                                                        <p class="text-sm font-medium
                                                                   text-gray-800">
                                                                ${lst.index + 1}.
                                                                ${leccion.titulo}
                                                        </p>
                                                        <c:if test="${not empty leccion.contenido}">
                                                            <p class="text-xs text-gray-500
                                                                       mt-1 line-clamp-2"
                                                               style="display:-webkit-box;
                                                                      -webkit-line-clamp:2;
                                                                      -webkit-box-orient:vertical;
                                                                      overflow:hidden">
                                                                    ${leccion.contenido}
                                                            </p>
                                                        </c:if>
                                                    </div>
                                                    <i data-lucide="file-text"
                                                       class="w-4 h-4 text-gray-400
                                                              flex-shrink-0 mt-0.5"></i>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-sm text-gray-400 italic">
                                            Sin lecciones
                                        </p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Botón publicar (CU-07) — visible cuando hay módulos -->
                <div class="mt-8 p-6 bg-indigo-50 border border-indigo-200
                            rounded-xl fade-in">
                    <div class="flex items-start gap-4">
                        <i data-lucide="rocket" class="w-8 h-8 text-indigo-600
                                                       flex-shrink-0 mt-1"></i>
                        <div class="flex-1">
                            <h3 class="font-semibold text-indigo-900 mb-1">
                                ¿Listo para publicar?
                            </h3>
                            <p class="text-sm text-indigo-700 mb-4">
                                Tu curso tiene ${modulos.size()} módulo(s).
                                Puedes publicarlo para que otros usuarios
                                puedan verlo y dar like.
                            </p>
                            <!--
                                Enlace al CU-07 PublicarServlet
                                Se implementa en el siguiente caso de uso
                            -->
                            <a href="${pageContext.request.contextPath}/publicar?id=${curso.id}"
                               class="inline-flex items-center gap-2 px-6 py-2.5
                                      bg-gradient-brand text-white rounded-lg
                                      font-semibold hover:shadow-lg transition text-sm">
                                <i data-lucide="send" class="w-4 h-4"></i>
                                Publicar curso
                            </a>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script>
    lucide.createIcons();
    setInterval(() => { lucide.createIcons(); }, 800);
</script>

</body>
</html>
