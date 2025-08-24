<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="vi">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Hóa đơn #${order.id}</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

  <style>
    :root {
      --brand: #0d6efd;
      --ink: #212529;
      --muted: #6c757d;
      --soft: #f8f9fa;
    }
    body { background: #f2f4f7; color: var(--ink); }
    .invoice {
      max-width: 960px;
      margin: 32px auto;
      background: #fff;
      border-radius: 16px;
      box-shadow: 0 8px 24px rgba(0,0,0,.06);
      overflow: hidden;
    }
    .invoice-header {
      background: linear-gradient(135deg, var(--brand), #5aa1ff);
      color: #fff;
      padding: 28px 28px 18px;
    }
    .brand {
      font-weight: 700;
      letter-spacing: .3px;
    }
    .badge-soft {
      background: rgba(255,255,255,.18);
      border: 1px solid rgba(255,255,255,.35);
      color: #fff;
    }
    .meta small { color: rgba(255,255,255,.85); }
    .section {
      padding: 24px 28px;
    }
    .table-invoice thead th {
      background: var(--soft);
      border-bottom: 1px solid #e9ecef;
      text-transform: uppercase;
      font-size: .76rem;
      letter-spacing: .04em;
      color: var(--muted);
    }
    .table-invoice td, .table-invoice th { vertical-align: middle; }
    .text-money { white-space: nowrap; text-align: right; }
    .totals .row > div { padding: 6px 0; }
    .totals .label { color: var(--muted); }
    .totals .value { text-align: right; font-weight: 600; }
    .totals .grand {
      border-top: 1px dashed #dfe3e8;
      margin-top: 8px; padding-top: 12px;
      font-size: 1.15rem;
    }
    .footer-note {
      background: var(--soft);
      color: var(--muted);
      font-size: .9rem;
      padding: 14px 28px;
    }
    .actions {
      padding: 0 28px 24px;
      display: flex; gap: .5rem; justify-content: flex-end;
    }
    @media print {
      body { background: #fff; }
      .invoice { box-shadow: none; margin: 0; border-radius: 0; }
      .actions, .btn, a[href]:after { display: none !important; }
      .invoice-header { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
    }
  </style>
</head>
<body>

  <div class="invoice">
    <!-- Header -->
    <div class="invoice-header">
      <div class="d-flex justify-content-between align-items-start flex-wrap gap-3">
        <div>
          <div class="brand h4 mb-1">BookStore</div>
          <div class="small">123 Đường ABC, Quận 1, TP.HCM</div>
          <div class="small">Hotline: 0900 000 000 • support@bookstore.vn</div>
        </div>
        <div class="text-end meta">
          <span class="badge badge-soft rounded-pill px-3 py-2 mb-2">HÓA ĐƠN</span>
          <div><small>Mã hóa đơn:</small> <strong>#${order.id}</strong></div>
          <div><small>Mã đơn:</small> <strong>${order.orderCode}</strong></div>
          <div><small>Ngày:</small> <strong>${order.orderDate}</strong></div>
          <div><small>Trạng thái:</small> <strong>${order.status}</strong></div>
          <div><small>Thanh toán:</small> <strong>${order.paymentStatus}</strong></div>
        </div>
      </div>
    </div>

    <!-- Customer (có thể bổ sung sau) -->
    <div class="section pt-3 pb-0">
      <div class="row g-3">
        <div class="col-md-6">
          <div class="fw-semibold">Khách hàng</div>
          <div class="text-muted small">Tên KH / SĐT / Email (tuỳ bạn đổ dữ liệu)</div>
        </div>
      </div>
    </div>

    <!-- Items -->
    <div class="section">
      <table class="table table-invoice">
        <thead>
          <tr>
            <th style="width:120px">Mã</th>
            <th>Sách</th>
            <th class="text-center" style="width:90px">SL</th>
            <th class="text-money" style="width:160px">Đơn giá</th>
            <th class="text-money" style="width:180px">Thành tiền</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${details}" var="d">
            <tr>
              <td class="text-muted">${d.book.code}</td>
              <td>
                <div class="fw-semibold">${d.book.title}</div>
              </td>
              <td class="text-center">${d.quantity}</td>
              <td class="text-money">
                <fmt:formatNumber value="${d.unitPrice}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
              </td>
              <td class="text-money">
                <fmt:formatNumber value="${d.lineTotal}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <!-- Totals -->
    <div class="section pt-0">
      <div class="totals">
        <div class="row">
          <div class="col-8 label">Tạm tính</div>
          <div class="col-4 value">
            <fmt:formatNumber value="${order.total}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
          </div>
        </div>
        <div class="row">
          <div class="col-8 label">Giảm giá</div>
          <div class="col-4 value">
            <fmt:formatNumber value="${order.discount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
          </div>
        </div>
        <div class="row grand">
          <div class="col-8">Tổng thanh toán</div>
          <div class="col-4 value">
            <fmt:formatNumber value="${order.grandTotal}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
          </div>
        </div>
      </div>
    </div>

    <!-- Actions -->
    <div class="actions">
      <a href="checkout" class="btn btn-outline-secondary">⬅ Quay lại POS</a>
      <button class="btn btn-primary" onclick="window.print()">🖨 In hóa đơn</button>
    </div>

    <!-- Footer -->
    <div class="footer-note">
      Cảm ơn bạn đã mua sắm tại BookStore. Hóa đơn có giá trị trong 7 ngày cho việc đổi trả theo chính sách cửa hàng.
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
