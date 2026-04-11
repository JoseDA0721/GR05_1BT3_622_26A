<%@ page import="logic.Curso" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Materiales del Curso SkillSwap</title>
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f4f2ff;
            min-height: 100vh;
            color: #1a1a2e;
        }

        nav {
            background: #ffffff;
            border-bottom: 1px solid #e8e4fb;
            padding: 0 2rem;
            height: 56px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
        }

        .brand-icon {
            width: 32px;
            height: 32px;
            background: #6c5ce7;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .brand-icon svg { width: 18px; height: 18px; }

        .brand-name {
            font-size: 16px;
            font-weight: 600;
            color: #1a1a2e;
        }

        .main {
            max-width: 820px;
            margin: 0 auto;
            padding: 2.5rem 1.5rem;
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            font-size: 13px;
            color: #6c5ce7;
            text-decoration: none;
            margin-bottom: 1.5rem;
        }

        .back-link:hover { text-decoration: underline; }

        /* DENIED STATE */
        .denied-card {
            background: #fff;
            border: 1px solid #fdd;
            border-radius: 16px;
            padding: 3rem 2rem;
            text-align: center;
        }

        .denied-icon {
            width: 60px;
            height: 60px;
            background: #fff0f0;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1.25rem;
            font-size: 26px;
        }

        .denied-title {
            font-size: 20px;
            font-weight: 600;
            color: #c0392b;
            margin-bottom: 0.5rem;
        }

        .denied-msg {
            font-size: 14px;
            color: #888;
            max-width: 340px;
            margin: 0 auto;
            line-height: 1.6;
        }

        /* GRANTED STATE */
        .course-header {
            background: #fff;
            border: 1px solid #e8e4fb;
            border-radius: 16px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .course-thumb {
            width: 64px;
            height: 64px;
            border-radius: 12px;
            background: linear-gradient(135deg, #6c5ce7 0%, #a29bfe 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            flex-shrink: 0;
        }

        .course-meta { flex: 1; }

        .course-title {
            font-size: 18px;
            font-weight: 600;
            color: #1a1a2e;
            margin-bottom: 4px;
        }

        .access-badge {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            background: #eafaf1;
            border: 1px solid #a9dfbf;
            border-radius: 20px;
            padding: 3px 12px;
            font-size: 12px;
            font-weight: 500;
            color: #1e8449;
        }

        /* MODULES */
        .section-label {
            font-size: 13px;
            font-weight: 600;
            color: #aaa;
            text-transform: uppercase;
            letter-spacing: 0.06em;
            margin-bottom: 0.75rem;
        }

        .modules-list {
            display: flex;
            flex-direction: column;
            gap: 10px;
            margin-bottom: 1.75rem;
        }

        .module-card {
            background: #fff;
            border: 1px solid #e8e4fb;
            border-radius: 12px;
            overflow: hidden;
        }

        .module-header {
            padding: 1rem 1.25rem;
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .module-num {
            width: 28px;
            height: 28px;
            border-radius: 8px;
            background: #ede9fd;
            color: #6c5ce7;
            font-size: 12px;
            font-weight: 600;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
        }

        .module-title {
            font-size: 14px;
            font-weight: 600;
            color: #1a1a2e;
            flex: 1;
        }

        .module-count {
            font-size: 12px;
            color: #aaa;
        }

        .lessons-list {
            border-top: 1px solid #f0edfb;
        }

        .lesson-row {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 0.75rem 1.25rem;
            border-bottom: 1px solid #f8f7fe;
            font-size: 13px;
            color: #444;
        }

        .lesson-row:last-child { border-bottom: none; }

        .lesson-icon {
            width: 28px;
            height: 28px;
            border-radius: 50%;
            background: #f4f2ff;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            font-size: 13px;
        }

        .lesson-name { flex: 1; }

        .lesson-duration {
            font-size: 12px;
            color: #bbb;
        }

        /* RESOURCES */
        .resources-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 10px;
        }

        .resource-card {
            background: #fff;
            border: 1px solid #e8e4fb;
            border-radius: 12px;
            padding: 1rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .resource-icon {
            width: 36px;
            height: 36px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
            flex-shrink: 0;
        }

        .res-pdf { background: #fff0f0; }
        .res-video { background: #fff8e1; }
        .res-link { background: #e8f5e9; }
        .res-zip { background: #ede9fd; }

        .resource-name {
            font-size: 13px;
            font-weight: 500;
            color: #1a1a2e;
            line-height: 1.3;
        }

        .resource-type {
            font-size: 11px;
            color: #aaa;
            margin-top: 2px;
        }
    </style>
</head>
<body>

<%
    logic.DatosEstudiante usuario =
            (logic.DatosEstudiante) session.getAttribute("usuarioLogeado");

    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Curso curso = (Curso) request.getAttribute("curso");
    Boolean accesoConcedido = (Boolean) request.getAttribute("accesoConcedido");
    if (accesoConcedido == null) {
        accesoConcedido = false;
    }
%>

<nav>
    <a class="brand" href="#">
        <div class="brand-icon">
            <svg viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M10 3L17 7V13L10 17L3 13V7L10 3Z" stroke="white" stroke-width="1.5" stroke-linejoin="round"/>
                <circle cx="10" cy="10" r="2" fill="white"/>
            </svg>
        </div>
        <span class="brand-name">SkillSwap</span>
    </a>
</nav>

<div class="main">

    <a href="index.jsp" class="back-link">&#8592; Volver al inicio</a>

    <% if (accesoConcedido) { %>

        <div class="course-header">
            <div class="course-thumb">&#128218;</div>
            <div class="course-meta">
                <p class="course-title"><%= curso != null ? curso.getNombre_curso() : "Curso" %></p>
                <span class="access-badge">&#10003; Acceso concedido</span>
            </div>
        </div>

        <p class="section-label">Modulos del curso</p>
        <div class="modules-list">

            <div class="module-card">
                <div class="module-header">
                    <div class="module-num">1</div>
                    <p class="module-title">Introduccion y fundamentos</p>
                    <span class="module-count">3 lecciones</span>
                </div>
                <div class="lessons-list">
                    <div class="lesson-row">
                        <div class="lesson-icon">&#9654;</div>
                        <span class="lesson-name">Bienvenida al curso</span>
                        <span class="lesson-duration">5 min</span>
                    </div>
                    <div class="lesson-row">
                        <div class="lesson-icon">&#9654;</div>
                        <span class="lesson-name">Conceptos clave</span>
                        <span class="lesson-duration">12 min</span>
                    </div>
                    <div class="lesson-row">
                        <div class="lesson-icon">&#128196;</div>
                        <span class="lesson-name">Lectura: Guia de inicio</span>
                        <span class="lesson-duration">PDF</span>
                    </div>
                </div>
            </div>

            <div class="module-card">
                <div class="module-header">
                    <div class="module-num">2</div>
                    <p class="module-title">Practica y aplicacion</p>
                    <span class="module-count">4 lecciones</span>
                </div>
                <div class="lessons-list">
                    <div class="lesson-row">
                        <div class="lesson-icon">&#9654;</div>
                        <span class="lesson-name">Ejercicio practico 1</span>
                        <span class="lesson-duration">18 min</span>
                    </div>
                    <div class="lesson-row">
                        <div class="lesson-icon">&#9654;</div>
                        <span class="lesson-name">Ejercicio practico 2</span>
                        <span class="lesson-duration">22 min</span>
                    </div>
                    <div class="lesson-row">
                        <div class="lesson-icon">&#128196;</div>
                        <span class="lesson-name">Plantilla de trabajo</span>
                        <span class="lesson-duration">ZIP</span>
                    </div>
                    <div class="lesson-row">
                        <div class="lesson-icon">&#9654;</div>
                        <span class="lesson-name">Revision y feedback</span>
                        <span class="lesson-duration">10 min</span>
                    </div>
                </div>
            </div>

            <div class="module-card">
                <div class="module-header">
                    <div class="module-num">3</div>
                    <p class="module-title">Proyecto final</p>
                    <span class="module-count">2 lecciones</span>
                </div>
                <div class="lessons-list">
                    <div class="lesson-row">
                        <div class="lesson-icon">&#9654;</div>
                        <span class="lesson-name">Instrucciones del proyecto</span>
                        <span class="lesson-duration">8 min</span>
                    </div>
                    <div class="lesson-row">
                        <div class="lesson-icon">&#9654;</div>
                        <span class="lesson-name">Cierre y proximos pasos</span>
                        <span class="lesson-duration">6 min</span>
                    </div>
                </div>
            </div>

        </div>

        <p class="section-label">Recursos descargables</p>
        <div class="resources-grid">
            <div class="resource-card">
                <div class="resource-icon res-pdf">&#128196;</div>
                <div>
                    <p class="resource-name">Guia completa</p>
                    <p class="resource-type">PDF -> 2.4 MB</p>
                </div>
            </div>
            <div class="resource-card">
                <div class="resource-icon res-video">&#127916;</div>
                <div>
                    <p class="resource-name">Grabacion sesion en vivo</p>
                    <p class="resource-type">Video -> 1.2 GB</p>
                </div>
            </div>
            <div class="resource-card">
                <div class="resource-icon res-zip">&#128230;</div>
                <div>
                    <p class="resource-name">Archivos del proyecto</p>
                    <p class="resource-type">ZIP -> 18 MB</p>
                </div>
            </div>
            <div class="resource-card">
                <div class="resource-icon res-link">&#128279;</div>
                <div>
                    <p class="resource-name">Recursos externos</p>
                    <p class="resource-type">Enlaces recomendados</p>
                </div>
            </div>
        </div>

    <% } else { %>

        <div class="denied-card">
            <div class="denied-icon">&#128274;</div>
            <p class="denied-title">Acceso denegado</p>
            <p class="denied-msg">Solo el creador del curso puede habilitar este acceso mediante Match.</p>
        </div>

    <% } %>

</div>
</body>
</html>
