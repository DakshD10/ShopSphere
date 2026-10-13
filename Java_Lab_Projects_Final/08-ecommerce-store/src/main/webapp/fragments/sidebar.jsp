<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="col-md-3 col-lg-2 px-0 admin-sidebar d-flex flex-column p-3">
    <div class="mb-4 px-2">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="d-flex align-items-center text-white text-decoration-none fw-bold fs-5">
            <i class="bi bi-shield-check text-primary fs-4 me-2"></i>
            <span>Admin<span class="text-primary">Panel</span></span>
        </a>
        <small class="text-secondary px-1 d-block" style="font-size: 0.75rem;">ShopSphere Control Center</small>
    </div>

    <ul class="nav nav-pills flex-column mb-auto gap-1">
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/dashboard" 
               class="nav-link text-light ${pageContext.request.requestURI.endsWith('dashboard.jsp') ? 'active' : ''}">
                <i class="bi bi-speedometer2"></i> Dashboard
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/products" 
               class="nav-link text-light ${pageContext.request.requestURI.endsWith('products.jsp') ? 'active' : ''}">
                <i class="bi bi-box-seam"></i> Products
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/categories" 
               class="nav-link text-light ${pageContext.request.requestURI.endsWith('categories.jsp') ? 'active' : ''}">
                <i class="bi bi-tags"></i> Categories
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/orders" 
               class="nav-link text-light ${pageContext.request.requestURI.endsWith('orders.jsp') ? 'active' : ''}">
                <i class="bi bi-receipt"></i> Orders
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/users" 
               class="nav-link text-light ${pageContext.request.requestURI.endsWith('users.jsp') ? 'active' : ''}">
                <i class="bi bi-people"></i> Customers
            </a>
        </li>
    </ul>

    <hr class="border-secondary opacity-25 my-3">

    <div class="d-flex flex-column gap-2 px-2">
        <a href="${pageContext.request.contextPath}/products" class="text-secondary small text-decoration-none d-flex align-items-center">
            <i class="bi bi-arrow-left me-2"></i> Customer Storefront
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="text-danger small text-decoration-none d-flex align-items-center">
            <i class="bi bi-box-arrow-right me-2"></i> Sign Out
        </a>
    </div>
</div>
