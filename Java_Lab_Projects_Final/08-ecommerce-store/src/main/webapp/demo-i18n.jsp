<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%-- Handle locale selection via query parameter ?lang=hi or default en --%>
<c:set var="userLang" value="${param.lang eq 'hi' ? 'hi' : 'en'}" />
<fmt:setLocale value="${userLang}" />
<fmt:setBundle basename="messages" var="msg" />

<!DOCTYPE html>
<html lang="${userLang}">
<head>
    <title><fmt:message key="app.title" bundle="${msg}"/> &middot; I18N Demo</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="bg-light d-flex flex-column min-vh-100">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5 bg-white mb-4">
            <span class="badge bg-primary-subtle text-primary fw-bold text-uppercase px-3 py-2 rounded-pill d-inline-block mb-2" style="width: fit-content;">
                RTU Advanced Java Syllabus Demonstration &middot; Unit 3 (Internationalization)
            </span>
            <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
                <h1 class="h2 fw-bold mb-0">Internationalization (I18N) &amp; Localization</h1>
                <div class="btn-group" role="group">
                    <a href="${pageContext.request.contextPath}/demo-i18n.jsp?lang=en" 
                       class="btn btn-sm ${userLang eq 'en' ? 'btn-primary' : 'btn-outline-primary'}">
                        English (EN)
                    </a>
                    <a href="${pageContext.request.contextPath}/demo-i18n.jsp?lang=hi" 
                       class="btn btn-sm ${userLang eq 'hi' ? 'btn-primary' : 'btn-outline-primary'}">
                        हिन्दी (HI)
                    </a>
                </div>
            </div>

            <p class="text-secondary leading-relaxed mb-4">
                Demonstrates <code>java.util.Locale</code>, <code>ResourceBundle</code>, and JSTL formatting tags
                (<code>&lt;fmt:message&gt;</code>, <code>&lt;fmt:formatNumber&gt;</code>, <code>&lt;fmt:formatDate&gt;</code>)
                reading localized text from <code>messages_en.properties</code> and <code>messages_hi.properties</code>.
            </p>

            <hr class="opacity-25 mb-4">

            <!-- Localized Sample Text Table -->
            <h5 class="fw-bold mb-3">1. Localized UI Text Bundle (Active Locale: <code>${userLang}</code>)</h5>
            <div class="table-responsive mb-4">
                <table class="table table-bordered align-middle">
                    <thead class="table-light small">
                        <tr>
                            <th style="width: 30%;">Resource Key</th>
                            <th style="width: 70%;">Localized String Output</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><code>app.title</code></td>
                            <td><strong class="text-primary"><fmt:message key="app.title" bundle="${msg}"/></strong></td>
                        </tr>
                        <tr>
                            <td><code>msg.welcome</code></td>
                            <td><fmt:message key="msg.welcome" bundle="${msg}"/></td>
                        </tr>
                        <tr>
                            <td><code>btn.addToCart</code></td>
                            <td><span class="badge bg-secondary"><fmt:message key="btn.addToCart" bundle="${msg}"/></span></td>
                        </tr>
                        <tr>
                            <td><code>btn.placeOrder</code></td>
                            <td><span class="badge bg-success"><fmt:message key="btn.placeOrder" bundle="${msg}"/></span></td>
                        </tr>
                        <tr>
                            <td><code>msg.orderConfirmed</code></td>
                            <td><span class="text-success fw-semibold"><fmt:message key="msg.orderConfirmed" bundle="${msg}"/></span></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Formatted Currencies & Dates -->
            <h5 class="fw-bold mb-3">2. Locale-Sensitive Number &amp; Date Formatting</h5>
            <div class="row g-3 mb-4">
                <div class="col-md-6">
                    <div class="p-3 border rounded-3 bg-light">
                        <h6 class="fw-bold mb-2">Currency &amp; Numeric Values</h6>
                        <c:set var="demoAmount" value="2999.50" />
                        <div class="small text-secondary mb-1">Standard Amount: <code>${demoAmount}</code></div>
                        <div class="fs-5 fw-bold text-dark">
                            Formatted Currency: ₹<fmt:formatNumber value="${demoAmount}" pattern="#,##0.00"/>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="p-3 border rounded-3 bg-light">
                        <h6 class="fw-bold mb-2">Date &amp; Time Formatting</h6>
                        <% request.setAttribute("todayDate", new java.util.Date()); %>
                        <div class="small text-secondary mb-1">Raw Date: <code>${todayDate}</code></div>
                        <div class="fs-6 fw-bold text-dark">
                            Formatted: <fmt:formatDate value="${todayDate}" pattern="EEEE, dd MMMM yyyy (hh:mm a)"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="text-end">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-sm rounded-3">
                    <i class="bi bi-arrow-left me-1"></i> Return to Storefront
                </a>
            </div>
        </div>
    </main>

    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
