<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Editar perfil</title>
    <%@ include file="../fragments/common-head.jspf" %>
</head>
<body class="bg-gradient-main min-h-screen">

<%@ include file="../fragments/nav.jspf" %>

<div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-6">
        <h1 class="text-3xl font-bold text-gray-900">Editar perfil</h1>
        <p class="text-gray-600 mt-1">Actualiza tu nombre y correo electrónico.</p>
    </div>

    <div class="bg-white p-8 rounded-2xl shadow-sm border border-gray-100">
        <c:if test="${not empty error}">
            <div class="mb-4 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg text-sm">
                ${error}
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="mb-4 p-4 bg-green-50 border border-green-200 text-green-700 rounded-lg text-sm">
                ${success}
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/profile/edit" class="space-y-5">
            <div>
                <label for="name" class="block text-sm font-medium text-gray-700 mb-2">Nombre</label>
                <input
                        type="text"
                        id="name"
                        name="name"
                        maxlength="100"
                        required
                        value="${formName}"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                        placeholder="Tu nombre"
                />
            </div>

            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-2">Correo electrónico</label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        required
                        value="${formEmail}"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                        placeholder="tu@email.com"
                />
            </div>

            <div class="bg-gray-50 border border-gray-200 rounded-xl p-5">
                <div class="flex items-start justify-between gap-4 flex-col sm:flex-row sm:items-center">
                    <div>
                        <h3 class="text-sm font-semibold text-gray-900">Contraseña</h3>
                        <p class="text-xs text-gray-500 mt-1">La edición de contraseña se realiza en una pantalla separada.</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/change-password"
                       class="inline-flex items-center gap-2 px-4 py-2 bg-gradient-brand text-white rounded-lg font-semibold hover:shadow-lg transition-shadow">
                        <i data-lucide="key-round" class="w-4 h-4"></i>
                        Cambiar contraseña
                    </a>
                </div>
            </div>

            <div class="flex items-center justify-end gap-3 pt-2">
                <a href="${pageContext.request.contextPath}/dashboard"
                   class="px-5 py-2.5 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition">
                    Cancelar
                </a>
                <button type="submit"
                        class="px-5 py-2.5 bg-gradient-brand text-white rounded-lg font-semibold hover:shadow-lg transition-shadow">
                    Guardar cambios
                </button>
            </div>
        </form>
    </div>
</div>

<%@ include file="../fragments/mobile-bottom-nav.jspf" %>

<script>
    lucide.createIcons();
</script>
</body>
</html>

