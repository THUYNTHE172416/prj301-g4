package controller.manager.manageCategory;

import dal.CategoryDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import model.Category;
import model.Users;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/category", "/category/store", "/category/update"})
public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Users user = (Users) request.getSession().getAttribute("currentUser");
        if (user == null || user.getRole().equals("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        CategoryDAO categoryDAO = new CategoryDAO();

        //delete
        String id = request.getParameter("id");
        String mode = request.getParameter("mode");

        if (id != null && mode != null && mode.equals("2")) {
            try {
                if (categoryDAO.deleteCategory(Integer.parseInt(id))) {
                    request.setAttribute("success", "Xóa danh mục thành công");
                }
            } catch (Exception e) {
                request.setAttribute("error", "Lỗi hệ thống không thể xóa danh mục");
            }
        }

        List<Category> list = categoryDAO.getAllCategory();
        request.setAttribute("data", list);
        request.getRequestDispatcher("/view-list-category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Users user = (Users) request.getSession().getAttribute("currentUser");
        if (user == null || user.getRole().equals("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        String action = request.getServletPath();

        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            if (action.equals("/category/store")) {
                Category category = new Category();
                // Xử lý hành động "Thêm"
                String name = request.getParameter("name");
                String slug = request.getParameter("slug");
                String description = request.getParameter("description");
                LocalDateTime creDatedAt = LocalDateTime.now();
                LocalDateTime upDatedAt = LocalDateTime.now();

                category.setName(name);
                category.setSlug(slug);
                category.setDescription(description);
                category.setStatus("ACTIVE");
                category.setCreatedAt(creDatedAt);
                category.setUpdatedAt(upDatedAt);

                boolean success = categoryDAO.addNewCategory(category);
                if (success) {
                    request.setAttribute("success", "Thêm danh mục mới thành công");
                } else {
                    request.setAttribute("error", "Lỗi hệ thống không thể thêm danh mục mới do trùng tên danh mục hoặc slug");
                }
            } else if (action.equals("/category/update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                Category category = categoryDAO.getCategoryById(id);
                // Xử lý hành động "Sửa"
                String name = request.getParameter("name");
                String slug = request.getParameter("slug");
                String description = request.getParameter("description");
                LocalDateTime upDatedAt = LocalDateTime.now();

                category.setName(name);
                category.setSlug(slug);
                category.setDescription(description);
                category.setCreatedAt(upDatedAt);
                boolean success = categoryDAO.updateCategory(category);
                if (success) {
                    request.setAttribute("success", "Cập nhật danh mục mới thành công");
                } else {
                    request.setAttribute("error", "Lỗi hệ thống không thể cập nhật danh do trùng tên danh mục hoặc slug");
                }
            }
        } catch (Exception e) {
            // Xử lý lỗi: Có thể gửi thông báo lỗi về client
            request.setAttribute("error", "Đã có lỗi xảy ra: " + e.getMessage());
        }

        doGet(request, response);
    }
}
