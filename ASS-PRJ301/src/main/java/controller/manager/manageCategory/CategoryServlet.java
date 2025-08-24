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

@WebServlet(name = "CategoryServlet", urlPatterns = {"/category", "/category/store", "/category/update"})
public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> list = categoryDAO.getAllCategory();
        request.setAttribute("data", list);
        request.getRequestDispatcher("/view-list-category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                category.setCreatedAt(creDatedAt);
                category.setUpdatedAt(upDatedAt);
                
                categoryDAO.addNewCategory(category);
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
                
                categoryDAO.updateCategory(category);
            }
        } catch (Exception e) {
            // Xử lý lỗi: Có thể gửi thông báo lỗi về client
            request.setAttribute("error", "Đã có lỗi xảy ra: " + e.getMessage());
        }

        doGet(request, response);
    }
}
