<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- header.jsp của bạn giữ nguyên --%>
<c:set var="pageTitle" value="Tồn kho"/>
<%@ include file="view/header.jsp" %>

<h2 class="mb-3">📦 Tồn kho</h2>

<form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/inventory">
    <div class="col-auto">
        <input type="text" name="name" class="form-control" placeholder="Tìm tên/mã sách"
               value="${fn:escapeXml(name)}">
    </div>

    <!-- NEW: chọn tình trạng -->
    <div class="col-auto">
        <select name="status" class="form-select">
            <option value=""    <c:if test="${empty status}">selected</c:if>>Tất cả</option>
            <option value="low" <c:if test="${status eq 'low'}">selected</c:if>>Thấp</option>
            <option value="ok"  <c:if test="${status eq 'ok'}">selected</c:if>>Ổn</option>
            </select>
        </div>

        <div class="col-auto">
            <button class="btn btn-dark">Lọc</button>           
    </div>
</form>

<c:if test="${not empty sessionScope.success}">
    <div class="alert alert-success">${sessionScope.success}</div>
    <c:remove var="success" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.error}">
    <div class="alert alert-danger">${sessionScope.error}</div>
    <c:remove var="error" scope="session"/>
</c:if>

<table class="table table-striped table-hover align-middle">
    <thead class="table-dark">
        <tr><th>Mã</th><th>Tên sách</th><th>Tồn hiện tại</th><th>Tối thiểu</th><th>Trạng thái</th><th class="text-end">Thao tác</th></tr>
    </thead>
    <tbody>
        <c:set var="lowCount" value="0"/>
        <c:forEach var="b" items="${books}">
            <c:set var="low" value="${(b.stockQty!=null?b.stockQty:0) < (b.minStock!=null?b.minStock:0)}"/>
            <c:if test="${low}"><c:set var="lowCount" value="${lowCount+1}"/></c:if>
                <tr>
                    <td>${b.code}</td>
                <td>${b.title}</td>
                <td>${b.stockQty}</td>
                <td>${b.minStock}</td>
                <td>
                    <c:choose>
                        <c:when test="${low}"><span class="badge text-bg-danger">Thấp</span></c:when>
                        <c:otherwise><span class="badge text-bg-success">Ổn</span></c:otherwise>
                    </c:choose>
                </td>
                <td class="text-end">
                    <form action="${pageContext.request.contextPath}/inventory" method="post"
                          class="d-inline-flex align-items-center gap-1">
                        <input type="hidden" name="keepName" value="${name}">
                        <input type="hidden" name="bookId" value="${b.id}">
                        <input type="number" name="qty" min="1" value="1"
                               class="form-control form-control-sm" style="width:80px" />
                        <!-- 2 nút submit dùng cùng form -->
                        <button type="submit" name="op" value="import" class="btn btn-sm btn-outline-primary">Nhập</button>                     
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty books}">
            <tr><td colspan="6" class="text-center text-muted">Không có dữ liệu</td></tr>
        </c:if>
    </tbody>
</table>

<c:if test="${lowCount > 0}">
    <div class="alert alert-warning mt-3">
        Có <strong>${lowCount}</strong> đầu sách dưới mức tối thiểu. Vui lòng nhập thêm hàng.
    </div>
</c:if>

<!-- Bảng biến động gần đây (kèm Delete) -->
<div class="card my-4">
    <div class="card-header fw-semibold">Biến động gần đây</div>
    <div class="table-responsive">
        <table class="table table-hover align-middle m-0">
            <thead class="table-light"><tr><th>#</th><th>Sách</th><th>Loại</th><th class="text-end">SL (±)</th><th>Lý do</th><th class="text-end"></th></tr></thead>
            <tbody>
                <c:forEach var="m" items="${recentMovements}">
                    <tr>
                        <td>${m.id}</td>
                        <td>${m.book.code} - ${m.book.title}</td>
                        <td>${m.type}</td>
                        <td class="text-end">${m.quantityChange}</td>
                        <td>${m.reason}</td>
                        <td class="text-end">
                            <form action="${pageContext.request.contextPath}/inventory" method="post" class="d-inline"
                                  onsubmit="return confirm('Xóa movement #${m.id}?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="keepName" value="${name}">
                                <input type="hidden" name="id" value="${m.id}">
                                <button class="btn btn-sm btn-outline-danger"
                                        <c:if test="${m.order != null}">disabled</c:if>>Xóa</button>
                                </form>
                            </td>
                        </tr>
                </c:forEach>
                <c:if test="${empty recentMovements}">
                    <tr><td colspan="6" class="text-center text-muted py-3">Chưa có biến động</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>
