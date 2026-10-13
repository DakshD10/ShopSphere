<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Product Management &middot; ShopSphere Admin</title>
    <%@ include file="../fragments/header.jsp" %>
</head>
<body class="bg-light">

    <!-- Header -->
    <header class="navbar navbar-dark bg-dark sticky-top py-2 px-3 shadow-sm d-flex justify-content-between">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <i class="bi bi-shield-lock-fill text-primary me-2"></i>ShopSphere Administration
        </a>
        <div class="d-flex align-items-center gap-3">
            <span class="text-secondary small d-none d-md-inline">Logged in as <strong><c:out value="${sessionScope.user.name}"/></strong></span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Sign Out</a>
        </div>
    </header>

    <div class="container-fluid">
        <div class="row">
            <%@ include file="../fragments/sidebar.jsp" %>

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="d-flex justify-content-between flex-wrap align-items-center pb-2 mb-4 border-bottom">
                    <div>
                        <h1 class="h3 fw-bold mb-1">Product Catalog Manager</h1>
                        <p class="text-secondary small mb-0">Create, update, inspect inventory, and remove products.</p>
                    </div>
                    <button class="btn btn-primary rounded-3 btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#productFormCollapse">
                        <i class="bi bi-plus-lg me-1"></i> New Product Form
                    </button>
                </div>

                <!-- Flash alerts -->
                <c:if test="${not empty sessionScope.notice}">
                    <div class="alert alert-success alert-dismissible fade show rounded-3" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.notice}"/>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <c:remove var="notice" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger alert-dismissible fade show rounded-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${sessionScope.error}"/>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <!-- Create / Edit Product Accordion Form -->
                <div class="collapse ${not empty editProduct ? 'show' : ''} mb-4" id="productFormCollapse">
                    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
                        <h5 class="fw-bold mb-3 text-primary">
                            <i class="bi bi-pencil-square me-2"></i>
                            ${not empty editProduct ? 'Edit Existing Product #' : 'Create New Product'} ${editProduct.productId}
                        </h5>

                        <form action="${pageContext.request.contextPath}/admin/products" method="post">
                            <input type="hidden" name="action" value="save">
                            <input type="hidden" name="productId" value="${not empty editProduct ? editProduct.productId : 0}">

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">Product Name</label>
                                    <input type="text" name="name" class="form-control" value="<c:out value='${editProduct.name}'/>" required>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-semibold">Brand</label>
                                    <input type="text" name="brand" class="form-control" value="<c:out value='${editProduct.brand}'/>">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-semibold">Category</label>
                                    <select name="categoryId" class="form-select" required>
                                        <c:forEach items="${categories}" var="cat">
                                            <option value="${cat.categoryId}" ${editProduct.categoryId eq cat.categoryId ? 'selected' : ''}>
                                                <c:out value="${cat.name}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-3">
                                    <label class="form-label small fw-semibold">Price (₹)</label>
                                    <input type="number" step="0.01" name="price" class="form-control" value="${editProduct.price}" required min="1">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-semibold">Discount (%)</label>
                                    <input type="number" step="0.01" name="discount" class="form-control" value="${editProduct.discount}" min="0" max="100">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-semibold">Stock Quantity</label>
                                    <input type="number" name="stock" class="form-control" value="${not empty editProduct ? editProduct.stock : 25}" required min="0">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-semibold">Image URL</label>
                                    <input type="url" name="imageUrl" class="form-control" value="<c:out value='${editProduct.imageUrl}'/>" placeholder="https://...">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label small fw-semibold">Description</label>
                                <textarea name="description" rows="3" class="form-control"><c:out value='${editProduct.description}'/></textarea>
                            </div>

                            <div class="form-check form-switch mb-4">
                                <input class="form-check-input" type="checkbox" name="status" id="prodStatus" 
                                       ${(empty editProduct or editProduct.status) ? 'checked' : ''}>
                                <label class="form-check-label small fw-semibold" for="prodStatus">Active (Visible in Storefront)</label>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary rounded-3 px-4 fw-semibold">
                                    <i class="bi bi-save me-1"></i> Save Product
                                </button>
                                <c:if test="${not empty editProduct}">
                                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-secondary rounded-3 px-3">
                                        Cancel Edit
                                    </a>
                                </c:if>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Products Table -->
                <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
                    <div class="table-responsive">
                        <table class="table align-middle table-hover mb-0">
                            <thead class="table-light small text-secondary">
                                <tr>
                                    <th class="ps-4">ID</th>
                                    <th>Product</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Discount</th>
                                    <th>Stock</th>
                                    <th>Status</th>
                                    <th class="pe-4 text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${products}" var="p">
                                    <tr>
                                        <td class="ps-4 fw-semibold">#${p.productId}</td>
                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <img src="<c:out value='${p.imageUrl}'/>" alt="" class="rounded-2" 
                                                     style="width: 42px; height: 42px; object-fit: cover;"
                                                     onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600'">
                                                <div>
                                                    <div class="fw-semibold text-dark"><c:out value="${p.name}"/></div>
                                                    <small class="text-muted"><c:out value="${p.brand}"/></small>
                                                </div>
                                            </div>
                                        </td>
                                        <td><span class="badge bg-secondary-subtle text-secondary"><c:out value="${p.categoryName}"/></span></td>
                                        <td class="fw-bold">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${p.discount > 0}"><span class="badge bg-danger-subtle text-danger">-${p.discountPercent}%</span></c:when>
                                                <c:otherwise><span class="text-muted small">None</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <span class="badge ${p.stock < 10 ? 'bg-warning text-dark' : 'bg-success-subtle text-success'}">
                                                ${p.stock} units
                                            </span>
                                        </td>
                                        <td>
                                            <span class="badge ${p.status ? 'bg-success' : 'bg-secondary'}">
                                                ${p.status ? 'Active' : 'Inactive'}
                                            </span>
                                        </td>
                                        <td class="pe-4 text-end">
                                            <a href="${pageContext.request.contextPath}/admin/products?editId=${p.productId}" 
                                               class="btn btn-outline-primary btn-sm me-1" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-inline"
                                                  onsubmit="return confirm('Delete this product permanently?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="productId" value="${p.productId}">
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
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
