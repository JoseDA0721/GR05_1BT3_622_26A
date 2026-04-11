<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="logic.Curso" %>
<%@ page import="logic.dao.CursoDAO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Cursos SkillSwap</title>
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f4f2ff;
            min-height: 100vh;
            color: #1a1a2e;
        }

        /* NAV */
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

        .nav-user {
            width: 34px;
            height: 34px;
            border-radius: 50%;
            background: #6c5ce7;
            color: #fff;
            font-size: 13px;
            font-weight: 600;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        /* MAIN LAYOUT */
        .main {
            max-width: 960px;
            margin: 0 auto;
            padding: 2.5rem 1.5rem;
        }

        /* CREATE COURSE SECTION */
        .section-card {
            background: #ffffff;
            border: 1px solid #e8e4fb;
            border-radius: 16px;
            padding: 1.75rem;
            margin-bottom: 2rem;
        }

        .section-title {
            font-size: 18px;
            font-weight: 600;
            color: #1a1a2e;
            margin-bottom: 0.25rem;
        }

        .section-subtitle {
            font-size: 13px;
            color: #888;
            margin-bottom: 1.25rem;
        }

        .input-row {
            display: flex;
            gap: 12px;
            align-items: center;
        }

        .input-row input[type="text"] {
            flex: 1;
            padding: 10px 14px;
            font-size: 14px;
            border: 1px solid #ddd;
            border-radius: 8px;
            outline: none;
            background: #fafafa;
            color: #1a1a2e;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .input-row input[type="text"]:focus {
            border-color: #6c5ce7;
            box-shadow: 0 0 0 3px rgba(108, 92, 231, 0.12);
            background: #fff;
        }

        .input-row input[type="text"]::placeholder { color: #bbb; }

        .btn-primary {
            padding: 10px 20px;
            font-size: 14px;
            font-weight: 500;
            color: #fff;
            background: #6c5ce7;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            white-space: nowrap;
            transition: background 0.2s, transform 0.1s;
        }

        .btn-primary:hover { background: #5a4bd1; }
        .btn-primary:active { transform: scale(0.98); }

        /* COURSES LIST HEADER */
        .list-header {
            display: flex;
            align-items: baseline;
            justify-content: space-between;
            margin-bottom: 1rem;
        }

        .list-title {
            font-size: 18px;
            font-weight: 600;
            color: #1a1a2e;
        }

        /* COURSE CARDS GRID */
        .courses-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 1rem;
        }

        .course-card {
            background: #ffffff;
            border: 1px solid #e8e4fb;
            border-radius: 16px;
            padding: 1.25rem 1.5rem;
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .course-name {
            font-size: 15px;
            font-weight: 600;
            color: #1a1a2e;
            line-height: 1.4;
        }

        .course-actions {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
        }

        .btn-action {
            flex: 1;
            min-width: 80px;
            padding: 8px 10px;
            font-size: 12px;
            font-weight: 500;
            border-radius: 8px;
            border: 1px solid #e0ddf8;
            background: #f9f8ff;
            color: #6c5ce7;
            cursor: pointer;
            text-align: center;
            transition: background 0.15s, border-color 0.15s;
        }

        .btn-action:hover {
            background: #ede9fd;
            border-color: #c5bff5;
        }

        .btn-like {
            background: #fff0f5;
            color: #d63384;
            border-color: #f5c6d8;
        }

        .btn-like:hover {
            background: #fce0ec;
            border-color: #f0a0c0;
        }

        .divider { display: none; }
    </style>
</head>
<body>

<%
logic.DatosEstudiante usuario =
        (logic.DatosEstudiante)
        session.getAttribute("usuarioLogeado");

if(usuario == null){
    response.sendRedirect("login.jsp");
    return;
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
    <div class="nav-user">U</div>
</nav>

<div class="main">

    <div class="section-card">
        <p class="section-title">Crear nuevo curso</p>
        <p class="section-subtitle">Comparte tu conocimiento con la comunidad</p>
        <form action="CrearCursoServlet" method="POST">
            <div class="input-row">
                <input type="text" name="nombre_curso" placeholder="Ej. Desarrollo Web con React" required>
                <button type="submit" class="btn-primary">+ Crear Curso</button>
            </div>
        </form>
    </div>

    <div class="list-header">
        <h2 class="list-title">Cursos disponibles</h2>
    </div>

    <div class="courses-grid">
        <%
        List<Curso> curso = new CursoDAO().listar();
        for(Curso c : curso){ %>
        <div class="course-card">
            <p class="course-name"><%= c.getNombre_curso() %></p>
            <div class="course-actions">

                <form action="LikeCursoServlet" method="post">
                    <input type="hidden" name="idCurso" value="<%= c.getId_curso() %>">
                    <button type="submit" class="btn-action btn-like">Like</button>
                </form>

                <form action="VerLikesCursoServlet" method="get">
                    <input type="hidden" name="idCurso" value="<%= c.getId_curso() %>">
                    <button type="submit" class="btn-action">Ver Likes</button>
                </form>

                <form action="MaterialCursoServlet" method="get">
                    <input type="hidden" name="idCurso" value="<%= c.getId_curso() %>">
                    <button type="submit" class="btn-action">Materiales</button>
                </form>

            </div>
        </div>
        <% } %>
    </div>

</div>
</body>
</html>
