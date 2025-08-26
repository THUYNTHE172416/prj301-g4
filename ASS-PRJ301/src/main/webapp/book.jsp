<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    request.setAttribute("pageTitle", "Quản lý sách");
    request.setAttribute("active", "books");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">📖 Danh mục sách</h2>

<!-- Search function -->
<div class="d-flex justify-content-between mb-3">
    <form action="management-book" method="post" class="d-flex" style="max-width: 400px;">
        <input name="search" value="${search}" id="txtSearch" type="search" class="form-control me-2"
               placeholder="Tìm theo tên sách/mã sách"/>
        <button type="submit" name="btnSearch" class="btn btn-outline-primary">Tìm</button>
    </form>

    <!-- button thêm sách -->
    <a href="/ass-g6/add-new-book" class="btn btn-success">+ Thêm sách</a>
</div>

<form action="management-book" method="get" class="d-flex align-items-center mb-3" style="max-width: 250px;">
    <div class="d-flex align-items-center mb-3" style="max-width: 250px;">
        <input type="hidden" name="col" value="">
        <label for="pageSize" class="me-2">Hiển thị:</label>
        <select name="pageSize" id="pageSize" class="form-select">
            <option value="5"  ${pageSize == 5 ? 'selected' : ''}>5</option>
            <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
            <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
            <option value="50" ${pageSize == 50 ? 'selected' : ''}>50</option>
        </select>
    </div>
    <div class="d-flex align-items-center gap-3">
        <label><input type="checkbox" name="col" value="code"
                      ${col == null or fn:contains(fn:join(col, ','), 'code') ? 'checked' : ''}> Mã sách</label>

        <label><input type="checkbox" name="col" value="cover"
                      ${col == null or fn:contains(fn:join(col, ','), 'cover') ? 'checked' : ''}> Ảnh bìa</label>

        <label><input type="checkbox" name="col" value="category"
                      ${col == null or fn:contains(fn:join(col, ','), 'category') ? 'checked' : ''}> Thể loại</label>

        <label><input type="checkbox" name="col" value="price"
                      ${col == null or fn:contains(fn:join(col, ','), 'price') ? 'checked' : ''}> Giá bán</label>

        <label><input type="checkbox" name="col" value="stock"
                      ${col == null or fn:contains(fn:join(col, ','), 'stock') ? 'checked' : ''}> Số lượng</label>

        <button type="submit" id="btnApply" class="btn btn-outline-primary btn-sm">Lưu</button>
    </div>
</form>

<table class="table table-striped table-hover align-middle shadow-sm rounded">
    <thead class="table-dark text-center">
        <tr>
            <c:if test="${col == null or fn:contains(fn:join(col, ','), 'code')}">
                <th class="col-md-1 col-code">Mã sách</th>
            </c:if>
            <c:if test="${col == null or fn:contains(fn:join(col, ','), 'cover')}">
                <th class="col-md-2 col-cover">Ảnh bìa</th>
            </c:if>
            <th class="col-md col-title">Tên sách</th> <!-- luôn hiển thị -->
            <c:if test="${col == null or fn:contains(fn:join(col, ','), 'category')}">
                <th class="col-md-1 col-category">Thể loại</th>
            </c:if>
            <c:if test="${col == null or fn:contains(fn:join(col, ','), 'price')}">
                <th class="col-md-1 col-price">Giá bán</th>
            </c:if>
            <c:if test="${col == null or fn:contains(fn:join(col, ','), 'stock')}">
                <th class="col-md-1 col-stock">Số lượng</th>
            </c:if>
            <th class="col-md-2 col-action">Hành động</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="c" items="${listBook}">
            <tr class="text-center">
                <c:if test="${col == null or fn:contains(fn:join(col, ','), 'code')}">
                    <td class="col-code">${c.code}</td>
                </c:if>
                <c:if test="${col == null or fn:contains(fn:join(col, ','), 'cover')}">
                    <td class="col-cover">
                        <img src="${c.coverUrl}" alt="${c.title}" class="img-fluid rounded border"
                             style="max-height:120px; object-fit:contain"/>
                    </td>
                </c:if>
                <td class="col-title text-start fw-semibold">${c.title}</td>
                <c:if test="${col == null or fn:contains(fn:join(col, ','), 'category')}">
                    <td class="col-category">${c.category.name}</td>
                </c:if>
                <c:if test="${col == null or fn:contains(fn:join(col, ','), 'price')}">
                    <td class="col-price fw-bold text-success">
                        <fmt:formatNumber value="${c.price}" type="number" pattern="###,###"/> ₫
                    </td>
                </c:if>
                <c:if test="${col == null or fn:contains(fn:join(col, ','), 'stock')}">
                    <td class="col-stock"><span class="badge bg-info text-dark">${c.stockQty}</span></td>
                </c:if>
                <td class="col-action">
                    <div class="d-flex justify-content-center gap-2">
                        <a href="/ass-g6/edit-book?id=${c.id}" class="btn btn-sm btn-outline-primary">
                            <i class="bi bi-pencil-square"></i> Sửa
                        </a>
                        <a href="/ass-g6/management-book?id=${c.id}&mode=2"
                           class="btn btn-sm btn-outline-danger"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa sách này?')">
                            <i class="bi bi-trash"></i> Xóa
                        </a>
                    </div>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<!-- Pagination -->
<nav aria-label="Page navigation">
    <ul class="pagination justify-content-center">
        <c:if test="${currentPage > 1}">
            <li class="page-item">
                <a class="page-link" href="management-book?page=${currentPage - 1}&pageSize=${pageSize}">Trước</a>
            </li>
        </c:if>
        <c:forEach begin="1" end="${totalPages}" var="i">
            <li class="page-item ${i == currentPage ? 'active' : ''}">
                <a class="page-link" href="management-book?page=${i}&pageSize=${pageSize}">${i}</a>
            </li>
        </c:forEach>
        <c:if test="${currentPage < totalPages}">
            <li class="page-item">
                <a class="page-link" href="management-book?page=${currentPage + 1}&pageSize=${pageSize}">Sau</a>
            </li>
        </c:if>
    </ul>
</nav>

<!-- Footer & JS -->
<footer class="bg-dark text-white-50 py-3 mt-4">
    <div class="container small text-center">
        © 2025 BookStore
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<!-- hiển thị thông báo -->
<c:if test="${not empty error}">
    <script>
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            html: '${error}',
            confirmButtonText: 'OK'
        });
    </script>
</c:if>

<c:if test="${not empty success}">
    <script>
        Swal.fire({
            icon: 'success',
            title: 'Thành công',
            text: '${success}',
            confirmButtonText: 'OK'
        });
    </script>
</c:if>
</body>
</html>
