<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Matches</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="min-h-screen bg-gray-50">
<!-- Navigation -->
<nav class="bg-white border-b border-gray-200 sticky top-0">
    <div class="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
        <div class="flex items-center gap-6">
            <a href="${pageContext.request.contextPath}/dashboard" class="text-gray-700 hover:text-indigo-600">Dashboard</a>
            <a href="${pageContext.request.contextPath}/explore" class="text-gray-700 hover:text-indigo-600">Explorar</a>
            <a href="${pageContext.request.contextPath}/my-courses" class="text-gray-700 hover:text-indigo-600">Mis Cursos</a>
            <a href="${pageContext.request.contextPath}/matches" class="text-indigo-600 font-semibold">Matches</a>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="text-gray-600 hover:text-red-600">Salir</a>
    </div>
</nav>

<div class="max-w-7xl mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-gray-900 mb-2">Gestión de Matches</h1>
    <p class="text-gray-600 mb-6">Acepta likes en tus cursos para habilitar acceso a materiales.</p>

    <div class="bg-white border border-gray-200 rounded-xl p-4 mb-6">
        <form method="GET" action="${pageContext.request.contextPath}/matches" class="flex flex-col md:flex-row gap-3">
            <select name="cursoId" class="px-4 py-2 border border-gray-300 rounded-lg">
                <option value="">Todos mis cursos</option>
                <c:forEach var="course" items="${myCourses}">
                    <option value="${course.id}" ${selectedCourseId == course.id ? 'selected' : ''}>${course.titulo}</option>
                </c:forEach>
            </select>
            <button type="submit" class="px-5 py-2 bg-indigo-600 text-white rounded-lg">Filtrar</button>
        </form>
    </div>

    <div class="grid lg:grid-cols-2 gap-6">
        <section class="bg-white border border-gray-200 rounded-xl p-5">
            <h2 class="text-xl font-bold text-gray-900 mb-1">Interesados (${interestedCount})</h2>
            <p class="text-sm text-gray-500 mb-4">Usuarios que dieron like y todavía no tienen match.</p>

            <c:choose>
                <c:when test="${empty interested}">
                    <p class="text-gray-500">No hay likes pendientes para aplicar match.</p>
                </c:when>
                <c:otherwise>
                    <div class="space-y-3">
                        <c:forEach var="person" items="${interested}">
                            <div class="border border-gray-200 rounded-lg p-4">
                                <p class="font-semibold text-gray-900">${person.name}</p>
                                <p class="text-sm text-gray-600">${person.email}</p>
                                <p class="text-sm text-indigo-600 mt-1">Curso: ${person.myCourse}</p>

                                <form method="POST" action="${pageContext.request.contextPath}/accept-match" class="mt-3">
                                    <input type="hidden" name="cursoId" value="${person.myCourseId}">
                                    <input type="hidden" name="usuarioObjetivoId" value="${person.userId}">
                                    <button type="submit" class="px-4 py-2 bg-indigo-600 text-white rounded-lg text-sm font-semibold">Aplicar Match</button>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

        <section class="bg-white border border-gray-200 rounded-xl p-5">
            <h2 class="text-xl font-bold text-gray-900 mb-1">Matches Activos (${matchesCount})</h2>
            <p class="text-sm text-gray-500 mb-4">Accesos ya habilitados por match.</p>

            <c:choose>
                <c:when test="${empty matches}">
                    <p class="text-gray-500">Aun no tienes matches activos.</p>
                </c:when>
                <c:otherwise>
                    <div class="space-y-3">
                        <c:forEach var="match" items="${matches}">
                            <div class="border border-gray-200 rounded-lg p-4 bg-indigo-50">
                                <p class="font-semibold text-gray-900">${match.name}</p>
                                <p class="text-sm text-gray-600">${match.email}</p>
                                <p class="text-sm text-indigo-700 mt-1">Curso: ${match.course}</p>
                                <a href="${pageContext.request.contextPath}/course-material?id=${match.courseId}" class="inline-block mt-3 text-sm font-semibold text-indigo-700 hover:text-indigo-900">Ver material</a>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </div>
</div>
</body>
</html>