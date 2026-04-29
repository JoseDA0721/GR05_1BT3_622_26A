<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Explorar Cursos</title>
    <%@ include file="../fragments/common-head.jspf" %>
    <!-- Red Saberes Common Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/redsaberes-common.css?v=1.0">
</head>
<body class="bg-gradient-main min-h-screen">

<!-- Navigation Header -->
<%@ include file="../fragments/nav.jspf" %>

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
<!-- Mobile Navigation (Bottom) -->
<%@ include file="../fragments/mobile-bottom-nav.jspf" %>

<script>
    // Inicializar Lucide Icons
    lucide.createIcons();
    // Refresh icons after any dynamic content change
    setInterval(() => {
        lucide.createIcons();
    }, 1000);

    function toggleLike(courseId, button) {
        const isLiked = button.classList.contains('bg-pink-500');

        if (isLiked) {
            // Desmarcar like
            button.classList.remove('bg-pink-500', 'text-white', 'scale-110', 'heart-liked');
            button.classList.add('bg-white/90', 'text-gray-600');
        } else {
            // Marcar like
            button.classList.remove('bg-white/90', 'text-gray-600');
            button.classList.add('bg-pink-500', 'text-white', 'scale-110', 'heart-liked');
        }

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