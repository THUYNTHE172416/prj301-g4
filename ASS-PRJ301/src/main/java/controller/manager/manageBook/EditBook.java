/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.manageBook;

import dal.BookDAO;
import dal.CategoryDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.Category;

@WebServlet(name = "EditBook", urlPatterns = {"/edit-book"})
public class EditBook extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookDAO bookDao = new BookDAO();
        Book book = new Book();
        
        CategoryDAO categoryDao = new CategoryDAO();
        List<Category> categoryList = new ArrayList<>();

        String id = request.getParameter("id") == null
                ? (String) request.getAttribute("id")
                : request.getParameter("id");
        
        try {
            int bookId = Integer.parseInt(id);
            book = bookDao.getBookById(bookId);
            categoryList = categoryDao.getAllCategory();

            request.setAttribute("book", book);
            request.setAttribute("categoryList", categoryList);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "The system error");
        }
        request.setAttribute("id", id);
        request.getRequestDispatcher("edit-book.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookDAO bookDao = new BookDAO();
        CategoryDAO categoryDao = new CategoryDAO();

        Book book = new Book();
        try {
            String id = request.getParameter("bookId");
            Long bookId = Long.parseLong(id);

            String code = request.getParameter("code");
            String isbn = request.getParameter("isbn");
            String title = request.getParameter("title");
            
            String categoryId = request.getParameter("categoryId");
            Category category = categoryDao.getCategoryById(Integer.parseInt(categoryId));

            String price = request.getParameter("price");
            Float priceBook = Float.parseFloat(price);

            String stockQty = request.getParameter("stockQty");
            Integer stockQtyBook = Integer.parseInt(stockQty);

            String coverUrl = request.getParameter("coverUrl");
            String description = request.getParameter("description");
            String status = request.getParameter("status");
            
            book = bookDao.getBookById(bookId);
            book.setCode(code);
            book.setTitle(title);
            book.setIsbn(isbn);
            book.setPrice(priceBook);
            book.setStockQty(stockQtyBook);
            book.setMinStock(book.getMinStock());
            book.setCoverUrl(coverUrl);
            book.setDescription(description);
            book.setStatus(status);
            book.setVersion(book.getId() + 1);
            book.setCategory(category);

            bookDao.updateBook(book);
            
            request.setAttribute("sucess", "Thay đổi thành công");
            request.setAttribute("id", id);
        } catch (Exception e) {
            request.setAttribute("error", "The system error");
        }

        doGet(request, response);
    }
}
