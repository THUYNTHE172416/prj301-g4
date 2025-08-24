package controller;

import dal.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;
import model.OrderDetail;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class OrderServlet extends HttpServlet {
    
    private OrderDAO orderDAO;
    
    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());
        
        switch (path) {
            case "/orders":
                showOrderList(request, response);
                break;
            case "/orders-test":
                showOrderTest(request, response);
                break;
            case "/order-detail":
                showOrderDetail(request, response);
                break;
            case "/order-search":
                searchOrders(request, response);
                break;
            case "/order-stats":
                showOrderStats(request, response);
                break;
            default:
                response.sendRedirect("orders");
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void showOrderTest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            request.getRequestDispatcher("orders-test.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    
    private void showOrderList(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            List<Order> orders = orderDAO.getAllOrders();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tải danh sách đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        }
    }
    
    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            String orderIdStr = request.getParameter("id");
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                response.sendRedirect("orders");
                return;
            }
            
            Long orderId = Long.parseLong(orderIdStr);
            Order order = orderDAO.findOrderById(orderId);
            
            if (order == null) {
                request.setAttribute("error", "Không tìm thấy đơn hàng");
                request.getRequestDispatcher("orders.jsp").forward(request, response);
                return;
            }
            
            List<OrderDetail> orderDetails = orderDAO.findOrderDetails(orderId);
            
            request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetails);
            request.getRequestDispatcher("order-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("orders");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tải chi tiết đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        }
    }
    
    private void searchOrders(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            String status = request.getParameter("status");
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            
            List<Order> orders = orderDAO.searchOrders(keyword, status, dateFrom, dateTo);
            request.setAttribute("orders", orders);
            request.setAttribute("searchKeyword", keyword);
            request.setAttribute("searchStatus", status);
            request.setAttribute("searchDateFrom", dateFrom);
            request.setAttribute("searchDateTo", dateTo);
            
            request.getRequestDispatcher("orders.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tìm kiếm đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        }
    }
    
    private void showOrderStats(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            String dateStr = request.getParameter("date");
            LocalDate date;
            
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                date = LocalDate.parse(dateStr);
            } else {
                date = LocalDate.now();
            }
            
            int totalOrders = orderDAO.getTotalOrdersByDate(date);
            int totalBooksSold = orderDAO.getTotalBooksSoldByDate(date);
            double totalRevenue = orderDAO.getTotalRevenueByDate(date);
            
            // Chuyển LocalDate thành Date để JSP có thể format
            Date statsDate = java.sql.Date.valueOf(date);
            request.setAttribute("statsDate", statsDate);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalBooksSold", totalBooksSold);
            request.setAttribute("totalRevenue", totalRevenue);
            
            request.getRequestDispatcher("order-stats.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tải thống kê: " + e.getMessage());
            request.getRequestDispatcher("order-stats.jsp").forward(request, response);
        }
    }
}
