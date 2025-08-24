<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Quản lý đơn hàng</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .search-form {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .order-status {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-pending { background: #fff3cd; color: #856404; }
        .status-confirmed { background: #d1ecf1; color: #0c5460; }
        .status-shipped { background: #d4edda; color: #155724; }
        .status-delivered { background: #c3e6cb; color: #155724; }
        .status-cancelled { background: #f8d7da; color: #721c24; }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-2 bg-dark text-white min-vh-100 p-3">
                <h4 class="text-center mb-4">Quản lý nhà sách</h4>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link text-white" href="home.jsp">
                            <i class="fas fa-home"></i> Trang chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-white active" href="orders.jsp">
                            <i class="fas fa-shopping-cart"></i> Đơn hàng
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-white" href="order-stats">
                            <i class="fas fa-chart-bar"></i> Thống kê
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-white" href="home.jsp">
                            <i class="fas fa-book"></i> Sách
                        </a>
                    </li>
                </ul>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-10 p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h2><i class="fas fa-shopping-cart"></i> Quản lý đơn hàng</h2>
                <a href="order-stats" class="btn btn-info">
                    <i class="fas fa-chart-bar"></i> Xem thống kê
                </a>
                </div>
                
                <!-- Search Form -->
                <div class="search-form">
                    <form method="GET" action="order-search" class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label">Từ khóa</label>
                            <input type="text" class="form-control" name="keyword" 
                                   placeholder="Mã đơn hàng, ghi chú..." 
                                   value="${searchKeyword}">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Trạng thái</label>
                            <select class="form-select" name="status">
                                <option value="">Tất cả</option>
                                <option value="PENDING" ${searchStatus == 'PENDING' ? 'selected' : ''}>Chờ xử lý</option>
                                <option value="CONFIRMED" ${searchStatus == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                                <option value="SHIPPED" ${searchStatus == 'SHIPPED' ? 'selected' : ''}>Đã giao</option>
                                <option value="DELIVERED" ${searchStatus == 'DELIVERED' ? 'selected' : ''}>Đã nhận</option>
                                <option value="CANCELLED" ${searchStatus == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Từ ngày</label>
                            <input type="date" class="form-control" name="dateFrom" value="${searchDateFrom}">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Đến ngày</label>
                            <input type="date" class="form-control" name="dateTo" value="${searchDateTo}">
                        </div>
                        <div class="col-md-3 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary me-2">
                                <i class="fas fa-search"></i> Tìm kiếm
                            </button>
                            <a href="orders.jsp" class="btn btn-secondary">
                                <i class="fas fa-refresh"></i> Làm mới
                            </a>
                        </div>
                    </form>
                </div>
                
                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-triangle"></i> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <!-- Orders Table -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-list"></i> Danh sách đơn hàng 
                            <span class="badge bg-primary">${orders.size()}</span>
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Mã đơn hàng</th>
                                        <th>Ngày đặt</th>
                                        <th>Khách hàng</th>
                                        <th>Tổng tiền</th>
                                        <th>Trạng thái</th>
                                        <th>Thanh toán</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty orders}">
                                            <tr>
                                                <td colspan="7" class="text-center text-muted">
                                                    <i class="fas fa-inbox fa-2x mb-2"></i><br>
                                                    Không có đơn hàng nào
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="order" items="${orders != null ? orders : []}">
                                                <tr>
                                                    <td>
                                                        <strong>${order.orderCode}</strong>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${order.orderDate}" 
                                                                      pattern="dd/MM/yyyy HH:mm"/>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-secondary">
                                                            ID: ${order.customerId}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <span class="text-success fw-bold">
                                                            <fmt:formatNumber value="${order.grandTotal}" 
                                                                              pattern="#,##0 VND"/>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${order.status == 'PENDING'}">
                                                                <span class="order-status status-pending">Pending</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'CONFIRMED'}">
                                                                <span class="order-status status-confirmed">Confirmed</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'SHIPPED'}">
                                                                <span class="order-status status-shipped">Shipped</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'DELIVERED'}">
                                                                <span class="order-status status-delivered">Delivered</span>
                                                            </c:when>
                                                            <c:when test="${order.status == 'CANCELLED'}">
                                                                <span class="order-status status-cancelled">Cancelled</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="order-status">${order.status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${order.paymentStatus == 'PAID'}">
                                                                <span class="badge bg-success">Paid</span>
                                                            </c:when>
                                                            <c:when test="${order.paymentStatus == 'UNPAID'}">
                                                                <span class="badge bg-warning">Unpaid</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">${order.paymentStatus}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="order-detail?id=${order.id}" 
                                                           class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-eye"></i> Chi tiết
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
