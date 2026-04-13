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

