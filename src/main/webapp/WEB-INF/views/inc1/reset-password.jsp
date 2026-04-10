<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Restablecer Contraseña - SkillSwap</title>

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
          Red Saberes
        </span>
      </a>
      <h1 class="text-3xl font-bold text-gray-900 mb-2">Restablecer Contraseña</h1>
      <p class="text-gray-600">Ingresa tu nueva contraseña</p>
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

      <form action="${pageContext.request.contextPath}/reset-password" method="POST" id="resetPasswordForm" class="space-y-5">

        <!-- Token oculto -->
        <input type="hidden" name="token" value="${param.token}" />

        <!-- Nueva Contraseña -->
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
            Nueva Contraseña
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
          <p class="text-xs text-gray-500 mt-1">Mínimo 6 caracteres</p>
        </div>

        <!-- Confirmar Contraseña -->
        <div>
          <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">
            Confirmar Nueva Contraseña
          </label>
          <div class="relative">
            <i data-lucide="lock" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"></i>
            <input
                    type="password"
                    id="confirmPassword"
                    name="confirmPassword"
                    required
                    minlength="6"
                    class="w-full pl-11 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                    placeholder="••••••••"
            />
            <button
                    type="button"
                    onclick="togglePassword('confirmPassword', 'toggleIcon2')"
                    class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition"
            >
              <i data-lucide="eye" id="toggleIcon2" class="w-5 h-5"></i>
            </button>
          </div>
          <p id="passwordError" class="text-sm text-red-600 mt-1 hidden">Las contraseñas no coinciden</p>
        </div>

        <!-- Indicador de fortaleza de contraseña -->
        <div id="passwordStrength" class="hidden">
          <div class="flex items-center justify-between mb-1">
            <span class="text-xs text-gray-600">Fortaleza de la contraseña:</span>
            <span id="strengthText" class="text-xs font-semibold"></span>
          </div>
          <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
            <div id="strengthBar" class="h-full transition-all duration-300"></div>
          </div>
        </div>

        <!-- Botón de Restablecer -->
        <button
                type="submit"
                class="w-full bg-gradient-brand text-white py-3 rounded-lg font-semibold hover:shadow-lg transition-shadow"
        >
          Restablecer Contraseña
        </button>
      </form>

      <!-- Link a Login -->
      <div class="mt-6 text-center">
        <a href="${pageContext.request.contextPath}/login" class="inline-flex items-center gap-2 text-gray-600 hover:text-gray-900 transition">
          <i data-lucide="arrow-left" class="w-4 h-4"></i>
          Volver al inicio de sesión
        </a>
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
  document.getElementById('resetPasswordForm').addEventListener('submit', function(e) {
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

  // Indicador de fortaleza de contraseña
  const passwordInput = document.getElementById('password');
  const strengthDiv = document.getElementById('passwordStrength');
  const strengthBar = document.getElementById('strengthBar');
  const strengthText = document.getElementById('strengthText');

  passwordInput.addEventListener('input', function() {
    const password = this.value;

    if (password.length === 0) {
      strengthDiv.classList.add('hidden');
      return;
    }

    strengthDiv.classList.remove('hidden');

    // Calcular fortaleza
    let strength = 0;
    if (password.length >= 6) strength++;
    if (password.length >= 10) strength++;
    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[^a-zA-Z\d]/.test(password)) strength++;

    // Actualizar barra
    const percentage = (strength / 5) * 100;
    strengthBar.style.width = percentage + '%';

    // Colores y texto según fortaleza
    if (strength <= 2) {
      strengthBar.className = 'h-full bg-red-500 transition-all duration-300';
      strengthText.textContent = 'Débil';
      strengthText.className = 'text-xs font-semibold text-red-600';
    } else if (strength <= 3) {
      strengthBar.className = 'h-full bg-yellow-500 transition-all duration-300';
      strengthText.textContent = 'Media';
      strengthText.className = 'text-xs font-semibold text-yellow-600';
    } else {
      strengthBar.className = 'h-full bg-green-500 transition-all duration-300';
      strengthText.textContent = 'Fuerte';
      strengthText.className = 'text-xs font-semibold text-green-600';
    }
  });
</script>

</body>
</html>
