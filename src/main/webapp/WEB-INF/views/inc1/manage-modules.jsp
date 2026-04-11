<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion de Modulos - RedSaberes</title>
    <script src="https://cdn.tailwindcss.com"></script>
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
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<nav class="bg-white/90 border-b border-gray-200 sticky top-0 z-40">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="h-16 flex items-center justify-between">
            <a href="${pageContext.request.contextPath}/dashboard" class="flex items-center gap-2">
                <div class="w-10 h-10 bg-gradient-brand rounded-xl flex items-center justify-center">
                    <i data-lucide="book-open" class="w-6 h-6 text-white"></i>
                </div>
                <span class="text-2xl font-bold text-gradient-brand">RedSaberes</span>
            </a>

            <div class="flex items-center gap-3">
                <a href="${pageContext.request.contextPath}/edit-course?id=${curso.id}" class="px-4 py-2 text-sm text-gray-600 hover:text-gray-900">
                    Volver a edicion del curso
                </a>
                <a href="${pageContext.request.contextPath}/my-courses" class="px-4 py-2 text-sm text-gray-600 hover:text-gray-900">
                    Mis cursos
                </a>
            </div>
        </div>
    </div>
</nav>

<div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-6">
        <p class="text-sm text-indigo-600 font-medium">Gestion de modulos</p>
        <h1 class="text-3xl font-bold text-gray-900">${curso.titulo}</h1>
        <p class="text-gray-500 text-sm mt-1">Edita, elimina o reordena los modulos de tu curso.</p>
    </div>

    <c:if test="${not empty exito}">
        <div class="mb-4 p-3 rounded-lg border border-green-200 bg-green-50 text-green-700 text-sm">
            ${exito}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="mb-4 p-3 rounded-lg border border-red-200 bg-red-50 text-red-700 text-sm">
            ${error}
        </div>
    </c:if>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div class="lg:col-span-1">
            <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
                <h2 class="font-semibold text-gray-900 mb-4 flex items-center gap-2">
                    <i data-lucide="edit-3" class="w-4 h-4 text-indigo-600"></i>
                    Editar modulo
                </h2>

                <c:choose>
                    <c:when test="${not empty moduloEditar}">
                        <form method="post" action="${pageContext.request.contextPath}/edit-course" class="space-y-4">
                            <input type="hidden" name="action" value="update" />
                            <input type="hidden" name="moduloId" value="${moduloEditar.id}" />

                            <div>
                                <label class="block text-sm text-gray-700 mb-1">Titulo</label>
                                <input type="text" name="tituloModulo" value="${moduloEditar.titulo}" required
                                       class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-600 focus:border-transparent" />
                            </div>

                            <div>
                                <label class="block text-sm text-gray-700 mb-1">Orden</label>
                                <input type="number" min="1" name="orden" value="${moduloEditar.orden}"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-600 focus:border-transparent" />
                            </div>

                            <button type="submit" class="w-full py-2.5 rounded-lg text-white bg-gradient-brand font-semibold text-sm">
                                Guardar cambios
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="text-sm text-gray-500">
                            Selecciona un modulo en la lista para editar su titulo u orden.
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5 mt-6">
                <h2 class="font-semibold text-gray-900 mb-3">Reordenamiento</h2>
                <p class="text-sm text-gray-500 mb-4">Activa el modo de reordenamiento para cambiar el orden visual de los modulos.</p>
                <a href="${pageContext.request.contextPath}/edit-course?action=reorder&cursoId=${curso.id}"
                   class="inline-flex items-center justify-center w-full gap-2 py-2.5 rounded-lg border border-indigo-200 text-indigo-700 bg-indigo-50 hover:bg-indigo-100 text-sm font-medium">
                    <i data-lucide="arrow-up-down" class="w-4 h-4"></i>
                    Activar reordenamiento
                </a>
            </div>
        </div>

        <div class="lg:col-span-2">
            <div class="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
                <div class="px-5 py-4 border-b border-gray-100 flex items-center justify-between">
                    <h2 class="font-semibold text-gray-900">Modulos actuales</h2>
                    <span class="text-xs font-semibold px-2 py-1 rounded-full bg-indigo-100 text-indigo-700">${modulos.size()}</span>
                </div>

                <c:if test="${empty modulos}">
                    <div class="p-8 text-center text-gray-500 text-sm">No hay modulos para gestionar.</div>
                </c:if>

                <c:if test="${not empty modulos}">
                    <div class="divide-y divide-gray-100">
                        <c:forEach var="modulo" items="${modulos}">
                            <div class="p-4 flex items-start justify-between gap-4">
                                <div>
                                    <p class="font-medium text-gray-900">${modulo.titulo}</p>
                                    <p class="text-xs text-gray-500 mt-1">
                                        Orden: ${modulo.orden} | Lecciones: ${modulo.lecciones.size()}
                                    </p>
                                </div>
                                <div class="flex items-center gap-2">
                                    <a href="${pageContext.request.contextPath}/edit-course?action=edit&id=${modulo.id}"
                                       class="px-3 py-2 rounded-lg text-xs font-medium bg-indigo-50 text-indigo-700 hover:bg-indigo-100">
                                        Editar
                                    </a>
                                    <form method="post" action="${pageContext.request.contextPath}/edit-course" onsubmit="return confirm('Eliminar este modulo?');">
                                        <input type="hidden" name="action" value="delete" />
                                        <input type="hidden" name="moduloId" value="${modulo.id}" />
                                        <button type="submit" class="px-3 py-2 rounded-lg text-xs font-medium bg-red-50 text-red-700 hover:bg-red-100">
                                            Eliminar
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>

            <c:if test="${modoReordenar and not empty modulos}">
                <div class="bg-white rounded-xl border border-gray-100 shadow-sm mt-6 p-5">
                    <h3 class="font-semibold text-gray-900 mb-2">Guardar nuevo orden</h3>
                    <p class="text-sm text-gray-500 mb-4">Usa los botones para mover cada modulo y luego guarda el orden.</p>

                    <form method="post" action="${pageContext.request.contextPath}/edit-course" id="formReorder">
                        <input type="hidden" name="action" value="saveOrder" />
                        <input type="hidden" name="cursoId" value="${curso.id}" />

                        <ul id="sortableList" class="space-y-2">
                            <c:forEach var="modulo" items="${modulos}">
                                <li class="reorder-item p-3 border border-gray-200 rounded-lg flex items-center justify-between bg-gray-50">
                                    <input type="hidden" name="moduloIds" value="${modulo.id}" />
                                    <span class="text-sm font-medium text-gray-800">${modulo.titulo}</span>
                                    <div class="flex gap-2">
                                        <button type="button" class="move-up px-2 py-1 text-xs rounded bg-white border border-gray-300">Subir</button>
                                        <button type="button" class="move-down px-2 py-1 text-xs rounded bg-white border border-gray-300">Bajar</button>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>

                        <button type="submit" class="mt-4 inline-flex items-center gap-2 px-5 py-2.5 rounded-lg text-sm text-white bg-gradient-brand font-semibold">
                            <i data-lucide="save" class="w-4 h-4"></i>
                            Guardar orden
                        </button>
                    </form>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script>
    lucide.createIcons();

    document.querySelectorAll('.move-up').forEach(function(button) {
        button.addEventListener('click', function() {
            var item = this.closest('.reorder-item');
            var prev = item.previousElementSibling;
            if (prev) {
                item.parentNode.insertBefore(item, prev);
            }
        });
    });

    document.querySelectorAll('.move-down').forEach(function(button) {
        button.addEventListener('click', function() {
            var item = this.closest('.reorder-item');
            var next = item.nextElementSibling;
            if (next) {
                item.parentNode.insertBefore(next, item);
            }
        });
    });
</script>

</body>
</html>