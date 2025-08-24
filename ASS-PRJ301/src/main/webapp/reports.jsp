<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    request.setAttribute("pageTitle", "Báo cáo");
    request.setAttribute("active", "reports");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">📊 Báo cáo</h2>

<div class="card mt-4">
    <div class="card-header">Sách bán chạy</div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table mb-0">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Tên sách</th>
                        <th>Đã bán</th>
                        <th>Doanh thu</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty topSellingBooks}">
                            <c:forEach var="book" items="${topSellingBooks}" varStatus="loop">
                                <tr>
                                    <td><c:out value="${loop.index + 1}"/></td>
                                    <td><c:out value="${book.title}"/></td>
                                    <td><c:out value="${book.quantity}"/></td>
                                    <td><fmt:formatNumber value="${book.lineTotal}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="text-center text-muted">Không có dữ liệu.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="card mt-4">
    <div class="card-header">Doanh thu theo ngày</div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <c:choose>
                <c:when test="${not empty dailyRevenues}">
                    <table class="table mb-0">
                        <thead>
                            <tr>
                                <th>Ngày</th>
                                <th>Doanh thu</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="revenue" items="${dailyRevenues}">
                                <tr>
                                    <td><c:out value="${revenue[0]}"/></td>
                                    <td><fmt:formatNumber value="${revenue[1]}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="text-muted text-center p-3">Không có dữ liệu.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="card mt-4">
    <div class="card-header">Doanh thu theo tháng</div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <c:choose>
                <c:when test="${not empty monthlyRevenues}">
                    <table class="table mb-0">
                        <thead>
                            <tr>
                                <th>Tháng</th>
                                <th>Doanh thu</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="revenue" items="${monthlyRevenues}">
                                <tr>
                                    <td><c:out value="${revenue[0]}"/></td>
                                    <td><fmt:formatNumber value="${revenue[1]}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="text-muted text-center p-3">Không có dữ liệu.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="card mt-4">
    <div class="card-header">Doanh số bán hàng theo nhân viên và sách</div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table mb-0">
                <thead>
                    <tr>
                        <th>Tên nhân viên</th>
                        <th>Tên sách</th>
                        <th>Số lượng đã bán</th>
                        <th>Doanh thu</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty salesByEmployeeAndBook}">
                            <c:forEach var="sale" items="${salesByEmployeeAndBook}">
                                <tr>
                                    <td><c:out value="${sale[0]}"/></td>
                                    <td><c:out value="${sale[1]}"/></td>
                                    <td><c:out value="${sale[2]}"/></td>
                                    <td><fmt:formatNumber value="${sale[3]}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="text-center text-muted">Không có dữ liệu.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<footer class="bg-dark text-white-50 py-3 mt-4">
    <div class="container small text-center">© 2025 BookStore</div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>