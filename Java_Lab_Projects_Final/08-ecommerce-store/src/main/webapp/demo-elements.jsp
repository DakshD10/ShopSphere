<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>JSP Traditional Syntax &amp; Lifecycle &middot; RTU Syllabus Demo</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="bg-light d-flex flex-column min-vh-100">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5 bg-white mb-4">
            <span class="badge bg-primary-subtle text-primary fw-bold text-uppercase px-3 py-2 rounded-pill d-inline-block mb-2" style="width: fit-content;">
                RTU Advanced Java Syllabus Demonstration &middot; Unit 4
            </span>
            <h1 class="h2 fw-bold mb-3">JSP Architecture, Lifecycle &amp; Traditional Syntax Elements</h1>
            <p class="text-secondary leading-relaxed mb-4">
                This page demonstrates traditional JSP elements as specified in the RTU Advanced Java syllabus 
                (Section 16 of the project guide), while the core ShopSphere e-commerce application uses 
                Expression Language (EL) and JSTL for clean MVC separation.
            </p>

            <hr class="opacity-25 mb-4">

            <!-- 1. Traditional JSP Elements -->
            <h4 class="fw-bold mb-3 text-dark"><i class="bi bi-code-slash me-2 text-primary"></i>1. Traditional JSP Syntax Elements</h4>
            
            <%-- 1. JSP Declaration: class-level members placed outside _jspService() in generated servlet --%>
            <%! 
                private int serverInstanceCounter = 0; 
                public synchronized int getNextCounter() { 
                    return ++serverInstanceCounter; 
                } 
            %>

            <%-- 2. JSP Scriptlet: executable code placed directly inside _jspService() --%>
            <%
                String demoStudent = "Advanced Java Student";
                String serverPlatform = application.getServerInfo();
                java.util.Date accessTime = new java.util.Date();
                int currentCounter = getNextCounter();
            %>

            <div class="table-responsive mb-4">
                <table class="table table-bordered align-middle">
                    <thead class="table-light small">
                        <tr>
                            <th style="width: 20%;">Element Type</th>
                            <th style="width: 35%;">Syntax Example</th>
                            <th style="width: 45%;">Live Evaluated Output</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><strong class="text-primary">JSP Declaration</strong></td>
                            <td><code>&lt;%! int counter = 0; int increment() { ... } %&gt;</code></td>
                            <td>Declared server instance method executed. Counter = <strong><%= currentCounter %></strong></td>
                        </tr>
                        <tr>
                            <td><strong class="text-primary">JSP Scriptlet</strong></td>
                            <td><code>&lt;% String student = "Advanced Java Student"; %&gt;</code></td>
                            <td>Variable defined inside <code>_jspService()</code> method.</td>
                        </tr>
                        <tr>
                            <td><strong class="text-primary">JSP Expression</strong></td>
                            <td><code>&lt;%= studentName %&gt;</code></td>
                            <td><code>&lt;%= demoStudent %&gt;</code> evaluates to: <strong><%= demoStudent %></strong></td>
                        </tr>
                        <tr>
                            <td><strong class="text-primary">JSP Expression</strong></td>
                            <td><code>&lt;%= new java.util.Date() %&gt;</code></td>
                            <td>Server Timestamp: <code><%= accessTime %></code></td>
                        </tr>
                        <tr>
                            <td><strong class="text-primary">JSP Comment</strong></td>
                            <td><code>&lt;%-- Server-only comment --%&gt;</code></td>
                            <td><span class="text-muted small">Processed by Jasper compiler; stripped from final HTML output sent to browser.</span></td>
                        </tr>
                        <tr>
                            <td><strong class="text-success">Expression Language (EL)</strong></td>
                            <td><code>\${appName}</code> &middot; <code>\${param.test}</code></td>
                            <td>Evaluates to: <strong>${applicationScope.appName}</strong> (from ServletContext)</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 2. JSP Lifecycle Explanation -->
            <h4 class="fw-bold mb-3 text-dark"><i class="bi bi-arrow-repeat me-2 text-primary"></i>2. JSP Translation &amp; Lifecycle Phases</h4>
            <div class="row g-3 mb-4">
                <div class="col-md-3">
                    <div class="p-3 border rounded-3 bg-light text-center h-100">
                        <span class="badge bg-dark rounded-circle p-2 mb-2">1</span>
                        <h6 class="fw-bold mb-1">Translation</h6>
                        <small class="text-muted">Tomcat Jasper translates <code>.jsp</code> into a Java Servlet source file (<code>*_jsp.java</code>).</small>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="p-3 border rounded-3 bg-light text-center h-100">
                        <span class="badge bg-dark rounded-circle p-2 mb-2">2</span>
                        <h6 class="fw-bold mb-1">Compilation</h6>
                        <small class="text-muted">The Java compiler compiles the servlet source into executable bytecode (<code>*_jsp.class</code>).</small>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="p-3 border rounded-3 bg-light text-center h-100">
                        <span class="badge bg-dark rounded-circle p-2 mb-2">3</span>
                        <h6 class="fw-bold mb-1">Loading &amp; Init</h6>
                        <small class="text-muted">Container loads the class, creates an instance, and invokes <code>jspInit()</code> lifecycle method.</small>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="p-3 border rounded-3 bg-light text-center h-100">
                        <span class="badge bg-dark rounded-circle p-2 mb-2">4</span>
                        <h6 class="fw-bold mb-1">Service &amp; Destroy</h6>
                        <small class="text-muted">Each client HTTP request calls <code>_jspService()</code>. When unloaded, <code>jspDestroy()</code> cleans up.</small>
                    </div>
                </div>
            </div>

            <!-- 3. Reusable Custom Tags Demo -->
            <h4 class="fw-bold mb-3 text-dark"><i class="bi bi-stars me-2 text-primary"></i>3. Custom JSP Tag Files (Section 17)</h4>
            <div class="p-3 border rounded-3 bg-light mb-4">
                <p class="small text-secondary mb-2">
                    ShopSphere implements reusable components in <code>WEB-INF/tags/rating.tag</code> and <code>WEB-INF/tags/productCard.tag</code>:
                </p>
                <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
                <div class="d-flex align-items-center gap-3">
                    <span>Rendered via custom <code>&lt;tags:rating&gt;</code>:</span>
                    <tags:rating score="4.8" reviewCount="42" />
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
