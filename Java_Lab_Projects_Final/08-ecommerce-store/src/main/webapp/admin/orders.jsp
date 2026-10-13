<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Order Management &middot; ShopSphere Admin</title>
    <%@ include file="../fragments/header.jsp" %>
</head>
<body class="bg-light">

    <header class="navbar navbar-dark bg-dark sticky-top py-2 px-3 shadow-sm d-flex justify-content-between">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <i class="bi bi-shield-lock-fill text-primary me-2"></i>ShopSphere Administration
        </a>
        <div class="d-flex align-items-center gap-3">
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Sign Out</a>
        </div>
    </header>

    <div class="container-fluid">
        <div class="row">
            <%@ include file="../fragments/sidebar.jsp" %>

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="pb-2 mb-4 border-bottom">
                    <h1 class="h3 fw-bold mb-1">Customer Orders Management</h1>
                    <p class="text-secondary small mb-0">Track shipments, advance delivery states, and verify mock payments.</p>
                </div>

                <c:if test="${not empty sessionScope.notice}">
                    <div class="alert alert-success alert-dismissible fade show rounded-3" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.notice}"/>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <c:remove var="notice" scope="session"/>
                </c:if>

                <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                    <div class="table-responsive">
                        <table class="table align-middle table-hover mb-0">
                            <thead class="table-light small text-secondary">
                                <tr>
                                    <th class="ps-4">Order ID</th>
                                    <th>Customer ID</th>
                                    <th>Date Placed</th>
                                    <th>Total Amount</th>
                                    <th>Payment</th>
                                    <th>Delivery Status</th>
                                    <th class="pe-4 text-end">Update Progress</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${orders}" var="o">
                                    <tr>
                                        <td class="ps-4 fw-bold">#${o.orderId}</td>
                                        <td>User #${o.userId}</td>
                                        <td class="small text-muted">
                                            <fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, hh:mm a"/>
                                        </td>
                                        <td class="fw-bold text-dark">
                                            ₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/>
                                        </td>
                                        <td>
                                            <span class="badge ${o.paymentStatus eq 'PAID' or o.paymentStatus eq 'SUCCESS' ? 'bg-success-subtle text-success' : 'bg-warning-subtle text-warning-emphasis'}">
                                                ${o.paymentMethod}: ${o.paymentStatus}
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${o.orderStatus eq 'DELIVERED'}"><span class="badge bg-success">Delivered</span></c:when>
                                                <c:when test="${o.orderStatus eq 'SHIPPED'}"><span class="badge bg-warning text-dark">Shipped</span></c:when>
                                                <c:when test="${o.orderStatus eq 'CONFIRMED'}"><span class="badge bg-info text-dark">Confirmed</span></c:when>
                                                <c:when test="${o.orderStatus eq 'CANCELLED'}"><span class="badge bg-danger">Cancelled</span></c:when>
                                                <c:otherwise><span class="badge bg-primary">Placed</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="pe-4 text-end">
                                            <!-- Inline Update Form -->
                                            <form action="${pageContext.request.contextPath}/admin/orders" method="post" class="d-inline-flex gap-1 align-items-center">
                                                <input type="hidden" name="orderId" value="${o.orderId}">
                                                <select name="orderStatus" class="form-select form-select-sm" style="width: 125px;">
                                                    <option value="PLACED" ${o.orderStatus eq 'PLACED' ? 'selected' : ''}>Placed</option>
                                                    <option value="CONFIRMED" ${o.orderStatus eq 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                                    <option value="SHIPPED" ${o.orderStatus eq 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                                    <option value="DELIVERED" ${o.orderStatus eq 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                                    <option value="CANCELLED" ${o.orderStatus eq 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                                </select>
                                                <select name="paymentStatus" class="form-select form-select-sm" style="width: 105px;">
                                                    <option value="PENDING" ${o.paymentStatus eq 'PENDING' ? 'selected' : ''}>Pending</option>
                                                    <option value="PAID" ${o.paymentStatus eq 'PAID' or o.paymentStatus eq 'SUCCESS' ? 'selected' : ''}>Paid</option>
                                                    <option value="FAILED" ${o.paymentStatus eq 'FAILED' ? 'selected' : ''}>Failed</option>
                                                </select>
                                                <button type="submit" class="btn btn-outline-primary btn-sm" title="Save Status">
                                                    <i class="bi bi-check2"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
