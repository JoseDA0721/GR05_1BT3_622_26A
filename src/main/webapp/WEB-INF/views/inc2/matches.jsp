<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Matches - SkillSwap</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap');

        * {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }

        .backdrop-blur-md {
            backdrop-filter: blur(12px);
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
        }
    </style>
</head>
<body class="min-h-screen bg-gray-50">
<!-- Navigation -->
<nav class="bg-white/80 backdrop-blur-md border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
            <div class="flex items-center gap-8">
                <a href="/dashboard" class="flex items-center gap-2">
                    <div class="w-10 h-10 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-xl flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                        </svg>
                    </div>
                    <span class="text-2xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                            SkillSwap
                        </span>
                </a>
                <div class="hidden md:flex items-center gap-6">
                    <a href="/dashboard" class="text-gray-700 hover:text-indigo-600 transition-colors">
                        Dashboard
                    </a>
                    <a href="/explore" class="text-gray-700 hover:text-indigo-600 transition-colors">
                        Explorar
                    </a>
                    <a href="/my-courses" class="text-gray-700 hover:text-indigo-600 transition-colors">
                        Mis Cursos
                    </a>
                    <a href="/matches" class="text-indigo-600 font-semibold border-b-2 border-indigo-600 pb-1">
                        Matches
                    </a>
                </div>
            </div>
            <div class="flex items-center gap-4">
                <div class="flex items-center gap-3">
                    <div class="w-10 h-10 bg-gradient-to-br from-indigo-500 to-purple-500 rounded-full flex items-center justify-center text-white font-semibold">
                        ${sessionScope.user.name.substring(0,1)}
                    </div>
                </div>
                <a href="/logout" class="p-2 text-gray-600 hover:text-red-600 transition-colors" title="Cerrar sesión">
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
                                <div class="flex items-center gap-6 p-6 bg-gray-50 rounded-xl hover:bg-gray-100 transition" id="interested-${person.id}">
                                    <img
                                            src="${person.image}"
                                            alt="${person.name}"
                                            class="w-16 h-16 rounded-full object-cover"
                                            onerror="this.src='https://ui-avatars.com/api/?name=${person.name}&background=6366f1&color=fff'"
                                    />
                                    <div class="flex-1">
                                        <h3 class="font-semibold text-gray-900 mb-1">${person.name}</h3>
                                        <p class="text-sm text-gray-600 mb-2">${person.email}</p>
                                        <div class="flex items-center gap-2 text-sm">
                                            <span class="text-gray-600">Interesado en:</span>
                                            <span class="font-medium text-indigo-600">${person.myCourse}</span>
                                        </div>
                                        <div class="flex items-center gap-2 text-sm">
                                            <span class="text-gray-600">Su curso:</span>
                                            <span class="font-medium text-purple-600">${person.course}</span>
                                        </div>
                                    </div>
                                    <div class="flex gap-2">
                                        <button
                                                onclick="acceptMatch(${person.id})"
                                                class="px-6 py-2 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg hover:shadow-lg transition flex items-center gap-2"
                                        >
                                            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                            </svg>
                                            <span>Aceptar Match</span>
                                        </button>
                                        <button
                                                onclick="rejectInterest(${person.id})"
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
                                            src="${match.image}"
                                            alt="${match.name}"
                                            class="w-16 h-16 rounded-full object-cover"
                                            onerror="this.src='https://ui-avatars.com/api/?name=${match.name}&background=6366f1&color=fff'"
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
                                            href="/course/${match.courseId}"
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

<script>
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

    function acceptMatch(personId) {
        // Aquí harías una llamada AJAX al servidor
        fetch('/api/matches/accept', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ personId: personId })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Remover de la lista de interesados
                    document.getElementById('interested-' + personId).remove();

                    // Actualizar contador
                    const count = parseInt(document.getElementById('interested-count').textContent) - 1;
                    document.getElementById('interested-count').textContent = count;

                    const matchCount = parseInt(document.getElementById('matches-count').textContent) + 1;
                    document.getElementById('matches-count').textContent = matchCount;

                    // Mostrar mensaje de éxito
                    alert('¡Match aceptado exitosamente!');
                }
            });
    }

    function rejectInterest(personId) {
        if (confirm('¿Estás seguro de que quieres rechazar este interés?')) {
            fetch('/api/matches/reject', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ personId: personId })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        document.getElementById('interested-' + personId).remove();

                        const count = parseInt(document.getElementById('interested-count').textContent) - 1;
                        document.getElementById('interested-count').textContent = count;
                    }
                });
        }
    }
</script>
</body>
</html>