<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${course.title} - SkillSwap</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap');

    * {
      font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
    }

    .backdrop-blur-md {
      backdrop-filter: blur(12px);
    }

    .prose {
      max-width: none;
    }

    .prose p {
      margin-bottom: 1rem;
    }
  </style>
</head>
<body class="min-h-screen flex flex-col bg-gray-50">
<c:choose>
  <c:when test="${!hasMatch}">
    <!-- Locked Content Screen -->
    <div class="min-h-screen flex items-center justify-center">
      <div class="text-center max-w-md px-4">
        <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-10 h-10 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
          </svg>
        </div>
        <h2 class="text-2xl font-bold text-gray-900 mb-4">Contenido Bloqueado</h2>
        <p class="text-gray-600 mb-8">
          Necesitas tener un match confirmado con el creador para acceder a este curso.
        </p>
        <a href="/explore" class="inline-block px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
          Explorar Cursos
        </a>
      </div>
    </div>
  </c:when>
  <c:otherwise>
    <!-- Header -->
    <nav class="bg-white/80 backdrop-blur-md border-b border-gray-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <a href="/dashboard" class="flex items-center gap-2">
            <div class="w-10 h-10 bg-gradient-to-br from-indigo-600 to-purple-600 rounded-xl flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
            </div>
            <span class="text-2xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                                SkillSwap
                            </span>
          </a>
          <a href="/explore" class="text-gray-600 hover:text-gray-900 transition">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
            </svg>
          </a>
        </div>
      </div>
    </nav>

    <div class="flex-1 flex">
      <!-- Sidebar - Course Content -->
      <div class="w-80 bg-white border-r border-gray-200 overflow-y-auto">
        <div class="p-6">
          <h2 class="text-xl font-bold text-gray-900 mb-1">${course.title}</h2>
          <p class="text-sm text-gray-600 mb-4">por ${course.author}</p>

          <!-- Progress -->
          <div class="mb-6">
            <div class="flex items-center justify-between text-sm mb-2">
              <span class="text-gray-600">Progreso del curso</span>
              <span class="font-semibold text-indigo-600">${course.progress}%</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2">
              <div class="bg-indigo-600 h-2 rounded-full" style="width: ${course.progress}%"></div>
            </div>
          </div>

          <!-- Modules -->
          <div class="space-y-4">
            <c:forEach var="module" items="${course.modules}">
              <div>
                <h3 class="font-semibold text-gray-900 mb-2">${module.title}</h3>
                <div class="space-y-1">
                  <c:forEach var="lesson" items="${module.lessons}">
                    <a
                            href="?lessonId=${lesson.id}"
                            class="block w-full text-left px-4 py-3 rounded-lg transition ${lessonId == lesson.id ? 'bg-indigo-50 text-indigo-600' : 'hover:bg-gray-50 text-gray-700'}"
                    >
                      <div class="flex items-center gap-3">
                        <c:choose>
                          <c:when test="${lesson.completed}">
                            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-green-600 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                          </c:when>
                          <c:otherwise>
                            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-gray-400 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                          </c:otherwise>
                        </c:choose>
                        <div class="flex-1 min-w-0">
                          <div class="text-sm font-medium truncate">${lesson.title}</div>
                          <div class="text-xs text-gray-500">${lesson.duration}</div>
                        </div>
                      </div>
                    </a>
                  </c:forEach>
                </div>
              </div>
            </c:forEach>
          </div>
        </div>
      </div>

      <!-- Main Content Area -->
      <div class="flex-1 overflow-y-auto bg-gray-50">
        <div class="max-w-4xl mx-auto p-8">
          <!-- Video/Content Area -->
          <div class="bg-gray-900 rounded-xl aspect-video mb-8 flex items-center justify-center">
            <div class="text-center text-white">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-20 h-20 mx-auto mb-4 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <p class="text-lg">Contenido de la lección: ${currentLesson.title}</p>
            </div>
          </div>

          <!-- Lesson Details -->
          <div class="bg-white rounded-xl p-8 shadow-sm border border-gray-100">
            <h1 class="text-3xl font-bold text-gray-900 mb-4">${currentLesson.title}</h1>
            <p class="text-gray-600 mb-6">
              En esta lección aprenderás los conceptos fundamentales para dominar esta tecnología.
              Los temas cubiertos incluyen los fundamentos teóricos y ejercicios prácticos.
            </p>

            <div class="border-t border-gray-200 pt-6 mt-6">
              <h3 class="font-semibold text-gray-900 mb-4">Contenido de la lección</h3>
              <div class="prose max-w-none text-gray-600">
                  ${currentLesson.content}
              </div>
            </div>

            <div class="flex gap-4 mt-8">
              <form action="/api/lessons/${currentLesson.id}/complete" method="POST" style="display: inline;">
                <button type="submit" class="px-6 py-3 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-lg hover:shadow-lg transition">
                  Marcar como Completada
                </button>
              </form>
              <c:if test="${not empty nextLesson}">
                <a href="?lessonId=${nextLesson.id}" class="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition">
                  Siguiente Lección →
                </a>
              </c:if>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:otherwise>
</c:choose>
</body>
</html>