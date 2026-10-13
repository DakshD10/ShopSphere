<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account &middot; ShopSphere</title>
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
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">Sign In</a>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="container my-auto py-5">
        <div class="row justify-content-center">
            <div class="col-md-7 col-lg-5">
                <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5 bg-white">
                    <div class="text-center mb-4">
                        <div class="d-inline-flex align-items-center justify-content-center bg-primary-subtle text-primary rounded-circle mb-3" style="width: 60px; height: 60px;">
                            <i class="bi bi-person-plus fs-2"></i>
                        </div>
                        <h2 class="fw-bold h4">Create an Account</h2>
                        <p class="text-muted small">Join ShopSphere to explore products and place orders</p>
                    </div>

                    <!-- Alerts -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger d-flex align-items-center rounded-3 py-2" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <div><c:out value="${error}"/></div>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="post" class="needs-validation">
                        <div class="mb-3">
                            <label for="name" class="form-label small fw-semibold">Full Name</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="bi bi-person text-muted"></i></span>
                                <input type="text" class="form-control border-start-0" id="name" name="name"
                                       value="<c:out value='${name}'/>" placeholder="e.g. Aryan Sharma" required autofocus maxlength="100">
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label small fw-semibold">Email Address</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
                                <input type="email" class="form-control border-start-0" id="email" name="email"
                                       value="<c:out value='${email}'/>" placeholder="name@example.com" required maxlength="160">
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="mobile" class="form-label small fw-semibold">Mobile Number</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="bi bi-phone text-muted"></i></span>
                                <input type="tel" class="form-control border-start-0" id="mobile" name="mobile"
                                       value="<c:out value='${mobile}'/>" placeholder="e.g. 9876543210" maxlength="20">
                            </div>
                        </div>

                        <div class="mb-4">
                            <label for="password" class="form-label small fw-semibold">Password</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light border-end-0"><i class="bi bi-shield-lock text-muted"></i></span>
                                <input type="password" class="form-control border-start-0" id="password" name="password"
                                       placeholder="Minimum 6 characters" required minlength="6">
                            </div>
                            <div class="form-text">Must be at least 6 characters long.</div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 rounded-3 fw-semibold">
                            Create Account <i class="bi bi-check-lg ms-1"></i>
                        </button>
                    </form>

                    <div class="text-center mt-4 text-muted small">
                        Already have an account? 
                        <a href="${pageContext.request.contextPath}/login" class="fw-semibold text-decoration-none">Sign In</a>
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
