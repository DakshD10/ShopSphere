<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Customer Management &middot; ShopSphere Admin</title>
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
                    <h1 class="h3 fw-bold mb-1">Customer Accounts</h1>
                    <p class="text-secondary small mb-0">View registered customers and manage account access statuses.</p>
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
                                    <th class="ps-4">User ID</th>
                                    <th>Customer Name</th>
                                    <th>Email Address</th>
                                    <th>Mobile</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th class="pe-4 text-end">Update Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${users}" var="u">
                                    <tr>
                                        <td class="ps-4 fw-bold">#${u.userId}</td>
                                        <td>
                                            <div class="fw-semibold text-dark"><c:out value="${u.name}"/></div>
                                            <small class="text-muted">Joined <fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy"/></small>
                                        </td>
                                        <td><code><c:out value="${u.email}"/></code></td>
                                        <td><c:out value="${not empty u.mobile ? u.mobile : 'None'}"/></td>
                                        <td>
                                            <span class="badge ${u.admin ? 'bg-primary' : 'bg-secondary'}">
                                                <c:out value="${u.role}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <span class="badge ${u.status eq 'ACTIVE' ? 'bg-success' : 'bg-warning text-dark'}">
                                                <c:out value="${u.status}"/>
                                            </span>
                                        </td>
                                        <td class="pe-4 text-end">
                                            <form action="${pageContext.request.contextPath}/admin/users" method="post" class="d-inline-flex gap-1 align-items-center">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <select name="status" class="form-select form-select-sm" style="width: 120px;">
                                                    <option value="ACTIVE" ${u.status eq 'ACTIVE' ? 'selected' : ''}>Active</option>
                                                    <option value="INACTIVE" ${u.status eq 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                                                    <option value="SUSPENDED" ${u.status eq 'SUSPENDED' ? 'selected' : ''}>Suspended</option>
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
