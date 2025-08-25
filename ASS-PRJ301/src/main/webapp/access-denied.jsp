<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    request.setAttribute("pageTitle", "Truy cập bị từ chối");
%>
<%@ include file="view/header.jsp" %>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm">
                <div class="card-body p-5 text-center">
                    <h1 class="display-4 text-danger mb-3">Không đủ thẩm quyền</h1>
                    <h2 class="card-title text-dark">Truy Cập Bị Từ Chối</h2>
                    <p class="card-text text-muted mt-3">
                        Bạn không có quyền xem nội dung này.
                    </p>
                    <c:if test="${not empty errorMessage}">
                        <p class="card-text text-danger mt-3">
                            <c:out value="${errorMessage}"/>
                        </p>
                    </c:if>
                    
                    <a href="${pageContext.request.contextPath}/dashboard.jsp" class="btn btn-primary mt-4">Quay về Trang chủ</a>
                    
                </div>
            </div>
        </div>
    </div>
</div>

<footer class="bg-light text-center text-lg-start mt-5">
    </footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>