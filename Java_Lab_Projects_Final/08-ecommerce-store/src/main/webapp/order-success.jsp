<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Order Placed Successfully &middot; ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5 bg-white text-center">
                    <div class="d-inline-flex align-items-center justify-content-center bg-success-subtle text-success rounded-circle mx-auto mb-3" style="width: 72px; height: 72px;">
                        <i class="bi bi-check2-circle display-4"></i>
                    </div>

                    <h1 class="h3 fw-bold mb-2">Order Confirmed!</h1>
                    <p class="text-secondary small mb-4">
                        Thank you for your order. We have recorded your purchase in the database.
                    </p>

                    <div class="bg-light p-3 rounded-3 mb-4 d-inline-block mx-auto text-start">
                        <div class="small">Order Reference: <strong class="text-primary fs-6">#${order.orderId}</strong></div>
                        <div class="small text-muted">Payment: <strong>${order.paymentMethod} (${order.paymentStatus})</strong> &middot; Status: <span class="badge bg-primary-subtle text-primary">${order.orderStatus}</span></div>
                    </div>

                    <!-- Items Summary Card -->
                    <div class="text-start border rounded-3 p-3 mb-4">
                        <h6 class="fw-bold mb-3">Purchased Items</h6>
                        <c:forEach items="${order.items}" var="item">
                            <div class="d-flex justify-content-between align-items-center py-2 border-bottom small">
                                <div>
                                    <span class="fw-semibold"><c:out value="${item.productName}"/></span>
                                    <span class="text-muted ms-2">&times; ${item.quantity}</span>
                                </div>
                                <span class="fw-bold">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
                            </div>
                        </c:forEach>

                        <div class="d-flex justify-content-between pt-3 fw-bold">
                            <span>Total Paid</span>
                            <span class="text-primary fs-5">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                        </div>
                    </div>

                    <div class="d-flex justify-content-center gap-3">
                        <a href="${pageContext.request.contextPath}/order-details?id=${order.orderId}" class="btn btn-outline-primary rounded-3 px-4 py-2">
                            <i class="bi bi-eye me-1"></i> View Order Details
                        </a>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-3 px-4 py-2">
                            <i class="bi bi-shop me-1"></i> Continue Shopping
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
