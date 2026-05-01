<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Gestión de Matches</title>
    <%@ include file="../fragments/common-head.jspf" %>
    <style>
        /* Tab Content Visibility */
        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
            animation: fadeIn 0.3s ease-out;
        }
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<!-- Navigation Header -->
<%@ include file="../fragments/nav.jspf" %>
<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <!-- Header -->
    <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Gestión de Matches</h1>
        <p class="text-gray-600">Administra tus conexiones de aprendizaje</p>
    </div>

    <!-- Tabs -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 mb-6">
        <div class="flex border-b border-gray-200">
            <button
                    onclick="switchTab('interested')"
                    id="tab-interested"
                    class="tab-button flex-1 px-6 py-4 text-center font-semibold transition text-indigo-600 border-b-2 border-indigo-600"
            >
                <div class="flex items-center justify-center gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                    </svg>
                    <span>Interesados (<span id="interested-count">${interestedCount}</span>)</span>
                </div>
            </button>
            <button
                    onclick="switchTab('matches')"
                    id="tab-matches"
                    class="tab-button flex-1 px-6 py-4 text-center font-semibold transition text-gray-600 hover:text-gray-900"
            >
                <div class="flex items-center justify-center gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    <span>Matches Activos (<span id="matches-count">${matchesCount}</span>)</span>
                </div>
            </button>
        </div>

        <div class="p-6">
            <!-- Interested Tab -->
            <div id="content-interested" class="tab-content active">
                <div class="space-y-4">
                    <c:choose>
                        <c:when test="${empty interested}">
                            <div class="text-center py-12 text-gray-500">
                                <svg xmlns="http://www.w3.org/2000/svg" class="w-12 h-12 mx-auto mb-4 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                </svg>
                                <p>No hay usuarios interesados en tus cursos</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="person" items="${interested}">
                                <div class="flex items-center gap-6 p-6 bg-gray-50 rounded-xl hover:bg-gray-100 transition" id="interested-${person.userId}">
                                    <img
                                            src="https://ui-avatars.com/api/?name=${person.name}&background=6366f1&color=fff"
                                            alt="${person.name}"
                                            class="w-16 h-16 rounded-full object-cover"
                                    />
                                    <div class="flex-1">
                                        <h3 class="font-semibold text-gray-900 mb-1">${person.name}</h3>
                                        <p class="text-sm text-gray-600 mb-2">${person.email}</p>
                                        <div class="flex items-center gap-2 text-sm">
                                            <span class="text-gray-600">Interesado en:</span>
                                            <span class="font-medium text-indigo-600">${person.myCourse}</span>
                                        </div>
                                        <div class="flex items-center gap-2 text-sm">
                                            <span class="text-gray-600">Estado:</span>
                                            <span class="font-medium text-purple-600">Pendiente de confirmacion</span>
                                        </div>
                                    </div>
                                    <div class="flex gap-2">
                                        <button
                                                onclick="acceptMatch(${person.userId}, ${person.myCourseId})"
                                                class="px-6 py-2 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg hover:shadow-lg transition flex items-center gap-2"
                                        >
                                            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                            </svg>
                                            <span>Aceptar Match</span>
                                        </button>
                                        <button
                                                onclick="rejectInterest(${person.userId})"
                                                class="p-2 bg-red-50 text-red-600 rounded-lg hover:bg-red-100 transition"
                                        >
                                            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                                            </svg>
                                        </button>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Matches Tab -->
            <div id="content-matches" class="tab-content">
                <div class="space-y-4">
                    <c:choose>
                        <c:when test="${empty matches}">
                            <div class="text-center py-12 text-gray-500">
                                <svg xmlns="http://www.w3.org/2000/svg" class="w-12 h-12 mx-auto mb-4 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                                </svg>
                                <p>No tienes matches activos todavía</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="match" items="${matches}">
                                <div class="flex items-center gap-6 p-6 bg-gradient-to-r from-indigo-50 to-purple-50 rounded-xl">
                                    <img
                                            src="https://ui-avatars.com/api/?name=${match.name}&background=6366f1&color=fff"
                                            alt="${match.name}"
                                            class="w-16 h-16 rounded-full object-cover"
                                    />
                                    <div class="flex-1">
                                        <h3 class="font-semibold text-gray-900 mb-1">${match.name}</h3>
                                        <p class="text-sm text-gray-600 mb-2">${match.email}</p>
                                        <div class="flex items-center gap-2 text-sm mb-1">
                                            <span class="text-gray-600">Acceso a:</span>
                                            <span class="font-medium text-purple-600">${match.course}</span>
                                        </div>
                                        <div class="flex items-center gap-2 text-sm">
                                            <span class="text-gray-600">Match desde:</span>
                                            <span class="font-medium text-gray-900">${match.matchDate}</span>
                                        </div>
                                    </div>
                                    <a
                                            href="${pageContext.request.contextPath}/course-overview?id=${match.courseId}"
                                            class="px-6 py-2 bg-white text-indigo-600 rounded-lg hover:shadow-lg transition font-semibold"
                                    >
                                        Ver Curso →
                                    </a>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
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

    function switchTab(tabName) {
        // Update tab buttons
        document.querySelectorAll('.tab-button').forEach(btn => {
            btn.classList.remove('text-indigo-600', 'border-b-2', 'border-indigo-600');
            btn.classList.add('text-gray-600', 'hover:text-gray-900');
        });

        document.getElementById('tab-' + tabName).classList.remove('text-gray-600', 'hover:text-gray-900');
        document.getElementById('tab-' + tabName).classList.add('text-indigo-600', 'border-b-2', 'border-indigo-600');

        // Update content
        document.querySelectorAll('.tab-content').forEach(content => {
            content.classList.remove('active');
        });
        document.getElementById('content-' + tabName).classList.add('active');
    }

    function acceptMatch(personId, courseId) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '${pageContext.request.contextPath}/accept-match';

        const courseInput = document.createElement('input');
        courseInput.type = 'hidden';
        courseInput.name = 'cursoId';
        courseInput.value = courseId;
        form.appendChild(courseInput);

        const userInput = document.createElement('input');
        userInput.type = 'hidden';
        userInput.name = 'usuarioObjetivoId';
        userInput.value = personId;
        form.appendChild(userInput);

        document.body.appendChild(form);
        form.submit();
    }

    function rejectInterest(personId) {
        if (confirm('¿Estás seguro de que quieres rechazar este interés?')) {
            const row = document.getElementById('interested-' + personId);
            if (row) {
                row.remove();
                const count = parseInt(document.getElementById('interested-count').textContent) - 1;
                document.getElementById('interested-count').textContent = Math.max(0, count);
            }
        }
    }
</script>
</body>
</html>