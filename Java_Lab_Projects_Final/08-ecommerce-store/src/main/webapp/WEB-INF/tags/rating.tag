<%@ tag description="Reusable Star Rating Tag" pageEncoding="UTF-8" %>
<%@ attribute name="score" type="java.lang.Double" required="true" %>
<%@ attribute name="reviewCount" type="java.lang.Integer" required="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="d-inline-flex align-items-center gap-1">
    <div class="star-rating">
        <c:forEach begin="1" end="5" var="i">
            <i class="bi ${i <= score ? 'bi-star-fill text-warning' : 'bi-star text-muted'}"></i>
        </c:forEach>
    </div>
    <span class="fw-bold small ms-1">${score > 0 ? score : 'New'}</span>
    <c:if test="${not empty reviewCount and reviewCount > 0}">
        <span class="text-muted small">(${reviewCount})</span>
    </c:if>
</div>
