<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - SkillSwap</title>

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
            <h1 class="text-3xl font-bold text-gray-900 mb-2">Crea tu cuenta</h1>
            <p class="text-gray-600">Comienza tu viaje de aprendizaje colaborativo</p>
        </div>

        <!-- Formulario -->
        <div class="bg-white p-8 rounded-2xl shadow-xl fade-in">

            <!-- Mensajes de Error/Éxito -->
            <c:if test="${not empty error}">
                <div class="mb-4 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg">
                    <p class="text-sm">${error}</p>
                </div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="mb-4 p-4 bg-green-50 border border-green-200 text-green-700 rounded-lg flex items-center gap-2">
                    <i data-lucide="check-circle-2" class="w-5 h-5"></i>
                    <p class="text-sm">${success}</p>
                </div>
            </c:if>

            <form action="registerServlet" method="POST" id="registerForm" class="space-y-5">

                <!-- Nombre Completo -->
                <div>
                    <label for="name" class="block text-sm font-medium text-gray-700 mb-2">
                        Nombre Completo
                    </label>
                    <div class="relative">
                        <i data-lucide="user" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"></i>
                        <input
                                type="text"
                                id="name"
                                name="name"
                                required
                                maxlength="100"
                                value="${param.name}"
                                class="w-full pl-11 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                placeholder="Juan Pérez"
                        />
                    </div>
                </div>

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
                                minlength="6"
                                class="w-full pl-11 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                placeholder="••••••••"
                        />
                        <button
                                type="button"
                                onclick="togglePassword('password', 'toggleIcon1')"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition"
                        >
                            <i data-lucide="eye" id="toggleIcon1" class="w-5 h-5"></i>
                        </button>
                    </div>
                </div>

                <!-- Confirmar Contraseña -->
                <div>
                    <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">
                        Confirmar Contraseña
                    </label>
                    <div class="relative">
                        <i data-lucide="lock" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"></i>
                        <input
                                type="password"
                                id="confirmPassword"
                                name="confirmPassword"
                                required
                                minlength="6"
                                class="w-full pl-11 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                placeholder="••••••••"
                        />
                    </div>
                    <p id="passwordError" class="text-sm text-red-600 mt-1 hidden">Las contraseñas no coinciden</p>
                </div>

                <!-- Términos y Condiciones -->
                <div class="flex items-start">
                    <input
                            type="checkbox"
                            id="terms"
                            name="terms"
                            required
                            class="w-4 h-4 mt-1 text-indigo-600 border-gray-300 rounded focus:ring-indigo-600"
                    />
                    <label for="terms" class="ml-2 text-sm text-gray-600">
                        Acepto los <a href="terminos.jsp" class="text-indigo-600 hover:text-indigo-700 font-semibold">términos y condiciones</a>
                        y la <a href="privacidad.jsp" class="text-indigo-600 hover:text-indigo-700 font-semibold">política de privacidad</a>
                    </label>
                </div>

                <!-- Botón de Registro -->
                <button
                        type="submit"
                        class="w-full bg-gradient-brand text-white py-3 rounded-lg font-semibold hover:shadow-lg transition-shadow"
                >
                    Crear Cuenta
                </button>
            </form>

            <!-- Link a Login -->
            <div class="mt-6 text-center">
                <p class="text-gray-600">
                    ¿Ya tienes una cuenta?
                    <a href="login.jsp" class="text-indigo-600 hover:text-indigo-700 font-semibold">
                        Inicia Sesión
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

    // Validación de contraseñas
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const errorMsg = document.getElementById('passwordError');

        if (password !== confirmPassword) {
            e.preventDefault();
            errorMsg.classList.remove('hidden');
            document.getElementById('confirmPassword').classList.add('border-red-500');
        } else {
            errorMsg.classList.add('hidden');
            document.getElementById('confirmPassword').classList.remove('border-red-500');
        }
    });

    // Limpiar error cuando el usuario escribe
    document.getElementById('confirmPassword').addEventListener('input', function() {
        const errorMsg = document.getElementById('passwordError');
        errorMsg.classList.add('hidden');
        this.classList.remove('border-red-500');
    });
</script>

</body>
</html>
