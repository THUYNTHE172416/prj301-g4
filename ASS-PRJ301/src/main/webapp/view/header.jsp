<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${pageTitle != null ? pageTitle : 'BookStore'}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            overflow-x: hidden;
        }
        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            height: 100vh;
            width: 220px;
            background-color: #343a40;
            padding-top: 60px; /* để chừa chỗ cho header */
        }
        .sidebar a {
            color: #adb5bd;
            text-decoration: none;
            display: block;
            padding: 10px 15px;
        }
        .sidebar a:hover, .sidebar a.active {
            background-color: #495057;
            color: #fff;
        }
        .content {
            margin-left: 220px;
            padding: 20px;
        }
        .header {
            position: fixed;
            top: 0;
            left: 220px;
            right: 0;
            height: 60px;
            background-color: #212529;
            color: white;
            display: flex;
            align-items: center;
            padding: 0 20px;
            z-index: 1000;
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h5 class="text-white text-center">📚 BookStore</h5>
    <a href="${ctx}/dashboard.jsp" class="${active eq 'dashboard' ? 'active' : ''}">🏠 Tổng quan</a>
    <a href="${ctx}/management-book" class="${active eq 'books' ? 'active' : ''}">📖 Quản lý sách</a>
    <a href="${ctx}/checkout" class="${active eq 'pos' ? 'active' : ''}">💳 Bán hàng</a>
    <a href="${ctx}/orders.jsp" class="${active eq 'orders' ? 'active' : ''}">🧾 Đơn hàng</a>
    <a href="${ctx}/inventory.jsp" class="${active eq 'inventory' ? 'active' : ''}">📦 Tồn kho</a>
    <a href="${ctx}/reports.jsp" class="${active eq 'reports' ? 'active' : ''}">📊 Báo cáo</a>
   <a href="${ctx}/promotion" class="${active eq 'promotion' ? 'active' : ''}">🎟️ Promotion</a>

</div>

<!-- Header -->
<div class="header">
    <div class="flex-grow-1">
        <strong><c:out value="${pageTitle != null ? pageTitle : 'BookStore'}"/></strong>
    </div>
    <div class="btn-group" role="group">
    <a href="${ctx}/login.jsp" class="btn btn-sm btn-warning">Đăng nhập</a>
    <a href="${ctx}/register.jsp" class="btn btn-sm btn-outline-warning">Tạo tài khoản</a>
</div>

</div>

<!-- Nội dung chính -->
<div class="content">
