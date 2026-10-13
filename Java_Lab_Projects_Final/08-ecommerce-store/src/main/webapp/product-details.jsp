<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title><c:out value="${product.name}"/> &middot; ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products?category=${product.categoryId}" class="text-decoration-none"><c:out value="${product.categoryName}"/></a></li>
                <li class="breadcrumb-item active" aria-current="page"><c:out value="${product.name}"/></li>
            </ol>
        </nav>

        <c:if test="${param.review eq 'success'}">
            <div class="alert alert-success alert-dismissible fade show rounded-3 mb-4" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>Thank you! Your product review has been submitted.
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="row g-5">
            <!-- Product Image -->
            <div class="col-md-6">
                <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white p-3">
                    <div class="position-relative" style="height: 420px; background: #f8fafc; border-radius: 1rem; overflow: hidden;">
                        <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>" 
                             class="w-100 h-100 object-fit-contain"
                             onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600'">
                        <c:if test="${product.discount > 0}">
                            <span class="badge-discount fs-6 px-3 py-2">
                                -${product.discountPercent}% OFF
                            </span>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- Product Information -->
            <div class="col-md-6">
                <div class="ps-md-3">
                    <span class="badge bg-secondary-subtle text-secondary px-3 py-1 rounded-pill small fw-semibold text-uppercase mb-2">
                        <c:out value="${product.brand}"/> &middot; <c:out value="${product.categoryName}"/>
                    </span>

                    <h1 class="h2 fw-bold mb-2"><c:out value="${product.name}"/></h1>

                    <!-- Rating summary -->
                    <div class="d-flex align-items-center gap-2 mb-3">
                        <div class="star-rating">
                            <c:forEach begin="1" end="5" var="i">
                                <i class="bi ${i <= avgRating ? 'bi-star-fill text-warning' : 'bi-star text-muted'}"></i>
                            </c:forEach>
                        </div>
                        <span class="fw-bold small">${avgRating > 0 ? avgRating : 'New'}</span>
                        <span class="text-muted small">(${reviews.size()} customer reviews)</span>
                    </div>

                    <!-- Price -->
                    <div class="d-flex align-items-baseline gap-3 mb-4">
                        <span class="display-6 fw-bold text-dark">
                            ₹<fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00"/>
                        </span>
                        <c:if test="${product.discount > 0}">
                            <span class="text-muted text-decoration-line-through fs-5">
                                ₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                            </span>
                            <span class="badge bg-danger-subtle text-danger rounded-pill fw-semibold small">
                                Save ₹<fmt:formatNumber value="${product.price - product.discountedPrice}" pattern="#,##0.00"/>
                            </span>
                        </c:if>
                    </div>

                    <!-- Description -->
                    <div class="mb-4">
                        <h6 class="fw-bold text-uppercase small text-muted">About this item</h6>
                        <p class="text-secondary leading-relaxed"><c:out value="${product.description}"/></p>
                    </div>

                    <!-- Stock Status -->
                    <div class="mb-4">
                        <c:choose>
                            <c:when test="${product.stock > 10}">
                                <span class="badge bg-success-subtle text-success px-3 py-2 rounded-pill fw-semibold">
                                    <i class="bi bi-check-circle-fill me-1"></i> In Stock (${product.stock} units available)
                                </span>
                            </c:when>
                            <c:when test="${product.stock > 0}">
                                <span class="badge bg-warning-subtle text-warning-emphasis px-3 py-2 rounded-pill fw-semibold">
                                    <i class="bi bi-exclamation-circle-fill me-1"></i> Only ${product.stock} left in stock - order soon
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger-subtle text-danger px-3 py-2 rounded-pill fw-semibold">
                                    <i class="bi bi-x-circle-fill me-1"></i> Currently Out of Stock
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Add to Cart Form -->
                    <form action="${pageContext.request.contextPath}/cart" method="post" class="mb-4">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productId}">

                        <div class="row g-2 align-items-center mb-3">
                            <div class="col-4 col-sm-3">
                                <label for="quantity" class="form-label small fw-semibold">Quantity</label>
                                <select name="quantity" id="quantity" class="form-select" ${product.stock < 1 ? 'disabled' : ''}>
                                    <c:forEach begin="1" end="${product.stock > 5 ? 5 : product.stock}" var="q">
                                        <option value="${q}">${q}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-8 col-sm-9 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary w-100 py-2 rounded-3 fw-semibold"
                                        ${product.stock < 1 ? 'disabled' : ''}>
                                    <i class="bi bi-cart-plus me-2"></i>Add to Cart
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Add to Wishlist -->
                    <form action="${pageContext.request.contextPath}/wishlist" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <button type="submit" class="btn btn-outline-secondary w-100 rounded-3">
                            <i class="bi bi-heart me-2"></i>Save to Wishlist
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <hr class="my-5 opacity-25">

        <!-- Reviews Section -->
        <div class="row g-5">
            <div class="col-lg-7">
                <h4 class="fw-bold mb-4">Customer Reviews (${reviews.size()})</h4>

                <c:choose>
                    <c:when test="${empty reviews}">
                        <div class="card border-0 rounded-4 bg-light p-4 text-center text-muted">
                            <i class="bi bi-chat-left-dots fs-3 mb-2"></i>
                            <p class="mb-0">No reviews yet for this product. Be the first to review it!</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex flex-column gap-3">
                            <c:forEach items="${reviews}" var="rev">
                                <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <div class="d-flex align-items-center gap-2">
                                            <div class="bg-primary-subtle text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold" 
                                                 style="width: 32px; height: 32px; font-size: 0.85rem;">
                                                ${rev.userName.substring(0, 1).toUpperCase()}
                                            </div>
                                            <span class="fw-bold small"><c:out value="${rev.userName}"/></span>
                                        </div>
                                        <div class="star-rating">
                                            <c:forEach begin="1" end="5" var="s">
                                                <i class="bi ${s <= rev.rating ? 'bi-star-fill text-warning' : 'bi-star text-muted'}"></i>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <p class="text-secondary small mb-0"><c:out value="${rev.reviewText}"/></p>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Write a Review Box -->
            <div class="col-lg-5">
                <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                    <h5 class="fw-bold mb-3">Write a Customer Review</h5>

                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <form action="${pageContext.request.contextPath}/add-review" method="post">
                                <input type="hidden" name="productId" value="${product.productId}">

                                <div class="mb-3">
                                    <label for="rating" class="form-label small fw-semibold">Overall Rating</label>
                                    <select name="rating" id="rating" class="form-select" required>
                                        <option value="5">★★★★★ (5 Stars - Excellent)</option>
                                        <option value="4">★★★★☆ (4 Stars - Very Good)</option>
                                        <option value="3">★★★☆☆ (3 Stars - Average)</option>
                                        <option value="2">★★☆☆☆ (2 Stars - Below Average)</option>
                                        <option value="1">★☆☆☆☆ (1 Star - Poor)</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="reviewText" class="form-label small fw-semibold">Your Comments</label>
                                    <textarea name="reviewText" id="reviewText" rows="4" class="form-control" 
                                              placeholder="Share details of your experience with this item..." required></textarea>
                                </div>

                                <button type="submit" class="btn btn-primary w-100 rounded-3 fw-semibold">
                                    Submit Review
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-4">
                                <p class="text-muted small mb-3">You must be logged in to post a review.</p>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary btn-sm rounded-3">
                                    Sign In to Review
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
