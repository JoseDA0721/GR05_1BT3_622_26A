<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>

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

        /* Animación de slide in */
        @keyframes slideIn {
            from { opacity: 0; transform: translateX(-20px); }
            to { opacity: 1; transform: translateX(0); }
        }

        .slide-in {
            animation: slideIn 0.6s ease-out;
        }

        /* Hover scale */
        .hover-scale {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .hover-scale:hover {
            transform: translateY(-5px);
        }

        /* Backdrop blur support */
        .backdrop-blur-md {
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
        }
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<!-- Navigation Header -->
<nav class="bg-white/80 backdrop-blur-md border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">

            <!-- Logo y Navegación -->
            <div class="flex items-center gap-8">
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
                    <a href="${pageContext.request.contextPath}/dashboard" class="text-indigo-600 font-semibold border-b-2 border-indigo-600 pb-1">
                        Dashboard
                    </a>
                    <a href="${pageContext.request.contextPath}/explore" class="text-gray-700 hover:text-indigo-600 transition-colors">
                        Explorar
                    </a>
                    <a href="${pageContext.request.contextPath}/my-courses" class="text-gray-700 hover:text-indigo-600 transition-colors">
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
                    <div class="hidden md:block">
                        <div class="text-sm font-semibold text-gray-900">${sessionScope.usuario.nombre}</div>
                        <div class="text-xs text-gray-500">${sessionScope.usuario.correoElectronico}</div>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/logout"
                   class="p-2 text-gray-600 hover:text-red-600 transition-colors"
                   title="Cerrar sesión">
                    <i data-lucide="log-out" class="w-5 h-5"></i>
                </a>
            </div>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

    <!-- Welcome Section -->
    <div class="mb-8 slide-in">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">
            ¡Bienvenido, ${sessionScope.usuario.nombre}!
        </h1>
        <p class="text-gray-600">Aquí está tu resumen de actividad</p>
    </div>

    <!-- Stats Cards -->
    <div class="grid md:grid-cols-4 gap-6 mb-8">

        <!-- Cursos Creados -->
        <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 fade-in hover-scale">
            <div class="flex items-center gap-4">
                <div class="w-12 h-12 bg-indigo-100 rounded-lg flex items-center justify-center">
                    <i data-lucide="book-marked" class="w-6 h-6 text-indigo-600"></i>
                </div>
                <div>
                    <div class="counter text-2xl font-bold text-gray-900">
                        ${dashboardStats.coursesCreated}
                    </div>
                    <div class="text-sm text-gray-600">Cursos Creados</div>
                </div>
            </div>
        </div>

        <!-- Likes Recibidos -->
        <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 fade-in hover-scale" style="animation-delay: 0.1s">
            <div class="flex items-center gap-4">
                <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                    <i data-lucide="heart" class="w-6 h-6 text-purple-600"></i>
                </div>
                <div>
                    <div class="counter text-2xl font-bold text-gray-900">
                        ${dashboardStats.likesReceived}
                    </div>
                    <div class="text-sm text-gray-600">Likes Recibidos</div>
                </div>
            </div>
        </div>

        <!-- Matches Activos -->
        <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 fade-in hover-scale" style="animation-delay: 0.2s">
            <div class="flex items-center gap-4">
                <div class="w-12 h-12 bg-pink-100 rounded-lg flex items-center justify-center">
                    <i data-lucide="users" class="w-6 h-6 text-pink-600"></i>
                </div>
                <div>
                    <div class="counter text-2xl font-bold text-gray-900">
                        ${dashboardStats.activeMatches}
                    </div>
                    <div class="text-sm text-gray-600">Matches Activos</div>
                </div>
            </div>
        </div>

        <!-- Cursos Inscritos -->
        <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 fade-in hover-scale" style="animation-delay: 0.3s">
            <div class="flex items-center gap-4">
                <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                    <i data-lucide="trending-up" class="w-6 h-6 text-green-600"></i>
                </div>
                <div>
                    <div class="counter text-2xl font-bold text-gray-900">
                        ${dashboardStats.enrolledCourses}
                    </div>
                    <div class="text-sm text-gray-600">Cursos Inscritos</div>
                </div>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="grid md:grid-cols-3 gap-6 mb-8">

        <!-- Crear Nuevo Curso -->
        <a href="${pageContext.request.contextPath}/create-course"
           class="bg-gradient-brand p-8 rounded-2xl text-white hover:shadow-xl transition-shadow hover-scale">
            <i data-lucide="plus-circle" class="w-10 h-10 mb-4"></i>
            <h3 class="text-xl font-bold mb-2">Crear Nuevo Curso</h3>
            <p class="opacity-90">Comparte tu conocimiento con la comunidad</p>
        </a>

        <!-- Explorar Cursos -->
        <a href="${pageContext.request.contextPath}/explore"
           class="bg-white p-8 rounded-2xl border-2 border-gray-200 hover:border-indigo-600 hover:shadow-lg transition-all hover-scale">
            <i data-lucide="search" class="w-10 h-10 text-indigo-600 mb-4"></i>
            <h3 class="text-xl font-bold text-gray-900 mb-2">Explorar Cursos</h3>
            <p class="text-gray-600">Encuentra cursos que te interesen</p>
        </a>

        <!-- Ver Matches -->
        <a href="${pageContext.request.contextPath}/matches"
           class="bg-white p-8 rounded-2xl border-2 border-gray-200 hover:border-purple-600 hover:shadow-lg transition-all hover-scale">
            <i data-lucide="users" class="w-10 h-10 text-purple-600 mb-4"></i>
            <h3 class="text-xl font-bold text-gray-900 mb-2">Ver Matches</h3>
            <p class="text-gray-600">Gestiona tus conexiones de aprendizaje</p>
        </a>
    </div>

    <!-- My Courses Section -->
    <div>
        <div class="flex items-center justify-between mb-6">
            <h2 class="text-2xl font-bold text-gray-900">Mis Cursos</h2>
            <a href="${pageContext.request.contextPath}/my-courses" class="text-indigo-600 hover:text-indigo-700 font-semibold transition">
                Ver todos →
            </a>
        </div>

        <!-- Cursos Grid -->
        <c:choose>
            <c:when test="${not empty userCourses}">
                <div class="grid md:grid-cols-3 gap-6">
                    <c:forEach var="course" items="${userCourses}" varStatus="status">
                        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-lg transition-shadow hover-scale fade-in"
                             style="animation-delay: ${status.index * 0.1}s">

                            <!-- Course Image -->
                            <img src="${course.imageUrl}"
                                 alt="${course.title}"
                                 class="w-full h-40 object-cover"
                                 onerror="this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=400&h=250&fit=crop'" />

                            <!-- Course Info -->
                            <div class="p-6">
                                <div class="inline-block px-3 py-1 bg-green-100 text-green-700 text-xs font-semibold rounded-full mb-3">
                                        ${course.status}
                                </div>

                                <h3 class="text-lg font-bold text-gray-900 mb-2 line-clamp-2">
                                        ${course.title}
                                </h3>

                                <p class="text-sm text-gray-600 mb-4">
                                    por ${course.authorName}
                                </p>

                                <!-- Stats -->
                                <div class="flex items-center justify-between text-sm text-gray-500">
                                    <div class="flex items-center gap-1">
                                        <i data-lucide="heart" class="w-4 h-4"></i>
                                        <span>${course.likesCount} likes</span>
                                    </div>
                                    <div class="flex items-center gap-1">
                                        <i data-lucide="users" class="w-4 h-4"></i>
                                        <span>${course.matchesCount} matches</span>
                                    </div>
                                </div>

                                <!-- Action Buttons -->
                                <div class="mt-4 flex gap-2">
                                    <a href="${pageContext.request.contextPath}/edit-course?id=${course.id}"
                                       class="flex-1 text-center py-2 px-4 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition text-sm font-semibold">
                                        Editar
                                    </a>
                                    <a href="${pageContext.request.contextPath}/view-course?id=${course.id}"
                                       class="flex-1 text-center py-2 px-4 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition text-sm font-semibold">
                                        Ver
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>

            <c:otherwise>
                <!-- Empty State -->
                <div class="text-center py-16 bg-white rounded-2xl border-2 border-dashed border-gray-300">
                    <i data-lucide="book-open" class="w-16 h-16 text-gray-400 mx-auto mb-4"></i>
                    <h3 class="text-xl font-bold text-gray-900 mb-2">No tienes cursos creados</h3>
                    <p class="text-gray-600 mb-6">¡Crea tu primer curso y comparte tu conocimiento!</p>
                    <a href="${pageContext.request.contextPath}/create-course"
                       class="inline-flex items-center gap-2 px-6 py-3 bg-gradient-brand text-white rounded-lg font-semibold hover:shadow-lg transition">
                        <i data-lucide="plus-circle" class="w-5 h-5"></i>
                        Crear Curso
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Recent Activity (Optional) -->
    <div class="mt-8">
        <h2 class="text-2xl font-bold text-gray-900 mb-6">Actividad Reciente</h2>

        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <c:choose>
                <c:when test="${not empty recentActivity}">
                    <div class="divide-y divide-gray-100">
                        <c:forEach var="activity" items="${recentActivity}">
                            <div class="p-4 hover:bg-gray-50 transition">
                                <div class="flex items-start gap-4">
                                    <div class="w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center flex-shrink-0">
                                        <i data-lucide="${activity.icon}" class="w-5 h-5 text-indigo-600"></i>
                                    </div>
                                    <div class="flex-1">
                                        <p class="text-sm text-gray-900">
                                            <strong>${activity.userName}</strong> ${activity.action}
                                        </p>
                                        <p class="text-xs text-gray-500 mt-1">
                                            <fmt:formatDate value="${activity.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="p-8 text-center text-gray-500">
                        <i data-lucide="activity" class="w-12 h-12 text-gray-300 mx-auto mb-3"></i>
                        <p>No hay actividad reciente</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Mobile Navigation (Bottom) -->
<nav class="md:hidden fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 px-4 py-2 z-50">
    <div class="flex items-center justify-around">
        <a href="${pageContext.request.contextPath}/dashboard" class="flex flex-col items-center gap-1 text-indigo-600">
            <i data-lucide="layout-dashboard" class="w-6 h-6"></i>
            <span class="text-xs font-semibold">Dashboard</span>
        </a>
        <a href="${pageContext.request.contextPath}/explore" class="flex flex-col items-center gap-1 text-gray-600">
            <i data-lucide="search" class="w-6 h-6"></i>
            <span class="text-xs">Explorar</span>
        </a>
        <a href="${pageContext.request.contextPath}/my-courses" class="flex flex-col items-center gap-1 text-gray-600">
            <i data-lucide="book-marked" class="w-6 h-6"></i>
            <span class="text-xs">Cursos</span>
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

    // Animación de números contadores
    document.addEventListener('DOMContentLoaded', function() {
        const counters = document.querySelectorAll('.counter');

        counters.forEach(counter => {
            const target = parseInt(counter.innerText);
            if (isNaN(target)) return; // Saltar si no es un número

            const duration = 1000; // 1 segundo
            const step = target / (duration / 16); // 60fps
            let current = 0;

            const updateCounter = () => {
                current += step;
                if (current < target) {
                    counter.innerText = Math.ceil(current);
                    requestAnimationFrame(updateCounter);
                } else {
                    counter.innerText = target;
                }
            };

            updateCounter();
        });
    });

    // Refresh icons after any dynamic content change
    setInterval(() => {
        lucide.createIcons();
    }, 1000);
</script>

</body>
</html>
