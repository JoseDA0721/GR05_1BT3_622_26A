<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Explorar Cursos</title>
    <%@ include file="../fragments/common-head.jspf" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/redsaberes-common.css?v=1.0">
    <style>
        /* ── Botón corazón ── */
        .heart-btn {
            position: absolute;
            top: 1rem;
            right: 1rem;
            width: 2.5rem;
            height: 2.5rem;
            border-radius: 9999px;
            border: none;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: transform .2s, background .2s;
        }
        /* Sin like */
        .heart-btn.not-liked            { background: rgba(255,255,255,.88); }
        .heart-btn.not-liked svg        { fill: none; stroke: #6b7280; stroke-width: 2; }
        .heart-btn.not-liked:hover      { background: #fce7f3; }
        .heart-btn.not-liked:hover svg  { fill: #ec4899; stroke: #ec4899; }
        /* Con like */
        .heart-btn.is-liked             { background: #ec4899; transform: scale(1.08); }
        .heart-btn.is-liked svg         { fill: #fff; stroke: #ec4899; stroke-width: 2; }
        /* Animación pop */
        @keyframes heartPop {
            0%   { transform: scale(1);    }
            35%  { transform: scale(1.4);  }
            65%  { transform: scale(0.9);  }
            100% { transform: scale(1.08); }
        }
        .heart-btn.pop { animation: heartPop .4s cubic-bezier(.68,-.55,.27,1.55) forwards; }
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<%@ include file="../fragments/nav.jspf" %>

<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

    <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Explorar Cursos</h1>
        <p class="text-gray-600">Descubre cursos que te interesen y da "Like" para expresar tu interés</p>
    </div>

    <!-- Mostrar mensaje de error si la carga de perfil falló -->
    <c:if test="${param.error == 'profile_load' || param.error == 'user_not_found' || requestScope.profileLoadError}">
        <div class="mb-6 p-4 rounded-lg bg-red-50 border border-red-100 text-red-800">
            <strong>Error al cargar el perfil público.</strong>
            <c:choose>
                <c:when test="${param.error == 'user_not_found'}">El usuario solicitado no existe.</c:when>
                <c:when test="${requestScope.profileLoadError}">
                    Ocurrió un error interno mientras se cargaba el perfil.
                    <c:if test="${not empty requestScope.profileLoadErrorMessage}">
                        Detalle: ${requestScope.profileLoadErrorMessage}
                    </c:if>
                </c:when>
                <c:otherwise>Intente nuevamente más tarde.</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <!-- Búsqueda y filtro -->
    <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8">
        <form method="GET" action="${pageContext.request.contextPath}/explore"
              class="flex flex-col md:flex-row gap-4">
            <div class="flex-1 relative">
                <svg xmlns="http://www.w3.org/2000/svg"
                     class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400"
                     fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                </svg>
                <input type="text" name="search" placeholder="Buscar cursos..."
                       value="${param.search}"
                       class="w-full pl-11 pr-4 py-3 border border-gray-300 rounded-lg
                              focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"/>
            </div>
            <select name="category"
                    class="px-4 py-3 border border-gray-300 rounded-lg
                           focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                    onchange="this.form.submit()">
                <option value="Todas"        ${param.category == 'Todas' || empty param.category ? 'selected' : ''}>Todas</option>
                <option value="Programación" ${param.category == 'Programación' ? 'selected' : ''}>Programación</option>
                <option value="Diseño"       ${param.category == 'Diseño'       ? 'selected' : ''}>Diseño</option>
                <option value="Data Science" ${param.category == 'Data Science' ? 'selected' : ''}>Data Science</option>
                <option value="Marketing"    ${param.category == 'Marketing'    ? 'selected' : ''}>Marketing</option>
                <option value="Arte"         ${param.category == 'Arte'         ? 'selected' : ''}>Arte</option>
                <option value="Finanzas"     ${param.category == 'Finanzas'     ? 'selected' : ''}>Finanzas</option>
            </select>
        </form>
    </div>

    <!-- Grid de cursos -->
    <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        <c:forEach var="course" items="${courses}">
            <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden
                        hover:shadow-lg transition-shadow">

                <div class="relative">
                    <img src="${course.image}"
                         alt="${course.title}"
                         class="w-full h-48 object-cover"
                         onerror="this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=400&h=250&fit=crop'"/>

                        <%--
                            CLAVE: data-liked recibe el booleano del servidor ("true" / "false").
                            CSS controla el aspecto visual completo mediante is-liked / not-liked.
                            El SVG NO declara fill ni stroke en el HTML — los hereda del CSS.
                        --%>
                    <button type="button"
                            class="heart-btn ${course.liked ? 'is-liked' : 'not-liked'}"
                            data-liked="${course.liked}"
                            data-course-id="${course.id}"
                            onclick="toggleLike(this)"
                            title="${course.liked ? 'Ya diste like' : 'Dar like'}">
                        <svg viewBox="0 0 24 24" width="20" height="20"
                             xmlns="http://www.w3.org/2000/svg">
                            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67
                                     l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78
                                     l1.06 1.06L12 21.23l7.78-7.78
                                     1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                        </svg>
                    </button>
                </div>

                <div class="p-6">
                    <div class="flex items-center gap-2 mb-3">
                        <span class="px-3 py-1 bg-indigo-100 text-indigo-700
                                     text-xs font-semibold rounded-full">
                                ${course.category}
                        </span>
                        <span class="px-3 py-1 bg-gray-100 text-gray-700
                                     text-xs font-semibold rounded-full">
                                ${course.level}
                        </span>
                    </div>
                    <h3 class="text-lg font-bold text-gray-900 mb-2">${course.title}</h3>
                    <p class="text-sm text-gray-600 mb-4 line-clamp-2">${course.description}</p>

                    <div class="flex items-center justify-between">
                                <div class="flex items-center gap-2">
                                    <c:choose>
                                        <c:when test="${not empty course.authorId}">
                                            <a href="${pageContext.request.contextPath}/profile?id=${course.authorId}"
                                               class="flex items-center gap-2 no-underline">
                                                <div class="w-8 h-8 bg-gradient-to-br from-indigo-500 to-purple-500
                                                            rounded-full flex items-center justify-center
                                                            text-white text-xs font-semibold">
                                                    ${course.author.substring(0,1)}
                                                </div>
                                                <span class="text-sm text-gray-600 hover:text-indigo-600 font-medium">${course.author}</span>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="flex items-center gap-2">
                                                <div class="w-8 h-8 bg-gradient-to-br from-indigo-500 to-purple-500
                                                            rounded-full flex items-center justify-center
                                                            text-white text-xs font-semibold">
                                                    ${course.author.substring(0,1)}
                                                </div>
                                                <span class="text-sm text-gray-600">${course.author}</span>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                        <a href="${pageContext.request.contextPath}/course-overview?id=${course.id}"
                           class="text-sm text-indigo-600 hover:text-indigo-700 font-semibold">
                            Ver más →
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty courses}">
            <div class="md:col-span-2 lg:col-span-3 text-center py-12
                        bg-white rounded-xl border border-gray-100">
                <p class="text-gray-500 text-lg">No se encuentran cursos.</p>
            </div>
        </c:if>
    </div>
</div>

<%@ include file="../fragments/mobile-bottom-nav.jspf" %>

<script>
    lucide.createIcons();

    function toggleLike(button) {
        var alreadyLiked = button.getAttribute('data-liked') === 'true';
        var courseId     = button.getAttribute('data-course-id');

        // Si ya tiene like: solo rebote visual, no reenviar
        if (alreadyLiked) {
            button.classList.remove('pop');
            void button.offsetWidth;
            button.classList.add('pop');
            return;
        }

        // Actualizar UI de inmediato
        button.classList.remove('not-liked', 'pop');
        button.classList.add('is-liked');
        button.setAttribute('data-liked', 'true');
        button.title = 'Ya diste like';
        void button.offsetWidth;
        button.classList.add('pop');

        // Enviar al servidor tras la animación
        setTimeout(function () {
            var form  = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/like-course';
            var input = document.createElement('input');
            input.type  = 'hidden';
            input.name  = 'cursoId';
            input.value = courseId;
            form.appendChild(input);
            document.body.appendChild(form);
            form.submit();
        }, 420);
    }
</script>

</body>
</html>
