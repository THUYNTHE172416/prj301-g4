package controller;

import dal.ReportDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Users;


public class ReportServlet extends HttpServlet {

    private final ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        // 1. Kiểm tra trạng thái đăng nhập
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String userRole = currentUser.getRole();
        if (!"manager".equalsIgnoreCase(userRole) && !"admin".equalsIgnoreCase(userRole)) {
            // Chuyển hướng hoặc hiển thị thông báo lỗi
            request.setAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
            request.getRequestDispatcher("/access-denied.jsp").forward(request, response);
            return;
        }
        // Nếu người dùng đã đăng nhập và có quyền, thực hiện logic lấy dữ liệu
        try {
            request.setAttribute("topSellingBooks", reportDAO.getTopSellingBooks());
            request.setAttribute("dailyRevenues", reportDAO.getDailyRevenues());
            request.setAttribute("salesByEmployeeAndBook", reportDAO.getSalesByEmployeeAndBook());
            // Thêm dòng này để lấy dữ liệu doanh thu theo tháng
            request.setAttribute("monthlyRevenues", reportDAO.getMonthlyRevenues());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi tải báo cáo.");
        }

        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }
}