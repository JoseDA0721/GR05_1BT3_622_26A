<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil Público</title>
    <%@ include file="../fragments/common-head.jspf" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/redsaberes-common.css?v=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gradient-main min-h-screen">
    <%@ include file="../fragments/nav.jspf" %>

    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <c:choose>
            <c:when test="${not empty perfilPublico}">
                <div class="bg-white rounded-2xl border border-gray-200 p-8">
                    <div class="flex items-center justify-between mb-6">
                        <div>
                            <h1 class="text-2xl font-bold text-gray-900">${perfilPublico.nombreUsuario}</h1>
                            <p class="text-sm text-gray-500">Perfil público</p>
                        </div>
                        <div class="text-right">
                            <div class="text-sm text-gray-500">Cursos publicados</div>
                            <div class="text-2xl font-bold text-indigo-600">${perfilPublico.cursosCreados}</div>
                        </div>
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                        <div class="p-4 bg-indigo-50 rounded-xl text-center">
                            <div class="text-sm text-indigo-700">Matches activos</div>
                            <div class="text-xl font-bold text-indigo-900">${perfilPublico.matchesActivos}</div>
                        </div>
                        <div class="p-4 bg-amber-50 rounded-xl text-center">
                            <div class="text-sm text-amber-700">Calificación promedio</div>
                            <div class="text-xl font-bold text-amber-900">${perfilPublico.calificacionPromedio}</div>
                        </div>
                        <div class="p-4 bg-green-50 rounded-xl text-center">
                            <div class="text-sm text-green-700">Reseñas visibles</div>
                            <div class="text-xl font-bold text-green-900">${fn:length(perfilPublico.primerasResenas)}</div>
                        </div>
                    </div>

                    <div>
                        <h2 class="text-lg font-bold text-gray-900 mb-3">Primeras reseñas</h2>
                        <c:if test="${empty perfilPublico.primerasResenas}">
                            <p class="text-gray-500">No hay reseñas públicas aún.</p>
                        </c:if>
                        <c:forEach var="comentario" items="${perfilPublico.primerasResenas}">
                            <div class="mb-4 p-4 border border-gray-100 rounded-lg bg-white">
                                <p class="text-gray-800">${comentario}</p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-20">
                    <p class="text-gray-500">Perfil no encontrado.</p>
                    <a href="${pageContext.request.contextPath}/explore" class="mt-4 inline-flex items-center px-4 py-2 bg-indigo-600 text-white rounded-lg">Volver a explorar</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%@ include file="../fragments/mobile-bottom-nav.jspf" %>
</body>
</html>

