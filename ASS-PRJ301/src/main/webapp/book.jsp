<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("pageTitle", "Quản lý sách");
    request.setAttribute("active", "books");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">📖 Danh mục sách</h2>


<!--search function-->

<div class="d-flex justify-content-between mb-3">
    <form action="management-book" method="post" class="d-flex" style="max-width: 400px;">
        <input name="search" value="${search}" id="txtSearch" type="search" class="form-control me-2" 
               placeholder="Tìm theo tên sách/mã sách"/>
        <button type="submit" name="btnSearch" class="btn btn-outline-primary">Tìm</button>
    </form>
              
               
<!--button them sach-->
    <a href="/ass-g6/add-new-book" class="btn btn-success">+ Thêm sách</a>
</div>

               
               
<table class="table table-striped table-hover align-middle shadow-sm rounded">
    <thead class="table-dark text-center">
        <tr>
            <th class="col-md-1">Mã sách</th>
            <th class="col-md-2">Ảnh bìa</th>
            <th class="col-md">Tên sách</th>
            <th class="col-md-1">Thể loại</th>
            <th class="col-md-1">Giá bán</th>
            <th class="col-md-1">Số lượng</th>
            <th class="col-md-2">Hành động</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="c" items="${listBook}">
            <tr class="text-center">
                <!--code-->
                <td>${c.code}</td>

                <!--image-->
                <td>
                    <img src="${c.coverUrl}"
                         alt="${c.title}" 
                         class="img-fluid rounded border" 
                         style="max-height:120px; object-fit:contain"/>
                </td>

                <!--title (book name)-->
                <td class="text-start fw-semibold">${c.title}</td>

                <!--category name-->
                <td>${c.category.name}</td>

                <!--price-->
                <td class="fw-bold text-success">${c.price}₫</td>

                <!--stock quantity-->
                <td>
                    <span class="badge bg-info text-dark">${c.stockQty}</span>
                </td>

                <!--action-->
                <td>
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


<!-- Footer & JS -->
<footer class="bg-dark text-white-50 py-3 mt-4">
    <div class="container small text-center">
        © 2025 BookStore
    </div>
</footer>
<script src=""></script>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>





<!--hien thi thong bao-->
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
