<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>My Orders &middot; ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <h1 class="h3 fw-bold mb-4">My Orders</h1>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="card border-0 shadow-sm rounded-4 p-5 text-center bg-white">
                    <div class="mb-3 text-muted display-4"><i class="bi bi-box-seam"></i></div>
                    <h3 class="fw-bold">No Orders Yet</h3>
                    <p class="text-secondary small mb-4">You have not placed any orders with ShopSphere yet.</p>
                    <div>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-3 px-4 py-2">
                            Start Shopping
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="d-flex flex-column gap-3">
                    <c:forEach items="${orders}" var="o">
                        <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                            <div class="d-flex flex-column flex-sm-row justify-content-between align-items-sm-center border-bottom pb-3 mb-3 gap-2">
                                <div>
                                    <span class="fw-bold text-dark fs-5">Order #${o.orderId}</span>
                                    <span class="text-muted small ms-2">
                                        Placed on <fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, hh:mm a"/>
                                    </span>
                                </div>
                                <div class="d-flex align-items-center gap-2">
                                    <c:choose>
                                        <c:when test="${o.orderStatus eq 'DELIVERED'}">
                                            <span class="badge bg-success-subtle text-success px-3 py-2 rounded-pill fw-semibold">Delivered</span>
                                        </c:when>
                                        <c:when test="${o.orderStatus eq 'SHIPPED'}">
                                            <span class="badge bg-warning-subtle text-warning-emphasis px-3 py-2 rounded-pill fw-semibold">Shipped</span>
                                        </c:when>
                                        <c:when test="${o.orderStatus eq 'CANCELLED'}">
                                            <span class="badge bg-danger-subtle text-danger px-3 py-2 rounded-pill fw-semibold">Cancelled</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold">${o.orderStatus}</span>
                                        </c:otherwise>
                                    </c:choose>

                                    <a href="${pageContext.request.contextPath}/order-details?id=${o.orderId}" 
                                       class="btn btn-outline-primary btn-sm rounded-3">
                                        View Details
                                    </a>
                                </div>
                            </div>

                            <div class="row align-items-center">
                                <div class="col-md-8">
                                    <ul class="list-unstyled mb-0 small text-secondary">
                                        <c:forEach items="${o.items}" var="it" end="2">
                                            <li>&bull; <c:out value="${it.productName}"/> &times; ${it.quantity}</li>
                                        </c:forEach>
                                        <c:if test="${o.items.size() > 3}">
                                            <li class="text-muted">+ ${o.items.size() - 3} more item(s)</li>
                                        </c:if>
                                    </ul>
                                </div>
                                <div class="col-md-4 text-md-end mt-2 mt-md-0">
                                    <span class="small text-muted d-block">Total Paid</span>
                                    <span class="fs-5 fw-bold text-dark">
                                        ₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/>
                                    </span>
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
