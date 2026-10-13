<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>My Profile · ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

<%@ include file="fragments/navbar.jsp" %>

<div class="container py-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">My Profile</li>
        </ol>
    </nav>

    <!-- Flash Messages -->
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.flashSuccess}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashSuccess" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${sessionScope.flashError}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashError" scope="session"/>
    </c:if>

    <div class="row g-4">
        <!-- Left: Profile Info Card -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm text-center p-4">
                <div class="mb-3">
                    <div class="d-inline-flex align-items-center justify-content-center bg-primary text-white rounded-circle fs-1 shadow-sm"
                         style="width: 90px; height: 90px;">
                        <i class="bi bi-person-fill"></i>
                    </div>
                </div>
                <h4 class="fw-bold mb-1 text-navy"><c:out value="${profileUser.name}"/></h4>
                <p class="text-muted small mb-3"><c:out value="${profileUser.email}"/></p>

                <div class="d-flex justify-content-center gap-2 mb-3">
                    <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-3 py-2">
                        <i class="bi bi-shield-lock me-1"></i>Role: <c:out value="${profileUser.role}"/>
                    </span>
                    <span class="badge ${profileUser.status eq 'ACTIVE' ? 'bg-success-subtle text-success border border-success-subtle' : 'bg-secondary'} px-3 py-2">
                        <c:out value="${profileUser.status eq 'ACTIVE' ? 'Active Account' : profileUser.status}"/>
                    </span>
                </div>

                <hr>

                <div class="text-start small">
                    <div class="mb-2">
                        <span class="text-muted"><i class="bi bi-telephone me-2"></i>Mobile:</span>
                        <span class="fw-semibold"><c:out value="${not empty profileUser.mobile ? profileUser.mobile : 'Not provided'}"/></span>
                    </div>
                    <div class="mb-2">
                        <span class="text-muted"><i class="bi bi-calendar-event me-2"></i>Member Since:</span>
                        <span class="fw-semibold"><fmt:formatDate value="${profileUser.createdAt}" pattern="MMM dd, yyyy"/></span>
                    </div>
                </div>

                <div class="mt-4 pt-2">
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-primary w-100 mb-2">
                        <i class="bi bi-box-seam me-1"></i> View Order History
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger w-100">
                        <i class="bi bi-box-arrow-right me-1"></i> Sign Out
                    </a>
                </div>
            </div>
        </div>

        <!-- Right: Addresses & Orders -->
        <div class="col-lg-8">
            <!-- Saved Addresses Section -->
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center border-bottom">
                    <h5 class="card-title mb-0 fw-bold text-navy"><i class="bi bi-geo-alt me-2"></i>Saved Delivery Addresses</h5>
                    <button class="btn btn-sm btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#newAddressCollapse">
                        <i class="bi bi-plus-lg me-1"></i> Add Address
                    </button>
                </div>
                <div class="card-body">
                    <!-- Add Address Collapse Form -->
                    <div class="collapse mb-4" id="newAddressCollapse">
                        <div class="card card-body bg-light border">
                            <h6 class="fw-bold mb-3 text-navy">Add New Delivery Address</h6>
                            <form action="${pageContext.request.contextPath}/profile" method="POST">
                                <input type="hidden" name="action" value="addAddress">
                                <div class="row g-3">
                                    <div class="col-12">
                                        <label class="form-label small fw-semibold">Street Address / House No.</label>
                                        <input type="text" name="addressLine" class="form-control form-control-sm" required placeholder="e.g. 102 Crystal Residency, High Street">
                                    </div>
                                    <div class="col-md-5">
                                        <label class="form-label small fw-semibold">City</label>
                                        <input type="text" name="city" class="form-control form-control-sm" required placeholder="e.g. Jaipur">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label small fw-semibold">State</label>
                                        <input type="text" name="state" class="form-control form-control-sm" required placeholder="e.g. Rajasthan">
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label small fw-semibold">Pincode</label>
                                        <input type="text" name="pincode" class="form-control form-control-sm" required placeholder="302017">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label small fw-semibold">Address Label</label>
                                        <select name="addressType" class="form-select form-select-sm">
                                            <option value="HOME">Home</option>
                                            <option value="WORK">Work / Office</option>
                                            <option value="OTHER">Other</option>
                                        </select>
                                    </div>
                                    <div class="col-12 text-end">
                                        <button type="button" class="btn btn-sm btn-secondary me-2" data-bs-toggle="collapse" data-bs-target="#newAddressCollapse">Cancel</button>
                                        <button type="submit" class="btn btn-sm btn-primary">Save Address</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${not empty addresses}">
                            <div class="row g-3">
                                <c:forEach var="addr" items="${addresses}">
                                    <div class="col-md-6">
                                        <div class="border rounded p-3 h-100 bg-white">
                                            <div class="d-flex justify-content-between align-items-center mb-2">
                                                <span class="badge bg-secondary-subtle text-secondary border">
                                                    <c:out value="${addr.addressType}"/>
                                                </span>
                                            </div>
                                            <p class="mb-1 fw-medium text-dark"><c:out value="${addr.addressLine}"/></p>
                                            <p class="mb-0 text-muted small">
                                                <c:out value="${addr.city}"/>, <c:out value="${addr.state}"/> - <c:out value="${addr.pincode}"/>
                                            </p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-4 text-muted">
                                <i class="bi bi-geo-alt fs-2 d-block mb-2"></i>
                                <p class="mb-0">No saved addresses yet. Click "Add Address" above to save your shipping destination.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Recent Orders Summary -->
            <div class="card border-0 shadow-sm">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center border-bottom">
                    <h5 class="card-title mb-0 fw-bold text-navy"><i class="bi bi-receipt me-2"></i>Recent Orders</h5>
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-sm btn-outline-primary">View All</a>
                </div>
                <div class="card-body p-0">
                    <c:choose>
                        <c:when test="${not empty recentOrders}">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="ps-3">Order Number</th>
                                            <th>Date</th>
                                            <th>Status</th>
                                            <th>Total</th>
                                            <th class="text-end pe-3">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="ord" items="${recentOrders}" end="4">
                                            <tr>
                                                <td class="ps-3 fw-bold text-navy"><c:out value="${ord.orderNumber}"/></td>
                                                <td class="text-muted small"><fmt:formatDate value="${ord.createdAt}" pattern="MMM dd, yyyy"/></td>
                                                <td>
                                                    <span class="badge ${ord.status == 'DELIVERED' ? 'bg-success' : (ord.status == 'CANCELLED' ? 'bg-danger' : 'bg-primary')}">
                                                        <c:out value="${ord.status}"/>
                                                    </span>
                                                </td>
                                                <td class="fw-semibold text-primary">$<fmt:formatNumber value="${ord.totalAmount}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                                <td class="text-end pe-3">
                                                    <a href="${pageContext.request.contextPath}/order-details?id=${ord.orderId}" class="btn btn-sm btn-outline-secondary">
                                                        View
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-4 text-muted">
                                <i class="bi bi-box-seam fs-2 d-block mb-2"></i>
                                <p class="mb-0">No orders placed yet. <a href="${pageContext.request.contextPath}/products" class="text-primary text-decoration-none">Explore our catalog</a>!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
