<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Category Management &middot; ShopSphere Admin</title>
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
                    <h1 class="h3 fw-bold mb-1">Category Management</h1>
                    <p class="text-secondary small mb-0">Organize catalog navigation categories and visibility status.</p>
                </div>

                <c:if test="${not empty sessionScope.notice}">
                    <div class="alert alert-success alert-dismissible fade show rounded-3" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.notice}"/>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <c:remove var="notice" scope="session"/>
                </c:if>

                <div class="row g-4">
                    <!-- Form Column -->
                    <div class="col-lg-4">
                        <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                            <h5 class="fw-bold mb-3 text-primary">
                                ${not empty editCategory ? 'Edit Category #' : 'Add New Category'} ${editCategory.categoryId}
                            </h5>

                            <form action="${pageContext.request.contextPath}/admin/categories" method="post">
                                <input type="hidden" name="action" value="save">
                                <input type="hidden" name="categoryId" value="${not empty editCategory ? editCategory.categoryId : 0}">

                                <div class="mb-3">
                                    <label class="form-label small fw-semibold">Category Name</label>
                                    <input type="text" name="name" class="form-control" value="<c:out value='${editCategory.name}'/>" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label small fw-semibold">Description</label>
                                    <textarea name="description" rows="3" class="form-control"><c:out value='${editCategory.description}'/></textarea>
                                </div>

                                <div class="form-check form-switch mb-4">
                                    <input class="form-check-input" type="checkbox" name="status" id="catStatus" 
                                           ${(empty editCategory or editCategory.status) ? 'checked' : ''}>
                                    <label class="form-check-label small fw-semibold" for="catStatus">Active in Navigation</label>
                                </div>

                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-primary rounded-3 w-100 fw-semibold">
                                        <i class="bi bi-save me-1"></i> Save Category
                                    </button>
                                    <c:if test="${not empty editCategory}">
                                        <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-secondary rounded-3">
                                            Cancel
                                        </a>
                                    </c:if>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Table Column -->
                    <div class="col-lg-8">
                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                            <div class="table-responsive">
                                <table class="table align-middle table-hover mb-0">
                                    <thead class="table-light small text-secondary">
                                        <tr>
                                            <th class="ps-4">ID</th>
                                            <th>Category Name</th>
                                            <th>Description</th>
                                            <th>Status</th>
                                            <th class="pe-4 text-end">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${categories}" var="c">
                                            <tr>
                                                <td class="ps-4 fw-semibold">#${c.categoryId}</td>
                                                <td class="fw-bold text-dark"><c:out value="${c.name}"/></td>
                                                <td class="text-secondary small"><c:out value="${c.description}"/></td>
                                                <td>
                                                    <span class="badge ${c.status ? 'bg-success' : 'bg-secondary'}">
                                                        ${c.status ? 'Active' : 'Inactive'}
                                                    </span>
                                                </td>
                                                <td class="pe-4 text-end">
                                                    <a href="${pageContext.request.contextPath}/admin/categories?editId=${c.categoryId}" 
                                                       class="btn btn-outline-primary btn-sm me-1" title="Edit">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>
                                                    <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="d-inline"
                                                          onsubmit="return confirm('Delete this category?');">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="categoryId" value="${c.categoryId}">
                                                        <button type="submit" class="btn btn-outline-danger btn-sm" title="Delete">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
