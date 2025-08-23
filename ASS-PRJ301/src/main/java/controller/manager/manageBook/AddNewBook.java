package controller.manager.manageBook;

import dal.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Category;

@WebServlet(name="AddNewBook", urlPatterns={"/add-new-book"})
public class AddNewBook extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        CategoryDAO categoryDao = new CategoryDAO();
        List<Category> categoryList = new ArrayList<>();
        try {
            categoryList = categoryDao.getAllCategory();

            request.setAttribute("categoryList", categoryList);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "The system error");
        }
        request.getRequestDispatcher("add-book.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }
}
