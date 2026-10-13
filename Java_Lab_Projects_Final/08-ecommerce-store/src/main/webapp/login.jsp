<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In &middot; ShopSphere</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light d-flex flex-column min-vh-100">

    <!-- Top Navigation Bar -->
    <header class="navbar navbar-expand-lg navbar-dark bg-dark py-3">
        <div class="container">
            <a class="navbar-brand fw-bold fs-4" href="${pageContext.request.contextPath}/">
                <i class="bi bi-shop me-2 text-primary"></i>ShopSphere
            </a>
            <div class="d-flex">
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-light btn-sm">Create Account</a>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="container my-auto py-5">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5 bg-white">
                    <div class="text-center mb-4">
                        <div class="d-inline-flex align-items-center justify-content-center bg-primary-subtle text-primary rounded-circle mb-3" style="width: 60px; height: 60px;">
                            <i class="bi bi-person-lock fs-2"></i>
                        </div>
                        <h2 class="fw-bold h4">Welcome Back</h2>
                        <p class="text-muted small">Sign in to your customer or administrative account</p>
                    </div>

                    <!-- Alerts -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger d-flex align-items-center rounded-3 py-2" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <div><c:out value="${error}"/></div>
                        </div>
                    </c:if>

                    <c:if test="${not empty notice}">
                        <div class="alert alert-success d-flex align-items-center rounded-3 py-2" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i>
                            <div><c:out value="${notice}"/></div>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation">
                        <div class="mb-3">
                            <label for="email" class="form-label small fw-semibold">Email Address</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
                                <input type="email" class="form-control border-start-0" id="email" name="email"
                                       value="<c:out value='${email}'/>" placeholder="name@example.com" required autofocus>
                            </div>
                        </div>

                        <div class="mb-4">
                            <div class="d-flex justify-content-between align-items-center">
                                <label for="password" class="form-label small fw-semibold">Password</label>
                            </div>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="bi bi-shield-lock text-muted"></i></span>
                                <input type="password" class="form-control border-start-0" id="password" name="password"
                                       placeholder="Enter your password" required>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 rounded-3 fw-semibold">
                            Sign In <i class="bi bi-arrow-right ms-1"></i>
                        </button>
                    </form>

                    <!-- Seed Credentials Hint for Lab / Viva -->
                    <div class="bg-light rounded-3 p-3 mt-4 border border-dashed small">
                        <span class="fw-semibold text-secondary d-block mb-1">
                            <i class="bi bi-info-circle me-1"></i>Test Credentials:
                        </span>
                        <div class="text-muted">
                            <div><strong>Admin:</strong> <code>admin@shopsphere.com</code> / <code>admin123</code></div>
                            <div><strong>Customer:</strong> <code>student@shopsphere.com</code> / <code>customer123</code></div>
                        </div>
                    </div>

                    <div class="text-center mt-4 text-muted small">
                        Don't have an account yet? 
                        <a href="${pageContext.request.contextPath}/register" class="fw-semibold text-decoration-none">Sign Up</a>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer class="footer mt-auto py-3 bg-white border-top text-center text-muted small">
        <div class="container">
            ShopSphere &middot; RTU Advanced Java Capstone Practical &middot; Apache Tomcat 11
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
