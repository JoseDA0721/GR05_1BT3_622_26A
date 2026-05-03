<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${curso.titulo} - RedSaberes</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,wght@0,400;0,500;0,600;0,700;1,400&family=DM+Mono:wght@400;500&display=swap" rel="stylesheet">
  <style>
    * { font-family: 'DM Sans', sans-serif; }

    :root {
      --brand:       #5b47e0;
      --brand-light: #ede9ff;
      --brand-mid:   #7c6ff7;
      --surface:     #f8f7ff;
      --border:      #e8e5f5;
      --text-pri:    #1a1530;
      --text-sec:    #6b6694;
      --success:     #16a34a;
      --success-bg:  #f0fdf4;
      --success-bd:  #bbf7d0;
      --danger:      #dc2626;
      --danger-bg:   #fef2f2;
      --danger-bd:   #fecaca;
      --gold:        #f59e0b;
    }

    body { background: var(--surface); color: var(--text-pri); margin: 0; }

    /* ── NAV ── */
    .top-nav {
      position: sticky; top: 0; z-index: 50;
      height: 56px; background: #fff;
      border-bottom: 1px solid var(--border);
      display: flex; align-items: center;
      padding: 0 1.5rem; gap: 1rem;
    }
    .nav-logo {
      display: flex; align-items: center; gap: .5rem;
      font-weight: 700; font-size: 1.1rem;
      color: var(--brand); text-decoration: none;
    }
    .nav-logo-icon {
      width: 30px; height: 30px; background: var(--brand);
      border-radius: 8px; display: flex;
      align-items: center; justify-content: center;
    }
    .nav-back {
      margin-left: auto;
      display: flex; align-items: center; gap: .4rem;
      font-size: .83rem; color: var(--text-sec);
      text-decoration: none; padding: .3rem .7rem;
      border-radius: 8px; transition: background .15s;
    }
    .nav-back:hover { background: var(--brand-light); color: var(--brand); }

    /* ── HERO ── */
    .hero {
      position: relative; width: 100%; height: 280px;
      overflow: hidden; background: #1a1530;
    }
    .hero-img {
      width: 100%; height: 100%;
      object-fit: cover; opacity: .55;
    }
    .hero-overlay {
      position: absolute; inset: 0;
      background: linear-gradient(to top, rgba(26,21,48,.92) 0%, rgba(26,21,48,.3) 60%, transparent 100%);
    }
    .hero-content {
      position: absolute; bottom: 0; left: 0; right: 0;
      padding: 2rem 2.5rem;
    }
    .hero-badges { display: flex; gap: .5rem; margin-bottom: .6rem; flex-wrap: wrap; }
    .hero-badge {
      font-size: .7rem; font-weight: 700;
      letter-spacing: .05em; text-transform: uppercase;
      background: rgba(255,255,255,.18);
      color: rgba(255,255,255,.95);
      padding: .25rem .7rem; border-radius: 99px;
      border: 1px solid rgba(255,255,255,.25);
    }
    .hero-title {
      font-size: 2rem; font-weight: 700;
      color: #fff; line-height: 1.25;
      text-shadow: 0 2px 12px rgba(0,0,0,.4);
    }
    .hero-author {
      font-size: .85rem; color: rgba(255,255,255,.7);
      margin-top: .35rem;
    }

    /* ── LAYOUT ── */
    .page-body {
      max-width: 1100px; margin: 0 auto;
      padding: 2rem 1.5rem 4rem;
      display: grid;
      grid-template-columns: 1fr 340px;
      gap: 1.75rem;
      align-items: start;
    }
    @media (max-width: 820px) {
      .page-body { grid-template-columns: 1fr; }
      .hero-content { padding: 1.25rem 1.25rem; }
      .hero-title { font-size: 1.45rem; }
      .hero { height: 220px; }
    }

    /* ── CARDS ── */
    .card {
      background: #fff; border: 1px solid var(--border);
      border-radius: 16px; overflow: hidden;
      box-shadow: 0 2px 10px rgba(91,71,224,.05);
    }
    .card-header {
      padding: 1.1rem 1.5rem; border-bottom: 1px solid var(--border);
      display: flex; align-items: center;
      justify-content: space-between;
    }
    .card-title { font-size: .95rem; font-weight: 700; }
    .card-body { padding: 1.25rem 1.5rem; }

    /* ── STATS GRID ── */
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: .85rem;
      margin-bottom: 1.5rem;
    }
    @media (max-width: 600px) {
      .stats-grid { grid-template-columns: repeat(2, 1fr); }
    }
    .stat-card {
      background: #fff; border: 1px solid var(--border);
      border-radius: 12px; padding: 1rem;
      text-align: center;
    }
    .stat-num {
      font-size: 1.6rem; font-weight: 700;
      color: var(--brand); line-height: 1;
      margin-bottom: .3rem;
    }
    .stat-label { font-size: .72rem; color: var(--text-sec); font-weight: 500; text-transform: uppercase; letter-spacing: .04em; }

    /* ── RATING BAR ── */
    .rating-summary {
      display: flex; align-items: center; gap: 1.25rem;
      padding: 1.25rem 1.5rem; border-bottom: 1px solid var(--border);
    }
    .big-rating {
      text-align: center; flex-shrink: 0;
    }
    .big-rating-num {
      font-size: 3rem; font-weight: 700;
      color: var(--text-pri); line-height: 1;
    }
    .big-rating-stars { display: flex; gap: 2px; justify-content: center; margin: .3rem 0 .2rem; }
    .big-rating-count { font-size: .75rem; color: var(--text-sec); }
    .rating-bars { flex: 1; display: flex; flex-direction: column; gap: .35rem; }
    .rating-row { display: flex; align-items: center; gap: .6rem; font-size: .75rem; }
    .rating-row-label { color: var(--text-sec); min-width: 14px; text-align: right; }
    .rating-bar-track { flex: 1; height: 6px; background: var(--border); border-radius: 99px; overflow: hidden; }
    .rating-bar-fill { height: 100%; background: var(--gold); border-radius: 99px; }
    .rating-row-count { color: var(--text-sec); min-width: 18px; }

    /* ── STARS INPUT ── */
    .star-input { display: flex; gap: .35rem; margin-bottom: .75rem; }
    .star-btn {
      background: none; border: none; cursor: pointer;
      padding: 2px; font-size: 1.6rem; line-height: 1;
      color: var(--border); transition: color .1s, transform .1s;
    }
    .star-btn:hover, .star-btn.sel { color: var(--gold); }
    .star-btn:active { transform: scale(.88); }

    /* ── REVIEW LIST ── */
    .review-item {
      display: flex; gap: .9rem;
      padding: 1rem 0;
      border-bottom: 1px solid var(--border);
    }
    .review-item:last-child { border-bottom: none; }
    .review-avatar {
      width: 36px; height: 36px; border-radius: 50%;
      background: linear-gradient(135deg, var(--brand), var(--brand-mid));
      display: flex; align-items: center; justify-content: center;
      color: #fff; font-size: .8rem; font-weight: 700;
      flex-shrink: 0;
    }
    .review-body { flex: 1; min-width: 0; }
    .review-meta { display: flex; align-items: center; gap: .6rem; margin-bottom: .3rem; flex-wrap: wrap; }
    .review-author { font-size: .85rem; font-weight: 700; }
    .review-stars { display: flex; gap: 1px; }
    .review-date { font-size: .72rem; color: var(--text-sec); font-family: 'DM Mono', monospace; margin-left: auto; }
    .review-text { font-size: .85rem; color: #3d3558; line-height: 1.65; }
    .star-sm { font-size: .8rem; color: var(--gold); }
    .star-sm.empty { color: var(--border); }

    /* ── PLAN DE ESTUDIOS ── */
    .module-block { margin-bottom: .5rem; }
    .module-head {
      font-size: .72rem; font-weight: 700;
      letter-spacing: .06em; text-transform: uppercase;
      color: var(--text-sec); padding: .6rem 0 .3rem;
    }
    .lesson-row {
      display: flex; align-items: center; gap: .75rem;
      padding: .55rem .75rem; border-radius: 10px;
      text-decoration: none; color: var(--text-pri);
      transition: background .12s; margin-bottom: 2px;
      border: 1px solid transparent;
    }
    .lesson-row:hover { background: var(--brand-light); border-color: var(--border); }
    .lesson-dot {
      width: 28px; height: 28px; border-radius: 50%;
      border: 2px solid var(--border); flex-shrink: 0;
      display: flex; align-items: center; justify-content: center;
      font-size: .72rem; font-weight: 700; color: var(--text-sec);
    }
    .lesson-info { flex: 1; min-width: 0; }
    .lesson-title-sm { font-size: .82rem; font-weight: 600; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    .lesson-sub { font-size: .7rem; color: var(--text-sec); }
    .lesson-arrow { color: var(--text-sec); flex-shrink: 0; }

    /* ── SIDEBAR STICKY ── */
    .sidebar-sticky { position: sticky; top: 72px; display: flex; flex-direction: column; gap: 1rem; }

    /* ── CTA CARD ── */
    .cta-card {
      background: #fff; border: 1px solid var(--border);
      border-radius: 16px; overflow: hidden;
      box-shadow: 0 2px 10px rgba(91,71,224,.05);
    }
    .cta-thumb {
      width: 100%; height: 140px;
      object-fit: cover; background: var(--brand-light);
      display: block;
    }
    .cta-thumb-placeholder {
      width: 100%; height: 140px;
      background: linear-gradient(135deg, var(--brand-light), #ddd8ff);
      display: flex; align-items: center; justify-content: center;
    }
    .cta-body { padding: 1.25rem; }
    .cta-meta { display: flex; gap: .5rem; margin-bottom: .85rem; flex-wrap: wrap; }
    .cta-badge {
      font-size: .7rem; font-weight: 700; text-transform: uppercase;
      letter-spacing: .05em; padding: .25rem .65rem;
      border-radius: 99px; background: var(--brand-light); color: var(--brand);
    }
    .cta-desc { font-size: .85rem; color: var(--text-sec); line-height: 1.6; margin-bottom: 1.1rem; }
    .btn-cta {
      width: 100%; padding: .8rem;
      background: var(--brand); color: #fff;
      border: none; border-radius: 12px;
      font-size: .92rem; font-weight: 700;
      cursor: pointer; display: flex;
      align-items: center; justify-content: center;
      gap: .5rem; text-decoration: none;
      transition: background .15s, transform .1s;
      margin-bottom: .6rem;
    }
    .btn-cta:hover { background: #4a37cc; }
    .btn-cta:active { transform: scale(.98); }
    .btn-cta.secondary {
      background: #fff; color: var(--brand);
      border: 1.5px solid var(--brand);
    }
    .btn-cta.secondary:hover { background: var(--brand-light); }
    .cta-note { font-size: .75rem; color: var(--text-sec); text-align: center; }

    /* ── MINI STATS SIDEBAR ── */
    .mini-stats { display: flex; flex-direction: column; gap: .5rem; padding: 1rem 1.25rem; }
    .mini-stat-row {
      display: flex; align-items: center; justify-content: space-between;
      font-size: .83rem;
    }
    .mini-stat-label { color: var(--text-sec); display: flex; align-items: center; gap: .45rem; }
    .mini-stat-val { font-weight: 700; }

    /* ── FLASH ── */
    .flash-success {
      background: var(--success-bg); border: 1px solid var(--success-bd);
      color: var(--success); padding: .75rem 1rem;
      border-radius: 10px; font-size: .86rem;
      margin-bottom: 1rem; display: flex; align-items: center; gap: .5rem;
    }
    .flash-error {
      background: var(--danger-bg); border: 1px solid var(--danger-bd);
      color: var(--danger); padding: .75rem 1rem;
      border-radius: 10px; font-size: .86rem;
      margin-bottom: 1rem; display: flex; align-items: center; gap: .5rem;
    }

    /* ── ACCESO DENEGADO ── */
    .locked-banner {
      background: #fff; border: 1px solid var(--border);
      border-radius: 16px; padding: 2.5rem;
      text-align: center;
    }
    .locked-icon {
      width: 64px; height: 64px; border-radius: 50%;
      background: var(--brand-light);
      display: flex; align-items: center; justify-content: center;
      margin: 0 auto 1rem;
    }

    /* scrollbar sutil */
    ::-webkit-scrollbar { width: 5px; }
    ::-webkit-scrollbar-track { background: transparent; }
    ::-webkit-scrollbar-thumb { background: var(--border); border-radius: 99px; }
  </style>
</head>
<body>

<%-- ═══ NAV ═══ --%>
<nav class="top-nav">
  <a href="${pageContext.request.contextPath}/dashboard" class="nav-logo">
    <div class="nav-logo-icon">
      <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"
           stroke-linecap="round" stroke-linejoin="round" width="16" height="16">
        <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
        <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
      </svg>
    </div>
    RedSaberes
  </a>
  <a href="${pageContext.request.contextPath}/explore" class="nav-back">
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
      <path d="M19 12H5M12 5l-7 7 7 7"/>
    </svg>
    Volver a explorar
  </a>
</nav>

<%-- ═══ HERO ═══ --%>
<div class="hero">
  <c:choose>
    <c:when test="${not empty curso.imagenPortada}">
      <img src="${pageContext.request.contextPath}/uploads/portadas/${curso.imagenPortada}"
           alt="${curso.titulo}" class="hero-img"
           onerror="this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=1200&h=400&fit=crop'"/>
    </c:when>
    <c:otherwise>
      <img src="https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=1200&h=400&fit=crop"
           alt="${curso.titulo}" class="hero-img"/>
    </c:otherwise>
  </c:choose>
  <div class="hero-overlay"></div>
  <div class="hero-content">
    <div class="hero-badges">
      <c:if test="${not empty curso.categoria}">
        <span class="hero-badge">${curso.categoria}</span>
      </c:if>
      <c:if test="${not empty curso.nivelDificultad}">
        <span class="hero-badge">${curso.nivelDificultad}</span>
      </c:if>
    </div>
    <h1 class="hero-title">${curso.titulo}</h1>
    <c:if test="${not empty curso.usuario}">
      <div class="hero-author">por ${curso.usuario.nombre}</div>
    </c:if>
  </div>
</div>

<%-- ═══ BODY ═══ --%>
<div class="page-body">

  <%-- ══════════ COLUMNA IZQUIERDA ══════════ --%>
  <div>

    <%-- Flash messages --%>
    <c:if test="${not empty exito}">
      <div class="flash-success">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
          <polyline points="20 6 9 17 4 12"/>
        </svg>
        <c:out value="${exito}"/>
      </div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="flash-error">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
          <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/>
          <line x1="12" y1="16" x2="12" y2="16.01"/>
        </svg>
        <c:out value="${error}"/>
      </div>
    </c:if>

    <%-- Stats rápidas --%>
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-num">${fn:length(modulos)}</div>
        <div class="stat-label">Módulos</div>
      </div>
      <div class="stat-card">
        <div class="stat-num">${totalLecciones}</div>
        <div class="stat-label">Lecciones</div>
      </div>
      <div class="stat-card">
        <div class="stat-num">${likesCount}</div>
        <div class="stat-label">Likes</div>
      </div>
      <div class="stat-card">
        <div class="stat-num">${matchesCount}</div>
        <div class="stat-label">Matches</div>
      </div>
    </div>

    <%-- Descripción --%>
    <c:if test="${not empty curso.descripcion}">
      <div class="card" style="margin-bottom:1.25rem;">
        <div class="card-header"><span class="card-title">Acerca de este curso</span></div>
        <div class="card-body" style="font-size:.9rem;color:#3d3558;line-height:1.75;">
          <c:out value="${curso.descripcion}"/>
        </div>
      </div>
    </c:if>

    <%-- Plan de estudios --%>
    <c:if test="${not empty modulos}">
      <div class="card" style="margin-bottom:1.25rem;">
        <div class="card-header">
          <span class="card-title">Plan de estudios</span>
          <span style="font-size:.78rem;color:var(--text-sec);">
                        ${fn:length(modulos)} módulo<c:if test="${fn:length(modulos) != 1}">s</c:if>
                        · ${totalLecciones} lección<c:if test="${totalLecciones != 1}">es</c:if>
                    </span>
        </div>
        <div class="card-body" style="padding-top:.75rem;">
          <c:forEach var="modulo" items="${modulos}" varStatus="mIdx">
            <div class="module-block">
              <div class="module-head">${modulo.titulo}</div>
              <c:forEach var="leccion" items="${modulo.lecciones}" varStatus="lIdx">
                <c:choose>
                  <c:when test="${accesoConcedido}">
                    <a href="${pageContext.request.contextPath}/course-material?id=${curso.id}"
                       class="lesson-row">
                      <div class="lesson-dot">${lIdx.index + 1}</div>
                      <div class="lesson-info">
                        <div class="lesson-title-sm">${leccion.titulo}</div>
                        <div class="lesson-sub">Lección ${lIdx.index + 1}</div>
                      </div>
                      <svg class="lesson-arrow" viewBox="0 0 24 24" fill="none"
                           stroke="currentColor" stroke-width="2" width="14" height="14">
                        <path d="M9 18l6-6-6-6"/>
                      </svg>
                    </a>
                  </c:when>
                  <c:otherwise>
                    <div class="lesson-row" style="opacity:.55;cursor:default;">
                      <div class="lesson-dot" style="border-color:var(--border);">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
                             stroke-width="2" width="12" height="12">
                          <rect x="3" y="11" width="18" height="11" rx="2"/>
                          <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                        </svg>
                      </div>
                      <div class="lesson-info">
                        <div class="lesson-title-sm">${leccion.titulo}</div>
                        <div class="lesson-sub">Lección ${lIdx.index + 1}</div>
                      </div>
                    </div>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </div>
          </c:forEach>
        </div>
      </div>
    </c:if>

    <%-- Sección reseñas y calificaciones --%>
    <div class="card">
      <div class="card-header">
        <span class="card-title">Calificaciones y reseñas</span>
        <span style="font-size:.78rem;background:var(--brand-light);color:var(--brand);
                             padding:.2rem .6rem;border-radius:99px;font-weight:700;">
          ${totalResenas}
        </span>
      </div>

      <%-- Resumen de estrellas --%>
      <div class="rating-summary">
        <div class="big-rating">
          <div class="big-rating-num">
            <c:choose>
              <c:when test="${not empty promedioEstrellas}">
                <fmt:formatNumber value="${promedioEstrellas}" minFractionDigits="1" maxFractionDigits="1"/>
              </c:when>
              <c:otherwise>—</c:otherwise>
            </c:choose>
          </div>
            <div class="rating-bars">
              <%-- JSTL no permite step negativo; iteramos ascendente y calculamos el valor de estrella en orden descendente --%>
              <c:forEach begin="1" end="5" var="i">
                <c:set var="starVal" value="${6 - i}"/>
                <%-- Contar reseÃ±as con este valor --%>
                <c:set var="countStar" value="0"/>
                <c:forEach var="r" items="${resenas}">
                  <c:if test="${r.estrellas == starVal}">
                    <c:set var="countStar" value="${countStar + 1}"/>
                  </c:if>
                </c:forEach>
                <c:set var="pct" value="${totalResenas > 0 ? (countStar * 100 / totalResenas) : 0}"/>
                <div class="rating-row">
                  <span class="rating-row-label" style="color:var(--gold);">★</span>
                  <span style="font-size:.72rem;color:var(--text-sec);min-width:8px;">${starVal}</span>
                  <div class="rating-bar-track">
                    <div class="rating-bar-fill" style="width:${pct}%;"></div>
                  </div>
                  <span class="rating-row-count">${countStar}</span>
                  </div>
                </c:forEach>
              </div>
      </div>

      <%-- Formulario nueva reseña --%>
      <c:choose>
        <c:when test="${puedeResenar}">
          <div style="padding:1.25rem 1.5rem;border-bottom:1px solid var(--border);">
            <div style="font-size:.82rem;font-weight:700;margin-bottom:.65rem;">
              Deja tu reseña
            </div>
            <form method="post"
                  action="${pageContext.request.contextPath}/course-overview?id=${curso.id}">

              <div class="star-input" id="starInput">
                <c:forEach begin="1" end="5" var="sv">
                  <button type="button" class="star-btn"
                          data-val="${sv}"
                          onclick="setStar(${sv})">★</button>
                </c:forEach>
              </div>
              <input type="hidden" name="estrellas" id="estrellasInput" value="0"/>

              <textarea name="comentario" rows="3" maxlength="255"
                        placeholder="Comparte tu experiencia con este curso (opcional, máx. 255 caracteres)…"
                        style="width:100%;border:1.5px solid var(--border);border-radius:10px;
                                             padding:.7rem .9rem;font-size:.86rem;font-family:'DM Sans',sans-serif;
                                             color:var(--text-pri);resize:none;outline:none;box-sizing:border-box;
                                             transition:border-color .15s;"
                        onfocus="this.style.borderColor='var(--brand)'"
                        onblur="this.style.borderColor='var(--border)'"></textarea>

              <div style="display:flex;justify-content:flex-end;margin-top:.6rem;">
                <button type="submit" id="submitReview"
                        style="padding:.55rem 1.25rem;background:var(--brand);color:#fff;
                                               border:none;border-radius:9px;font-size:.85rem;font-weight:700;
                                               cursor:pointer;transition:background .15s;opacity:.5;"
                        disabled>
                  Publicar reseña
                </button>
              </div>
            </form>
          </div>
        </c:when>
        <c:when test="${yaReseno}">
          <div style="padding:1rem 1.5rem;border-bottom:1px solid var(--border);
                                background:var(--success-bg);display:flex;align-items:center;gap:.5rem;
                                font-size:.84rem;color:var(--success);">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
            Ya dejaste una reseña para este curso.
          </div>
        </c:when>
        <c:when test="${!accesoConcedido}">
          <div style="padding:1rem 1.5rem;border-bottom:1px solid var(--border);
                                font-size:.84rem;color:var(--text-sec);
                                display:flex;align-items:center;gap:.5rem;">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15">
              <rect x="3" y="11" width="18" height="11" rx="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
            </svg>
            Necesitas acceso al curso para dejar una reseña.
          </div>
        </c:when>
      </c:choose>

      <%-- Lista de reseñas --%>
      <div class="card-body" style="padding-top:.75rem;">
        <c:choose>
          <c:when test="${not empty resenas}">
            <c:forEach var="resena" items="${resenas}">
              <div class="review-item">
                <div class="review-avatar">
                  <c:choose>
                    <c:when test="${not empty resena.usuario && not empty resena.usuario.nombre}">
                      ${fn:substring(resena.usuario.nombre, 0, 1)}
                    </c:when>
                    <c:otherwise>?</c:otherwise>
                  </c:choose>
                </div>
                <div class="review-body">
                  <div class="review-meta">
                                        <span class="review-author">
                                            <c:out value="${resena.usuario.nombre}"/>
                                        </span>
                    <div class="review-stars">
                      <c:forEach begin="1" end="5" var="s">
                        <span class="star-sm ${resena.estrellas >= s ? '' : 'empty'}">★</span>
                      </c:forEach>
                    </div>
                    <span class="review-date">
                                            <c:out value="${resena.fecha}"/>
                                        </span>
                  </div>
                  <c:if test="${not empty resena.comentario}">
                    <div class="review-text">
                      <c:out value="${resena.comentario}"/>
                    </div>
                  </c:if>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div style="text-align:center;padding:2rem 1rem;color:var(--text-sec);font-size:.86rem;">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"
                   width="36" height="36"
                   style="color:var(--border);margin:0 auto .65rem;display:block;">
                <path d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 0 0 .95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 0 0-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 0 0-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 0 0-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 0 0 .951-.69l1.519-4.674z"/>
              </svg>
              Aún no hay reseñas. ¡Sé el primero!
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

  </div><%-- fin columna izquierda --%>

  <%-- ══════════ SIDEBAR DERECHO ══════════ --%>
  <div class="sidebar-sticky">

    <%-- CTA Card --%>
    <div class="cta-card">
      <c:choose>
        <c:when test="${not empty curso.imagenPortada}">
          <img src="${pageContext.request.contextPath}/uploads/portadas/${curso.imagenPortada}"
               alt="${curso.titulo}" class="cta-thumb"
               onerror="this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=400&h=200&fit=crop'"/>
        </c:when>
        <c:otherwise>
          <div class="cta-thumb-placeholder">
            <svg viewBox="0 0 24 24" fill="none" stroke="var(--brand-mid)" stroke-width="1.5"
                 width="48" height="48">
              <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
              <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
            </svg>
          </div>
        </c:otherwise>
      </c:choose>

      <div class="cta-body">
        <div class="cta-meta">
          <c:if test="${not empty curso.nivelDificultad}">
            <span class="cta-badge">${curso.nivelDificultad}</span>
          </c:if>
          <c:if test="${not empty curso.categoria}">
            <span class="cta-badge" style="background:#f0f0ff;color:#555;">${curso.categoria}</span>
          </c:if>
        </div>

        <p class="cta-desc">
          <c:choose>
            <c:when test="${not empty curso.descripcion && fn:length(curso.descripcion) > 100}">
              ${fn:substring(curso.descripcion, 0, 100)}…
            </c:when>
            <c:when test="${not empty curso.descripcion}">
              ${curso.descripcion}
            </c:when>
            <c:otherwise>
              Accede al contenido completo de este curso.
            </c:otherwise>
          </c:choose>
        </p>

        <c:choose>
          <c:when test="${accesoConcedido}">
            <a href="${pageContext.request.contextPath}/course-material?id=${curso.id}"
               class="btn-cta">
              <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                <polygon points="5,3 19,12 5,21"/>
              </svg>
              Ir al contenido del curso
            </a>
            <p class="cta-note">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                   width="12" height="12" style="display:inline;vertical-align:middle;">
                <polyline points="20 6 9 17 4 12"/>
              </svg>
              Tienes acceso completo
            </p>
          </c:when>
          <c:otherwise>
            <div style="padding:.85rem;background:var(--brand-light);border-radius:10px;
                                    text-align:center;margin-bottom:.6rem;">
              <svg viewBox="0 0 24 24" fill="none" stroke="var(--brand)" stroke-width="2"
                   width="22" height="22" style="display:block;margin:0 auto .4rem;">
                <rect x="3" y="11" width="18" height="11" rx="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
              <div style="font-size:.8rem;font-weight:700;color:var(--brand);margin-bottom:.2rem;">
                Contenido bloqueado
              </div>
              <div style="font-size:.75rem;color:var(--text-sec);line-height:1.5;">
                Necesitas un match con el creador para acceder al material.
              </div>
            </div>
            <a href="${pageContext.request.contextPath}/explore" class="btn-cta secondary">
              Explorar más cursos
            </a>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <%-- Mini stats sidebar --%>
    <div class="card">
      <div class="card-header"><span class="card-title">Información del curso</span></div>
      <div class="mini-stats">
        <div class="mini-stat-row">
                    <span class="mini-stat-label">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
                        </svg>
                        Módulos
                    </span>
          <span class="mini-stat-val">${fn:length(modulos)}</span>
        </div>
        <div class="mini-stat-row">
                    <span class="mini-stat-label">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                            <circle cx="12" cy="12" r="10"/><polygon points="10,8 16,12 10,16"/>
                        </svg>
                        Lecciones
                    </span>
          <span class="mini-stat-val">${totalLecciones}</span>
        </div>
        <div class="mini-stat-row">
                    <span class="mini-stat-label">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                        </svg>
                        Likes
                    </span>
          <span class="mini-stat-val">${likesCount}</span>
        </div>
        <div class="mini-stat-row">
                    <span class="mini-stat-label">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/>
                            <path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                        </svg>
                        Matches
                    </span>
          <span class="mini-stat-val">${matchesCount}</span>
        </div>
            <c:if test="${not empty promedioEstrellas}">
          <div class="mini-stat-row">
                        <span class="mini-stat-label">
                            <span style="color:var(--gold);font-size:.9rem;">★</span>
                            Calificación
                        </span>
            <span class="mini-stat-val">
                            <fmt:formatNumber value="${promedioEstrellas}" minFractionDigits="1" maxFractionDigits="1"/> / 5
                        </span>
          </div>
        </c:if>
        <c:if test="${not empty curso.usuario}">
          <div class="mini-stat-row" style="padding-top:.25rem;border-top:1px solid var(--border);">
                        <span class="mini-stat-label">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                            </svg>
                            Instructor
                        </span>
            <span class="mini-stat-val" style="font-size:.82rem;">${curso.usuario.nombre}</span>
          </div>
        </c:if>
      </div>
    </div>

  </div><%-- fin sidebar --%>

</div><%-- fin page-body --%>

<script>
  var selectedStar = 0;

  function setStar(val) {
    selectedStar = val;
    document.getElementById('estrellasInput').value = val;
    var btns = document.querySelectorAll('.star-btn');
    btns.forEach(function(b) {
      var bVal = parseInt(b.getAttribute('data-val'));
      if (bVal <= val) {
        b.classList.add('sel');
      } else {
        b.classList.remove('sel');
      }
    });
    var submitBtn = document.getElementById('submitReview');
    if (submitBtn) {
      submitBtn.disabled = false;
      submitBtn.style.opacity = '1';
    }
  }

  /* Hover preview en estrellas */
  document.querySelectorAll('.star-btn').forEach(function(btn) {
    btn.addEventListener('mouseenter', function() {
      var hVal = parseInt(this.getAttribute('data-val'));
      document.querySelectorAll('.star-btn').forEach(function(b) {
        b.style.color = parseInt(b.getAttribute('data-val')) <= hVal
                ? 'var(--gold)' : 'var(--border)';
      });
    });
    btn.addEventListener('mouseleave', function() {
      document.querySelectorAll('.star-btn').forEach(function(b) {
        var bv = parseInt(b.getAttribute('data-val'));
        b.style.color = bv <= selectedStar ? 'var(--gold)' : 'var(--border)';
      });
    });
  });
</script>

</body>
</html>
