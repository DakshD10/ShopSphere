<%@ tag description="Reusable ShopSphere Product Card" pageEncoding="UTF-8" %>
<%@ attribute name="product" type="com.shopsphere.model.Product" required="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="card h-100 product-card shadow-sm border-0 rounded-4">
    <div class="img-container">
        <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}">
            <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>" 
                 onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600'">
        </a>
        <c:if test="${product.discount > 0}">
            <span class="badge-discount">-${product.discount.intValue()}% OFF</span>
        </c:if>
    </div>

    <div class="card-body p-3 d-flex flex-column">
        <div class="text-muted small mb-1 text-uppercase fw-semibold" style="font-size: 0.75rem;">
            <c:out value="${product.brand}"/> &middot; <c:out value="${product.categoryName}"/>
        </div>

        <h6 class="card-title fw-bold mb-1">
            <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" 
               class="text-dark text-decoration-none text-truncate d-block">
                <c:out value="${product.name}"/>
            </a>
        </h6>

        <p class="card-text text-secondary small mb-3 flex-grow-1" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
            <c:out value="${product.description}"/>
        </p>

        <div class="d-flex align-items-baseline gap-2 mb-3">
            <span class="fw-bold fs-5 text-dark">
                ₹<fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00"/>
            </span>
            <c:if test="${product.discount > 0}">
                <span class="text-muted text-decoration-line-through small">
                    ₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                </span>
            </c:if>
        </div>

        <div class="d-flex gap-2 align-items-center mt-auto">
            <form action="${pageContext.request.contextPath}/cart" method="post" class="flex-grow-1">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.productId}">
                <input type="hidden" name="quantity" value="1">
                <button type="submit" class="btn btn-primary btn-sm w-100 rounded-3 fw-semibold"
                        ${product.stock < 1 ? 'disabled' : ''}>
                    <i class="bi bi-cart-plus me-1"></i>
                    <c:choose>
                        <c:when test="${product.stock < 1}">Out of Stock</c:when>
                        <c:otherwise>Add to Cart</c:otherwise>
                    </c:choose>
                </button>
            </form>

            <form action="${pageContext.request.contextPath}/wishlist" method="post">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.productId}">
                <button type="submit" class="btn btn-outline-secondary btn-sm rounded-3" title="Save to Wishlist">
                    <i class="bi bi-heart"></i>
                </button>
            </form>
        </div>
    </div>
</div>
