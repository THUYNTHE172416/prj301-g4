<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("pageTitle", "Tổng quan");
  request.setAttribute("active", "dashboard");
%>
<%@ include file="view/header.jsp" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<style>
  /* Tối ưu hiển thị cho dashboard */
  .kpi-card { border: 0; box-shadow: 0 6px 18px rgba(0,0,0,.08); border-radius: 14px; }
  .kpi-card .card-body { padding: 18px 20px; }
  .kpi-title { letter-spacing: .04em; text-transform: uppercase; font-size: .78rem; opacity:.7 }
  .kpi-value { font-weight: 700; line-height: 1; }
  .trend-up { color: #28a745; font-weight: 600; }
  .trend-down { color: #dc3545; font-weight: 600; }
  .card-section { border: 0; box-shadow: 0 6px 18px rgba(0,0,0,.06); border-radius: 14px; }
  .table thead th { font-size: .85rem; text-transform: uppercase; letter-spacing: .04em; }
  .mini { font-size: .85rem; opacity: .8 }
  .progress { height: 8px; border-radius: 6px; }
  .avatar { width:28px; height:28px; border-radius:50%; object-fit:cover; }
  .stat-pill { font-size:.8rem; border-radius:999px; padding:.2rem .6rem; }
  @media (max-width: 992px){ .kpi-value { font-size: 1.75rem; } }
</style>

<c:set var="currentUser" value="${sessionScope.currentUser}" />

<c:if test="${currentUser != null}">
    <div class="alert alert-success mt-4">
        Chào mừng <c:out value="${currentUser.fullName}"/> . Vai trò của bạn là<c:out value="${currentUser.role}"/>.
    </div>
</c:if>

<h2 class="mb-4 d-flex align-items-center gap-2">🏠 Tổng quan hệ thống <span class="badge bg-secondary stat-pill">bản demo</span></h2>

<div class="row g-3">
  <div class="col-12 col-sm-6 col-lg-3">
    <div class="card kpi-card bg-primary text-white">
      <div class="card-body">
        <div class="kpi-title">Doanh thu hôm nay</div>
        <div class="display-5 kpi-value">5,200,000 đ</div>
        <div class="mini">So với hôm qua: <span class="trend-up">+8%</span></div>
      </div>
    </div>
  </div>
  <div class="col-12 col-sm-6 col-lg-3">
    <div class="card kpi-card" style="background: linear-gradient(135deg,#22c55e 0%, #16a34a 100%); color:#fff">
      <div class="card-body">
        <div class="kpi-title">Đơn hàng hôm nay</div>
        <div class="display-5 kpi-value">24</div>
        <div class="mini">Trung bình: 21 đơn/ngày</div>
      </div>
    </div>
  </div>
  <div class="col-12 col-sm-6 col-lg-3">
    <div class="card kpi-card" style="background: linear-gradient(135deg,#f59e0b 0%, #d97706 100%); color:#fff">
      <div class="card-body">
        <div class="kpi-title">Sách sắp hết</div>
        <div class="display-5 kpi-value">7</div>
        <div class="mini">Cần nhập lại trong tuần</div>
      </div>
    </div>
  </div>
  <div class="col-12 col-sm-6 col-lg-3">
    <div class="card kpi-card bg-dark text-white">
      <div class="card-body">
        <div class="kpi-title">Đầu sách</div>
        <div class="display-5 kpi-value">312</div>
        <div class="mini">Cập nhật gần nhất: hôm nay</div>
      </div>
    </div>
  </div>
</div>

<div class="row g-3 mt-1">
  <div class="col-lg-7">
    <div class="card card-section">
      <div class="card-header bg-white fw-semibold d-flex justify-content-between align-items-center">
        <span>🧾 Đơn hàng gần đây</span>
        <a class="btn btn-sm btn-outline-primary" href="orders.jsp">Xem tất cả</a>
      </div>
      <div class="card-body p-0">
        <table class="table mb-0 align-middle">
          <thead class="table-light">
            <tr><th>Mã đơn</th><th>Thời gian</th><th>NV</th><th>KH</th><th class="text-end">Tổng</th><th></th></tr>
          </thead>
          <tbody>
            <tr>
              <td>O003</td>
              <td>2025-08-21 15:32</td>
              <td><img class="avatar me-1" src="https://i.pravatar.cc/28?img=12" alt=""/> staff01</td>
              <td>Ngô Bá E</td>
              <td class="text-end">520,000</td>
              <td class="text-end"><a class="btn btn-sm btn-outline-secondary" href="orders.jsp">Chi tiết</a></td>
            </tr>
            <tr>
              <td>O002</td>
              <td>2025-08-21 09:05</td>
              <td><img class="avatar me-1" src="https://i.pravatar.cc/28?img=5" alt=""/> staff02</td>
              <td>Lê Thị D</td>
              <td class="text-end">180,000</td>
              <td class="text-end"><a class="btn btn-sm btn-outline-secondary" href="orders.jsp">Chi tiết</a></td>
            </tr>
            <tr>
              <td>O001</td>
              <td>2025-08-20 10:11</td>
              <td><img class="avatar me-1" src="https://i.pravatar.cc/28?img=8" alt=""/> staff01</td>
              <td>Nguyễn Văn C</td>
              <td class="text-end">350,000</td>
              <td class="text-end"><a class="btn btn-sm btn-outline-secondary" href="orders.jsp">Chi tiết</a></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <div class="col-lg-5">
    <div class="card card-section">
      <div class="card-header bg-white fw-semibold">📊 Sách bán chạy (tuần)</div>
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <div>
            <div class="fw-semibold">Spring Boot Nâng Cao</div>
            <div class="mini">Trần Thị B</div>
          </div>
          <div class="text-end" style="min-width:120px">
            <div class="mini">Đã bán: 56</div>
            <div class="progress"><div class="progress-bar" style="width: 85%"></div></div>
          </div>
        </div>
        <div class="d-flex justify-content-between align-items-center mb-2">
          <div>
            <div class="fw-semibold">Java Cơ Bản</div>
            <div class="mini">Nguyễn Văn A</div>
          </div>
          <div class="text-end" style="min-width:120px">
            <div class="mini">Đã bán: 49</div>
            <div class="progress"><div class="progress-bar" style="width: 72%"></div></div>
          </div>
        </div>
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <div class="fw-semibold">Truyện Kiều</div>
            <div class="mini">Nguyễn Du</div>
          </div>
          <div class="text-end" style="min-width:120px">
            <div class="mini">Đã bán: 41</div>
            <div class="progress"><div class="progress-bar" style="width: 60%"></div></div>
          </div>
        </div>
      </div>
      <div class="card-footer bg-white text-end">
        <a class="btn btn-sm btn-outline-secondary" href="reports.jsp">Xem báo cáo</a>
      </div>
    </div>
  </div>
</div>

<div class="row g-3 mt-1">
  <div class="col-lg-7">
    <div class="card card-section">
      <div class="card-header bg-white fw-semibold">⚠️ Cảnh báo tồn kho thấp</div>
      <div class="card-body p-0">
        <table class="table mb-0 align-middle">
          <thead class="table-light"><tr><th>Mã</th><th>Tên sách</th><th class="text-end">Tồn</th><th class="text-end">Tối thiểu</th><th>Trạng thái</th><th></th></tr></thead>
          <tbody>
            <tr>
              <td>B002</td><td>Spring Boot Nâng Cao</td>
              <td class="text-end">5</td><td class="text-end">8</td>
              <td><span class="badge bg-danger">Thấp</span></td>
              <td class="text-end"><a class="btn btn-sm btn-outline-primary" href="inventory.jsp">Điều chỉnh</a></td>
            </tr>
            <tr>
              <td>B010</td><td>Clean Code</td>
              <td class="text-end">3</td><td class="text-end">10</td>
              <td><span class="badge bg-danger">Thấp</span></td>
              <td class="text-end"><a class="btn btn-sm btn-outline-primary" href="inventory.jsp">Điều chỉnh</a></td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="card-footer bg-white">
        <span class="small text-muted">Tổng: 2 đầu sách cần nhập thêm.</span>
      </div>
    </div>
  </div>

  <div class="col-lg-5">
    <div class="card card-section">
      <div class="card-header bg-white fw-semibold">🗓️ Hoạt động gần đây</div>
      <div class="card-body">
        <ul class="list-unstyled mb-0">
          <li class="d-flex align-items-start gap-2 mb-3">
            <span class="badge bg-success stat-pill mt-1">+ Đơn</span>
            <div>
              <div class="fw-semibold">Đơn O003 đã thanh toán</div>
              <div class="mini">15:32 • bởi staff01</div>
            </div>
          </li>
          <li class="d-flex align-items-start gap-2 mb-3">
            <span class="badge bg-primary stat-pill mt-1">Kho</span>
            <div>
              <div class="fw-semibold">Nhập thêm 20 cuốn "Java Cơ Bản"</div>
              <div class="mini">10:45 • bởi owner</div>
            </div>
          </li>
          <li class="d-flex align-items-start gap-2">
            <span class="badge bg-warning text-dark stat-pill mt-1">Cảnh báo</span>
            <div>
              <div class="fw-semibold">"Clean Code" tồn dưới mức tối thiểu</div>
              <div class="mini">09:12 • hệ thống</div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>

</div> <footer class="bg-dark text-white-50 py-3 mt-4">
  <div class="container small text-center">© 2025 BookStore</div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>