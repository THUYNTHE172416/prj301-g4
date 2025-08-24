<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("pageTitle", "Quản lý sách");
    request.setAttribute("active", "books");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">📖 Danh mục sách</h2>

<div class="d-flex justify-content-between mb-3">
    <form action="management-book" method="post" class="d-flex" style="max-width: 400px;">
        <input name="search" id="txtSearch" type="text" class="form-control me-2" 
               placeholder="Tìm theo tên sách/mã sách"/>
        <button type="submit" name="btnSearch" class="btn btn-outline-primary">Tìm</button>
    </form>
    <a href="/add-new-book" class="btn btn-success">+ Thêm sách</a>
</div>

<table class="table table-striped table-hover align-middle">
    <thead class="table-dark">
        <tr>
            <th>Mã sách</th>
            <th>Ảnh bìa</th>
            <th>Tên sách</th>
            <th>Tác giả</th>
            <th>Thể loại</th>
            <th>Giá bán</th>
            <th>Số lượng</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="c" items="${listBook}">
            <tr>
                <td>${c.code}</td>
                <td><img src="${pageContext.request.contextPath}${c.coverUrl}" alt="Java" /></td>
                <td>${c.title}</td>
                <td>${dao.getAllAuthorByBookId(c.id)}</td>
                <td>${c.category.name}</td>
                <td>${c.price}</td>
                <td>${c.stockQty}</td>

                <td>
                    <c:if test="${c.status eq 'ACTIVE'}">
                        <a href="/ass-g6/edit-book?id=${c.id}" class="btn btn-sm btn-primary">Sửa</a>
                        <a href="/ass-g6/management-book?id=${c.id}&mode=2" class="btn btn-sm btn-danger">Xóa</a>
                    </c:if>
                </td>


            </tr>
        </c:forEach>

    </tbody>
</table>

<!-- Footer & JS -->
<footer class="bg-dark text-white-50 py-3 mt-4">
    <div class="container small text-center">
        © 2025 BookStore
    </div>
</footer>
<script src=""></script>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<c:if test="${not empty error}">
    <script>
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: '${error}', // lấy nội dung error từ server
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
