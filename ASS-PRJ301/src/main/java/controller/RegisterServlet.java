package controller;

import dal.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/auth/register")
public class RegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm");
        String role = request.getParameter("role");
        
        // Kiểm tra mật khẩu
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu nhập lại không khớp.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (userDao.findByUsername(username).isPresent()) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng Users mới
        Users newUser = new Users();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        
        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        newUser.setPasswordHash(hashedPassword);
        
        newUser.setRole(role);
        newUser.setActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        // SỬA: Thêm dòng này để thiết lập giá trị cho UpdatedAt
        newUser.setUpdatedAt(LocalDateTime.now()); 
        
        userDao.save(newUser);

        // Tự động đăng nhập người dùng vừa tạo
        HttpSession session = request.getSession();
        session.setAttribute("currentUser", newUser);
        session.setAttribute("userRole", newUser.getRole());
        session.setAttribute("message", "Tài khoản của bạn đã được tạo thành công!");
        
        String nextUrl = request.getParameter("next");
        if (nextUrl == null || nextUrl.trim().isEmpty()) {
            nextUrl = "/dashboard.jsp";
        }
        
        response.sendRedirect(request.getContextPath() + nextUrl);
    }
}