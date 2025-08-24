<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Voucher</title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <style>
            body {
                padding-top:64px;
            }
        </style>
    </head>
    <body>

        <%@ include file="view/header.jsp" %>

        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3 class="m-0">🎟️ Danh sách Voucher</h3>
                <form class="d-flex" action="promotion" method="get">
                    <input class="form-control form-control-sm me-2" name="keyword" placeholder="Tìm mã / tên..." value="${param.keyword}">
                    <button class="btn btn-sm btn-outline-primary">Tìm</button>
                </form>
            </div>

            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span>Voucher hiện có</span>
                    <!-- Thay link hiện tại -->
                    <button type="button" class="btn btn-sm btn-success"
                            data-bs-toggle="modal" data-bs-target="#createModal">
                        ➕ Thêm voucher
                    </button>

                </div>

                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-striped align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th style="width:120px">Mã</th>
                                    <th>Tên</th>
                                    <th style="width:120px">Loại</th>
                                    <th style="width:120px">Giá trị</th>
                                    <th style="width:180px">Thời gian</th>
                                    <th style="width:100px">Trạng thái</th>
                                    <th style="width:180px"></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${getall}" var="o">
                                    <tr>
                                        <td><code>${o.code}</code></td>
                                        <td>${o.name}</td>
                                        <td><span class="badge text-bg-secondary">${o.type}</span></td>
                                        <td>
                                            <c:if test="${o.type == 'PERCENT'}">${o.value}%</c:if>
                                            <c:if test="${o.type == 'FIXED'}"><fmt:formatNumber value="${o.value}" type="number"/> đ</c:if>
                                            </td>
                                            <td>
                                            ${o.startDate != null ? o.startDate.toLocalDate() : ''} -
                                            ${o.endDate   != null ? o.endDate.toLocalDate()   : ''}
                                        </td>
                                        <td>
                                            <c:if test="${o.active}"><span class="badge bg-success">Active</span></c:if>
                                            <c:if test="${!o.active}"><span class="badge bg-secondary">Inactive</span></c:if>
                                            </td>
                                            <td class="text-nowrap">
                                                <a href="promotion?action=edit&id=${o.id}" class="btn btn-sm btn-warning">Sửa</a>
                                            <a href="promotion?action=toggleActive&id=${o.id}"
                                               class="btn btn-sm ${o.active ? 'btn-outline-warning' : 'btn-outline-success'}">
                                                ${o.active ? 'Tắt' : 'Bật'}
                                            </a>
                                            <a href="promotion?action=delete&id=${o.id}" class="btn btn-sm btn-danger"
                                               onclick="return confirm('Xóa voucher này?')">Xóa</a>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty getall}">
                                    <tr><td colspan="7" class="text-center text-muted">Chưa có khuyến mãi</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="text-muted small mt-3">
                Mẹo: <strong>PERCENT</strong> = giảm theo %, <strong>FIXED</strong> = giảm số tiền cố định.
            </div>
        </div>

        <!-- Modal: Tạo voucher -->
        <div class="modal fade" id="createModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form class="modal-content" method="get" action="promotion">
                    <input type="hidden" name="action" value="create"/>

                    <div class="modal-header">
                        <h5 class="modal-title">Thêm voucher</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2">${error}</div>
                        </c:if>

                        <div class="mb-2">
                            <label class="form-label">Mã (unique)</label>
                            <input name="code" class="form-control" required value="${param.code}">
                        </div>

                        <div class="mb-2">
                            <label class="form-label">Tên</label>
                            <input name="name" class="form-control" required value="${param.name}">
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-2">
                                <label class="form-label">Loại</label>
                                <select name="type" class="form-select">
                                    <option value="PERCENT" ${param.type == 'PERCENT' ? 'selected' : ''}>PERCENT</option>
                                    <option value="FIXED"   ${param.type == 'FIXED'   ? 'selected' : ''}>FIXED</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-2">
                                <label class="form-label">Giá trị</label>
                                <input name="value" type="number" step="0.01" min="0" class="form-control" required value="${param.value}">
                                <div class="form-text">PERCENT = %, FIXED = số tiền</div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-2">
                                <label class="form-label">Bắt đầu</label>
                                <input name="start" type="datetime-local" class="form-control" required value="${param.start}">
                            </div>
                            <div class="col-md-6 mb-2">
                                <label class="form-label">Kết thúc</label>
                                <input name="end" type="datetime-local" class="form-control" required value="${param.end}">
                            </div>
                        </div>

                        <div class="form-check mt-2">
                            <input class="form-check-input" type="checkbox" name="active" id="cActive"
                                   ${param.active == 'on' ? 'checked' : 'checked'}>
                            <label class="form-check-label" for="cActive">Kích hoạt</label>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button class="btn btn-success">Lưu</button>
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Nếu có lỗi -> auto mở lại modal -->
        <c:if test="${openCreate}">
            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    var m = new bootstrap.Modal(document.getElementById('createModal'));
                    m.show();
                });
            </script>
        </c:if>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
