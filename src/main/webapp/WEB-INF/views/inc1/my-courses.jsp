<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Cursos - SkillSwap</title>

    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Lucide Icons -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <style>
        /* Gradientes personalizados */
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

        /* Animación de fade in */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .fade-in {
            animation: fadeIn 0.5s ease-out;
        }

        /* Hover scale */
        .hover-scale {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .hover-scale:hover {
            transform: translateY(-5px);
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
        }

        /* Backdrop blur */
        .backdrop-blur-md {
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
        }

        /* Line clamp */
        .line-clamp-2 {
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<!-- Navigation Header -->
<nav class="bg-white/80 backdrop-blur-md border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">

            <!-- Logo y Navegación -->            <div class="flex items-center gap-8">
                <a href="${pageContext.request.contextPath}/dashboard" class="flex items-center gap-2">
                    <div class="w-10 h-10 bg-gradient-brand rounded-xl flex items-center justify-center">
                        <i data-lucide="book-open" class="w-6 h-6 text-white"></i>
                    </div>
                    <span class="text-2xl font-bold text-gradient-brand">
                            Red Saberes
                        </span>
                </a>

                <!-- Desktop Navigation -->
                <div class="hidden md:flex items-center gap-6">
                    <a href="${pageContext.request.contextPath}/dashboard" class="text-gray-700 hover:text-indigo-600 transition-colors">
                        Dashboard
                    </a>
                    <a href="${pageContext.request.contextPath}/explore" class="text-gray-700 hover:text-indigo-600 transition-colors">
                        Explorar
                    </a>
                    <a href="${pageContext.request.contextPath}/my-courses" class="text-indigo-600 font-semibold border-b-2 border-indigo-600 pb-1">
                        Mis Cursos
                    </a>
                    <a href="${pageContext.request.contextPath}/matches" class="text-gray-700 hover:text-indigo-600 transition-colors">
                        Matches
                    </a>
                </div>
            </div>

            <!-- User Profile & Logout -->
            <div class="flex items-center gap-4">
                <div class="flex items-center gap-3">
                    <div class="w-10 h-10 bg-gradient-brand rounded-full flex items-center justify-center text-white font-semibold">
                        ${sessionScope.usuario.nombre.substring(0, 1).toUpperCase()}
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/logout"
                   class="p-2 text-gray-600 hover:text-red-600 transition-colors"
                   title="Cerrar sesión">                    <i data-lucide="log-out" class="w-5 h-5"></i>
                </a>
            </div>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

    <!-- Header -->
    <div class="flex items-center justify-between mb-8 fade-in">
        <div>
            <h1 class="text-3xl font-bold text-gray-900 mb-2">Mis Cursos</h1>
            <p class="text-gray-600">Gestiona tus cursos creados</p>
        </div>
        <a href="${pageContext.request.contextPath}/create-course"
           class="flex items-center gap-2 px-6 py-3 bg-gradient-brand text-white rounded-lg hover:shadow-lg transition">
            <i data-lucide="plus-circle" class="w-5 h-5"></i>
            <span>Crear Curso</span>
        </a>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty param.success}">
        <div class="mb-6 p-4 bg-green-50 border border-green-200 text-green-700 rounded-lg flex items-center gap-2 fade-in">
            <i data-lucide="check-circle-2" class="w-5 h-5 flex-shrink-0"></i>
            <p class="text-sm">
                <c:choose>
                    <c:when test="${param.success == 'created'}">¡Curso creado exitosamente!</c:when>
                    <c:when test="${param.success == 'published'}">¡Curso publicado exitosamente!</c:when>
                    <c:when test="${param.success == 'archived'}">Curso archivado correctamente.</c:when>
                    <c:when test="${param.success == 'deleted'}">Curso eliminado correctamente.</c:when>
                    <c:otherwise>Operación exitosa.</c:otherwise>                </c:choose>
            </p>
        </div>
    </c:if>

    <!-- Courses Grid -->
    <c:choose>
        <c:when test="${not empty myCourses}">
            <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                <c:forEach var="course" items="${myCourses}" varStatus="status">
                    <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden hover-scale fade-in"
                         style="animation-delay: ${status.index * 0.1}s">

                        <!-- Course Image -->
                        <img src="${course.imagenPortada}"
                             alt="${course.titulo}"
                             class="w-full h-48 object-cover"
                             onerror="this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=400&h=250&fit=crop'" />

                        <!-- Course Content -->
                        <div class="p-6">

                            <!-- Status Badge -->
                            <div class="mb-3">
                                <c:choose>
                                    <c:when test="${course.estado == 'PUBLICO'}">
                                            <span class="inline-block px-3 py-1 bg-green-100 text-green-700 text-xs font-semibold rounded-full">
                                                Publicado
                                            </span>
                                    </c:when>
                                    <c:when test="${course.estado == 'BORRADOR'}">
                                            <span class="inline-block px-3 py-1 bg-yellow-100 text-yellow-700 text-xs font-semibold rounded-full">
                                                Borrador
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                            <span class="inline-block px-3 py-1 bg-gray-100 text-gray-700 text-xs font-semibold rounded-full">
                                                Archivado
                                            </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <!-- Title -->
                            <h3 class="text-lg font-bold text-gray-900 mb-4 line-clamp-2">
                                    ${course.titulo}
                            </h3>

                            <!-- Stats Grid -->
                            <div class="grid grid-cols-3 gap-4 mb-4 text-sm">
                                <div class="text-center">
                                    <div class="text-gray-500">Módulos</div>                                    <div class="font-semibold text-gray-900">${course.modulosCount}</div>
                                </div>
                                <div class="text-center">
                                    <div class="text-gray-500">Likes</div>
                                    <div class="font-semibold text-gray-900">${course.likesCount}</div>
                                </div>
                                <div class="text-center">
                                    <div class="text-gray-500">Inscritos</div>
                                    <div class="font-semibold text-gray-900">${course.inscritosCount}</div>
                                </div>
                            </div>

                            <!-- Action Buttons -->
                            <div class="flex gap-2">
                                <a href="${pageContext.request.contextPath}/edit-course?id=${course.id}"
                                   class="flex-1 flex items-center justify-center gap-2 px-4 py-2 bg-indigo-50 text-indigo-600 rounded-lg hover:bg-indigo-100 transition">
                                    <i data-lucide="edit" class="w-4 h-4"></i>
                                    <span class="text-sm font-semibold">Editar</span>
                                </a>

                                <a href="${pageContext.request.contextPath}/view-course?id=${course.id}"
                                   class="flex items-center justify-center w-10 h-10 bg-gray-50 text-gray-600 rounded-lg hover:bg-gray-100 transition"
                                   title="Ver curso">
                                    <i data-lucide="eye" class="w-5 h-5"></i>
                                </a>

                                <c:if test="${course.estado == 'PUBLICO'}">
                                    <button onclick="archiveCourse(${course.id})"
                                            class="flex items-center justify-center w-10 h-10 bg-gray-50 text-gray-600 rounded-lg hover:bg-gray-100 transition"
                                            title="Archivar curso">
                                        <i data-lucide="archive" class="w-5 h-5"></i>
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>

        <c:otherwise>
            <!-- Empty State -->
            <div class="text-center py-20 bg-white rounded-2xl border-2 border-dashed border-gray-300 fade-in">
                <i data-lucide="book-open" class="w-20 h-20 text-gray-400 mx-auto mb-6"></i>
                <h3 class="text-2xl font-bold text-gray-900 mb-3">No tienes cursos creados</h3>
                <p class="text-gray-600 mb-8 max-w-md mx-auto">
                    Comienza a compartir tu conocimiento creando tu primer curso.
                    Es fácil y rápido.
                </p>
                <a href="${pageContext.request.contextPath}/create-course"
                   class="inline-flex items-center gap-2 px-8 py-4 bg-gradient-brand text-white rounded-lg font-semibold hover:shadow-xl transition text-lg">
                    <i data-lucide="plus-circle" class="w-6 h-6"></i>
                    Crear Mi Primer Curso
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Mobile Navigation (Bottom) -->
<nav class="md:hidden fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 px-4 py-2 z-50">
    <div class="flex items-center justify-around">
        <a href="${pageContext.request.contextPath}/dashboard" class="flex flex-col items-center gap-1 text-gray-600">
            <i data-lucide="layout-dashboard" class="w-6 h-6"></i>
            <span class="text-xs">Dashboard</span>
        </a>
        <a href="${pageContext.request.contextPath}/explore" class="flex flex-col items-center gap-1 text-gray-600">
            <i data-lucide="search" class="w-6 h-6"></i>
            <span class="text-xs">Explorar</span>
        </a>
        <a href="${pageContext.request.contextPath}/my-courses" class="flex flex-col items-center gap-1 text-indigo-600">
            <i data-lucide="book-marked" class="w-6 h-6"></i>
            <span class="text-xs font-semibold">Cursos</span>
        </a>
        <a href="${pageContext.request.contextPath}/matches" class="flex flex-col items-center gap-1 text-gray-600">
            <i data-lucide="users" class="w-6 h-6"></i>
            <span class="text-xs">Matches</span>
        </a>
    </div>
</nav>

<!-- Scripts -->
<script>
    // Inicializar Lucide Icons
    lucide.createIcons();

    // Funcion para archivar curso
    function archiveCourse(courseId) {
        if (confirm('¿Estás seguro de que deseas archivar este curso? Los estudiantes ya inscritos podrán continuar viéndolo.')) {
            window.location.href = '${pageContext.request.contextPath}/archive-course?id=' + courseId;
        }
    }

    // Refresh icons periodically
    setInterval(() => {
        lucide.createIcons();
    }, 1000);
</script>

</body>
</html>

