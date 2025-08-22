<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("pageTitle", "Bán hàng (POS)");
  request.setAttribute("active", "pos");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">💳 Bán hàng tại quầy</h2>

<div class="row g-3">
  <div class="col-md-6">
    <div class="card">
      <div class="card-header">Thêm sách vào giỏ</div>
      <div class="card-body">
        <form class="row g-2">
          <div class="col-8">
            <input type="text" class="form-control" placeholder="Nhập mã sách hoặc tên sách..."/>
          </div>
          <div class="col-4">
            <button class="btn btn-primary w-100">Thêm</button>
          </div>
        </form>
        <div class="small text-muted mt-2">* Dữ liệu demo, chưa gắn backend</div>
      </div>
    </div>

    <div class="card mt-3">
      <div class="card-header">Danh mục gợi ý</div>
      <div class="card-body p-0">
        <table class="table mb-0">
          <thead><tr><th>Mã</th><th>Tên</th><th>Giá</th><th></th></tr></thead>
          <tbody>
            <tr><td>B001</td><td>Java Cơ Bản</td><td>120,000 đ</td><td><button class="btn btn-sm btn-outline-primary">Chọn</button></td></tr>
            <tr><td>B002</td><td>Spring Boot Nâng Cao</td><td>180,000 đ</td><td><button class="btn btn-sm btn-outline-primary">Chọn</button></td></tr>
            <tr><td>B003</td><td>Truyện Kiều</td><td>95,000 đ</td><td><button class="btn btn-sm btn-outline-primary">Chọn</button></td></tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>
  <div class="col-md-6">

    <div class="card">
      <div class="card-header d-flex justify-content-between align-items-center">
        <span>Giỏ hàng</span>
        <span class="small text-muted">Mã đơn tạm: POS-0001</span>
      </div>
      <div class="card-body p-0">
        <table class="table align-middle mb-0">
          <thead><tr><th>Tên sách</th><th class="text-end">SL</th><th class="text-end">Đơn giá</th><th class="text-end">Thành tiền</th><th></th></tr></thead>
          <tbody>
            <tr>
              <td>Java Cơ Bản</td><td class="text-end">1</td><td class="text-end">120,000</td><td class="text-end">120,000</td>
              <td class="text-end"><button class="btn btn-sm btn-outline-danger">Xóa</button></td>
            </tr>
            <tr>
              <td>Spring Boot Nâng Cao</td><td class="text-end">2</td><td class="text-end">180,000</td><td class="text-end">360,000</td>
              <td class="text-end"><button class="btn btn-sm btn-outline-danger">Xóa</button></td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="card-footer">
        <div class="d-flex justify-content-between">
          <div>Mã khuyến mãi:</div>
          <div style="max-width:220px" class="d-flex">
            <input class="form-control form-control-sm me-2" placeholder="NHAPMA"/>
            <button class="btn btn-sm btn-outline-secondary">Áp dụng</button>
          </div>
        </div>
        <hr/>
        <div class="d-flex justify-content-between"><span>Tạm tính</span><strong>480,000 đ</strong></div>
        <div class="d-flex justify-content-between"><span>Giảm giá</span><strong>20,000 đ</strong></div>
        <div class="d-flex justify-content-between fs-5 mt-2"><span>Tổng thanh toán</span><strong>460,000 đ</strong></div>
        <div class="mt-3 d-flex justify-content-end">
          <button class="btn btn-success">Xác nhận &amp; In hóa đơn</button>
        </div>
      </div>
    </div>

  </div>
</div>

</div>
<footer class="bg-dark text-white-50 py-3 mt-4">
  <div class="container small text-center">© 2025 BookStore</div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
