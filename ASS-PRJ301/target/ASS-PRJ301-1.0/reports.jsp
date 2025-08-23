<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("pageTitle", "Báo cáo");
  request.setAttribute("active", "reports");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">📊 Báo cáo</h2>

<div class="row g-3">
  <div class="col-md-4">
    <div class="card border-0 shadow-sm">
      <div class="card-body">
        <div class="text-muted small">Doanh thu hôm nay</div>
        <div class="display-6">5,200,000 đ</div>
      </div>
    </div>
  </div>
  <div class="col-md-4">
    <div class="card border-0 shadow-sm">
      <div class="card-body">
        <div class="text-muted small">Doanh thu tuần</div>
        <div class="display-6">31,450,000 đ</div>
      </div>
    </div>
  </div>
  <div class="col-md-4">
    <div class="card border-0 shadow-sm">
      <div class="card-body">
        <div class="text-muted small">Doanh thu tháng</div>
        <div class="display-6">126,780,000 đ</div>
      </div>
    </div>
  </div>
</div>

<div class="card mt-4">
  <div class="card-header">Sách bán chạy (tháng)</div>
  <div class="card-body p-0">
    <table class="table mb-0">
      <thead><tr><th>#</th><th>Tên sách</th><th>Tác giả</th><th>Đã bán</th><th>Doanh thu</th></tr></thead>
      <tbody>
        <tr><td>1</td><td>Spring Boot Nâng Cao</td><td>Trần Thị B</td><td>210</td><td>37,800,000</td></tr>
        <tr><td>2</td><td>Java Cơ Bản</td><td>Nguyễn Văn A</td><td>186</td><td>22,320,000</td></tr>
        <tr><td>3</td><td>Truyện Kiều</td><td>Nguyễn Du</td><td>160</td><td>15,200,000</td></tr>
      </tbody>
    </table>
  </div>
</div>

<div class="card mt-4">
  <div class="card-header">Doanh thu theo ngày (mẫu)</div>
  <div class="card-body">
    <ul class="mb-0">
      <li>2025-08-18: 4,200,000</li>
      <li>2025-08-19: 5,050,000</li>
      <li>2025-08-20: 4,780,000</li>
      <li>2025-08-21: 5,600,000</li>
    </ul>
  </div>
</div>

</div>
<footer class="bg-dark text-white-50 py-3 mt-4">
  <div class="container small text-center">© 2025 BookStore</div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
