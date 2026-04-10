<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Curso - RedSaberes</title>

    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Lucide Icons -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <style>
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
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to   { opacity: 1; transform: translateY(0); }
        }
        .fade-in { animation: fadeIn 0.5s ease-out; }
        .backdrop-blur-md {
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
        }
    </style>
</head>
<body class="bg-gradient-main min-h-screen">

<!-- Navigation Header -->
<!-- CORRECCIÓN 2: rutas usan contextPath, no referencias directas a .jsp -->
<nav class="bg-white/80 backdrop-blur-md border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">

            <a href="${pageContext.request.contextPath}/dashboard" class="flex items-center gap-2">
                <div class="w-10 h-10 bg-gradient-brand rounded-xl flex items-center justify-center">
                    <i data-lucide="book-open" class="w-6 h-6 text-white"></i>
                </div>
                <span class="text-2xl font-bold text-gradient-brand">RedSaberes</span>
            </a>

            <!-- CORRECCIÓN 4: un solo botón submit, publicar es CU-07 -->
            <div class="flex items-center gap-4">
                <a href="${pageContext.request.contextPath}/my-courses"
                   class="flex items-center gap-2 px-4 py-2 text-gray-700 hover:text-gray-900 transition">
                    <i data-lucide="arrow-left" class="w-5 h-5"></i>
                    <span class="hidden sm:inline">Mis Cursos</span>
                </a>
                <button type="submit"
                        form="courseForm"
                        class="flex items-center gap-2 px-6 py-2 bg-gradient-brand text-white rounded-lg hover:shadow-lg transition">
                    <i data-lucide="save" class="w-5 h-5"></i>
                    <span>Guardar borrador</span>
                </button>
            </div>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

    <!-- Back Button -->
    <!-- CORRECCIÓN 2: ruta corregida -->
    <a href="${pageContext.request.contextPath}/my-courses"
       class="inline-flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-6 transition fade-in">
        <i data-lucide="arrow-left" class="w-5 h-5"></i>
        <span>Volver a Mis Cursos</span>
    </a>

    <!-- Page Header -->
    <div class="mb-8 fade-in">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Crear Nuevo Curso</h1>
        <p class="text-gray-600">
            Completa la información básica. Podrás agregar módulos y lecciones después de guardar.
        </p>
    </div>

    <!-- Error Message -->
    <c:if test="${not empty error}">
        <div class="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg flex items-center gap-2 fade-in">
            <i data-lucide="alert-circle" class="w-5 h-5 flex-shrink-0"></i>
            <p class="text-sm">${error}</p>
        </div>
    </c:if>

    <!-- Main Form -->
    <!-- CORRECCIÓN 1: name de campos corregidos para coincidir con CursoServlet -->
    <form id="courseForm"
          action="${pageContext.request.contextPath}/create-course"
          method="POST"
          enctype="multipart/form-data">

        <!-- Course Information -->
        <div class="bg-white p-8 rounded-xl shadow-sm border border-gray-100 mb-6 fade-in">
            <h2 class="text-xl font-bold text-gray-900 mb-6">Información del Curso</h2>

            <div class="space-y-6">

                <!-- Título — CORRECCIÓN 1: name="titulo" (antes "title") -->
                <div>
                    <label for="tituloInput" class="block text-sm font-medium text-gray-700 mb-2">
                        Título del Curso (máx. 100 caracteres) *
                    </label>
                    <input type="text"
                           id="tituloInput"
                           name="titulo"
                           maxlength="100"
                           required
                           value="${param.titulo}"
                           class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                           placeholder="Ej: Desarrollo Web con React" />
                    <div class="text-xs text-gray-500 mt-1">
                        <span id="charCount">0</span>/100 caracteres
                    </div>
                </div>

                <!-- Descripción — CORRECCIÓN 1: name="descripcion" (antes "description") -->
                <div>
                    <label for="descripcionInput" class="block text-sm font-medium text-gray-700 mb-2">
                        Descripción *
                    </label>
                    <textarea id="descripcionInput"
                              name="descripcion"
                              rows="4"
                              required
                              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition"
                              placeholder="Describe de qué trata tu curso...">${param.descripcion}</textarea>
                </div>

                <!-- Categoría y Nivel -->
                <div class="grid md:grid-cols-2 gap-6">

                    <!-- Categoría — CORRECCIÓN 1: name="categoria" (antes "category") -->
                    <div>
                        <label for="categoriaInput" class="block text-sm font-medium text-gray-700 mb-2">
                            Categoría *
                        </label>
                        <select id="categoriaInput"
                                name="categoria"
                                required
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition">
                            <option value="">Selecciona una categoría</option>
                            <option value="Programación"  ${param.categoria == 'Programación'  ? 'selected' : ''}>Programación</option>
                            <option value="Diseño"        ${param.categoria == 'Diseño'        ? 'selected' : ''}>Diseño</option>
                            <option value="Data Science"  ${param.categoria == 'Data Science'  ? 'selected' : ''}>Data Science</option>
                            <option value="Marketing"     ${param.categoria == 'Marketing'     ? 'selected' : ''}>Marketing</option>
                            <option value="Negocios"      ${param.categoria == 'Negocios'      ? 'selected' : ''}>Negocios</option>
                            <option value="Arte"          ${param.categoria == 'Arte'          ? 'selected' : ''}>Arte</option>
                            <option value="Música"        ${param.categoria == 'Música'        ? 'selected' : ''}>Música</option>
                            <option value="Idiomas"       ${param.categoria == 'Idiomas'       ? 'selected' : ''}>Idiomas</option>
                            <option value="Finanzas"      ${param.categoria == 'Finanzas'      ? 'selected' : ''}>Finanzas</option>
                            <option value="Salud"         ${param.categoria == 'Salud'         ? 'selected' : ''}>Salud</option>
                        </select>
                    </div>

                    <!-- Nivel — CORRECCIÓN 1: name="nivelDificultad" (antes "level") -->
                    <div>
                        <label for="nivelInput" class="block text-sm font-medium text-gray-700 mb-2">
                            Nivel de Dificultad *
                        </label>
                        <select id="nivelInput"
                                name="nivelDificultad"
                                required
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-600 focus:border-transparent transition">
                            <option value="Principiante" ${empty param.nivelDificultad || param.nivelDificultad == 'Principiante' ? 'selected' : ''}>Principiante</option>
                            <option value="Intermedio"   ${param.nivelDificultad == 'Intermedio'   ? 'selected' : ''}>Intermedio</option>
                            <option value="Avanzado"     ${param.nivelDificultad == 'Avanzado'     ? 'selected' : ''}>Avanzado</option>
                        </select>
                    </div>
                </div>

                <!-- Imagen de Portada — CORRECCIÓN 1: name="imagenPortada" (antes "imageFile") -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                        Imagen de Portada
                        <span class="text-gray-400 font-normal">(opcional, PNG o JPG hasta 5MB)</span>
                    </label>
                    <div id="dropzone"
                         class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-indigo-600 transition cursor-pointer"
                         onclick="document.getElementById('imagenPortada').click()">
                        <!-- CORRECCIÓN 1: id e name corregidos -->
                        <input type="file"
                               id="imagenPortada"
                               name="imagenPortada"
                               accept="image/*"
                               class="hidden"
                               onchange="handleFileSelect(event)" />
                        <i data-lucide="upload" class="w-12 h-12 text-gray-400 mx-auto mb-4"></i>
                        <p class="text-gray-600">Haz clic para subir una imagen o arrastra aquí</p>
                        <p class="text-sm text-gray-500 mt-2">PNG, JPG hasta 5MB</p>
                    </div>
                    <!-- Preview de imagen seleccionada -->
                    <div id="imagePreview" class="mt-4 hidden">
                        <img id="previewImg" src="" alt="Preview"
                             class="max-w-full h-48 object-cover rounded-lg mx-auto" />
                        <p id="fileName" class="text-xs text-center text-indigo-600 mt-2"></p>
                    </div>
                </div>

            </div>
        </div>

        <!-- CORRECCIÓN 3: sección de módulos ELIMINADA —
             los módulos se agregan en edit-course (CU-05)
             después de crear el curso como borrador        -->

        <!-- Botones inferiores -->
        <div class="flex gap-4 fade-in">
            <button type="submit"
                    class="flex-1 flex items-center justify-center gap-2 py-3 bg-gradient-brand text-white rounded-lg font-semibold hover:shadow-lg transition">
                <i data-lucide="save" class="w-5 h-5"></i>
                Guardar como borrador
            </button>
            <a href="${pageContext.request.contextPath}/my-courses"
               class="px-8 py-3 border border-gray-300 text-gray-600 rounded-lg hover:bg-gray-50 transition font-medium">
                Cancelar
            </a>
        </div>

    </form>
</div>

<!-- Scripts -->
<script>
    lucide.createIcons();

    // Contador de caracteres — CORRECCIÓN 1: referencia a tituloInput
    document.getElementById('tituloInput').addEventListener('input', function() {
        document.getElementById('charCount').textContent = this.value.length;
    });

    // Preview de imagen seleccionada
    // CORRECCIÓN 1: referencia a imagenPortada
    function handleFileSelect(event) {
        const file = event.target.files[0];
        if (!file) return;

        if (file.size > 5 * 1024 * 1024) {
            alert('El archivo es demasiado grande. Máximo 5MB.');
            event.target.value = '';
            return;
        }

        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('previewImg').src = e.target.result;
            document.getElementById('fileName').textContent = file.name;
            document.getElementById('imagePreview').classList.remove('hidden');
        };
        reader.readAsDataURL(file);
    }

    // Validación básica antes de submit
    document.getElementById('courseForm').addEventListener('submit', function(e) {
        const titulo      = document.getElementById('tituloInput').value.trim();
        const descripcion = document.getElementById('descripcionInput').value.trim();
        const categoria   = document.getElementById('categoriaInput').value;

        if (!titulo || !descripcion || !categoria) {
            e.preventDefault();
            alert('Por favor completa todos los campos obligatorios.');
        }
    });
</script>

</body>
</html>
