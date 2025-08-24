package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Lấy session hiện tại, không tạo mới nếu chưa có
        if (session != null) {
            session.invalidate(); // Hủy toàn bộ session
        }
        // SỬA LẠI DÒNG NÀY:
        // Chuyển hướng về trang dashboard.jsp
        response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
    }
}