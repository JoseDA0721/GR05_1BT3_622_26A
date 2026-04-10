<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Red Saberes - Democratiza el Conocimiento</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap');

        * {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }

        .backdrop-blur-md {
            backdrop-filter: blur(12px);
        }

        @keyframes float {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-10px); }
        }

        .animate-float {
            animation: float 3s ease-in-out infinite;
        }
    </style>
</head>
<body class="min-h-screen bg-gray-50">
<!-- Navigation -->
<nav class="bg-white/80 backdrop-blur-md border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
            <div class="flex items-center gap-2">
                <div class="w-10 h-10 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-xl flex items-center justify-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                    </svg>
                </div>
                <span class="text-2xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                        Red Saberes
                    </span>
            </div>
            <div class="flex items-center gap-4">
                <a href="${pageContext.request.contextPath}/login" class="text-gray-700 hover:text-indigo-600 transition-colors">
                    Iniciar Sesión
                </a>
                <a href="${pageContext.request.contextPath}/register" class="bg-gradient-to-r from-indigo-600 to-purple-600 text-white px-6 py-2 rounded-lg hover:shadow-lg transition-shadow">
                    Registrarse
                </a>
            </div>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
    <div class="text-center">
        <div class="inline-flex items-center gap-2 bg-indigo-100 text-indigo-700 px-4 py-2 rounded-full mb-6">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
            </svg>
            <span class="text-sm font-medium">Aprendizaje colaborativo peer-to-peer</span>
        </div>
        <h1 class="text-5xl md:text-6xl font-bold text-gray-900 mb-6">
            Comparte tu conocimiento,
            <br />
            <span class="bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                    Aprende de otros
                </span>
        </h1>
        <p class="text-xl text-gray-600 mb-10 max-w-2xl mx-auto">
            Red Saberes democratiza el conocimiento mediante un modelo de intercambio.
            Enseña lo que sabes y accede a cursos creados por otros miembros de la comunidad.
        </p>
        <div class="flex items-center justify-center gap-4 flex-wrap">
            <a href="${pageContext.request.contextPath}//register" class="bg-gradient-to-r from-indigo-600 to-purple-600 text-white px-8 py-4 rounded-lg text-lg font-semibold hover:shadow-xl transition-all inline-flex items-center gap-2">
                Comenzar Gratis
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                </svg>
            </a>
<%--            <a href="${pageContext.request.contextPath}/explore" class="bg-white text-gray-900 px-8 py-4 rounded-lg text-lg font-semibold border-2 border-gray-200 hover:border-indigo-600 transition-colors">--%>
<%--                Explorar Cursos--%>
<%--            </a>--%>
        </div>
    </div>
</section>

<!-- Features Section -->
<section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
    <div class="text-center mb-16">
        <h2 class="text-4xl font-bold text-gray-900 mb-4">¿Cómo funciona?</h2>
        <p class="text-xl text-gray-600">
            Un sistema de aprendizaje colaborativo basado en match
        </p>
    </div>

    <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
        <!-- Feature 1 -->
        <div class="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow">
            <div class="w-14 h-14 bg-indigo-100 rounded-xl flex items-center justify-center mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-indigo-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">Crea tu Curso</h3>
            <p class="text-gray-600">
                Comparte tus conocimientos creando cursos estructurados con módulos y lecciones.
            </p>
        </div>

        <!-- Feature 2 -->
        <div class="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow">
            <div class="w-14 h-14 bg-purple-100 rounded-xl flex items-center justify-center mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">Expresa Interés</h3>
            <p class="text-gray-600">
                Explora cursos y da "Like" a los que te interesen para manifestar tu interés.
            </p>
        </div>

        <!-- Feature 3 -->
        <div class="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow">
            <div class="w-14 h-14 bg-pink-100 rounded-xl flex items-center justify-center mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-pink-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">Haz Match</h3>
            <p class="text-gray-600">
                Cuando ambos usuarios se interesan en los cursos del otro, se crea un match.
            </p>
        </div>

        <!-- Feature 4 -->
        <div class="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow">
            <div class="w-14 h-14 bg-green-100 rounded-xl flex items-center justify-center mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">Accede al Contenido</h3>
            <p class="text-gray-600">
                Una vez confirmado el match, ambos usuarios pueden acceder a los cursos del otro.
            </p>
        </div>
    </div>
</section>

<!-- Stats Section -->
<section class="bg-gradient-to-r from-indigo-600 to-purple-600 py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid md:grid-cols-3 gap-8 text-center text-white">
            <div>
                <div class="text-5xl font-bold mb-2">5000+</div>
                <div class="text-xl opacity-90">Usuarios Activos</div>
            </div>
            <div>
                <div class="text-5xl font-bold mb-2">1200+</div>
                <div class="text-xl opacity-90">Cursos Publicados</div>
            </div>
            <div>
                <div class="text-5xl font-bold mb-2">10000+</div>
                <div class="text-xl opacity-90">Matches Realizados</div>
            </div>
        </div>
    </div>
</section>

<!-- CTA Section -->
<section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
    <div class="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-3xl p-12 text-center text-white">
        <h2 class="text-4xl font-bold mb-4">
            ¿Listo para comenzar tu viaje de aprendizaje?
        </h2>
        <p class="text-xl mb-8 opacity-90">
            Únete a nuestra comunidad y democratiza el conocimiento
        </p>
        <a href="${pageContext.request.contextPath}/register" class="inline-flex items-center gap-2 bg-white text-indigo-600 px-8 py-4 rounded-lg text-lg font-semibold hover:shadow-xl transition-all">
            Registrarse Gratis
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
        </a>
    </div>
</section>

<!-- Footer -->
<footer class="bg-gray-900 text-white py-12">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center">
            <div class="flex items-center justify-center gap-2 mb-4">
                <div class="w-10 h-10 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-xl flex items-center justify-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                    </svg>
                </div>
                <span class="text-2xl font-bold">Red Saberes</span>
            </div>
            <p class="text-gray-400 mb-4">
                Democratizando el conocimiento mediante aprendizaje entre pares
            </p>
            <p class="text-gray-500 text-sm">
                © 2026 Red Saberes. Todos los derechos reservados.
            </p>
        </div>
    </div>
</footer>
</body>
</html>