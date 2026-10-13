<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Order #${order.orderId} Details &middot; ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <a href="${pageContext.request.contextPath}/orders" class="text-decoration-none small text-muted">
                    <i class="bi bi-arrow-left me-1"></i> Back to Orders
                </a>
                <h1 class="h3 fw-bold mb-0 mt-1">Order #${order.orderId}</h1>
                <small class="text-muted">
                    Placed on <fmt:formatDate value="${order.createdAt}" pattern="dd MMMM yyyy, hh:mm a"/>
                </small>
            </div>
            <div>
                <span class="badge bg-primary px-3 py-2 rounded-pill fs-6 fw-semibold">${order.orderStatus}</span>
            </div>
        </div>

        <div class="row g-4">
            <!-- Left: Line Items -->
            <div class="col-lg-8">
                <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-4">
                    <h5 class="fw-bold mb-3">Purchased Line Items</h5>
                    <div class="table-responsive">
                        <table class="table align-middle">
                            <thead class="table-light small text-secondary">
                                <tr>
                                    <th>Item</th>
                                    <th>Unit Price</th>
                                    <th class="text-center">Quantity</th>
                                    <th class="text-end">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${order.items}" var="item">
                                    <tr>
                                        <td class="py-3">
                                            <span class="fw-semibold text-dark"><c:out value="${item.productName}"/></span>
                                        </td>
                                        <td>₹<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
                                        <td class="text-center">${item.quantity}</td>
                                        <td class="text-end fw-bold">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Shipping Address Card -->
                <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                    <h5 class="fw-bold mb-2">Delivery Address</h5>
                    <c:choose>
                        <c:when test="${not empty order.address}">
                            <div class="text-secondary small">
                                <span class="badge bg-secondary-subtle text-secondary mb-2">${order.address.addressType}</span>
                                <div class="fw-bold text-dark fs-6">${order.address.addressLine}</div>
                                <div>${order.address.city}, ${order.address.state} - ${order.address.pincode}</div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted small mb-0">Standard campus address recorded.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Right: Order Payment & Financials -->
            <div class="col-lg-4">
                <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-4">
                    <h5 class="fw-bold mb-3">Payment Information</h5>
                    <div class="mb-3 small">
                        <span class="text-muted d-block">Method:</span>
                        <strong class="text-dark">${order.paymentMethod}</strong>
                    </div>
                    <div class="mb-3 small">
                        <span class="text-muted d-block">Payment Status:</span>
                        <span class="badge ${order.paymentStatus eq 'PAID' or order.paymentStatus eq 'SUCCESS' ? 'bg-success-subtle text-success' : 'bg-warning-subtle text-warning-emphasis'} px-2 py-1">
                            ${order.paymentStatus}
                        </span>
                    </div>

                    <hr class="my-3 opacity-25">

                    <h6 class="fw-bold mb-2">Billing Summary</h6>
                    <c:if test="${order.discount > 0}">
                        <div class="d-flex justify-content-between mb-2 small text-success">
                            <span>Discount Applied:</span>
                            <span class="fw-semibold">-₹<fmt:formatNumber value="${order.discount}" pattern="#,##0.00"/></span>
                        </div>
                    </c:if>
                    <div class="d-flex justify-content-between align-items-baseline mb-0">
                        <span class="fw-bold">Total Paid:</span>
                        <span class="fw-bold text-primary fs-5">
                            ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
