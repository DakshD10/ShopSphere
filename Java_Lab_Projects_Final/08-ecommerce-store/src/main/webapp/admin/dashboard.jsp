<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Dashboard &middot; ShopSphere</title>
    <%@ include file="../fragments/header.jsp" %>
</head>
<body class="bg-light">

    <!-- Top Admin Header Bar -->
    <header class="navbar navbar-dark bg-dark sticky-top py-2 px-3 shadow-sm d-flex justify-content-between">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <i class="bi bi-shield-lock-fill text-primary me-2"></i>ShopSphere Administration
        </a>
        <div class="d-flex align-items-center gap-3">
            <span class="text-secondary small d-none d-md-inline">Logged in as <strong><c:out value="${sessionScope.user.name}"/></strong> (ADMIN)</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Sign Out</a>
        </div>
    </header>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar Navigation -->
            <%@ include file="../fragments/sidebar.jsp" %>

            <!-- Main Dashboard Area -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-4 border-bottom">
                    <h1 class="h3 fw-bold mb-0">Overview &amp; Metrics</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-sm btn-primary rounded-3">
                            <i class="bi bi-plus-lg me-1"></i> Add Product
                        </a>
                    </div>
                </div>

                <!-- KPI Metric Cards -->
                <div class="row g-4 mb-5">
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="card border-0 shadow-sm rounded-4 p-3 bg-white">
                            <div class="d-flex align-items-center justify-content-between mb-2">
                                <span class="text-muted small fw-semibold text-uppercase">Total Revenue</span>
                                <div class="bg-success-subtle text-success rounded-3 p-2">
                                    <i class="bi bi-currency-rupee fs-5"></i>
                                </div>
                            </div>
                            <h3 class="fw-bold mb-1">
                                ₹<fmt:formatNumber value="${metrics.totalRevenue}" pattern="#,##0.00"/>
                            </h3>
                            <small class="text-muted">Lifetime gross receipts</small>
                        </div>
                    </div>

                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="card border-0 shadow-sm rounded-4 p-3 bg-white">
                            <div class="d-flex align-items-center justify-content-between mb-2">
                                <span class="text-muted small fw-semibold text-uppercase">Total Orders</span>
                                <div class="bg-primary-subtle text-primary rounded-3 p-2">
                                    <i class="bi bi-receipt fs-5"></i>
                                </div>
                            </div>
                            <h3 class="fw-bold mb-1">${metrics.totalOrders}</h3>
                            <small class="text-muted">Orders placed</small>
                        </div>
                    </div>

                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="card border-0 shadow-sm rounded-4 p-3 bg-white">
                            <div class="d-flex align-items-center justify-content-between mb-2">
                                <span class="text-muted small fw-semibold text-uppercase">Pending Orders</span>
                                <div class="bg-warning-subtle text-warning-emphasis rounded-3 p-2">
                                    <i class="bi bi-clock-history fs-5"></i>
                                </div>
                            </div>
                            <h3 class="fw-bold mb-1">${metrics.pendingOrders}</h3>
                            <small class="text-muted">Awaiting confirmation</small>
                        </div>
                    </div>

                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="card border-0 shadow-sm rounded-4 p-3 bg-white">
                            <div class="d-flex align-items-center justify-content-between mb-2">
                                <span class="text-muted small fw-semibold text-uppercase">Customers</span>
                                <div class="bg-info-subtle text-info rounded-3 p-2">
                                    <i class="bi bi-people fs-5"></i>
                                </div>
                            </div>
                            <h3 class="fw-bold mb-1">${totalUsers}</h3>
                            <small class="text-muted">Registered accounts</small>
                        </div>
                    </div>
                </div>

                <div class="row g-4">
                    <!-- Low Stock Inventory Alerts -->
                    <div class="col-lg-6">
                        <div class="card border-0 shadow-sm rounded-4 p-4 bg-white h-100">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h5 class="fw-bold mb-0 text-danger d-flex align-items-center">
                                    <i class="bi bi-exclamation-octagon-fill me-2"></i> Low Stock Alerts
                                </h5>
                                <span class="badge bg-danger rounded-pill">${lowStockProducts.size()} items</span>
                            </div>

                            <c:choose>
                                <c:when test="${empty lowStockProducts}">
                                    <p class="text-muted small my-auto text-center py-4">All catalog items have healthy stock levels.</p>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table align-middle table-sm">
                                            <thead>
                                                <tr class="text-secondary small">
                                                    <th>Product</th>
                                                    <th>Category</th>
                                                    <th class="text-end">Stock Left</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${lowStockProducts}" var="lp">
                                                    <tr>
                                                        <td class="fw-semibold"><c:out value="${lp.name}"/></td>
                                                        <td class="small text-muted"><c:out value="${lp.categoryName}"/></td>
                                                        <td class="text-end">
                                                            <span class="badge ${lp.stock < 1 ? 'bg-danger' : 'bg-warning text-dark'}">
                                                                ${lp.stock} left
                                                            </span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Recent Orders Stream -->
                    <div class="col-lg-6">
                        <div class="card border-0 shadow-sm rounded-4 p-4 bg-white h-100">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h5 class="fw-bold mb-0">Recent Orders</h5>
                                <a href="${pageContext.request.contextPath}/admin/orders" class="small text-decoration-none">View All</a>
                            </div>

                            <c:choose>
                                <c:when test="${empty recentOrders}">
                                    <p class="text-muted small my-auto text-center py-4">No orders placed yet.</p>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table align-middle table-sm">
                                            <thead>
                                                <tr class="text-secondary small">
                                                    <th>Order ID</th>
                                                    <th>Total</th>
                                                    <th>Status</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${recentOrders}" var="ro">
                                                    <tr>
                                                        <td class="fw-semibold">#${ro.orderId}</td>
                                                        <td>₹<fmt:formatNumber value="${ro.totalAmount}" pattern="#,##0.00"/></td>
                                                        <td>
                                                            <span class="badge bg-primary-subtle text-primary">${ro.orderStatus}</span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
