<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("pageTitle", "Tồn kho");
  request.setAttribute("active", "inventory");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">📦 Tồn kho</h2>

<table class="table table-striped table-hover align-middle">
  <thead class="table-dark">
    <tr><th>Mã</th><th>Tên sách</th><th>Tồn hiện tại</th><th>Tối thiểu</th><th>Trạng thái</th><th></th></tr>
  </thead>
  <tbody>
    <tr>
      <td>B001</td><td>Java Cơ Bản</td><td>15</td><td>10</td>
      <td><span class="badge text-bg-success">Ổn</span></td>
      <td><a class="btn btn-sm btn-outline-primary" href="#">Cập nhật</a></td>
    </tr>
    <tr>
      <td>B002</td><td>Spring Boot Nâng Cao</td><td>5</td><td>8</td>
      <td><span class="badge text-bg-danger">Thấp</span></td>
      <td><a class="btn btn-sm btn-outline-primary" href="#">Cập nhật</a></td>
    </tr>
    <tr>
      <td>B003</td><td>Truyện Kiều</td><td>30</td><td>12</td>
      <td><span class="badge text-bg-success">Ổn</span></td>
      <td><a class="btn btn-sm btn-outline-primary" href="#">Cập nhật</a></td>
    </tr>
  </tbody>
</table>

<div class="alert alert-warning mt-3">
  Có <strong>1</strong> đầu sách dưới mức tối thiểu. Vui lòng nhập thêm hàng.
</div>

</div>
<footer class="bg-dark text-white-50 py-3 mt-4">
  <div class="container small text-center">© 2025 BookStore</div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
