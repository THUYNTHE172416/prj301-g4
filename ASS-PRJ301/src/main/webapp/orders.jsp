<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("pageTitle", "Đơn hàng");
  request.setAttribute("active", "orders");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">🧾 Danh sách đơn hàng</h2>

<form class="row g-2 mb-3">
  <div class="col-md-3"><input type="text" class="form-control" placeholder="Tìm theo mã đơn..."></div>
  <div class="col-md-3"><input type="date" class="form-control"></div>
  <div class="col-md-2"><button class="btn btn-outline-primary w-100">Tìm</button></div>
</form>

<div class="table-responsive">
  <table class="table table-hover align-middle">
    <thead class="table-dark">
      <tr><th>Mã đơn</th><th>Ngày bán</th><th>Nhân viên</th><th>Khách hàng</th><th class="text-end">Tổng tiền</th><th></th></tr>
    </thead>
    <tbody>
      <tr><td>O001</td><td>2025-08-20 10:11</td><td>staff01</td><td>Nguyễn Văn C</td><td class="text-end">350,000</td><td><a class="btn btn-sm btn-primary" href="#">Chi tiết</a></td></tr>
      <tr><td>O002</td><td>2025-08-21 09:05</td><td>staff02</td><td>Lê Thị D</td><td class="text-end">180,000</td><td><a class="btn btn-sm btn-primary" href="#">Chi tiết</a></td></tr>
      <tr><td>O003</td><td>2025-08-21 15:32</td><td>staff01</td><td>Ngô Bá E</td><td class="text-end">520,000</td><td><a class="btn btn-sm btn-primary" href="#">Chi tiết</a></td></tr>
    </tbody>
  </table>
</div>

</div>
<footer class="bg-dark text-white-50 py-3 mt-4">
  <div class="container small text-center">© 2025 BookStore</div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
