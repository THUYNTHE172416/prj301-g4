<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="vi">
    <head>
        <meta charset="utf-8"/>
        <title>Bán hàng (POS) — Demo Fix Cứng</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <style>
            /* Nếu header.jsp dùng navbar fixed-top, chừa khoảng trên để không bị đè */
            body {
                background:#f7f7f9;
                padding-top: 72px;
            }  /* điều chỉnh 56–80px tùy chiều cao navbar */
            .table td,.table th{
                vertical-align:middle
            }
            .money{
                white-space:nowrap
            }
            /* Căn nút sang phải */


        </style>
    </head>


    <body> 
        <%@ include file="view/header.jsp" %>
        <div class="container py-4">

            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2 class="mb-0">💳 Bán hàng tại quầy (Demo)</h2>
                <span class="small text-muted">Mã đơn tạm: <strong>POS-0001</strong></span>
            </div>

            <div class="row g-3">
                <!-- LEFT -->
                <div class="col-lg-6">
                    <div class="card">
                        <div class="card-header">Chọn sản phẩm thanh toán</div>
                        <div class="card-body">
                            <form class="row g-2" action="checkout" method="get">
                                <div class="col-4">
                                    <!-- dropdown chọn tìm theo -->
                                    <select class="form-select" name="type">
                                        <option value="code">Mã sách</option>
                                        <option value="title">Tên sách</option>
                                    </select>
                                </div>
                                <div class="col-8">
                                    <input type="text" class="form-control" name="key" value="${key}"placeholder="Nhập mã sách hoặc tên sách..."/>
                                </div>
                                <div class="col-4 ">
                                    <input type="submit" class="btn btn-primary" value="Tìm kiếm" />
                                </div>

                            </form>


                        </div>
                    </div>

                    <div class="card mt-3">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <span>Danh mục gợi ý</span>
                            <small class="text-muted">Nhấn “Chọn” để thêm</small>
                        </div>
                        <div class="card-body p-0">
                            <table class="table mb-0 align-middle">
                                <thead>
                                    <tr>
                                        <th style="width:110px">Mã</th>
                                        <th>Tên</th>
                                        <th class="text-end" style="width:140px">Giá</th>
                                        <th class="text-end" style="width:120px">
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${empty listBook}">
                                        <tr>
                                            <td colspan="4" class="text-center text-muted">Không tìm thấy sản phẩm</td>
                                        </tr>
                                    </c:if>
                                    <c:forEach items="${listBook}" var="c">
                                        <tr>
                                            <td>${c.code}</td>
                                            <td>${c.title}</td>
                                            <td class="text-end money">${c.price} đ</td>
                                            <td class="text-end">
                                                <!-- Dùng POST, không để id trên URL -->
                                                <form action="checkout" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="add"/>
                                                    <input type="hidden" name="id" value="${c.code}"/>
                                                    <button class="btn btn-sm btn-outline-primary">Chọn</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <c:if test="${param.type ne null and not empty param.key}">
                                <div class="mt-2 text-end">
                                    <a href="checkout" class="btn btn-sm btn-outline-secondary">⬅ Quay lại</a>
                                </div>
                            </c:if>
                        </div>
                    </div>

                </div>

                <!-- RIGHT -->
                <div class="col-lg-6">

                    <!-- Khách hàng (fix cứng) -->
                    <div class="card mb-3">
                        <div class="card-header">Khách hàng</div>
                        <div class="card-body">

                            <!-- Thông báo (nếu có) -->
                            <c:if test="${not empty msg}">
                                <div class="alert alert-warning py-1 px-2 mb-2">${msg}</div>
                            </c:if>

                            <c:choose>
                                <c:when test="${not empty selectedCustomer}">
                                    <div>
                                        <strong>${selectedCustomer.fullName}</strong>
                                        <span class="text-muted">(${selectedCustomer.phone})</span>
                                    </div>
                                    <form method="post" action="checkout" class="mt-2">
                                        <input type="hidden" name="action" value="clearCustom"/>
                                        <button class="btn btn-sm btn-outline-danger">Bỏ chọn</button>
                                    </form>
                                </c:when>

                                <c:otherwise>
                                    <!-- Form tìm khách -->
                                    <form method="post" action="checkout" class="row g-2 mt-2">
                                        <input type="hidden" name="action" value="findCustom"/>
                                        <div class="col-md-5">
                                            <input type="text" name="phone" placeholder="Tìm theo SĐT" class="form-control form-control-sm"/>
                                        </div>
                                        <div class="col-md-5">
                                            <input type="text" name="name" placeholder="Tìm theo tên" class="form-control form-control-sm"/>
                                        </div>
                                        <div class="col-md-2">
                                            <button type="submit" class="btn btn-sm btn-primary w-100">Tìm</button>
                                        </div>
                                    </form>

                                    <!-- Danh sách kết quả -->
                                    <c:if test="${not empty results}">
                                        <ul class="list-group list-group-sm mt-3">
                                            <c:forEach var="c" items="${results}">
                                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                                    <span><strong>${c.fullName}</strong> (${c.phone})</span>
                                                    <form method="post" action="checkout">
                                                        <input type="hidden" name="action" value="pickCustom"/>
                                                        <input type="hidden" name="customerId" value="${c.id}"/>
                                                        <button class="btn btn-sm btn-outline-success">Chọn</button>
                                                    </form>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <!-- Giỏ hàng (fix cứng 2 dòng) -->
                    <div class="card">
                        <div class="card-header">Giao dịch</div>
                        <div class="card-body p-0">
                            <table class="table align-middle mb-0">
                                <thead>
                                    <tr>
                                        <th>Tên sách</th>
                                        <th class="text-end" style="width:140px">SL</th>
                                        <th class="text-end" style="width:140px">Đơn giá</th>
                                        <th class="text-end" style="width:160px">Thành tiền</th>
                                        <th class="text-end" style="width:100px"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${cart}" var="c">
                                        <tr>
                                            <td>
                                                <div class="fw-semibold">${c.title}</div>
                                                <div class="small text-muted">${c.code}</div>
                                            </td>

                                            <td class="text-end">
                                                <div class="d-flex justify-content-end" style="gap:.25rem">
                                                    <!-- Giảm -->
                                                    <form action="checkout" method="post" class="d-inline">
                                                        <input type="hidden" name="action" value="dec"/>
                                                        <input type="hidden" name="id" value="${c.code}"/>
                                                        <button class="btn btn-sm btn-outline-secondary">−</button>
                                                    </form>

                                                    <!-- SL hiện tại -->
                                                    <input type="text" class="form-control form-control-sm text-end"
                                                           style="max-width:70px" value="${c.qty}" readonly/>

                                                    <!-- Tăng -->
                                                    <form action="checkout" method="post" class="d-inline">
                                                        <input type="hidden" name="action" value="inc"/>
                                                        <input type="hidden" name="id" value="${c.code}"/>
                                                        <button class="btn btn-sm btn-outline-secondary">+</button>
                                                    </form>
                                                </div>
                                            </td>
                                            <td class="text-end money">
                                                <fmt:formatNumber value="${c.unitPrice}" type="number"/> đ
                                            </td>
                                            <td class="text-end money">
                                                <fmt:formatNumber value="${c.lineTotal}" type="number"/> đ
                                            </td>
                                            <td class="text-end">
                                                <form action="checkout" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="remove"/>
                                                    <input type="hidden" name="id" value="${c.code}"/>
                                                    <button class="btn btn-sm btn-outline-danger">Xóa</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty cart}">
                                        <tr><td colspan="5" class="text-center text-muted">Giỏ hàng trống</td></tr>
                                    </c:if>
                                </tbody>

                            </table>
                        </div>

                        <!-- Khuyến mãi + Tổng tiền (fix cứng) -->

                    </div>

                    <hr/>

                    <form method="post" action="checkout" class="d-flex gap-2">
                        <input type="hidden" name="action" value="applyPromo"/>
                        <input type="text" name="promoCode" class="form-control" placeholder="Nhập mã giảm giá…" />
                        <button class="btn btn-outline-primary">Áp dụng</button>
                        <a class="btn btn-outline-secondary" href="checkout?action=removePromo">Bỏ mã</a>
                    </form>
                    <c:if test="${not empty discountLabel}">
                        <div class="text-muted small mt-1">${discountLabel}</div>
                    </c:if>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Phương thức thanh toán</label>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="cash" value="CASH" >
                            <label class="form-check-label">
                                Tiền mặt
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="bank" value="CARD">
                            <label class="form-check-label">
                                Chuyển khoản
                            </label>
                        </div>
                    </div>
                    <div class="d-flex justify-content-between">
                        <span>Tạm tính</span>
                        <strong class="money"><fmt:formatNumber value="${subtotal}" type="number"/> đ</strong>
                    </div>
                    <div class="d-flex justify-content-between">
                        <span>Giảm giá</span>
                        <strong class="money"><fmt:formatNumber value="${discount}" type="number"/> đ</strong>
                    </div>
                    <div class="d-flex justify-content-between fs-5 mt-2">
                        <span>Tổng thanh toán</span>
                        <strong class="money"><fmt:formatNumber value="${grandTotal}" type="number"/> đ</strong>
                    </div>
                    <div class="mt-3 d-flex justify-content-end" style="gap:.5rem">
                        <form action="checkout" method="post" class="d-inline">
                            <input type="hidden" name="action" value="clear"/>
                            <button class="btn btn-outline-secondary"
                                    onclick="return confirm('Xóa hết giỏ hàng?')">Xóa hết</button>
                        </form>
                        <form method="post" action="checkout">
                            <input type="hidden" name="action" value="pay"/>
                            <button class="btn btn-success" ${empty cart ? 'disabled' : ''}>
                                Xác nhận &amp; In hóa đơn
                            </button>
                        </form>
                    </div>

                </div>

            </div>
        </div>

        <footer class="text-center small text-muted mt-4">© 2025 BookStore — Demo cố định</footer>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
