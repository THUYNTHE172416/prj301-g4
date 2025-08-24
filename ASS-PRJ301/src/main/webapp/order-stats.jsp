<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thống kê đơn hàng</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .stats-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
            text-align: center;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .stats-card h3 {
            font-size: 2.5rem;
            margin-bottom: 10px;
        }
        .stats-card .icon {
            font-size: 3rem;
            margin-bottom: 15px;
            opacity: 0.8;
        }
        .date-picker {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .chart-container {
            background: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .metric-item {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            border-left: 4px solid #007bff;
        }
        .metric-value {
            font-size: 1.5rem;
            font-weight: bold;
            color: #007bff;
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
                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h2><i class="fas fa-chart-bar"></i> Thống kê đơn hàng</h2>
                <a href="orders.jsp" class="btn btn-outline-primary">
                    <i class="fas fa-shopping-cart"></i> Quay lại đơn hàng
                </a>
                </div>
                
                <!-- Date Picker -->
                <div class="date-picker">
                    <form method="GET" action="order-stats" class="row g-3 align-items-end">
                        <div class="col-md-4">
                            <label class="form-label">Chọn ngày thống kê</label>
                            <input type="date" class="form-control" name="date" 
                                   value="${statsDate}" onchange="this.form.submit()">
                        </div>
                        <div class="col-md-4">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search"></i> Xem thống kê
                            </button>
                            <a href="order-stats" class="btn btn-secondary">
                                <i class="fas fa-calendar-day"></i> Hôm nay
                            </a>
                        </div>
                        <div class="col-md-4 text-end">
                            <h5 class="mb-0">
                                <i class="fas fa-calendar"></i> 
                                Ngày: <fmt:formatDate value="${statsDate}" pattern="dd/MM/yyyy"/>
                            </h5>
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
                
                <!-- Statistics Cards -->
                <div class="row">
                    <div class="col-md-4">
                        <div class="stats-card">
                            <div class="icon">
                                <i class="fas fa-shopping-cart"></i>
                            </div>
                            <h3>${totalOrders}</h3>
                            <p class="mb-0">Tổng đơn hàng</p>
                        </div>
                    </div>
                    
                    <div class="col-md-4">
                        <div class="stats-card">
                            <div class="icon">
                                <i class="fas fa-book"></i>
                            </div>
                            <h3>${totalBooksSold}</h3>
                            <p class="mb-0">Sách đã bán</p>
                        </div>
                    </div>
                    
                    <div class="col-md-4">
                        <div class="stats-card">
                            <div class="icon">
                                <i class="fas fa-money-bill-wave"></i>
                            </div>
                            <h3><fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></h3>
                            <p class="mb-0">Doanh thu (VND)</p>
                        </div>
                    </div>
                </div>
                
                <!-- Detailed Metrics -->
                <div class="row">
                    <div class="col-md-6">
                        <div class="chart-container">
                            <h5><i class="fas fa-chart-pie"></i> Phân tích đơn hàng</h5>
                            <div class="metric-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span><i class="fas fa-shopping-cart text-primary"></i> Tổng đơn hàng</span>
                                    <span class="metric-value">${totalOrders}</span>
                                </div>
                            </div>
                            <div class="metric-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span><i class="fas fa-book text-success"></i> Sách đã bán</span>
                                    <span class="metric-value">${totalBooksSold}</span>
                                </div>
                            </div>
                            <div class="metric-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span><i class="fas fa-calculator text-info"></i> Trung bình/đơn hàng</span>
                                    <span class="metric-value">
                                        <c:choose>
                                            <c:when test="${totalOrders > 0}">
                                                <fmt:formatNumber value="${totalBooksSold / totalOrders}" pattern="#.##"/>
                                            </c:when>
                                            <c:otherwise>0</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="chart-container">
                            <h5><i class="fas fa-chart-line"></i> Phân tích doanh thu</h5>
                            <div class="metric-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span><i class="fas fa-money-bill-wave text-success"></i> Tổng doanh thu</span>
                                    <span class="metric-value">
                                        <fmt:formatNumber value="${totalRevenue}" pattern="#,##0 VND"/>
                                    </span>
                                </div>
                            </div>
                            <div class="metric-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span><i class="fas fa-chart-line text-warning"></i> Trung bình/đơn hàng</span>
                                    <span class="metric-value">
                                        <c:choose>
                                            <c:when test="${totalOrders > 0}">
                                                <fmt:formatNumber value="${totalRevenue / totalOrders}" pattern="#,##0 VND"/>
                                            </c:when>
                                            <c:otherwise>0 VND</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                            <div class="metric-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span><i class="fas fa-percentage text-info"></i> Hiệu quả bán hàng</span>
                                    <span class="metric-value">
                                        <c:choose>
                                            <c:when test="${totalBooksSold > 0}">
                                                <fmt:formatNumber value="${(totalRevenue / totalBooksSold) / 1000}" pattern="#.##"/>
                                            </c:when>
                                            <c:otherwise>0</c:otherwise>
                                        </c:choose>
                                        K/đơn vị
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Summary -->
                <div class="chart-container">
                    <h5><i class="fas fa-clipboard-list"></i> Tóm tắt cho ngày ${statsDate}</h5>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle"></i>
                                <strong>Ngày ${statsDate}:</strong> 
                                <strong>${totalOrders}</strong> đơn hàng được tạo, 
                                <strong>${totalBooksSold}</strong> sách được bán, 
                                tổng doanh thu đạt <strong><fmt:formatNumber value="${totalRevenue}" pattern="#,##0 VND"/></strong>.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
