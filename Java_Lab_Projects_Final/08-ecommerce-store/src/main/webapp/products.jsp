<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Products Catalog &middot; ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-4 flex-grow-1">

        <!-- Notice Alert -->
        <c:if test="${not empty param.notice}">
            <div class="alert alert-success alert-dismissible fade show rounded-3 mb-4" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>
                <c:choose>
                    <c:when test="${param.notice eq 'registered'}">Account created successfully! Welcome to ShopSphere.</c:when>
                    <c:when test="${param.notice eq 'added'}">Product added to your cart!</c:when>
                    <c:otherwise><c:out value="${param.notice}"/></c:otherwise>
                </c:choose>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- Hero Promo Banner -->
        <div class="hero-gradient p-4 p-md-5 mb-5 shadow-sm text-center text-md-start">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <span class="badge bg-primary px-3 py-2 rounded-pill fw-semibold mb-2">CAMPUS TECH &amp; ESSENTIALS</span>
                    <h1 class="display-6 fw-bold mb-2">Smart gear for modern college life.</h1>
                    <p class="lead text-light opacity-75 mb-0">High precision accessories, chargers, running shoes, and student essentials.</p>
                </div>
                <div class="col-md-4 text-center mt-3 mt-md-0">
                    <div class="d-inline-flex flex-column bg-white bg-opacity-10 p-3 rounded-4 backdrop-blur">
                        <span class="text-warning fw-bold fs-5"><i class="bi bi-tag-fill me-1"></i> WELCOME10</span>
                        <small class="text-light opacity-75">Use code for 10% off your order</small>
                    </div>
                </div>
            </div>
        </div>

        <!-- Filter & Sorting Bar -->
        <div class="row g-4 mb-4">
            <!-- Sidebar Filters -->
            <div class="col-lg-3">
                <div class="card border-0 shadow-sm rounded-4 p-4 bg-white sticky-top" style="top: 80px; z-index: 10;">
                    <h5 class="fw-bold mb-3 d-flex align-items-center justify-content-between">
                        <span>Filters</span>
                        <a href="${pageContext.request.contextPath}/products" class="small text-decoration-none fw-normal">Reset</a>
                    </h5>

                    <form action="${pageContext.request.contextPath}/products" method="get">
                        <!-- Retain search query if present -->
                        <c:if test="${not empty searchQuery}">
                            <input type="hidden" name="search" value="<c:out value='${searchQuery}'/>">
                        </c:if>

                        <!-- Categories Filter -->
                        <div class="mb-4">
                            <label class="form-label small fw-bold text-uppercase text-muted">Category</label>
                            <div class="list-group list-group-flush small">
                                <a href="${pageContext.request.contextPath}/products" 
                                   class="list-group-item list-group-item-action border-0 px-0 py-1 ${empty selectedCategory ? 'fw-bold text-primary' : 'text-secondary'}">
                                    All Categories
                                </a>
                                <c:forEach items="${categories}" var="cat">
                                    <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}" 
                                       class="list-group-item list-group-item-action border-0 px-0 py-1 ${selectedCategory eq cat.categoryId ? 'fw-bold text-primary' : 'text-secondary'}">
                                        <c:out value="${cat.name}"/>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>

                        <!-- Price Range Filter -->
                        <div class="mb-4">
                            <label class="form-label small fw-bold text-uppercase text-muted">Price Range (₹)</label>
                            <div class="d-flex gap-2">
                                <input type="number" name="minPrice" class="form-control form-control-sm" placeholder="Min" 
                                       value="<c:out value='${minPrice}'/>" min="0">
                                <input type="number" name="maxPrice" class="form-control form-control-sm" placeholder="Max" 
                                       value="<c:out value='${maxPrice}'/>" min="0">
                            </div>
                        </div>

                        <!-- Sort By -->
                        <div class="mb-4">
                            <label class="form-label small fw-bold text-uppercase text-muted">Sort By</label>
                            <select name="sort" class="form-select form-select-sm" onchange="this.form.submit()">
                                <option value="newest" ${sortBy eq 'newest' ? 'selected' : ''}>Newest First</option>
                                <option value="price_asc" ${sortBy eq 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                                <option value="price_desc" ${sortBy eq 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                                <option value="name_asc" ${sortBy eq 'name_asc' ? 'selected' : ''}>Name: A to Z</option>
                            </select>
                        </div>

                        <c:if test="${not empty selectedCategory}">
                            <input type="hidden" name="category" value="${selectedCategory}">
                        </c:if>

                        <button type="submit" class="btn btn-primary btn-sm w-100 rounded-3">
                            Apply Filters
                        </button>
                    </form>
                </div>
            </div>

            <!-- Product Cards Grid -->
            <div class="col-lg-9">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <span class="text-muted small">
                        Showing <strong>${products.size()}</strong> products
                        <c:if test="${not empty searchQuery}"> matching "<strong><c:out value='${searchQuery}'/></strong>"</c:if>
                    </span>
                </div>

                <c:choose>
                    <c:when test="${empty products}">
                        <div class="card border-0 shadow-sm rounded-4 p-5 text-center bg-white my-4">
                            <div class="mb-3 text-muted display-4"><i class="bi bi-box2"></i></div>
                            <h4 class="fw-bold">No Products Found</h4>
                            <p class="text-secondary small">Try adjusting your category filter, search keyword, or price range.</p>
                            <div>
                                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-sm rounded-3">
                                    Clear All Filters
                                </a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-xl-3 g-4">
                            <c:forEach items="${products}" var="p">
                                <div class="col">
                                    <div class="card h-100 product-card shadow-sm border-0 rounded-4">
                                        <div class="img-container">
                                            <a href="${pageContext.request.contextPath}/product-details?id=${p.productId}">
                                                <img src="<c:out value='${p.imageUrl}'/>" alt="<c:out value='${p.name}'/>" 
                                                     onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600'">
                                            </a>
                                            <c:if test="${p.discount > 0}">
                                                <span class="badge-discount">-${p.discount.intValue()}% OFF</span>
                                            </c:if>
                                        </div>

                                        <div class="card-body p-3 d-flex flex-column">
                                            <div class="text-muted small mb-1 text-uppercase fw-semibold" style="font-size: 0.75rem;">
                                                <c:out value="${p.brand}"/> &middot; <c:out value="${p.categoryName}"/>
                                            </div>

                                            <h6 class="card-title fw-bold mb-1">
                                                <a href="${pageContext.request.contextPath}/product-details?id=${p.productId}" 
                                                   class="text-dark text-decoration-none text-truncate d-block">
                                                    <c:out value="${p.name}"/>
                                                </a>
                                            </h6>

                                            <p class="card-text text-secondary small mb-3 flex-grow-1" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                                <c:out value="${p.description}"/>
                                            </p>

                                            <!-- Price & Availability -->
                                            <div class="d-flex align-items-baseline gap-2 mb-3">
                                                <span class="fw-bold fs-5 text-dark">
                                                    ₹<fmt:formatNumber value="${p.discountedPrice}" pattern="#,##0.00"/>
                                                </span>
                                                <c:if test="${p.discount > 0}">
                                                    <span class="text-muted text-decoration-line-through small">
                                                        ₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/>
                                                    </span>
                                                </c:if>
                                            </div>

                                            <!-- Action Row -->
                                            <div class="d-flex gap-2 align-items-center mt-auto">
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="flex-grow-1">
                                                    <input type="hidden" name="action" value="add">
                                                    <input type="hidden" name="productId" value="${p.productId}">
                                                    <input type="hidden" name="quantity" value="1">
                                                    <button type="submit" class="btn btn-primary btn-sm w-100 rounded-3 fw-semibold"
                                                            ${p.stock < 1 ? 'disabled' : ''}>
                                                        <i class="bi bi-cart-plus me-1"></i>
                                                        <c:choose>
                                                            <c:when test="${p.stock < 1}">Out of Stock</c:when>
                                                            <c:otherwise>Add to Cart</c:otherwise>
                                                        </c:choose>
                                                    </button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                                    <input type="hidden" name="action" value="add">
                                                    <input type="hidden" name="productId" value="${p.productId}">
                                                    <button type="submit" class="btn btn-outline-secondary btn-sm rounded-3" title="Add to Wishlist">
                                                        <i class="bi bi-heart"></i>
                                                    </button>
                                                </form>
                                            </div>

                                            <!-- Stock notice -->
                                            <div class="mt-2 text-end">
                                                <small class="${p.stock < 10 ? 'text-danger' : 'text-muted'} fw-semibold" style="font-size: 0.75rem;">
                                                    <c:choose>
                                                        <c:when test="${p.stock < 1}">Unavailable</c:when>
                                                        <c:when test="${p.stock < 10}">Only ${p.stock} left!</c:when>
                                                        <c:otherwise>${p.stock} in stock</c:otherwise>
                                                    </c:choose>
                                                </small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>

    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
