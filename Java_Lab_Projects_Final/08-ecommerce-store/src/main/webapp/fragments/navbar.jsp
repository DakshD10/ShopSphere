<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top py-2 shadow-sm">
    <div class="container">
        <!-- Brand -->
        <a class="navbar-brand d-flex align-items-center fw-bold fs-4" href="${pageContext.request.contextPath}/products">
            <i class="bi bi-shop me-2 text-primary fs-3"></i>
            <span>Shop<span class="text-primary">Sphere</span></span>
        </a>

        <!-- Responsive Toggle Button -->
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain"
                aria-controls="navbarMain" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <!-- Search Form -->
            <form class="d-flex mx-lg-auto my-2 my-lg-0 col-12 col-lg-5" action="${pageContext.request.contextPath}/products" method="get">
                <div class="input-group">
                    <input class="form-control bg-light border-0" type="search" name="search"
                           placeholder="Search headphones, charger, backpack..." 
                           value="<c:out value='${param.search}'/>" aria-label="Search">
                    <button class="btn btn-primary" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <!-- Right-Hand Actions -->
            <ul class="navbar-nav ms-auto align-items-center flex-row gap-3 my-2 my-lg-0">
                <!-- Syllabus Lab Demos Dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle text-light py-1 px-2 d-flex align-items-center gap-1" href="#" id="demosDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-mortarboard text-info"></i>
                        <span class="small fw-semibold d-none d-md-inline">Lab Demos</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3 mt-2" aria-labelledby="demosDropdown">
                        <li class="dropdown-header small text-muted">RTU Syllabus Demonstrations</li>
                        <li><a class="dropdown-item small" href="${pageContext.request.contextPath}/demo-elements.jsp"><i class="bi bi-code-slash me-2 text-primary"></i>JSP Scripting & Actions</a></li>
                        <li><a class="dropdown-item small" href="${pageContext.request.contextPath}/demo-i18n.jsp"><i class="bi bi-translate me-2 text-success"></i>I18N & ResourceBundle</a></li>
                        <li><a class="dropdown-item small" href="${pageContext.request.contextPath}/demo-url-protocol.jsp"><i class="bi bi-globe me-2 text-warning"></i>URL & Protocols</a></li>
                    </ul>
                </li>

                <!-- Wishlist Link -->
                <li class="nav-item">
                    <a class="nav-link text-light position-relative p-2" href="${pageContext.request.contextPath}/wishlist" title="Wishlist">
                        <i class="bi bi-heart fs-5"></i>
                    </a>
                </li>

                <!-- Shopping Cart Link with dynamic counter -->
                <li class="nav-item">
                    <a class="nav-link text-light position-relative p-2" href="${pageContext.request.contextPath}/cart" title="Shopping Cart">
                        <i class="bi bi-cart3 fs-5"></i>
                        <c:if test="${not empty sessionScope.cartCount && sessionScope.cartCount > 0}">
                            <span class="badge bg-danger rounded-pill cart-badge">
                                ${sessionScope.cartCount}
                            </span>
                        </c:if>
                    </a>
                </li>

                <!-- User Session Menu -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-light d-flex align-items-center gap-2" href="#" 
                               id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center" 
                                     style="width: 32px; height: 32px; font-weight: 700; font-size: 0.85rem;">
                                    ${sessionScope.user.name.substring(0, 1).toUpperCase()}
                                </div>
                                <span class="d-none d-md-inline small fw-semibold"><c:out value="${sessionScope.user.name}"/></span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3 mt-2" aria-labelledby="userDropdown">
                                <li class="dropdown-header small text-muted">
                                    Signed in as <strong><c:out value="${sessionScope.user.email}"/></strong>
                                </li>
                                <c:if test="${sessionScope.user.admin}">
                                    <li>
                                        <a class="dropdown-item text-primary fw-semibold" href="${pageContext.request.contextPath}/admin/dashboard">
                                            <i class="bi bi-speedometer2 me-2"></i>Admin Dashboard
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                </c:if>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-person me-2"></i>My Profile
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                                        <i class="bi bi-box-seam me-2"></i>My Orders
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/wishlist">
                                        <i class="bi bi-heart me-2"></i>Wishlist
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i>Sign Out
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">Sign In</a>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Register</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</header>
