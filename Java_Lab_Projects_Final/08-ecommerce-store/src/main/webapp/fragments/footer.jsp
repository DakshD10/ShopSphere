<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<footer class="bg-dark text-light py-5 mt-auto">
    <div class="container">
        <div class="row g-4 mb-4">
            <div class="col-md-4">
                <h5 class="fw-bold mb-3 d-flex align-items-center">
                    <i class="bi bi-shop me-2 text-primary"></i>ShopSphere
                </h5>
                <p class="text-secondary small">
                    A comprehensive, modern Java E-Commerce Management System implemented using Jakarta Servlet 6.1, JSP 3.1, JSTL 3.0, and MySQL on Apache Tomcat 11.
                </p>
                <div class="small text-muted">
                    Active HTTP Sessions: <span class="badge bg-primary">${applicationScope.activeSessionsCount != null ? applicationScope.activeSessionsCount : 1}</span>
                </div>
            </div>
            <div class="col-6 col-md-4">
                <h6 class="fw-bold mb-3 text-white">Architecture &amp; Security</h6>
                <ul class="list-unstyled small text-secondary d-flex flex-column gap-2">
                    <li><i class="bi bi-check2 text-primary me-2"></i>N-Tier MVC Layered Pattern</li>
                    <li><i class="bi bi-check2 text-primary me-2"></i>MySQL &amp; JDBC PreparedStatement</li>
                    <li><i class="bi bi-check2 text-primary me-2"></i>HttpSession &amp; Security Filters</li>
                    <li><i class="bi bi-check2 text-primary me-2"></i>Atomic Order Transactions</li>
                </ul>
            </div>
            <div class="col-6 col-md-4">
                <h6 class="fw-bold mb-3 text-white">Syllabus Demonstrations</h6>
                <ul class="list-unstyled small text-secondary d-flex flex-column gap-2">
                    <li>
                        <a href="${pageContext.request.contextPath}/demo-elements.jsp" class="text-light text-decoration-none">
                            <i class="bi bi-code-square text-info me-2"></i>JSP Syntax &amp; Lifecycle Demo
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/demo-i18n.jsp" class="text-light text-decoration-none">
                            <i class="bi bi-translate text-info me-2"></i>Internationalization (I18N) Demo
                        </a>
                    </li>
                    <li><i class="bi bi-laptop text-primary me-2"></i>Swing MVC Desktop Admin</li>
                    <li><i class="bi bi-hdd-network text-primary me-2"></i>Java Sockets &amp; RMI Modules</li>
                </ul>
            </div>
        </div>
        <hr class="border-secondary opacity-25">
        <div class="d-flex flex-column flex-sm-row justify-content-between align-items-center small text-secondary">
            <span>&copy; 2026 ShopSphere Project. Built for RTU Advanced Java Practical Examination.</span>
            <span class="mt-2 mt-sm-0">Apache Tomcat 11 &middot; Java 17+ &middot; Jakarta EE 10</span>
        </div>
    </div>
</footer>

<!-- Bootstrap 5 JavaScript Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
