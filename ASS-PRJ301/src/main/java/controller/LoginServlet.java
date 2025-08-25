package controller;

import dal.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
import org.mindrot.jbcrypt.BCrypt; // Import thư viện BCrypt

import java.io.IOException;
import java.util.Optional;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        Optional<Users> userOptional = userDao.findByUsername(username);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            // So sánh password nhập vào với passwordHash trong DB
            // và kiểm tra vai trò
            if (BCrypt.checkpw(password, user.getPasswordHash()) && user.getRole().equals(role)) {
                // Đăng nhập thành công
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user);
                session.setAttribute("userRole", user.getRole());

                String nextUrl = request.getParameter("next");
                if (nextUrl == null || nextUrl.trim().isEmpty()) {
                    nextUrl = "/dashboard.jsp";
                }

                response.sendRedirect(request.getContextPath() + nextUrl);
                return;
            }
        }

        // Đăng nhập thất bại
        request.setAttribute("error", "Tên đăng nhập, mật khẩu hoặc vai trò không chính xác.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
