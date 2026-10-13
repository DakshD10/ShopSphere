<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Secure Checkout &middot; ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

    <%@ include file="fragments/navbar.jsp" %>

    <main class="container my-5 flex-grow-1">
        <h1 class="h3 fw-bold mb-4">Secure Checkout</h1>

        <c:if test="${not empty sessionScope.checkoutError}">
            <div class="alert alert-danger alert-dismissible fade show rounded-3 mb-4" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${sessionScope.checkoutError}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="checkoutError" scope="session"/>
        </c:if>

        <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkoutForm">
            <div class="row g-4">
                <!-- Left: Shipping Address & Payment -->
                <div class="col-lg-7">
                    <!-- 1. Shipping Address Section -->
                    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white mb-4">
                        <h5 class="fw-bold mb-3 d-flex align-items-center">
                            <span class="badge bg-primary rounded-circle me-2 p-2" style="width: 28px; height: 28px;">1</span>
                            Shipping Address
                        </h5>

                        <c:if test="${not empty addresses}">
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-uppercase text-muted">Select Saved Address</label>
                                <c:forEach items="${addresses}" var="addr" varStatus="st">
                                    <div class="form-check p-3 border rounded-3 mb-2 bg-light">
                                        <input class="form-check-input" type="radio" name="addressOption" id="addr_${addr.addressId}" 
                                               value="existing" ${st.first ? 'checked' : ''} onchange="toggleAddressMode(false)">
                                        <label class="form-check-label w-100 ps-2" for="addr_${addr.addressId}">
                                            <span class="badge bg-secondary-subtle text-secondary mb-1"><c:out value="${addr.addressType}"/></span>
                                            <div class="fw-semibold text-dark"><c:out value="${addr.addressLine}"/></div>
                                            <small class="text-muted"><c:out value="${addr.city}"/>, <c:out value="${addr.state}"/> - <c:out value="${addr.pincode}"/></small>
                                        </label>
                                        <input type="hidden" name="addressId" value="${addr.addressId}">
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>

                        <!-- Option to Enter New Address -->
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="radio" name="addressOption" id="addr_new" 
                                   value="new" ${empty addresses ? 'checked' : ''} onchange="toggleAddressMode(true)">
                            <label class="form-check-label fw-bold" for="addr_new">
                                + Ship to a new address
                            </label>
                        </div>

                        <!-- New Address Fields Form -->
                        <div id="newAddressFields" class="${empty addresses ? '' : 'd-none'} p-3 border rounded-3 bg-light">
                            <div class="mb-3">
                                <label for="newAddressLine" class="form-label small fw-semibold">Street Address / Hostel / Room</label>
                                <input type="text" class="form-control" id="newAddressLine" name="newAddressLine" 
                                       placeholder="e.g. Hostel Block B, Room 302, University Road">
                            </div>

                            <div class="row g-2 mb-3">
                                <div class="col-sm-5">
                                    <label for="newCity" class="form-label small fw-semibold">City</label>
                                    <input type="text" class="form-control" id="newCity" name="newCity" placeholder="Jaipur">
                                </div>
                                <div class="col-sm-4">
                                    <label for="newState" class="form-label small fw-semibold">State</label>
                                    <input type="text" class="form-control" id="newState" name="newState" placeholder="Rajasthan">
                                </div>
                                <div class="col-sm-3">
                                    <label for="newPincode" class="form-label small fw-semibold">Pincode</label>
                                    <input type="text" class="form-control" id="newPincode" name="newPincode" placeholder="302022">
                                </div>
                            </div>

                            <div>
                                <label for="newAddressType" class="form-label small fw-semibold">Address Type</label>
                                <select class="form-select form-select-sm" id="newAddressType" name="newAddressType">
                                    <option value="HOME">Home</option>
                                    <option value="WORK">Work / Campus Lab</option>
                                    <option value="OTHER">Other</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- 2. Payment Method Section -->
                    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                        <h5 class="fw-bold mb-3 d-flex align-items-center">
                            <span class="badge bg-primary rounded-circle me-2 p-2" style="width: 28px; height: 28px;">2</span>
                            Payment Method
                        </h5>

                        <div class="d-flex flex-column gap-2">
                            <div class="form-check p-3 border rounded-3 bg-light">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_cod" value="COD" checked>
                                <label class="form-check-label ps-2" for="pay_cod">
                                    <div class="fw-bold text-dark"><i class="bi bi-cash me-2 text-success"></i>Cash on Delivery (COD)</div>
                                    <small class="text-muted">Pay in cash or UPI when your shipment arrives.</small>
                                </label>
                            </div>

                            <div class="form-check p-3 border rounded-3 bg-light">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_upi" value="UPI">
                                <label class="form-check-label ps-2" for="pay_upi">
                                    <div class="fw-bold text-dark"><i class="bi bi-qr-code-scan me-2 text-primary"></i>UPI / Instant QR</div>
                                    <small class="text-muted">Simulate instant UPI transaction reference generation.</small>
                                </label>
                            </div>

                            <div class="form-check p-3 border rounded-3 bg-light">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_card" value="CARD">
                                <label class="form-check-label ps-2" for="pay_card">
                                    <div class="fw-bold text-dark"><i class="bi bi-credit-card me-2 text-info"></i>Debit / Credit Card</div>
                                    <small class="text-muted">Simulate Visa/MasterCard payment transaction.</small>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right: Order Review & Total -->
                <div class="col-lg-5">
                    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white sticky-top" style="top: 80px;">
                        <h5 class="fw-bold mb-3">Order Items (${totals.itemCount})</h5>

                        <div class="d-flex flex-column gap-2 mb-3 overflow-auto" style="max-height: 240px;">
                            <c:forEach items="${cartItems}" var="item">
                                <div class="d-flex justify-content-between align-items-center small py-1 border-bottom">
                                    <div class="text-truncate pe-2">
                                        <div class="fw-semibold text-truncate"><c:out value="${item.product.name}"/></div>
                                        <span class="text-muted">${item.quantity} × ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></span>
                                    </div>
                                    <span class="fw-bold text-dark">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Price Breakdown (Server-Calculated) -->
                        <div class="d-flex justify-content-between mb-2 small text-secondary">
                            <span>Subtotal</span>
                            <span class="fw-semibold text-dark">₹<fmt:formatNumber value="${totals.subtotal}" pattern="#,##0.00"/></span>
                        </div>

                        <c:if test="${totals.discount > 0}">
                            <div class="d-flex justify-content-between mb-2 small text-success">
                                <span>Discount (${appliedCoupon.code})</span>
                                <span class="fw-semibold">-₹<fmt:formatNumber value="${totals.discount}" pattern="#,##0.00"/></span>
                            </div>
                        </c:if>

                        <div class="d-flex justify-content-between mb-3 small text-secondary">
                            <span>Delivery Shipping</span>
                            <span class="text-success fw-semibold">FREE</span>
                        </div>

                        <hr class="my-3 opacity-25">

                        <div class="d-flex justify-content-between align-items-baseline mb-4">
                            <span class="fw-bold fs-5">Final Amount</span>
                            <span class="fw-bold fs-4 text-primary">
                                ₹<fmt:formatNumber value="${totals.total}" pattern="#,##0.00"/>
                            </span>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-3 rounded-3 fw-bold fs-6">
                            <i class="bi bi-shield-check me-2"></i>Place Order &amp; Pay
                        </button>

                        <div class="text-center mt-3 text-muted small">
                            <i class="bi bi-lock-fill me-1"></i>Transactional JDBC persistence with rollback protection.
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </main>

    <%@ include file="fragments/footer.jsp" %>

    <script>
        function toggleAddressMode(isNew) {
            const newFields = document.getElementById('newAddressFields');
            if (isNew) {
                newFields.classList.remove('d-none');
            } else {
                newFields.classList.add('d-none');
            }
        }
    </script>
</body>
</html>
