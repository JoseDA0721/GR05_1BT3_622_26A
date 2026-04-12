<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="logic.Curso" %>
<%@ page import="logic.DatosEstudiante" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Likes del Curso SkillSwap</title>
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
            max-width: 700px;
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

        .page-title {
            font-size: 22px;
            font-weight: 600;
            color: #1a1a2e;
            margin-bottom: 0.25rem;
        }

        .course-name {
            font-size: 14px;
            color: #888;
            margin-bottom: 1.75rem;
        }

        .stat-badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: #fff0f5;
            border: 1px solid #f5c6d8;
            border-radius: 20px;
            padding: 4px 14px;
            font-size: 13px;
            font-weight: 500;
            color: #d63384;
            margin-bottom: 1.5rem;
        }

        .empty-state {
            background: #fff;
            border: 1px solid #e8e4fb;
            border-radius: 16px;
            padding: 3rem 2rem;
            text-align: center;
            color: #aaa;
            font-size: 14px;
        }

        .users-list {
            display: flex;
            flex-direction: column;
            gap: 10px;
            list-style: none;
        }

        .user-row {
            background: #ffffff;
            border: 1px solid #e8e4fb;
            border-radius: 12px;
            padding: 1rem 1.25rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .avatar {
            width: 38px;
            height: 38px;
            border-radius: 50%;
            background: #ede9fd;
            color: #6c5ce7;
            font-size: 13px;
            font-weight: 600;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
        }

        .user-full-name {
            font-size: 14px;
            font-weight: 500;
            color: #1a1a2e;
        }

        .user-handle {
            font-size: 12px;
            color: #aaa;
            margin-top: 2px;
        }

        .match-badge {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            background: #eafaf1;
            border: 1px solid #a9dfbf;
            border-radius: 20px;
            padding: 4px 12px;
            font-size: 12px;
            font-weight: 500;
            color: #1e8449;
            white-space: nowrap;
        }

        .btn-match {
            padding: 7px 16px;
            font-size: 13px;
            font-weight: 500;
            color: #fff;
            background: #6c5ce7;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            white-space: nowrap;
            transition: background 0.2s, transform 0.1s;
        }

        .btn-match:hover { background: #5a4bd1; }
        .btn-match:active { transform: scale(0.98); }
    </style>
</head>
<body>

<%
    Curso curso = (Curso) request.getAttribute("curso");
    List<DatosEstudiante> usuariosLike =
            (List<DatosEstudiante>) request.getAttribute("usuariosLike");
    Boolean esCreador = (Boolean) request.getAttribute("esCreador");
    Set<Integer> idsMatch = (Set<Integer>) request.getAttribute("idsMatch");
    if (esCreador == null) {
        esCreador = false;
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

    <h1 class="page-title">Usuarios que dieron like</h1>
    <% if (curso != null) { %>
    <p class="course-name"><%= curso.getNombre_curso() %></p>
    <% } %>

    <% if (usuariosLike == null || usuariosLike.isEmpty()) { %>
    <div class="empty-state">Este curso aun no tiene likes.</div>
    <% } else { %>

    <div class="stat-badge">
        <%= usuariosLike.size() %> like <%= usuariosLike.size() != 1 ? "s" : "" %>
    </div>

    <ul class="users-list">
        <% for (DatosEstudiante est : usuariosLike) {
            boolean yaTieneMatch = idsMatch != null && idsMatch.contains(est.getId());
        %>
        <li class="user-row">
            <div class="user-info">
                <div class="avatar"><%= est.getNombre().substring(0,1).toUpperCase() %><%= est.getApellido().substring(0,1).toUpperCase() %></div>
                <div>
                    <p class="user-full-name"><%= est.getNombre() %> <%= est.getApellido() %></p>
                    <p class="user-handle">@<%= est.getUsuario() %></p>
                </div>
            </div>

            <% if (esCreador) { %>
                <% if (yaTieneMatch) { %>
                    <span class="match-badge">&#10003; Match aplicado</span>
                <% } else { %>
                    <form action="MatchCursoServlet" method="post">
                        <input type="hidden" name="idCurso" value="<%= curso.getId_curso() %>">
                        <input type="hidden" name="idUsuarioObjetivo" value="<%= est.getId() %>">
                        <button type="submit" class="btn-match">Match</button>
                    </form>
                <% } %>
            <% } %>
        </li>
        <% } %>
    </ul>

    <% } %>
</div>
</body>
</html>
