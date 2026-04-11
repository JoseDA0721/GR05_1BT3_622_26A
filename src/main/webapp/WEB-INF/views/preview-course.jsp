<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${curso.titulo}" /> - Vista Previa</title>
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

<nav class="bg-white/85 backdrop-blur border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <a href="${pageContext.request.contextPath}/my-courses"
           class="inline-flex items-center gap-2 text-gray-700 hover:text-indigo-600 transition">
            <i data-lucide="arrow-left" class="w-5 h-5"></i>
            <span class="font-medium">Volver a Mis Cursos</span>
        </a>

        <div class="hidden sm:flex items-center gap-2 px-3 py-1.5 rounded-full bg-indigo-50 text-indigo-700 text-sm font-medium">
            <i data-lucide="eye" class="w-4 h-4"></i>
            Vista previa del estudiante
        </div>

        <a href="${pageContext.request.contextPath}/edit-course?id=${curso.id}"
           class="inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700 transition font-medium">
            <i data-lucide="edit-3" class="w-4 h-4"></i>
            Editar
        </a>
    </div>
</nav>

<main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
    <section class="mb-6 rounded-2xl overflow-hidden border border-gray-200 bg-white">
        <div class="relative h-56 sm:h-72">
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
                <h2 class="font-bold text-gray-900 text-lg">Plan de estudios</h2>
                <p class="text-sm text-gray-500 mt-1">Selecciona una leccion para ver su contenido.</p>
            </div>

            <div class="p-5 border-b border-gray-100">
                <div class="flex justify-between text-sm mb-2">
                    <span class="text-gray-600">Progreso de vista previa</span>
                    <span id="progressLabel" class="font-semibold text-indigo-600">0%</span>
                </div>
                <div class="h-2 rounded-full bg-gray-100 overflow-hidden">
                    <div id="progressBar" class="h-full bg-brand rounded-full w-0 transition-all duration-300"></div>
                </div>
            </div>

            <div class="max-h-[520px] overflow-y-auto p-4 space-y-4">
                <c:choose>
                    <c:when test="${not empty curso.modulos}">
                        <c:forEach var="modulo" items="${curso.modulos}" varStatus="mStatus">
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
                            <p class="text-sm text-gray-500">No hay modulos todavia.</p>
                            <a href="${pageContext.request.contextPath}/edit-course?id=${curso.id}"
                               class="inline-flex items-center gap-2 mt-3 px-3 py-2 rounded-lg bg-indigo-600 text-white text-sm hover:bg-indigo-700 transition">
                                <i data-lucide="plus" class="w-4 h-4"></i>
                                Agregar contenido
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </aside>

        <section class="lg:col-span-8 space-y-6">
            <article class="bg-white border border-gray-200 rounded-2xl p-6 sm:p-8">
                <div class="flex items-start justify-between gap-4">
                    <div>
                        <p id="lessonModule" class="text-sm font-semibold text-indigo-600 mb-2">Vista general</p>
                        <h2 id="lessonTitle" class="text-2xl sm:text-3xl font-bold text-gray-900">Descripcion del curso</h2>
                    </div>
                    <span id="lessonOrder" class="px-3 py-1 rounded-full bg-gray-100 text-gray-600 text-xs font-semibold">-</span>
                </div>
                <div class="mt-6 border-t border-gray-100 pt-6">
                    <p id="lessonContent" class="text-gray-700 leading-relaxed whitespace-pre-wrap"><c:out value="${empty curso.descripcion ? 'Este curso todavia no tiene descripcion.' : curso.descripcion}" /></p>
                </div>
            </article>

            <article class="bg-white border border-gray-200 rounded-2xl p-6">
                <h3 class="font-bold text-gray-900 mb-3">Resumen del curso</h3>
                <div class="grid grid-cols-3 gap-3">
                    <div class="rounded-xl bg-indigo-50 p-4 text-center">
                        <div class="text-xl font-bold text-indigo-700"><c:out value="${empty curso.modulosCount ? 0 : curso.modulosCount}" /></div>
                        <div class="text-xs text-indigo-700/80">Modulos</div>
                    </div>
                    <div class="rounded-xl bg-rose-50 p-4 text-center">
                        <div class="text-xl font-bold text-rose-700"><c:out value="${empty curso.likesCount ? 0 : curso.likesCount}" /></div>
                        <div class="text-xs text-rose-700/80">Likes</div>
                    </div>
                    <div class="rounded-xl bg-emerald-50 p-4 text-center">
                        <div class="text-xl font-bold text-emerald-700"><c:out value="${empty curso.inscritosCount ? 0 : curso.inscritosCount}" /></div>
                        <div class="text-xs text-emerald-700/80">Inscritos</div>
                    </div>
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

    const seenLessons = new Set();
    const totalLessons = lessonItems.length;

    function setProgress() {
        const percent = totalLessons === 0 ? 0 : Math.round((seenLessons.size / totalLessons) * 100);
        progressBar.style.width = percent + '%';
        progressLabel.textContent = percent + '%';
    }

    function selectLesson(item) {
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

        seenLessons.add(order);
        setProgress();
    }

    lessonItems.forEach((item) => {
        item.addEventListener('click', () => selectLesson(item));
    });

    if (lessonItems.length > 0) {
        selectLesson(lessonItems[0]);
    } else {
        setProgress();
    }
</script>
</body>
</html>
