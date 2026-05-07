<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${curso.titulo}" /> - Aula</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://unpkg.com/lucide@latest"></script>
    <style>
        .bg-main {
            background: linear-gradient(135deg, #eef2ff 0%, #ffffff 50%, #f7f3ff 100%);
        }

        .bg-brand {
            background: linear-gradient(90deg, #4f46e5 0%, #9333ea 100%);
        }

        .lesson-item.active {
            border-color: #6366f1;
            background: #eef2ff;
        }

        .lesson-item.completed {
            background: #f0fdf4;
            border-color: #86efac;
        }
    </style>
</head>
<body class="bg-main min-h-screen text-gray-800">
<c:set var="coverImage" value="https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=1600&amp;h=600&amp;fit=crop" />
<c:if test="${not empty curso.imagenPortada}">
    <c:choose>
        <c:when test="${fn:startsWith(curso.imagenPortada, 'http://') or fn:startsWith(curso.imagenPortada, 'https://')}">
            <c:set var="coverImage" value="${curso.imagenPortada}" />
        </c:when>
        <c:when test="${fn:startsWith(curso.imagenPortada, '/')}">
            <c:set var="coverImage" value="${pageContext.request.contextPath}${curso.imagenPortada}" />
        </c:when>
        <c:otherwise>
            <c:set var="coverImage" value="${pageContext.request.contextPath}/${curso.imagenPortada}" />
        </c:otherwise>
    </c:choose>
</c:if>

<c:choose>
    <c:when test="${not accesoConcedido}">
        <div class="min-h-screen flex items-center justify-center px-4">
            <div class="text-center max-w-md">
                <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6">
                    <i data-lucide="lock" class="w-10 h-10 text-gray-400"></i>
                </div>
                <h2 class="text-2xl font-bold text-gray-900 mb-4">Contenido bloqueado</h2>
                <p class="text-gray-600 mb-8">
                    Necesitas tener un match confirmado con el creador para acceder a las lecciones.
                </p>
                <a href="${pageContext.request.contextPath}/course-material?id=${curso.id}" class="inline-block px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
                    Ver detalle del curso
                </a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <nav class="bg-white/85 backdrop-blur border-b border-gray-200 sticky top-0 z-50">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
                <a href="${pageContext.request.contextPath}/course-material?id=${curso.id}"
                   class="inline-flex items-center gap-2 text-gray-700 hover:text-indigo-600 transition">
                    <i data-lucide="arrow-left" class="w-5 h-5"></i>
                    <span class="font-medium">Volver al detalle</span>
                </a>

                <div class="hidden sm:flex items-center gap-2 px-3 py-1.5 rounded-full bg-green-50 text-green-700 text-sm font-medium">
                    <i data-lucide="play-circle" class="w-4 h-4"></i>
                    Aula del curso
                </div>

                <button onclick="window.location.reload()" class="inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-gray-100 text-gray-700 hover:bg-gray-200 transition font-medium">
                    <i data-lucide="refresh-cw" class="w-4 h-4"></i>
                    Recargar
                </button>
            </div>
        </nav>

        <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
            <section class="mb-6 rounded-2xl overflow-hidden border border-gray-200 bg-white">
                <div class="relative h-44 sm:h-56">
                    <img
                            src="${coverImage}"
                            alt="<c:out value='${curso.titulo}'/>"
                            class="w-full h-full object-cover" />
                    <div class="absolute inset-0 bg-gradient-to-t from-black/70 via-black/20 to-transparent"></div>
                    <div class="absolute bottom-0 left-0 right-0 p-5 sm:p-6">
                        <div class="flex flex-wrap gap-2 mb-2">
                            <span class="px-3 py-1 rounded-full bg-white/20 text-white text-xs font-semibold backdrop-blur">
                                <c:out value="${curso.categoria}" />
                            </span>
                            <span class="px-3 py-1 rounded-full bg-white/20 text-white text-xs font-semibold backdrop-blur">
                                <c:out value="${curso.nivelDificultad}" />
                            </span>
                        </div>
                        <h1 class="text-2xl sm:text-3xl font-bold text-white"><c:out value="${curso.titulo}" /></h1>
                    </div>
                </div>
            </section>

            <div class="grid lg:grid-cols-12 gap-6">
                <aside class="lg:col-span-4 bg-white border border-gray-200 rounded-2xl overflow-hidden">
                    <div class="p-5 border-b border-gray-100">
                        <h2 class="font-bold text-gray-900 text-lg">Lecciones</h2>
                        <p class="text-sm text-gray-500 mt-1">Selecciona una leccion para continuar aprendiendo.</p>
                    </div>

                    <div class="p-5 border-b border-gray-100">
                        <div class="flex justify-between text-sm mb-2">
                            <span class="text-gray-600">Progreso del curso</span>
                            <span id="progressLabel" class="font-semibold text-indigo-600">${progresoGuardado != null ? progresoGuardado : 0}%</span>
                        </div>
                        <div class="h-2 rounded-full bg-gray-100 overflow-hidden">
                            <div id="progressBar" class="h-full bg-brand rounded-full transition-all duration-300" style="width: ${progresoGuardado != null ? progresoGuardado : 0}%;"></div>
                        </div>
                    </div>

                    <div class="max-h-[520px] overflow-y-auto p-4 space-y-4">
                        <c:choose>
                            <c:when test="${not empty modulos}">
                                <c:forEach var="modulo" items="${modulos}" varStatus="mStatus">
                                    <div>
                                        <h3 class="text-sm font-bold text-gray-800 mb-2">
                                            Modulo ${mStatus.index + 1}: <c:out value="${modulo.titulo}" />
                                        </h3>
                                        <div class="space-y-2">
                                            <c:choose>
                                                <c:when test="${not empty modulo.lecciones}">
                                                    <c:forEach var="leccion" items="${modulo.lecciones}" varStatus="lStatus">
                                                        <button
                                                                type="button"
                                                                class="lesson-item w-full text-left p-3 border border-gray-200 rounded-xl hover:bg-gray-50 transition"
                                                                data-lesson-id="${leccion.id}"
                                                                data-lesson-title="<c:out value='${leccion.titulo}'/>"
                                                                data-lesson-content="<c:out value='${leccion.contenido}'/>"
                                                                data-module-title="<c:out value='${modulo.titulo}'/>"
                                                                data-order="${mStatus.index + 1}.${lStatus.index + 1}">
                                                            <div class="flex items-start gap-3">
                                                                <div class="w-8 h-8 rounded-full bg-indigo-100 text-indigo-700 flex items-center justify-center text-xs font-bold flex-shrink-0">
                                                                    ${lStatus.index + 1}
                                                                </div>
                                                                <div class="min-w-0">
                                                                    <p class="text-sm font-semibold text-gray-900 truncate"><c:out value="${leccion.titulo}" /></p>
                                                                    <p class="text-xs text-gray-500">Leccion ${lStatus.index + 1}</p>
                                                                </div>
                                                            </div>
                                                        </button>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="p-3 border border-dashed border-gray-200 rounded-xl text-xs text-gray-500">
                                                        Este modulo aun no tiene lecciones.
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-10">
                                    <i data-lucide="layers" class="w-10 h-10 text-gray-300 mx-auto mb-3"></i>
                                    <p class="text-sm text-gray-500">No hay modulos disponibles.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </aside>

                <section class="lg:col-span-8">
                    <article class="bg-white border border-gray-200 rounded-2xl p-6 sm:p-8 min-h-[420px]">
                        <div class="flex items-start justify-between gap-4">
                            <div>
                                <p id="lessonModule" class="text-sm font-semibold text-indigo-600 mb-2">Descripcion del curso</p>
                                <h2 id="lessonTitle" class="text-2xl sm:text-3xl font-bold text-gray-900"><c:out value="${curso.titulo}" /></h2>
                            </div>
                            <span id="lessonOrder" class="px-3 py-1 rounded-full bg-gray-100 text-gray-600 text-xs font-semibold">-</span>
                        </div>
                        <div class="mt-6 border-t border-gray-100 pt-6">
                            <p id="lessonContent" class="text-gray-700 leading-relaxed whitespace-pre-wrap"><c:out value="${empty curso.descripcion ? 'Este curso aun no tiene descripcion.' : curso.descripcion}" /></p>
                        </div>
                    </article>
                </section>
            </div>
        </main>

        <script>
            lucide.createIcons();

            const lessonItems = Array.from(document.querySelectorAll('.lesson-item'));
            const lessonTitle = document.getElementById('lessonTitle');
            const lessonContent = document.getElementById('lessonContent');
            const lessonModule = document.getElementById('lessonModule');
            const lessonOrder = document.getElementById('lessonOrder');
            const progressBar = document.getElementById('progressBar');
            const progressLabel = document.getElementById('progressLabel');
            const cursoId = ${curso.id};
            let currentProgress = ${progresoGuardado != null ? progresoGuardado : 0};
            const totalLessons = lessonItems.length;
            const progressPerLesson = totalLessons > 0 ? 100 / totalLessons : 0;

            function saveProgressToServer(progressValue) {
                fetch('${pageContext.request.contextPath}/update-progress', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `cursoId=${cursoId}&progreso=${progressValue}`
                }).catch(() => {});
            }

            function updateProgressUI() {
                progressBar.style.width = currentProgress + '%';
                progressLabel.textContent = currentProgress + '%';
            }

            function markCompletedLessons() {
                if (progressPerLesson === 0) {
                    return;
                }

                const completedLessonsCount = Math.floor(currentProgress / progressPerLesson);
                lessonItems.forEach((item, index) => {
                    if (index < completedLessonsCount) {
                        item.classList.add('completed');
                    }
                });
            }

            function selectLesson(item, index, updateProgress = true) {
                lessonItems.forEach((node) => node.classList.remove('active'));
                item.classList.add('active');

                const title = item.dataset.lessonTitle || 'Leccion sin titulo';
                const content = item.dataset.lessonContent || 'Esta leccion no tiene contenido todavia.';
                const moduleTitle = item.dataset.moduleTitle || 'Modulo';
                const order = item.dataset.order || '-';

                lessonTitle.textContent = title;
                lessonContent.textContent = content.trim() ? content : 'Esta leccion no tiene contenido todavia.';
                lessonModule.textContent = moduleTitle;
                lessonOrder.textContent = order;

                if (updateProgress) {
                    const newProgress = Math.min(100, Math.ceil(((index + 1) / totalLessons) * 100));
                    if (newProgress > currentProgress) {
                        currentProgress = newProgress;
                        updateProgressUI();
                        saveProgressToServer(currentProgress);
                    }
                }

                item.classList.add('completed');
            }

            updateProgressUI();
            markCompletedLessons();

            lessonItems.forEach((item, index) => {
                item.addEventListener('click', () => selectLesson(item, index, true));
            });

            if (lessonItems.length > 0) {
                selectLesson(lessonItems[0], 0, false);
            }
        </script>
    </c:otherwise>
</c:choose>
</body>
</html>
