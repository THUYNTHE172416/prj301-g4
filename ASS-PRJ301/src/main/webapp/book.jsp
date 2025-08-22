<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("pageTitle", "Danh mục sách");
    request.setAttribute("active", "books");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">📖 Danh mục sách</h2>

<div class="d-flex justify-content-between mb-3">
    <form class="d-flex" style="max-width: 400px;">
        <input type="text" class="form-control me-2" placeholder="Tìm theo tên / tác giả / thể loại">
        <button class="btn btn-outline-primary">Tìm</button>
    </form>
    <a href="#" class="btn btn-success">+ Thêm sách</a>
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
        <tr>
            <td>B001</td>
            <td><img src="" alt="Java" /></td>
            <td>Java Cơ Bản</td>
            <td>Nguyễn Văn A</td>
            <td>Lập trình</td>
            <td>120,000 đ</td>
            <td>15</td>
            <td>
                <a href="#" class="btn btn-sm btn-primary">Sửa</a>
                <a href="#" class="btn btn-sm btn-danger">Xóa</a>
            </td>
        </tr>
        <tr>
            <td>B002</td>
            <td><img src="" alt="Spring Boot" /></td>
            <td>Spring Boot Nâng Cao</td>
            <td>Trần Thị B</td>
            <td>Lập trình</td>
            <td>180,000 đ</td>
            <td>5</td>
            <td>
                <a href="#" class="btn btn-sm btn-primary">Sửa</a>
                <a href="#" class="btn btn-sm btn-danger">Xóa</a>
            </td>
        </tr>
        <tr>
            <td>B003</td>
            <td><img src="" alt="Truyện Kiều" /></td>
            <td>Truyện Kiều</td>
            <td>Nguyễn Du</td>
            <td>Văn học</td>
            <td>95,000 đ</td>
            <td>30</td>
            <td>
                <a href="#" class="btn btn-sm btn-primary">Sửa</a>
                <a href="#" class="btn btn-sm btn-danger">Xóa</a>
            </td>
        </tr>
    </tbody>
</table>

</div> <!-- đóng .content mở ở headerMenu.jsp -->

<!-- Footer & JS -->
<footer class="bg-dark text-white-50 py-3 mt-4">
  <div class="container small text-center">
    © 2025 BookStore
  </div>
</footer>
<script src=""></script>
</body>
</html>
