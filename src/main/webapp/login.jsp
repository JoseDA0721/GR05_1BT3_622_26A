<%@ page import = "java.util.List" %>
<%@ page import = "logic.DatosEstudiante" %>
<%@ page import = "controller.LoginServlet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login SkillSwap</title>
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f4f2ff;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .page-wrapper {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 100%;
            padding: 2rem 1rem;
        }

        .card {
            background: #ffffff;
            border-radius: 16px;
            border: 1px solid #e8e4fb;
            padding: 2.5rem 2rem;
            width: 100%;
            max-width: 420px;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 2rem;
        }

        .brand-icon {
            width: 36px;
            height: 36px;
            background: #6c5ce7;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .brand-icon svg {
            width: 20px;
            height: 20px;
        }

        .brand-name {
            font-size: 18px;
            font-weight: 600;
            color: #1a1a2e;
        }

        .card-title {
            font-size: 22px;
            font-weight: 600;
            color: #1a1a2e;
            margin-bottom: 0.25rem;
        }

        .card-subtitle {
            font-size: 14px;
            color: #888;
            margin-bottom: 1.75rem;
        }

        .form-group {
            margin-bottom: 1.25rem;
        }

        label {
            display: block;
            font-size: 13px;
            font-weight: 500;
            color: #444;
            margin-bottom: 6px;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px 14px;
            font-size: 14px;
            border: 1px solid #ddd;
            border-radius: 8px;
            outline: none;
            color: #1a1a2e;
            background: #fafafa;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        input[type="text"]:focus,
        input[type="password"]:focus {
            border-color: #6c5ce7;
            box-shadow: 0 0 0 3px rgba(108, 92, 231, 0.12);
            background: #fff;
        }

        .error-msg {
            display: flex;
            align-items: center;
            gap: 8px;
            background: #fff0f0;
            border: 1px solid #ffd0d0;
            border-radius: 8px;
            padding: 10px 14px;
            font-size: 13px;
            color: #c0392b;
            margin-bottom: 1.25rem;
        }

        .error-dot {
            width: 6px;
            height: 6px;
            border-radius: 50%;
            background: #e74c3c;
            flex-shrink: 0;
        }

        .btn-submit {
            width: 100%;
            padding: 11px;
            font-size: 15px;
            font-weight: 500;
            color: #fff;
            background: #6c5ce7;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: background 0.2s, transform 0.1s;
            margin-top: 0.25rem;
        }

        .btn-submit:hover { background: #5a4bd1; }
        .btn-submit:active { transform: scale(0.99); }
    </style>
</head>
<body>
<div class="page-wrapper">
    <div class="card">

        <div class="brand">
            <div class="brand-icon">
                <svg viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M10 3L17 7V13L10 17L3 13V7L10 3Z" stroke="white" stroke-width="1.5" stroke-linejoin="round"/>
                    <circle cx="10" cy="10" r="2" fill="white"/>
                </svg>
            </div>
            <span class="brand-name">SkillSwap</span>
        </div>

        <h1 class="card-title">Bienvenido de nuevo</h1>
        <p class="card-subtitle">Ingresa tus credenciales para continuar</p>

        <form action="LoginServlet" method="POST">

            <% String error = (String) request.getAttribute("error");
               if (error != null) { %>
            <div class="error-msg">
                <span class="error-dot"></span>
                <%= error %>
            </div>
            <% } %>

            <div class="form-group">
                <label for="usuario">Usuario</label>
                <input type="text" id="usuario" name="usuario" placeholder="Ingresa tu Usuario" required>
            </div>

            <div class="form-group">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena" placeholder="Ingresa tu Contraseña" required>
            </div>

            <button type="submit" class="btn-submit">Iniciar sesion</button>

        </form>
    </div>
</div>
</body>
</html>
