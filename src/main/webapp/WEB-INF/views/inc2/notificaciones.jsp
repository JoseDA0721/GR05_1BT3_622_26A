<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Centro de Notificaciones</title>
    <%@ include file="../fragments/common-head.jspf" %>
    <style>
        /* Estado de notificación */
        .notificacion-item {
            transition: all 0.3s ease;
            border-left: 4px solid transparent;
        }

        .notificacion-item.no-leida {
            background-color: #f0f9ff;
            border-left-color: #4f46e5;
        }

        .notificacion-item.leida {
            background-color: #ffffff;
            border-left-color: #d1d5db;
        }

        .notificacion-item:hover {
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .badge-no-leida {
            display: inline-block;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background-color: #4f46e5;
            margin-right: 8px;
        }

        .badge-leida {
            display: none;
        }

        .tipo-notificacion {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            margin-right: 8px;
        }

        .tipo-like {
            background-color: #fee2e2;
            color: #991b1b;
        }

        .tipo-review {
            background-color: #fef3c7;
            color: #92400e;
        }

        .fecha-notificacion {
            font-size: 13px;
            color: #9ca3af;
        }

        .btn-marcar-leida {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 8px 12px;
            font-size: 13px;
            background-color: #4f46e5;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-marcar-leida:hover {
            background-color: #4338ca;
            box-shadow: 0 2px 8px rgba(79, 70, 229, 0.3);
        }

        .notificaciones-vacia {
            text-align: center;
            padding: 60px 20px;
        }

        .notificaciones-vacia svg {
            width: 64px;
            height: 64px;
            margin: 0 auto 20px;
            opacity: 0.5;
        }
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<!-- Navigation Header -->
<%@ include file="../fragments/nav.jspf" %>

<div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <!-- Header -->
    <div class="mb-8">
        <h1 class="text-4xl font-bold text-gray-900 mb-2">Centro de Notificaciones</h1>
        <p class="text-gray-600">Gestiona tus notificaciones de likes y reseñas</p>
    </div>

    <!-- Filtros (opcional) -->
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 mb-6">
        <div class="flex items-center justify-between">
            <div class="flex gap-2">
                <button
                    onclick="filterNotifications('todas')"
                    id="filter-todas"
                    class="px-4 py-2 rounded-lg font-medium text-indigo-600 bg-indigo-50 transition"
                >
                    Todas
                </button>
                <button
                    onclick="filterNotifications('no-leidas')"
                    id="filter-no-leidas"
                    class="px-4 py-2 rounded-lg font-medium text-gray-600 hover:text-gray-900 transition"
                >
                    No leídas
                </button>
                <button
                    onclick="filterNotifications('leidas')"
                    id="filter-leidas"
                    class="px-4 py-2 rounded-lg font-medium text-gray-600 hover:text-gray-900 transition"
                >
                    Leídas
                </button>
            </div>
            <span class="text-sm text-gray-600">
                Total: <span id="total-notificaciones">${fn:length(todasNotificaciones)}</span>
            </span>
        </div>
    </div>

     <!-- Lista de Notificaciones -->
     <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
         <c:choose>
             <c:when test="${empty todasNotificaciones}">
                <!-- Estado vacío -->
                <div class="notificaciones-vacia">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/>
                    </svg>
                    <h3 class="text-lg font-semibold text-gray-900 mb-2">No hay notificaciones</h3>
                    <p class="text-gray-600">¡Todas tus notificaciones han sido leídas!</p>
                </div>
            </c:when>
            <c:otherwise>
             <!-- Lista de notificaciones -->
                 <div class="divide-y divide-gray-100">
                     <c:forEach var="notif" items="${todasNotificaciones}">
                        <div class="notificacion-item ${notif.estado == 'NO_LEIDO' ? 'no-leida' : 'leida'} p-6 flex items-start justify-between gap-6"
                             id="notif-${notif.id}">
                            <!-- Contenido principal -->
                            <div class="flex-1">
                                <!-- Encabezado con estado y tipo -->
                                <div class="flex items-center gap-2 mb-2">
                                    <c:if test="${notif.estado == 'NO_LEIDO'}">
                                        <span class="badge-no-leida"></span>
                                    </c:if>
                                    <!-- Tipo de notificación -->
                                    <c:choose>
                                        <c:when test="${notif.tipo == 'LIKE_RECIBIDO'}">
                                            <span class="tipo-notificacion tipo-like">
                                                <i data-lucide="heart" class="w-3 h-3 inline"></i> Like recibido
                                            </span>
                                        </c:when>
                                        <c:when test="${notif.tipo == 'REVIEW_RECIBIDA'}">
                                            <span class="tipo-notificacion tipo-review">
                                                <i data-lucide="star" class="w-3 h-3 inline"></i> Reseña recibida
                                            </span>
                                        </c:when>
                                    </c:choose>
                                </div>

                                <!-- Descripción -->
                                <p class="text-base text-gray-900 font-medium mb-2">
                                    ${notif.descripcion}
                                </p>

                                <!-- Fecha -->
                                <span class="fecha-notificacion">
                                    ${notif.fechaCreacionFormateada}
                                </span>
                            </div>

                            <!-- Acción: Marcar como leída -->
                            <c:if test="${notif.estado == 'NO_LEIDO'}">
                                <form method="POST" action="${pageContext.request.contextPath}/notificaciones"
                                      class="flex items-center">
                                    <input type="hidden" name="notificacionId" value="${notif.id}"/>
                                    <button type="submit" class="btn-marcar-leida"
                                            title="Marcar como leído">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none"
                                             viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                        </svg>
                                        <span>Marcar leída</span>
                                    </button>
                                </form>
                            </c:if>
                            <c:if test="${notif.estado == 'LEIDO'}">
                                <div class="flex items-center gap-2 text-gray-400 text-sm">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="currentColor"
                                         viewBox="0 0 24 24">
                                        <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z"/>
                                    </svg>
                                    <span>Leída</span>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Información adicional -->
    <div class="mt-6 text-center text-gray-600 text-sm">
        <p>Las notificaciones se mostrarán aquí automáticamente cuando otros usuarios den like o dejen reseñas en tus cursos.</p>
    </div>
</div>

<script>
    // Filtrar notificaciones
    function filterNotifications(filter) {
        const items = document.querySelectorAll('.notificacion-item');
        let visible = 0;

        items.forEach(item => {
            if (filter === 'todas') {
                item.style.display = '';
                visible++;
            } else if (filter === 'no-leidas' && item.classList.contains('no-leida')) {
                item.style.display = '';
                visible++;
            } else if (filter === 'leidas' && item.classList.contains('leida')) {
                item.style.display = '';
                visible++;
            } else {
                item.style.display = 'none';
            }
        });

        // Actualizar botones de filtro
        document.querySelectorAll('[id^="filter-"]').forEach(btn => {
            btn.classList.remove('bg-indigo-50', 'text-indigo-600');
            btn.classList.add('text-gray-600', 'hover:text-gray-900');
        });
        document.getElementById(`filter-${filter}`).classList.add('bg-indigo-50', 'text-indigo-600');

        // Mostrar mensaje si no hay resultados
        if (visible === 0) {
            const container = document.querySelector('.divide-y');
            if (!document.getElementById('no-results-message')) {
                const msg = document.createElement('div');
                msg.id = 'no-results-message';
                msg.className = 'notificaciones-vacia p-12';
                msg.innerHTML = `
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                    </svg>
                    <h3 class="text-lg font-semibold text-gray-900 mb-2">No hay notificaciones</h3>
                    <p class="text-gray-600">No se encontraron notificaciones con este filtro</p>
                `;
                container.parentElement.appendChild(msg);
            }
        } else {
            const msg = document.getElementById('no-results-message');
            if (msg) msg.remove();
        }
    }

    // Inicializar Lucide icons
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }
</script>

</body>
</html>

