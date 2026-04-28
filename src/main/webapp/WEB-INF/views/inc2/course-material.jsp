<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Material del Curso</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="min-h-screen bg-gray-50">
<nav class="bg-white border-b border-gray-200">
    <div class="max-w-6xl mx-auto px-4 py-4 flex justify-between items-center">
        <a href="${pageContext.request.contextPath}/explore" class="text-indigo-600 font-semibold">Explorar</a>
        <a href="${pageContext.request.contextPath}/matches" class="text-gray-600 hover:text-indigo-600">Matches</a>
    </div>
</nav>

<div class="max-w-6xl mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-gray-900 mb-2">${curso.titulo}</h1>
    <p class="text-gray-600 mb-6">${curso.descripcion}</p>

    <c:if test="${not empty exito}">
        <div class="mb-6 rounded-lg border border-emerald-200 bg-emerald-50 p-4 text-emerald-700">
            <c:out value="${exito}"/>
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="mb-6 rounded-lg border border-red-200 bg-red-50 p-4 text-red-700">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${accesoConcedido}">
            <div class="mb-6 inline-flex items-center px-3 py-1 rounded-full bg-emerald-100 text-emerald-700 text-sm font-semibold">
                Acceso concedido por match
            </div>

            <c:choose>
                <c:when test="${not empty modulos}">
                    <div class="space-y-4">
                        <c:forEach var="modulo" items="${modulos}" varStatus="m">
                            <div class="bg-white border border-gray-200 rounded-xl">
                                <div class="px-5 py-4 border-b border-gray-100">
                                    <h2 class="font-bold text-gray-900">Modulo ${m.index + 1}: ${modulo.titulo}</h2>
                                </div>
                                <div class="p-5">
                                    <c:choose>
                                        <c:when test="${not empty modulo.lecciones}">
                                            <ul class="space-y-3">
                                                <c:forEach var="leccion" items="${modulo.lecciones}" varStatus="l">
                                                    <li class="p-3 bg-gray-50 rounded-lg">
                                                        <p class="font-semibold text-gray-900">Leccion ${l.index + 1}: ${leccion.titulo}</p>
                                                        <p class="text-sm text-gray-600 mt-1">${leccion.contenido}</p>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-sm text-gray-500">Este modulo aun no tiene lecciones.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="bg-white border border-dashed border-gray-300 rounded-xl p-6 text-gray-500">
                        El curso aun no tiene material disponible.
                    </div>
                </c:otherwise>
            </c:choose>

            <section class="mt-8 bg-white border border-gray-200 rounded-xl">
                <div class="px-5 py-4 border-b border-gray-100">
                    <h2 class="text-xl font-bold text-gray-900">Comentarios del curso</h2>
                    <p class="text-sm text-gray-600 mt-1">Maximo 255 caracteres. No se permiten palabras ofensivas ni spam.</p>
                </div>

                <div class="p-5 border-b border-gray-100">
                    <form method="post" action="${pageContext.request.contextPath}/course-material?id=${curso.id}">
                        <label for="comentario" class="block text-sm font-semibold text-gray-700 mb-2">Escribe tu comentario</label>
                        <textarea
                                id="comentario"
                                name="comentario"
                                rows="3"
                                maxlength="255"
                                required
                                class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                                placeholder="Comparte una opinion util sobre el contenido del curso"></textarea>
                        <div class="mt-3 flex justify-end">
                            <button type="submit" class="rounded-lg bg-indigo-600 px-4 py-2 text-white hover:bg-indigo-700">
                                Publicar comentario
                            </button>
                        </div>
                    </form>
                </div>

                <div class="p-5">
                    <c:choose>
                        <c:when test="${not empty comentarios}">
                            <ul class="space-y-3">
                                <c:forEach var="comentario" items="${comentarios}">
                                    <li class="rounded-lg border border-gray-200 p-4 bg-gray-50">
                                        <div class="flex items-center justify-between gap-4 mb-2">
                                            <p class="text-sm font-semibold text-gray-800">
                                                <c:out value="${comentario.usuario.nombre}"/>
                                            </p>
                                            <p class="text-xs text-gray-500">
                                                <c:out value="${comentario.fecha}"/>
                                            </p>
                                        </div>
                                        <p class="text-sm text-gray-700">
                                            <c:out value="${comentario.comentario}"/>
                                        </p>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <p class="text-sm text-gray-500">Aun no hay comentarios. Se el primero en comentar este curso.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </c:when>
        <c:otherwise>
            <div class="bg-white border border-red-200 rounded-xl p-8 text-center">
                <p class="text-2xl mb-2">Acceso denegado</p>
                <p class="text-gray-600">Necesitas que el creador te aplique match para ver este material.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

