<%--
  Created by IntelliJ IDEA.
  User: LFVT24
  Date: 9/5/2026
  Time: 23:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Cambiar Contraseña</title>
    <%@ include file="../fragments/common-head.jspf" %>
</head>
<body class="bg-gradient-main min-h-screen">

<%@ include file="../fragments/nav.jspf" %>

<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

    <div class="mb-8 slide-in">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Cambiar contraseña</h1>
        <p class="text-gray-600">Actualiza tu contraseña para mantener segura tu cuenta</p>
    </div>

    <div class="max-w-2xl">
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-8 fade-in">

            <c:if test="${not empty error}">
                <div class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg flex items-center gap-2">
                    <i data-lucide="alert-circle" class="w-5 h-5 flex-shrink-0"></i>
                    <p class="text-sm">${error}</p>
                </div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="mb-6 p-4 bg-green-50 border border-green-200 text-green-700 rounded-lg flex items-center gap-2">
                    <i data-lucide="check-circle-2" class="w-5 h-5 flex-shrink-0"></i>
                    <p class="text-sm">${success}</p>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/change-password"
                  method="POST"
                  id="changePasswordForm"
                  class="space-y-6">

                <div>
                    <label for="currentPassword" class="block text-sm font-medium text-gray-700 mb-2">
                        Contraseña actual
                    </label>
                    <div class="relative">
                        <i data-lucide="lock" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"></i>
                        <input
                                type="password"
                                id="currentPassword"
                                name="currentPassword"
                                required
                                class="w-full pl-11 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                placeholder="Ingresa tu contraseña actual"
                        />
                        <button
                                type="button"
                                onclick="togglePassword('currentPassword', 'toggleCurrentIcon')"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition"
                                title="Mostrar u ocultar contraseña"
                        >
                            <i data-lucide="eye" id="toggleCurrentIcon" class="w-5 h-5"></i>
                        </button>
                    </div>
                </div>

                <div>
                    <label for="newPassword" class="block text-sm font-medium text-gray-700 mb-2">
                        Nueva contraseña
                    </label>
                    <div class="relative">
                        <i data-lucide="shield-check" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"></i>
                        <input
                                type="password"
                                id="newPassword"
                                name="newPassword"
                                required
                                minlength="8"
                                class="w-full pl-11 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                placeholder="Mínimo 8 caracteres"
                        />
                        <button
                                type="button"
                                onclick="togglePassword('newPassword', 'toggleNewIcon')"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition"
                                title="Mostrar u ocultar contraseña"
                        >
                            <i data-lucide="eye" id="toggleNewIcon" class="w-5 h-5"></i>
                        </button>
                    </div>
                    <p class="text-xs text-gray-500 mt-2">
                        Debe tener al menos 8 caracteres, una mayúscula y un número.
                    </p>
                    <p id="securityError" class="text-sm text-red-600 mt-2 hidden">
                        La contraseña no cumple con los requisitos mínimos de seguridad.
                    </p>
                </div>

                <div>
                    <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">
                        Confirmar nueva contraseña
                    </label>
                    <div class="relative">
                        <i data-lucide="lock-keyhole" class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"></i>
                        <input
                                type="password"
                                id="confirmPassword"
                                name="confirmPassword"
                                required
                                minlength="8"
                                class="w-full pl-11 pr-12 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                                placeholder="Repite la nueva contraseña"
                        />
                        <button
                                type="button"
                                onclick="togglePassword('confirmPassword', 'toggleConfirmIcon')"
                                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition"
                                title="Mostrar u ocultar contraseña"
                        >
                            <i data-lucide="eye" id="toggleConfirmIcon" class="w-5 h-5"></i>
                        </button>
                    </div>
                    <p id="matchError" class="text-sm text-red-600 mt-2 hidden">
                        Las contraseñas no coinciden.
                    </p>
                </div>

                <div id="passwordStrength" class="hidden">
                    <div class="flex items-center justify-between mb-1">
                        <span class="text-xs text-gray-600">Fortaleza de la contraseña:</span>
                        <span id="strengthText" class="text-xs font-semibold"></span>
                    </div>
                    <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
                        <div id="strengthBar" class="h-full transition-all duration-300"></div>
                    </div>
                </div>

                <div class="flex flex-col sm:flex-row gap-3 pt-2">
                    <button
                            type="submit"
                            class="inline-flex items-center justify-center gap-2 px-6 py-3 bg-gradient-brand text-white rounded-lg font-semibold hover:shadow-lg transition-shadow"
                    >
                        <i data-lucide="save" class="w-5 h-5"></i>
                        Guardar cambio
                    </button>

                    <a href="${pageContext.request.contextPath}/dashboard"
                       class="inline-flex items-center justify-center gap-2 px-6 py-3 bg-gray-100 text-gray-700 rounded-lg font-semibold hover:bg-gray-200 transition">
                        <i data-lucide="arrow-left" class="w-5 h-5"></i>
                        Volver
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="../fragments/mobile-bottom-nav.jspf" %>

<script>
    lucide.createIcons();

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

        lucide.createIcons();
    }

    const form = document.getElementById('changePasswordForm');
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const securityError = document.getElementById('securityError');
    const matchError = document.getElementById('matchError');
    const strengthDiv = document.getElementById('passwordStrength');
    const strengthBar = document.getElementById('strengthBar');
    const strengthText = document.getElementById('strengthText');

    function isSecurePassword(password) {
        return /^(?=.*[A-Z])(?=.*\d).{8,}$/.test(password);
    }

    function updateStrength(password) {
        if (password.length === 0) {
            strengthDiv.classList.add('hidden');
            return;
        }

        strengthDiv.classList.remove('hidden');

        let strength = 0;
        if (password.length >= 8) strength++;
        if (password.length >= 12) strength++;
        if (/[A-Z]/.test(password)) strength++;
        if (/\d/.test(password)) strength++;
        if (/[^a-zA-Z\d]/.test(password)) strength++;

        const percentage = (strength / 5) * 100;
        strengthBar.style.width = percentage + '%';

        if (strength <= 2) {
            strengthBar.className = 'h-full bg-red-500 transition-all duration-300';
            strengthText.textContent = 'Débil';
            strengthText.className = 'text-xs font-semibold text-red-600';
        } else if (strength <= 4) {
            strengthBar.className = 'h-full bg-yellow-500 transition-all duration-300';
            strengthText.textContent = 'Media';
            strengthText.className = 'text-xs font-semibold text-yellow-600';
        } else {
            strengthBar.className = 'h-full bg-green-500 transition-all duration-300';
            strengthText.textContent = 'Fuerte';
            strengthText.className = 'text-xs font-semibold text-green-600';
        }
    }

    newPassword.addEventListener('input', function () {
        updateStrength(this.value);
        securityError.classList.add('hidden');
        this.classList.remove('border-red-500');
    });

    confirmPassword.addEventListener('input', function () {
        matchError.classList.add('hidden');
        this.classList.remove('border-red-500');
    });

    form.addEventListener('submit', function (event) {
        let hasError = false;

        if (!isSecurePassword(newPassword.value)) {
            securityError.classList.remove('hidden');
            newPassword.classList.add('border-red-500');
            hasError = true;
        }

        if (newPassword.value !== confirmPassword.value) {
            matchError.classList.remove('hidden');
            confirmPassword.classList.add('border-red-500');
            hasError = true;
        }

        if (hasError) {
            event.preventDefault();
        }
    });

    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('currentPassword').focus();
    });
</script>

</body>
</html>

