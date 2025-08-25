<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Chi tiết đơn hàng</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .order-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .order-info {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .order-status {
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: bold;
        }
        .status-pending { background: #fff3cd; color: #856404; }
        .status-confirmed { background: #d1ecf1; color: #0c5460; }
        .status-shipped { background: #d4edda; color: #155724; }
        .status-delivered { background: #c3e6cb; color: #155724; }
        .status-cancelled { background: #f8d7da; color: #721c24; }
        .book-item {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            background: white;
        }
        .book-cover {
            width: 60px;
            height: 80px;
            object-fit: cover;
            border-radius: 4px;
        }
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
                <!-- Back Button -->
                <div class="mb-3">
                    <a href="orders.jsp" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left"></i> Quay lại danh sách
                    </a>
                </div>
                
                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-triangle"></i> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <!-- Order Header -->
                <div class="order-header">
                    <div class="row align-items-center">
                        <div class="col-md-8">
                            <h2 class="mb-2">
                                <i class="fas fa-receipt"></i> Đơn hàng: ${order.orderCode}
                            </h2>
                            <p class="mb-0">
                                <i class="fas fa-calendar"></i> 
                                Ngày đặt: <c:choose><c:when test="${order.orderDate != null}"><c:out value="${order.orderDate}"/></c:when><c:otherwise><span class="text-muted">N/A</span></c:otherwise></c:choose>
                            </p>
                        </div>
                        <div class="col-md-4 text-end">
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
                        </div>
                    </div>
                </div>
                
                <!-- Order Information -->
                <div class="row">
                    <div class="col-md-6">
                        <div class="order-info">
                            <h5><i class="fas fa-info-circle"></i> Thông tin đơn hàng</h5>
                            <table class="table table-borderless">
                                <tr>
                                    <td><strong>Mã đơn hàng:</strong></td>
                                    <td>${order.orderCode}</td>
                                </tr>
                                <tr>
                                    <td><strong>Ngày đặt:</strong></td>
                                    <td><c:choose><c:when test="${order.orderDate != null}"><c:out value="${order.orderDate}"/></c:when><c:otherwise><span class="text-muted">N/A</span></c:otherwise></c:choose></td>
                                </tr>
                                <tr>
                                    <td><strong>Trạng thái:</strong></td>
                                    <td>${order.status}</td>
                                </tr>
                                <tr>
                                    <td><strong>Phương thức thanh toán:</strong></td>
                                    <td>${order.paymentMethod}</td>
                                </tr>
                                <tr>
                                    <td><strong>Trạng thái thanh toán:</strong></td>
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
                                </tr>
                            </table>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="order-info">
                            <h5><i class="fas fa-calculator"></i> Thông tin thanh toán</h5>
                            <table class="table table-borderless">
                                <tr>
                                    <td><strong>Tổng tiền:</strong></td>
                                    <td class="text-end">
                                        ${order.total} VND
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>Giảm giá:</strong></td>
                                    <td class="text-end text-danger">
                                        -${order.discount} VND
                                    </td>
                                </tr>
                                <tr class="table-active">
                                    <td><strong>Tổng cộng:</strong></td>
                                    <td class="text-end text-success fw-bold fs-5">
                                        ${order.grandTotal} VND
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                
                <!-- Customer Information -->
                <div class="order-info">
                    <h5><i class="fas fa-user"></i> Thông tin khách hàng</h5>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Mã khách hàng:</strong> ${order.customerId}</p>
                            <p><strong>Mã nhân viên:</strong> ${order.cashierUserId}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Ghi chú:</strong> ${order.note != null ? order.note : 'Không có'}</p>
                        </div>
                    </div>
                </div>
                
                <!-- Order Details -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-list"></i> Chi tiết đơn hàng 
                            <span class="badge bg-primary">${orderDetails.size()}</span>
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty orderDetails}">
                                <div class="text-center text-muted py-4">
                                    <i class="fas fa-inbox fa-2x mb-2"></i><br>
                                    Không có chi tiết đơn hàng
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="detail" items="${orderDetails}">
                                    <div class="book-item">
                                        <div class="row align-items-center">
                                            <div class="col-md-2">
                                                <img src="img/img_book/default-book.png" alt="Book Cover" class="book-cover">
                                            </div>
                                            <div class="col-md-4">
                                                <c:choose>
                                                    <c:when test="${detail.book != null}">
                                                        <h6 class="mb-1">${detail.book.title != null ? detail.book.title : 'No Title'}</h6>
                                                                                                <small class="text-muted">Mã sách: ${detail.book.code != null ? detail.book.code : 'N/A'}</small>
                                    </c:when>
                                    <c:otherwise>
                                        <h6 class="mb-1 text-muted">Thông tin sách không có</h6>
                                        <small class="text-muted">Mã sách: N/A</small>
                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="col-md-2 text-center">
                                                <span class="badge bg-secondary">Số lượng: ${detail.quantity}</span>
                                            </div>
                                            <div class="col-md-2 text-center">
                                                <span class="text-muted">Đơn giá:</span><br>
                                                <strong>${detail.unitPrice} VND</strong>
                                            </div>
                                            <div class="col-md-2 text-end">
                                                <span class="text-success fw-bold">
                                                    ${detail.lineTotal} VND
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
