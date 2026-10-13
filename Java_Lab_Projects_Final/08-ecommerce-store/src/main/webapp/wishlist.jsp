<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>My Wishlist &middot; ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <h1 class="h3 fw-bold mb-4">Saved in Wishlist</h1>

        <c:choose>
            <c:when test="${empty wishlistItems}">
                <div class="card border-0 shadow-sm rounded-4 p-5 text-center bg-white">
                    <div class="mb-3 text-muted display-4"><i class="bi bi-heart"></i></div>
                    <h3 class="fw-bold">Your Wishlist is Empty</h3>
                    <p class="text-secondary small mb-4">Explore our product catalog and save items you like!</p>
                    <div>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-3 px-4 py-2">
                            Explore Catalog
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
                    <c:forEach items="${wishlistItems}" var="it">
                        <div class="col">
                            <div class="card h-100 border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                                <div style="height: 180px; background: #f8fafc; overflow: hidden;">
                                    <img src="<c:out value='${it.product.imageUrl}'/>" alt="<c:out value='${it.product.name}'/>" 
                                         class="w-100 h-100 object-fit-cover"
                                         onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600'">
                                </div>
                                <div class="card-body p-3 d-flex flex-column">
                                    <span class="text-muted small text-uppercase fw-semibold" style="font-size: 0.75rem;">
                                        <c:out value="${it.product.brand}"/>
                                    </span>
                                    <h6 class="fw-bold mb-2">
                                        <a href="${pageContext.request.contextPath}/product-details?id=${it.productId}" 
                                           class="text-dark text-decoration-none text-truncate d-block">
                                            <c:out value="${it.product.name}"/>
                                        </a>
                                    </h6>
                                    <div class="fw-bold fs-5 text-dark mb-3">
                                        ₹<fmt:formatNumber value="${it.product.discountedPrice}" pattern="#,##0.00"/>
                                    </div>

                                    <div class="mt-auto d-flex flex-column gap-2">
                                        <form action="${pageContext.request.contextPath}/cart" method="post">
                                            <input type="hidden" name="action" value="add">
                                            <input type="hidden" name="productId" value="${it.productId}">
                                            <input type="hidden" name="quantity" value="1">
                                            <button type="submit" class="btn btn-primary btn-sm w-100 rounded-3 fw-semibold">
                                                <i class="bi bi-cart-plus me-1"></i> Move to Cart
                                            </button>
                                        </form>

                                        <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="productId" value="${it.productId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm w-100 rounded-3">
                                                <i class="bi bi-trash3 me-1"></i> Remove
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
