<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${curso.titulo} - RedSaberes</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=DM+Mono:wght@400;500&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'DM Sans', sans-serif; }

        :root {
            --brand: #5b47e0;
            --brand-light: #ede9ff;
            --brand-mid: #7c6ff7;
            --surface: #f8f7ff;
            --border: #e8e5f5;
            --text-primary: #1a1530;
            --text-secondary: #6b6694;
            --sidebar-w: 300px;
        }

        body { background: var(--surface); color: var(--text-primary); }

        /* ── TOP NAV ── */
        .top-nav {
            position: sticky;
            top: 0;
            z-index: 50;
            height: 56px;
            background: #fff;
            border-bottom: 1px solid var(--border);
            display: flex;
            align-items: center;
            padding: 0 1.25rem;
            gap: 1rem;
        }
        .nav-logo {
            display: flex;
            align-items: center;
            gap: .5rem;
            font-weight: 700;
            font-size: 1.15rem;
            color: var(--brand);
            text-decoration: none;
        }
        .nav-logo-icon {
            width: 32px;
            height: 32px;
            background: var(--brand);
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .nav-back {
            margin-left: auto;
            display: flex;
            align-items: center;
            gap: .4rem;
            font-size: .85rem;
            color: var(--text-secondary);
            text-decoration: none;
            padding: .35rem .75rem;
            border-radius: 8px;
            transition: background .15s;
        }
        .nav-back:hover { background: var(--brand-light); color: var(--brand); }

        /* ── LAYOUT ── */
        .course-layout {
            display: flex;
            height: calc(100vh - 56px);
            overflow: hidden;
        }

        /* ── SIDEBAR ── */
        .sidebar {
            width: var(--sidebar-w);
            min-width: var(--sidebar-w);
            background: #fff;
            border-right: 1px solid var(--border);
            display: flex;
            flex-direction: column;
            overflow: hidden;
        }
        .sidebar-header {
            padding: 1.25rem 1.25rem .75rem;
            border-bottom: 1px solid var(--border);
        }
        .course-title-sidebar {
            font-size: .95rem;
            font-weight: 700;
            line-height: 1.35;
            color: var(--text-primary);
            margin-bottom: .15rem;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        .course-author {
            font-size: .78rem;
            color: var(--text-secondary);
        }

        /* Progress */
        .progress-wrap { padding: .85rem 1.25rem; border-bottom: 1px solid var(--border); }
        .progress-label {
            display: flex;
            justify-content: space-between;
            font-size: .78rem;
            color: var(--text-secondary);
            margin-bottom: .45rem;
        }
        .progress-pct { font-weight: 700; color: var(--brand); }
        .progress-track {
            height: 6px;
            background: var(--brand-light);
            border-radius: 99px;
            overflow: hidden;
        }
        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, var(--brand), var(--brand-mid));
            border-radius: 99px;
            transition: width .4s ease;
        }

        /* Module list */
        .sidebar-modules {
            flex: 1;
            overflow-y: auto;
            padding: .75rem 0 2rem;
        }
        .module-section { margin-bottom: .25rem; }
        .module-label {
            font-size: .72rem;
            font-weight: 700;
            letter-spacing: .06em;
            text-transform: uppercase;
            color: var(--text-secondary);
            padding: .6rem 1.25rem .3rem;
        }
        .lesson-btn {
            width: 100%;
            display: flex;
            align-items: center;
            gap: .75rem;
            padding: .6rem 1.25rem;
            border: none;
            background: transparent;
            cursor: pointer;
            text-align: left;
            transition: background .12s;
            border-left: 3px solid transparent;
        }
        .lesson-btn:hover { background: var(--surface); }
        .lesson-btn.active {
            background: var(--brand-light);
            border-left-color: var(--brand);
        }
        .lesson-icon {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            border: 2px solid var(--border);
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            transition: border-color .12s, background .12s;
        }
        .lesson-btn.active .lesson-icon {
            border-color: var(--brand);
            background: var(--brand);
        }
        .lesson-btn.done .lesson-icon {
            border-color: #22c55e;
            background: #22c55e;
        }
        .lesson-icon svg { width: 14px; height: 14px; }
        .lesson-btn.active .lesson-icon svg,
        .lesson-btn.done .lesson-icon svg { color: #fff; }
        .lesson-btn:not(.active):not(.done) .lesson-icon svg { color: #b0aac8; }
        .lesson-text { flex: 1; min-width: 0; }
        .lesson-name {
            font-size: .82rem;
            font-weight: 600;
            color: var(--text-primary);
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .lesson-btn.active .lesson-name { color: var(--brand); }
        .lesson-min {
            font-size: .72rem;
            color: var(--text-secondary);
            font-family: 'DM Mono', monospace;
        }

        /* ── MAIN CONTENT ── */
        .main-content {
            flex: 1;
            overflow-y: auto;
            padding: 2rem 2.5rem 4rem;
            max-width: 820px;
        }

        /* Content card */
        .content-card {
            background: #fff;
            border: 1px solid var(--border);
            border-radius: 18px;
            overflow: hidden;
            margin-bottom: 1.5rem;
            box-shadow: 0 2px 12px rgba(91,71,224,.06);
        }

        /* Lesson body */
        .lesson-body { padding: 2rem 2.25rem 1.5rem; }
        .lesson-meta-tag {
            display: inline-flex;
            align-items: center;
            gap: .35rem;
            font-size: .72rem;
            font-weight: 600;
            letter-spacing: .04em;
            text-transform: uppercase;
            color: var(--brand);
            background: var(--brand-light);
            padding: .25rem .65rem;
            border-radius: 99px;
            margin-bottom: .85rem;
        }
        .lesson-main-title {
            font-size: 1.65rem;
            font-weight: 700;
            color: var(--text-primary);
            line-height: 1.3;
            margin-bottom: .75rem;
        }
        .lesson-intro {
            font-size: .93rem;
            color: var(--text-secondary);
            line-height: 1.7;
            margin-bottom: 1.5rem;
        }
        .lesson-divider {
            border: none;
            border-top: 1px solid var(--border);
            margin: 1.5rem 0;
        }
        .section-label {
            font-size: .8rem;
            font-weight: 700;
            letter-spacing: .06em;
            text-transform: uppercase;
            color: var(--text-secondary);
            margin-bottom: .85rem;
        }
        .lesson-content-text {
            font-size: .92rem;
            color: #3d3558;
            line-height: 1.8;
            white-space: pre-wrap;
        }

        /* Action bar */
        .action-bar {
            display: flex;
            align-items: center;
            gap: .75rem;
            padding: 1.25rem 2.25rem;
            border-top: 1px solid var(--border);
            background: #fdfdff;
        }
        .btn-complete {
            padding: .65rem 1.35rem;
            background: var(--brand);
            color: #fff;
            border: none;
            border-radius: 10px;
            font-size: .88rem;
            font-weight: 600;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: .45rem;
            transition: background .15s, transform .1s;
        }
        .btn-complete:hover { background: #4a37cc; }
        .btn-complete:active { transform: scale(.97); }
        .btn-complete.done-state {
            background: #dcfce7;
            color: #16a34a;
            cursor: default;
        }
        .btn-next {
            padding: .65rem 1.25rem;
            background: #fff;
            color: var(--text-primary);
            border: 1.5px solid var(--border);
            border-radius: 10px;
            font-size: .88rem;
            font-weight: 600;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: .45rem;
            text-decoration: none;
            transition: border-color .15s, background .15s;
        }
        .btn-next:hover { border-color: var(--brand); background: var(--brand-light); color: var(--brand); }

        /* Access denied */
        .access-denied {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100%;
            text-align: center;
            padding: 3rem;
        }
        .lock-circle {
            width: 80px;
            height: 80px;
            background: var(--brand-light);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1.25rem;
        }
        .lock-circle svg { width: 36px; height: 36px; color: var(--brand); }

        /* ── COMMENTS ── */
        .comments-card {
            background: #fff;
            border: 1px solid var(--border);
            border-radius: 18px;
            overflow: hidden;
            box-shadow: 0 2px 12px rgba(91,71,224,.06);
        }
        .comments-header {
            padding: 1.25rem 2rem;
            border-bottom: 1px solid var(--border);
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .comments-header h2 { font-size: 1rem; font-weight: 700; }
        .comments-count {
            font-size: .78rem;
            font-weight: 700;
            color: var(--brand);
            background: var(--brand-light);
            padding: .2rem .6rem;
            border-radius: 99px;
        }
        .comment-form-wrap {
            padding: 1.25rem 2rem;
            border-bottom: 1px solid var(--border);
        }
        .comment-textarea {
            width: 100%;
            border: 1.5px solid var(--border);
            border-radius: 12px;
            padding: .8rem 1rem;
            font-size: .88rem;
            font-family: 'DM Sans', sans-serif;
            color: var(--text-primary);
            resize: none;
            transition: border-color .15s;
            outline: none;
        }
        .comment-textarea:focus { border-color: var(--brand); }
        .comment-submit {
            margin-top: .65rem;
            display: flex;
            justify-content: flex-end;
        }
        .btn-submit-comment {
            padding: .55rem 1.2rem;
            background: var(--brand);
            color: #fff;
            border: none;
            border-radius: 9px;
            font-size: .85rem;
            font-weight: 600;
            cursor: pointer;
            transition: background .15s;
        }
        .btn-submit-comment:hover { background: #4a37cc; }

        .comment-list { padding: 1rem 2rem 1.5rem; display: flex; flex-direction: column; gap: .85rem; }
        .comment-item {
            display: flex;
            gap: .85rem;
        }
        .comment-avatar {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            background: linear-gradient(135deg, var(--brand), var(--brand-mid));
            display: flex;
            align-items: center;
            justify-content: center;
            color: #fff;
            font-size: .82rem;
            font-weight: 700;
            flex-shrink: 0;
        }
        .comment-bubble {
            flex: 1;
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 0 12px 12px 12px;
            padding: .75rem 1rem;
        }
        .comment-meta {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: .35rem;
        }
        .comment-author { font-size: .82rem; font-weight: 700; }
        .comment-date { font-size: .72rem; color: var(--text-secondary); font-family: 'DM Mono', monospace; }
        .comment-text { font-size: .85rem; color: #3d3558; line-height: 1.65; }
        .empty-comments {
            text-align: center;
            padding: 2.5rem 1rem;
            color: var(--text-secondary);
            font-size: .88rem;
        }

        /* Flash messages */
        .flash-success {
            background: #f0fdf4;
            border: 1px solid #bbf7d0;
            color: #15803d;
            padding: .75rem 1.1rem;
            border-radius: 10px;
            font-size: .86rem;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: .5rem;
        }
        .flash-error {
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #dc2626;
            padding: .75rem 1.1rem;
            border-radius: 10px;
            font-size: .86rem;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: .5rem;
        }

        /* Responsive: hide sidebar on mobile, show toggle */
        @media (max-width: 768px) {
            .sidebar { display: none; position: fixed; top: 56px; left: 0; bottom: 0; z-index: 40; width: 85vw; }
            .sidebar.open { display: flex; }
            .main-content { padding: 1.25rem 1rem 4rem; }
            .mobile-menu-btn { display: flex; }
        }
        @media (min-width: 769px) {
            .mobile-menu-btn { display: none; }
        }
        .mobile-menu-btn {
            align-items: center;
            gap: .4rem;
            padding: .35rem .7rem;
            border: 1.5px solid var(--border);
            border-radius: 8px;
            background: transparent;
            cursor: pointer;
            font-size: .82rem;
            color: var(--text-secondary);
        }
        .mobile-overlay {
            display: none;
            position: fixed;
            inset: 0;
            background: rgba(0,0,0,.35);
            z-index: 39;
        }
        .mobile-overlay.open { display: block; }

        /* Scrollbar */
        .sidebar-modules::-webkit-scrollbar,
        .main-content::-webkit-scrollbar { width: 5px; }
        .sidebar-modules::-webkit-scrollbar-track,
        .main-content::-webkit-scrollbar-track { background: transparent; }
        .sidebar-modules::-webkit-scrollbar-thumb,
        .main-content::-webkit-scrollbar-thumb { background: var(--border); border-radius: 99px; }
    </style>
</head>
<body>

<%-- ════════════════════════ TOP NAV ════════════════════════ --%>
<nav class="top-nav">
    <a href="${pageContext.request.contextPath}/dashboard" class="nav-logo">
        <div class="nav-logo-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="18" height="18">
                <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
                <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
            </svg>
        </div>
        RedSaberes
    </a>

    <button class="mobile-menu-btn" onclick="toggleSidebar()" aria-label="Menú">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
            <line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="18" x2="21" y2="18"/>
        </svg>
        Módulos
    </button>

    <a href="${pageContext.request.contextPath}/explore" class="nav-back">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15">
            <path d="M19 12H5M12 5l-7 7 7 7"/>
        </svg>
        Volver a explorar
    </a>
</nav>

<div id="mobileOverlay" class="mobile-overlay" onclick="toggleSidebar()"></div>

<div class="course-layout">

    <%-- ════════════════════════ SIDEBAR ════════════════════════ --%>
    <aside class="sidebar" id="sidebar">

        <div class="sidebar-header">
            <div class="course-title-sidebar">${curso.titulo}</div>
            <c:if test="${not empty curso.usuario}">
                <div class="course-author">por ${curso.usuario.nombre}</div>
            </c:if>
        </div>

        <div class="progress-wrap">
            <div class="progress-label">
                <span>Progreso del curso</span>
                <span class="progress-pct" id="progressPct">0%</span>
            </div>
            <div class="progress-track">
                <div class="progress-fill" id="progressFill" style="width:0%"></div>
            </div>
        </div>

        <div class="sidebar-modules">
            <c:choose>
                <c:when test="${not empty modulos}">
                    <c:forEach var="modulo" items="${modulos}" varStatus="mIdx">
                        <div class="module-section">
                            <div class="module-label">${modulo.titulo}</div>
                            <c:forEach var="leccion" items="${modulo.lecciones}" varStatus="lIdx">
                                <c:set var="lessonId" value="lesson-${mIdx.index}-${lIdx.index}" />
                                <button
                                        class="lesson-btn ${mIdx.index == 0 && lIdx.index == 0 ? 'active' : ''}"
                                        id="btn-${lessonId}"
                                        onclick="selectLesson(
                                                '${lessonId}',
                                                '${fn:escapeXml(leccion.titulo)}',
                                                '${fn:escapeXml(modulo.titulo)}',
                                                '${fn:escapeXml(leccion.contenido)}'
                                                )"
                                >
                                    <div class="lesson-icon">
                                            <%-- Icono play (pendiente) o check (completada) --%>
                                        <svg viewBox="0 0 24 24" fill="currentColor">
                                            <polygon points="5,3 19,12 5,21" class="icon-play"/>
                                        </svg>
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" class="icon-check" style="display:none">
                                            <polyline points="20 6 9 17 4 12"/>
                                        </svg>
                                    </div>
                                    <div class="lesson-text">
                                        <div class="lesson-name">${leccion.titulo}</div>
                                        <div class="lesson-min">Lección ${lIdx.index + 1}</div>
                                    </div>
                                </button>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div style="padding:1.5rem 1.25rem; color:var(--text-secondary); font-size:.85rem;">
                        Este curso aún no tiene módulos disponibles.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </aside>

    <%-- ════════════════════════ MAIN ════════════════════════ --%>
    <main class="main-content">

        <c:choose>
            <c:when test="${accesoConcedido}">

                <%-- Flash messages --%>
                <c:if test="${not empty exito}">
                    <div class="flash-success">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16"><polyline points="20 6 9 17 4 12"/></svg>
                        <c:out value="${exito}"/>
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="flash-error">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12" y2="16.01"/></svg>
                        <c:out value="${error}"/>
                    </div>
                </c:if>

                <%-- Tarjeta de contenido de la lección --%>
                <div class="content-card">
                    <div class="lesson-body">
                        <div class="lesson-meta-tag" id="lessonModule">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="12" height="12">
                                <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
                                <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
                            </svg>
                            <span id="lessonModuleText">
                                <c:choose>
                                    <c:when test="${not empty modulos}">
                                        ${modulos[0].titulo}
                                    </c:when>
                                    <c:otherwise>Módulo</c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <h1 class="lesson-main-title" id="lessonTitle">
                            <c:choose>
                                <c:when test="${not empty modulos && not empty modulos[0].lecciones}">
                                    ${modulos[0].lecciones[0].titulo}
                                </c:when>
                                <c:otherwise>${curso.titulo}</c:otherwise>
                            </c:choose>
                        </h1>

                        <p class="lesson-intro" id="lessonIntro">
                            En esta lección aprenderás los conceptos fundamentales del módulo.
                            Los temas cubiertos incluyen fundamentos teóricos y ejercicios prácticos.
                        </p>

                        <hr class="lesson-divider">

                        <div class="section-label">Contenido de la lección</div>
                        <div class="lesson-content-text" id="lessonContent">
                            <c:choose>
                                <c:when test="${not empty modulos && not empty modulos[0].lecciones && not empty modulos[0].lecciones[0].contenido}">
                                    ${modulos[0].lecciones[0].contenido}
                                </c:when>
                                <c:otherwise>
                                    ${curso.descripcion}
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="action-bar">
                        <button class="btn-complete" id="btnComplete" onclick="markComplete()">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="15" height="15">
                                <polyline points="20 6 9 17 4 12"/>
                            </svg>
                            Marcar como Completada
                        </button>
                        <button class="btn-next" id="btnNext" onclick="goNext()">
                            Siguiente Lección
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15">
                                <path d="M5 12h14M12 5l7 7-7 7"/>
                            </svg>
                        </button>
                    </div>
                </div>

                <%-- Sección de comentarios --%>
                <div class="comments-card">
                    <div class="comments-header">
                        <h2>Comentarios del curso</h2>
                        <c:choose>
                            <c:when test="${not empty comentarios}">
                                <span class="comments-count">${fn:length(comentarios)}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="comments-count">0</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="comment-form-wrap">
                        <form method="post" action="${pageContext.request.contextPath}/course-material?id=${curso.id}">
                            <textarea
                                    name="comentario"
                                    id="comentario"
                                    rows="3"
                                    maxlength="255"
                                    required
                                    class="comment-textarea"
                                    placeholder="Comparte tu opinión sobre este curso (máx. 255 caracteres)…"
                            ></textarea>
                            <div class="comment-submit">
                                <button type="submit" class="btn-submit-comment">Publicar comentario</button>
                            </div>
                        </form>
                    </div>

                    <div class="comment-list">
                        <c:choose>
                            <c:when test="${not empty comentarios}">
                                <c:forEach var="comentario" items="${comentarios}">
                                    <div class="comment-item">
                                        <div class="comment-avatar">
                                            <c:choose>
                                                <c:when test="${not empty comentario.usuario && not empty comentario.usuario.nombre}">
                                                    ${fn:substring(comentario.usuario.nombre, 0, 1)}
                                                </c:when>
                                                <c:otherwise>?</c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="comment-bubble">
                                            <div class="comment-meta">
                                                <span class="comment-author">
                                                    <c:out value="${comentario.usuario.nombre}"/>
                                                </span>
                                                <span class="comment-date">
                                                    <c:out value="${comentario.fecha}"/>
                                                </span>
                                            </div>
                                            <div class="comment-text">
                                                <c:out value="${comentario.comentario}"/>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-comments">
                                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="36" height="36" style="color:var(--border);margin:0 auto .75rem;display:block">
                                        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                                    </svg>
                                    Aún no hay comentarios. ¡Sé el primero en comentar!
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </c:when>
            <c:otherwise>
                <%-- Acceso denegado --%>
                <div class="access-denied">
                    <div class="lock-circle">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                            <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                        </svg>
                    </div>
                    <h2 style="font-size:1.35rem;font-weight:700;margin-bottom:.5rem;">Contenido bloqueado</h2>
                    <p style="color:var(--text-secondary);font-size:.92rem;max-width:340px;line-height:1.65;margin-bottom:1.5rem;">
                        Necesitas tener un match confirmado con el creador del curso para acceder a este material.
                    </p>
                    <a href="${pageContext.request.contextPath}/explore"
                       style="display:inline-flex;align-items:center;gap:.45rem;padding:.65rem 1.35rem;background:var(--brand);color:#fff;border-radius:10px;font-weight:600;font-size:.88rem;text-decoration:none;">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15">
                            <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
                        </svg>
                        Explorar cursos
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </main>
</div>

<%-- ═══════════ JS: navegación entre lecciones + progreso ═══════════ --%>
<script>
    (function () {
        /* ── Construir índice de lecciones desde el DOM ── */
        const allBtns   = Array.from(document.querySelectorAll('.lesson-btn'));
        const completed = new Set();
        let   currentIdx = 0;

        const elTitle   = document.getElementById('lessonTitle');
        const elModule  = document.getElementById('lessonModuleText');
        const elContent = document.getElementById('lessonContent');
        const elIntro   = document.getElementById('lessonIntro');
        const btnComplete = document.getElementById('btnComplete');
        const btnNext     = document.getElementById('btnNext');

        /* Extraer datos de cada botón via atributos data-* generados en el JSP.
           Como JSTL ya escapa los atributos con fn:escapeXml, los leemos
           directamente del DOM para evitar problemas de comillas. */
        function buildIndex() {
            // Los datos ya están en el onclick, pero es más limpio leerlos de data-attrs.
            // Los inyectamos ahora mismo para no reescribir el JSP con data-* extensos.
        }

        /* Mapa lessonId → datos completos */
        const lessonData = {};

        <%-- Iterar módulos/lecciones para generar el mapa JS --%>
        <c:forEach var="modulo" items="${modulos}" varStatus="mIdx">
        <c:forEach var="leccion" items="${modulo.lecciones}" varStatus="lIdx">
        lessonData['lesson-${mIdx.index}-${lIdx.index}'] = {
            title  : ${fn:replace(fn:replace(fn:replace(fn:escapeXml(leccion.titulo), '\\', '\\\\'), "'", "\\'"), '&#10;', '\\n')
                        != null ? "'" += fn:replace(fn:replace(fn:replace(fn:escapeXml(leccion.titulo), '\\', '\\\\'), "'", "\\'"), '&#10;', '\\n') += "'" : "''"},
            module : '${fn:replace(fn:replace(fn:escapeXml(modulo.titulo), "'", "\\'"), '\\', '\\\\')}',
            content: '${fn:replace(fn:replace(fn:replace(fn:escapeXml(leccion.contenido), "\\", "\\\\"), "'", "\\'"), "&#10;", "\\n")}'
        };
        </c:forEach>
        </c:forEach>

        /* ── Selección de lección ── */
        window.selectLesson = function(id, title, module, content) {
            /* Desactivar botón anterior */
            if (allBtns[currentIdx]) {
                allBtns[currentIdx].classList.remove('active');
            }
            /* Activar nuevo */
            const btn = document.getElementById('btn-' + id);
            if (!btn) return;
            currentIdx = allBtns.indexOf(btn);
            btn.classList.add('active');

            /* Actualizar panel principal */
            if (elTitle)   elTitle.textContent   = title   || '—';
            if (elModule)  elModule.textContent  = module  || '—';
            if (elIntro)   elIntro.textContent   = 'En esta lección aprenderás los conceptos fundamentales del módulo.';
            if (elContent) elContent.textContent = content || 'Esta lección aún no tiene contenido.';

            /* Resetear botón completar según estado guardado */
            updateCompleteBtn(id);
            updateNextBtn();

            /* Scroll top en mobile */
            const main = document.querySelector('.main-content');
            if (main) main.scrollTo({ top: 0, behavior: 'smooth' });

            /* Cerrar sidebar en mobile */
            closeSidebar();
        };

        /* ── Marcar como completada ── */
        window.markComplete = function() {
            if (!btnComplete || btnComplete.classList.contains('done-state')) return;
            const id = allBtns[currentIdx]?.id.replace('btn-', '');
            if (!id) return;
            completed.add(id);

            /* Visual del botón de la lección en sidebar */
            const btn = allBtns[currentIdx];
            if (btn) {
                btn.classList.remove('active');
                btn.classList.add('done');
                const play  = btn.querySelector('.icon-play');
                const check = btn.querySelector('.icon-check');
                if (play)  play.style.display  = 'none';
                if (check) check.style.display = 'block';
                btn.classList.add('active');
            }

            updateCompleteBtn(id);
            updateProgress();
        };

        function updateCompleteBtn(id) {
            if (!btnComplete) return;
            if (completed.has(id)) {
                btnComplete.classList.add('done-state');
                btnComplete.innerHTML = `
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="15" height="15">
                    <polyline points="20 6 9 17 4 12"/>
                </svg>
                ¡Lección completada!`;
            } else {
                btnComplete.classList.remove('done-state');
                btnComplete.innerHTML = `
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" width="15" height="15">
                    <polyline points="20 6 9 17 4 12"/>
                </svg>
                Marcar como Completada`;
            }
        }

        /* ── Siguiente lección ── */
        window.goNext = function() {
            const nextBtn = allBtns[currentIdx + 1];
            if (!nextBtn) return;
            nextBtn.click();
        };

        function updateNextBtn() {
            if (!btnNext) return;
            const hasNext = currentIdx < allBtns.length - 1;
            btnNext.style.display = hasNext ? '' : 'none';
        }

        /* ── Progreso ── */
        function updateProgress() {
            const total  = allBtns.length;
            if (!total) return;
            const pct    = Math.round((completed.size / total) * 100);
            const fill   = document.getElementById('progressFill');
            const label  = document.getElementById('progressPct');
            if (fill)  fill.style.width = pct + '%';
            if (label) label.textContent = pct + '%';
        }

        /* ── Mobile sidebar ── */
        window.toggleSidebar = function() {
            const sb  = document.getElementById('sidebar');
            const ov  = document.getElementById('mobileOverlay');
            if (!sb || !ov) return;
            sb.classList.toggle('open');
            ov.classList.toggle('open');
        };
        function closeSidebar() {
            const sb = document.getElementById('sidebar');
            const ov = document.getElementById('mobileOverlay');
            if (sb) sb.classList.remove('open');
            if (ov) ov.classList.remove('open');
        }

        /* ── Init ── */
        updateNextBtn();
        updateProgress();

    })();
</script>

</body>
</html>
