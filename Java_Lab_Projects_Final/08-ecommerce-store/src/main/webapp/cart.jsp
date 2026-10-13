<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Shopping Cart &middot; ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <h1 class="h3 fw-bold mb-4">Your Shopping Cart</h1>

        <!-- Flash alerts -->
        <c:if test="${not empty sessionScope.notice}">
            <div class="alert alert-success alert-dismissible fade show rounded-3" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.notice}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="notice" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.cartError}">
            <div class="alert alert-danger alert-dismissible fade show rounded-3" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${sessionScope.cartError}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="cartError" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.couponNotice}">
            <div class="alert alert-success alert-dismissible fade show rounded-3" role="alert">
                <i class="bi bi-tag-fill me-2"></i><c:out value="${sessionScope.couponNotice}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="couponNotice" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.couponError}">
            <div class="alert alert-warning alert-dismissible fade show rounded-3" role="alert">
                <i class="bi bi-exclamation-circle-fill me-2"></i><c:out value="${sessionScope.couponError}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="couponError" scope="session"/>
        </c:if>

        <c:choose>
            <c:when test="${empty cartItems}">
                <!-- Empty Cart State -->
                <div class="card border-0 shadow-sm rounded-4 p-5 text-center bg-white my-4">
                    <div class="mb-3 text-muted display-4"><i class="bi bi-cart-x"></i></div>
                    <h3 class="fw-bold">Your Cart is Empty</h3>
                    <p class="text-secondary small mb-4">You have not added any items to your shopping cart yet.</p>
                    <div>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary px-4 py-2 rounded-3 fw-semibold">
                            <i class="bi bi-arrow-left me-1"></i> Continue Shopping
                        </a>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="row g-4">
                    <!-- Cart Items Table List -->
                    <div class="col-lg-8">
                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                            <div class="table-responsive">
                                <table class="table align-middle mb-0">
                                    <thead class="table-light small text-uppercase text-secondary">
                                        <tr>
                                            <th scope="col" class="ps-4">Product</th>
                                            <th scope="col">Price</th>
                                            <th scope="col" style="width: 140px;">Quantity</th>
                                            <th scope="col">Subtotal</th>
                                            <th scope="col" class="pe-4 text-end">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${cartItems}" var="item">
                                            <tr>
                                                <td class="ps-4 py-3">
                                                    <div class="d-flex align-items-center gap-3">
                                                        <img src="<c:out value='${item.product.imageUrl}'/>" 
                                                             alt="<c:out value='${item.product.name}'/>" 
                                                             class="rounded-3" style="width: 64px; height: 64px; object-fit: cover;"
                                                             onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600'">
                                                        <div>
                                                            <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}"
                                                               class="fw-bold text-dark text-decoration-none d-block">
                                                                <c:out value="${item.product.name}"/>
                                                            </a>
                                                            <small class="text-muted"><c:out value="${item.product.brand}"/></small>
                                                        </div>
                                                    </div>
                                                </td>

                                                <td>
                                                    <span class="fw-semibold">
                                                        ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/>
                                                    </span>
                                                </td>

                                                <td>
                                                    <!-- Quantity Updater -->
                                                    <form action="${pageContext.request.contextPath}/cart" method="post" class="d-flex align-items-center gap-1">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="productId" value="${item.productId}">
                                                        <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stock}" 
                                                               class="form-control form-control-sm text-center" style="width: 65px;" required>
                                                        <button type="submit" class="btn btn-outline-secondary btn-sm" title="Update Quantity">
                                                            <i class="bi bi-check-lg"></i>
                                                        </button>
                                                    </form>
                                                </td>

                                                <td>
                                                    <span class="fw-bold text-dark">
                                                        ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                                    </span>
                                                </td>

                                                <td class="pe-4 text-end">
                                                    <!-- Remove Form -->
                                                    <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                                        <input type="hidden" name="action" value="remove">
                                                        <input type="hidden" name="productId" value="${item.productId}">
                                                        <button type="submit" class="btn btn-outline-danger btn-sm border-0" title="Remove item">
                                                            <i class="bi bi-trash3"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="mt-3">
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary btn-sm rounded-3">
                                <i class="bi bi-arrow-left me-1"></i> Continue Shopping
                            </a>
                        </div>
                    </div>

                    <!-- Order Summary Sidebar -->
                    <div class="col-lg-4">
                        <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
                            <h5 class="fw-bold mb-3">Order Summary</h5>

                            <div class="d-flex justify-content-between mb-2 small text-secondary">
                                <span>Items Subtotal (${totals.itemCount} items)</span>
                                <span class="fw-semibold text-dark">₹<fmt:formatNumber value="${totals.subtotal}" pattern="#,##0.00"/></span>
                            </div>

                            <c:if test="${totals.discount > 0}">
                                <div class="d-flex justify-content-between mb-2 small text-success">
                                    <span>Discount (<c:out value="${appliedCoupon.code}"/>)</span>
                                    <span class="fw-semibold">-₹<fmt:formatNumber value="${totals.discount}" pattern="#,##0.00"/></span>
                                </div>
                            </c:if>

                            <div class="d-flex justify-content-between mb-3 small text-secondary">
                                <span>Shipping Delivery</span>
                                <span class="text-success fw-semibold">FREE</span>
                            </div>

                            <hr class="my-3 opacity-25">

                            <div class="d-flex justify-content-between align-items-baseline mb-4">
                                <span class="fw-bold fs-5">Estimated Total</span>
                                <span class="fw-bold fs-4 text-primary">
                                    ₹<fmt:formatNumber value="${totals.total}" pattern="#,##0.00"/>
                                </span>
                            </div>

                            <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary w-100 py-3 rounded-3 fw-bold">
                                Proceed to Checkout <i class="bi bi-arrow-right ms-1"></i>
                            </a>
                        </div>

                        <!-- Coupon Box -->
                        <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                            <h6 class="fw-bold mb-3 d-flex align-items-center">
                                <i class="bi bi-ticket-perforated me-2 text-primary"></i>Have a Promo Coupon?
                            </h6>

                            <c:choose>
                                <c:when test="${not empty appliedCoupon}">
                                    <div class="d-flex justify-content-between align-items-center bg-success-subtle p-2 px-3 rounded-3 mb-2">
                                        <div>
                                            <span class="fw-bold text-success"><c:out value="${appliedCoupon.code}"/></span>
                                            <small class="text-success d-block">Coupon Active</small>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="mb-0">
                                            <input type="hidden" name="action" value="removeCoupon">
                                            <button type="submit" class="btn btn-outline-danger btn-sm py-0 px-2">Remove</button>
                                        </form>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/cart" method="post">
                                        <input type="hidden" name="action" value="applyCoupon">
                                        <div class="input-group">
                                            <input type="text" name="couponCode" class="form-control text-uppercase" placeholder="e.g. WELCOME10" required>
                                            <button type="submit" class="btn btn-outline-primary">Apply</button>
                                        </div>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
