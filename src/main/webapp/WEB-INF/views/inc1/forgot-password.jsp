<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Contraseña - SkillSwap</title>

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

        /* Animación de check */
        @keyframes scaleIn {
            from { transform: scale(0); }
            to { transform: scale(1); }
        }

        .scale-in {
            animation: scaleIn 0.5s ease-out;
        }
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<c:choose>
    <%-- Pantalla de confirmación de envío --%>
    <c:when test="${not empty param.sent || not empty sessionScope.resetEmailSent}">
        <div class="min-h-screen flex items-center justify-center px-4">
            <div class="max-w-md w-full text-center fade-in">

                <!-- Icono de éxito -->
                <div class="inline-flex items-center justify-center w-20 h-20 bg-green-100 rounded-full mb-6 scale-in">
                    <i data-lucide="check-circle-2" class="w-12 h-12 text-green-600"></i>
                </div>

                <h2 class="text-3xl font-bold text-gray-900 mb-4">¡Correo enviado!</h2>

                <p class="text-gray-600 mb-8">
                    Se ha enviado un enlace de recuperación a
                    <strong>${not empty param.email ? param.email : sessionScope.resetEmail}</strong>.
                    El enlace será válido por 24 horas.
                </p>

                <a href="${pageContext.request.contextPath}/login"
                   class="inline-flex items-center gap-2 text-indigo-600 hover:text-indigo-700 font-semibold transition">
                    <i data-lucide="arrow-left" class="w-5 h-5"></i>
                    Volver al inicio de sesión
                </a>

                    <%-- Limpiar sesión temporal --%>
                <%
                    session.removeAttribute("resetEmailSent");
                    session.removeAttribute("resetEmail");
                %>
            </div>
        </div>
    </c:when>

    <%-- Formulario de recuperación --%>
    <c:otherwise>
        <div class="min-h-screen flex items-center justify-center px-4 py-12">
            <div class="max-w-md w-full">

                <!-- Header -->
                <div class="text-center mb-8 fade-in">
                    <a href="index.jsp" class="inline-flex items-center justify-center gap-2 mb-6">
                        <div class="w-12 h-12 bg-gradient-brand rounded-xl flex items-center justify-center">
                            <i data-lucide="book-open" class="w-7 h-7 text-white"></i>
                        </div>
                        <span class="text-3xl font-bold text-gradient-brand">
                                Red Saberes
                            </span>
                    </a>
                    <h1 class="text-3xl font-bold text-gray-900 mb-2">¿Olvidaste tu contraseña?</h1>
                    <p class="text-gray-600">
                        Ingresa tu correo electrónico y te enviaremos un enlace para recuperarla
                    </p>
                </div>

                <!-- Formulario -->
                <div class="bg-white p-8 rounded-2xl shadow-xl fade-in">

                    <!-- Mensajes de Error -->
                    <c:if test="${not empty error}">
                        <div class="mb-4 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg flex items-center gap-2">
                            <i data-lucide="alert-circle" class="w-5 h-5 flex-shrink-0"></i>
                            <p class="text-sm">${error}</p>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/forgot-password" method="POST" id="forgotPasswordForm" class="space-y-6">

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
                                        autofocus
                                        value="${param.email}"
                                        class="w-full pl-11 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                        placeholder="tu@email.com"
                                />
                            </div>
                        </div>

                        <!-- Botón de Enviar -->
                        <button
                                type="submit"
                                class="w-full bg-gradient-brand text-white py-3 rounded-lg font-semibold hover:shadow-lg transition-shadow"
                        >
                            Enviar Enlace de Recuperación
                        </button>
                    </form>

                    <!-- Link a Login -->
                    <div class="mt-6 text-center">
                        <a href="${pageContext.request.contextPath}/login"
                           class="inline-flex items-center gap-2 text-gray-600 hover:text-gray-900 transition">
                            <i data-lucide="arrow-left" class="w-4 h-4"></i>
                            Volver al inicio de sesión
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<!-- Scripts -->
<script>
    // Inicializar Lucide Icons
    lucide.createIcons();
</script>

</body>
</html>