<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Orders Simple Test</title>
</head>
<body>
    <h1>Orders Test Page</h1>
    
    <c:if test="${orders != null}">
        <p>Number of orders: ${orders.size()}</p>
        
        <c:forEach var="order" items="${orders}">
            <div>
                <strong>Order Code:</strong> ${order.orderCode}<br>
                <strong>Order Date:</strong> ${order.orderDate}<br>
                <strong>Status:</strong> ${order.status}<br>
                <hr>
            </div>
        </c:forEach>
    </c:if>
    
    <c:if test="${orders == null}">
        <p>No orders found</p>
    </c:if>
</body>
</html>
