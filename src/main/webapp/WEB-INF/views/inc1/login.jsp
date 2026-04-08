<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - SkillSwap</title>

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
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<!-- Contenedor Principal -->
<div class="min-h-screen flex items-center justify-center px-4 py-12">
    <div class="max-w-md w-full">

        <!-- Header -->
        <div class="text-center mb-8 fade-in">
            <a href="index.jsp" class="inline-flex items-center justify-center gap-2 mb-6">
                <div class="w-12 h-12 bg-gradient-brand rounded-xl flex items-center justify-center">
                    <i data-lucide="book-open" class="w-7 h-7 text-white"></i>
                </div>
                <span class="text-3xl font-bold text-gradient-brand">
                        RedSaberes
                    </span>
            </a>
            <h1 class="text-3xl font-bold text-gray-900 mb-2">¡Bienvenido de vuelta!</h1>
            <p class="text-gray-600">Ingresa a tu cuenta para continuar</p>
        </div>

        <!-- Formulario -->
        <div class="bg-white p-8 rounded-2xl shadow-xl fade-in">

            <!-- Mensajes de Error/Éxito -->
            <c:if test="${not empty error}">
                <div class="mb-4 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg flex items-center gap-2">
                    <i data-lucide="alert-circle" class="w-5 h-5 flex-shrink-0"></i>
                    <p class="text-sm">${error}</p>
                </div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="mb-4 p-4 bg-green-50 border border-green-200 text-green-700 rounded-lg flex items-center gap-2">
                    <i data-lucide="check-circle-2" class="w-5 h-5 flex-shrink-0"></i>
                    <p class="text-sm">${success}</p>
                </div>
            </c:if>

            <form action="loginServlet" method="POST" id="loginForm" class="space-y-6">

                <!-- Correo Electrónico -->
                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                        Correo Electrónico
                    </label>
                    <div class="relative">
                        <i data-lucide="mail" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"></i>
                        <input
                                type="email"
                                id="email"
                                name="email"
                                required
                                value="${param.email}"
                                class="w-full pl-11 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                placeholder="tu@email.com"
                        />
                    </div>
                </div>

                <!-- Contraseña -->
                <div>
                    <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                        Contraseña
                    </label>
                    <div class="relative">
                        <i data-lucide="lock" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"></i>
                        <input
                                type="password"
                                id="password"
                                name="password"
                                required
                                class="w-full pl-11 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                placeholder="••••••••"
                        />
                        <button
                                type="button"
                                onclick="togglePassword('password', 'toggleIcon')"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition"
                        >
                            <i data-lucide="eye" id="toggleIcon" class="w-5 h-5"></i>
                        </button>
                    </div>
                </div>

                <!-- Recordarme y Olvidaste contraseña -->
                <div class="flex items-center justify-between">
                    <label class="flex items-center">
                        <input
                                type="checkbox"
                                name="remember"
                                class="w-4 h-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-600"
                        />
                        <span class="ml-2 text-sm text-gray-600">Recordarme</span>
                    </label>
                    <a
                            href="${pageContext.request.contextPath}/forgot-password"
                            class="text-sm text-indigo-600 hover:text-indigo-700 transition"
                    >
                        ¿Olvidaste tu contraseña?
                    </a>
                </div>

                <!-- Botón de Login -->
                <button
                        type="submit"
                        class="w-full bg-gradient-brand text-white py-3 rounded-lg font-semibold hover:shadow-lg transition-shadow"
                >
                    Iniciar Sesión
                </button>
            </form>

            <!-- Link a Registro -->
            <div class="mt-6 text-center">
                <p class="text-gray-600">
                    ¿No tienes una cuenta?
                    <a href="${pageContext.request.contextPath}/register" class="text-indigo-600 hover:text-indigo-700 font-semibold">
                        Regístrate
                    </a>
                </p>
            </div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script>
    // Inicializar Lucide Icons
    lucide.createIcons();

    // Toggle password visibility
    function togglePassword(inputId, iconId) {
        const input = document.getElementById(inputId);
        const icon = document.getElementById(iconId);

        if (input.type === 'password') {
            input.type = 'text';
            icon.setAttribute('data-lucide', 'eye-off');
        } else {
            input.type = 'password';
            icon.setAttribute('data-lucide', 'eye');
        }

        // Re-render icons
        lucide.createIcons();
    }

    // Auto-focus en el primer campo
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('email').focus();
    });
</script>

</body>
</html>