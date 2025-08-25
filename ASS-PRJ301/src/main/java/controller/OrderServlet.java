package controller;

import dal.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.persistence.EntityManager;
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
            case "/orders-simple":
                showOrderSimple(request, response);
                break;
            case "/orders-test":
                showOrderTest(request, response);
                break;
            case "/test":
                showTest(request, response);
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
    
    private void showOrderSimple(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            List<Order> orders = orderDAO.getAllOrders();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("orders-simple.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
    
    private void showTest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            request.getRequestDispatcher("test.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
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
        // Check authentication
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Test database connection trước
            System.out.println("DEBUG: Testing database connection...");
            try {
                EntityManager testEm = orderDAO.getEntityManagerFactory().createEntityManager();
                System.out.println("DEBUG: EntityManager created successfully");
                
                // Test query đơn giản
                Long orderCount = testEm.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
                System.out.println("DEBUG: Total orders in database: " + orderCount);
                
                if (orderCount > 0) {
                    // Test lấy order đầu tiên
                    Order firstOrder = testEm.createQuery("SELECT o FROM Order o ORDER BY o.id", Order.class)
                                           .setMaxResults(1).getSingleResult();
                    System.out.println("DEBUG: First order ID: " + firstOrder.getId());
                    System.out.println("DEBUG: First order Date: " + firstOrder.getOrderDate());
                }
                
                testEm.close();
            } catch (Exception dbEx) {
                System.err.println("DEBUG: Database test failed: " + dbEx.getMessage());
                dbEx.printStackTrace();
            }
            
            List<Order> orders = orderDAO.getAllOrders();
            System.out.println("DEBUG: Số lượng orders: " + (orders != null ? orders.size() : "null"));
            if (orders != null && !orders.isEmpty()) {
                System.out.println("DEBUG: Order đầu tiên: " + orders.get(0));
                System.out.println("DEBUG: OrderDate đầu tiên: " + orders.get(0).getOrderDate());
            }
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("ERROR in showOrderList: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("orders.jsp").forward(request, response);
        }
    }
    
    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Check authentication
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
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
        // Check authentication
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
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
        // Check authentication
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
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
    
    // Authentication methods
    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        
        Object currentUser = session.getAttribute("currentUser");
        return currentUser != null;
    }
    
    private boolean checkRole(HttpServletRequest request, String... allowedRoles) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null) {
            return false;
        }
        
        for (String role : allowedRoles) {
            if (userRole.equals(role)) {
                return true;
            }
        }
        return false;
    }
}
