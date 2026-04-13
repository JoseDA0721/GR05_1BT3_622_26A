<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Explorar Cursos - Red Saberes</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap');

        * {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }

        .line-clamp-2 {
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }

        .backdrop-blur-md {
            backdrop-filter: blur(12px);
        }

        .heart-filled {
            fill: currentColor;
        }

        @keyframes heartBeat {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.1); }
        }

        .heart-liked {
            animation: heartBeat 0.3s ease-in-out;
        }
    </style>
</head>
<body class="min-h-screen bg-gray-50">
<!-- Navigation -->
<nav class="bg-white/80 backdrop-blur-md border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
            <div class="flex items-center gap-8">
                <a href="${pageContext.request.contextPath}/dashboard" class="flex items-center gap-2">
                    <div class="w-10 h-10 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-xl flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                        </svg>
                    </div>
                    <span class="text-2xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                            Red Saberes
                        </span>
                </a>
                <div class="hidden md:flex items-center gap-6">
                    <a href="${pageContext.request.contextPath}/dashboard" class="text-gray-700 hover:text-indigo-600 transition-colors">
                        Dashboard
                    </a>
                    <a href="${pageContext.request.contextPath}/explore" class="text-indigo-600 font-semibold border-b-2 border-indigo-600 pb-1">
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
            <div class="flex items-center gap-4">
                <div class="flex items-center gap-3">
                    <div class="w-10 h-10 bg-gradient-to-br from-indigo-500 to-purple-500 rounded-full flex items-center justify-center text-white font-semibold">
                        ${sessionScope.usuario.nombre.substring(0,1)}
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="p-2 text-gray-600 hover:text-red-600 transition-colors" title="Cerrar sesión">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                </a>
            </div>
        </div>
    </div>
</nav>

<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <!-- Header -->
    <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Explorar Cursos</h1>
        <p class="text-gray-600">Descubre cursos que te interesen y da "Like" para expresar tu interés</p>
    </div>

    <!-- Search and Filter -->
    <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8">
        <form method="GET" action="${pageContext.request.contextPath}/explore" class="flex flex-col md:flex-row gap-4">
            <div class="flex-1 relative">
                <svg xmlns="http://www.w3.org/2000/svg" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
                <input
                        type="text"
                        name="search"
                        placeholder="Buscar cursos..."
                        value="${param.search}"
                        class="w-full pl-11 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                />
            </div>
            <div class="flex items-center gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                </svg>
                <select
                        name="category"
                        class="px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                        onchange="this.form.submit()"
                >
                    <option value="Todas" ${param.category == 'Todas' || empty param.category ? 'selected' : ''}>Todas</option>
                    <option value="Programación" ${param.category == 'Programación' ? 'selected' : ''}>Programación</option>
                    <option value="Diseño" ${param.category == 'Diseño' ? 'selected' : ''}>Diseño</option>
                    <option value="Data Science" ${param.category == 'Data Science' ? 'selected' : ''}>Data Science</option>
                    <option value="Marketing" ${param.category == 'Marketing' ? 'selected' : ''}>Marketing</option>
                    <option value="Arte" ${param.category == 'Arte' ? 'selected' : ''}>Arte</option>
                    <option value="Finanzas" ${param.category == 'Finanzas' ? 'selected' : ''}>Finanzas</option>
                </select>
            </div>
        </form>
    </div>

    <!-- Courses Grid -->
    <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        <c:forEach var="course" items="${courses}">
            <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-lg transition-shadow">
                <div class="relative">
                    <img
                            src="${course.image}"
                            alt="${course.title}"
                            class="w-full h-48 object-cover"
                            onerror="this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=400&h=250&fit=crop'"
                    />
                    <button
                            onclick="toggleLike(${course.id}, this)"
                            class="heart-btn absolute top-4 right-4 w-10 h-10 rounded-full flex items-center justify-center transition-all ${course.liked ? 'bg-pink-500 text-white scale-110' : 'bg-white/90 text-gray-600 hover:bg-pink-500 hover:text-white'}"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 ${course.liked ? 'heart-filled' : ''}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                        </svg>
                    </button>
                </div>
                <div class="p-6">
                    <div class="flex items-center gap-2 mb-3">
                            <span class="px-3 py-1 bg-indigo-100 text-indigo-700 text-xs font-semibold rounded-full">
                                    ${course.category}
                            </span>
                        <span class="px-3 py-1 bg-gray-100 text-gray-700 text-xs font-semibold rounded-full">
                                ${course.level}
                        </span>
                    </div>
                    <h3 class="text-lg font-bold text-gray-900 mb-2">${course.title}</h3>
                    <p class="text-sm text-gray-600 mb-4 line-clamp-2">${course.description}</p>
                    <div class="flex items-center justify-between">
                        <div class="flex items-center gap-2">
                            <div class="w-8 h-8 bg-gradient-to-br from-indigo-500 to-purple-500 rounded-full flex items-center justify-center text-white text-xs font-semibold">
                                    ${course.author.substring(0,1)}
                            </div>
                            <span class="text-sm text-gray-600">${course.author}</span>
                        </div>
                        <a
                                href="${pageContext.request.contextPath}/course-material?id=${course.id}"
                                class="text-sm text-indigo-600 hover:text-indigo-700 font-semibold"
                        >
                            Ver más →
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty courses}">
            <div class="md:col-span-2 lg:col-span-3 text-center py-12 bg-white rounded-xl border border-gray-100">
                <p class="text-gray-500 text-lg">No se encuentran cursos.</p>
            </div>
        </c:if>
    </div>
</div>

<script>
    function toggleLike(courseId, button) {
        const isLiked = button.classList.contains('bg-pink-500');
        if (isLiked) {
            return;
        }

        button.classList.remove('bg-white/90', 'text-gray-600');
        button.classList.add('bg-pink-500', 'text-white', 'scale-110', 'heart-liked');

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '${pageContext.request.contextPath}/like-course';

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'cursoId';
        input.value = courseId;
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();
    }
</script>
</body>
</html>