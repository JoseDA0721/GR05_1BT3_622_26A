<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Explorar Cursos</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="min-h-screen bg-gray-50">
<!-- Navigation -->
<nav class="bg-white border-b border-gray-200 sticky top-0">
    <div class="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
        <div class="flex items-center gap-6">
            <a href="${pageContext.request.contextPath}/dashboard" class="text-gray-700 hover:text-indigo-600">Dashboard</a>
            <a href="${pageContext.request.contextPath}/explore" class="text-indigo-600 font-semibold">Explorar</a>
            <a href="${pageContext.request.contextPath}/my-courses" class="text-gray-700 hover:text-indigo-600">Mis Cursos</a>
            <a href="${pageContext.request.contextPath}/matches" class="text-gray-700 hover:text-indigo-600">Matches</a>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="text-gray-600 hover:text-red-600">Salir</a>
    </div>
</nav>

<div class="max-w-7xl mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-gray-900 mb-2">Explorar Cursos</h1>
    <p class="text-gray-600 mb-6">Da like a los cursos y espera el match del creador para acceder a materiales.</p>

    <form method="GET" action="${pageContext.request.contextPath}/explore" class="bg-white border border-gray-200 rounded-xl p-4 mb-6 flex flex-col md:flex-row gap-3">
        <input type="text" name="search" value="${param.search}" placeholder="Buscar cursos" class="flex-1 px-4 py-2 border border-gray-300 rounded-lg" />
        <select name="category" class="px-4 py-2 border border-gray-300 rounded-lg">
            <option value="Todas" ${param.category == 'Todas' || empty param.category ? 'selected' : ''}>Todas</option>
            <option value="Programación" ${param.category == 'Programación' ? 'selected' : ''}>Programación</option>
            <option value="Diseño" ${param.category == 'Diseño' ? 'selected' : ''}>Diseño</option>
            <option value="Data Science" ${param.category == 'Data Science' ? 'selected' : ''}>Data Science</option>
            <option value="Marketing" ${param.category == 'Marketing' ? 'selected' : ''}>Marketing</option>
            <option value="Arte" ${param.category == 'Arte' ? 'selected' : ''}>Arte</option>
            <option value="Finanzas" ${param.category == 'Finanzas' ? 'selected' : ''}>Finanzas</option>
        </select>
        <button type="submit" class="px-5 py-2 bg-indigo-600 text-white rounded-lg">Filtrar</button>
    </form>

    <c:choose>
        <c:when test="${empty courses}">
            <div class="bg-white border border-dashed border-gray-300 rounded-xl p-8 text-gray-500">
                No hay cursos disponibles con esos filtros.
            </div>
        </c:when>
        <c:otherwise>
            <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                <c:forEach var="course" items="${courses}">
                    <div class="bg-white border border-gray-200 rounded-xl overflow-hidden">
                        <img src="${course.image}" alt="${course.title}" class="w-full h-44 object-cover"
                             onerror="this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=400&h=250&fit=crop'" />
                        <div class="p-5">
                            <div class="flex gap-2 mb-2">
                                <span class="px-2 py-1 text-xs rounded bg-indigo-100 text-indigo-700">${course.category}</span>
                                <span class="px-2 py-1 text-xs rounded bg-gray-100 text-gray-700">${course.level}</span>
                            </div>
                            <h2 class="font-bold text-gray-900 mb-2">${course.title}</h2>
                            <p class="text-sm text-gray-600 mb-3">${course.description}</p>
                            <p class="text-sm text-gray-500 mb-4">Creador: ${course.author}</p>

                            <div class="flex gap-2">
                                <c:choose>
                                    <c:when test="${course.liked}">
                                        <button class="px-4 py-2 rounded-lg bg-pink-100 text-pink-700 text-sm font-semibold" disabled>Like enviado</button>
                                    </c:when>
                                    <c:otherwise>
                                        <form method="POST" action="${pageContext.request.contextPath}/like-course">
                                            <input type="hidden" name="cursoId" value="${course.id}">
                                            <button type="submit" class="px-4 py-2 rounded-lg bg-pink-500 text-white text-sm font-semibold">Dar Like</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>

                                <a href="${pageContext.request.contextPath}/course-material?id=${course.id}"
                                   class="px-4 py-2 rounded-lg bg-indigo-600 text-white text-sm font-semibold">
                                    Ver Material
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>