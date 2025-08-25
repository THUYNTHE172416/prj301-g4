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
import model.Users;

@WebServlet(name = "EditBook", urlPatterns = {"/edit-book"})
public class EditBook extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Users user = (Users) request.getSession().getAttribute("currentUser");
        if (user == null || user.getRole().equals("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
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
        
        Users user = (Users) request.getSession().getAttribute("currentUser");
        if (user == null || user.getRole().equals("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        BookDAO bookDao = new BookDAO();
        CategoryDAO categoryDao = new CategoryDAO();
        String error = "";
        Book book = new Book();
        try {
            String id = request.getParameter("bookId");
            Long bookId = Long.parseLong(id);

            // code
            String code = request.getParameter("code");
            if (code == null || code.isBlank()) {
                error += "Mã sách không được để trống.<br>";
            }

            // ma xuat ban
            String isbn = request.getParameter("isbn");
            if (isbn == null || isbn.isBlank()) {
                error += "ISBN không được để trống.<br>";
            }

            // book name
            String title = request.getParameter("title");
            if (title == null || title.isBlank()) {
                error += "Tên sách không được để trống.<br>";
                
                
            }// price 
            String price = request.getParameter("price");
            if (price == null || price.isBlank()) {
                error += "Giá sách không được để trống.<br>";
            }
            Float priceBook = null;
            try {
                if (price != null && !price.isBlank()) {
                    priceBook = Float.parseFloat(price);
                    if (priceBook <= 0) {
                        error += "Giá sách phải > 0.<br>";
                    }
                }
            } catch (NumberFormatException e) {
                error += "Giá sách phải là số hợp lệ.<br>";
            }

            
            // stock quantity
            String stockQty = request.getParameter("stockQty");
            if (stockQty == null || stockQty.isBlank()) {
                error += "Số lượng không được để trống.<br>";
            }
            Integer stockQtyBook = null;
            try {
                if (stockQty != null && !stockQty.isBlank()) {
                    stockQtyBook = Integer.parseInt(stockQty);
                    if (stockQtyBook <= 0) {
                        error += "Số lượng phải > 0.<br>";
                    }
                }
            } catch (NumberFormatException e) {
                error += "Số lượng phải là số hợp lệ.<br>";
            }

            

            // category
            String categoryId = request.getParameter("categoryId");
            if (categoryId == null || categoryId.isBlank()) {
                error += "Danh mục không được để trống.<br>";
            }
            Category category = null;
            try {
                if (categoryId != null && !categoryId.isBlank()) {
                    // lấy 1 đối tượng category theo id
                    category = categoryDao.getCategoryById(Integer.parseInt(categoryId));
                    if (category == null) {
                        error += "Danh mục không tồn tại.<br>";
                    }
                }
            } catch (NumberFormatException e) {
                error += "ID danh mục không hợp lệ.<br>";
            }

            String coverUrl = request.getParameter("coverUrl");
            String description = request.getParameter("description");
            
            
            //status
            String status = request.getParameter("status");
            if (status == null || status.isBlank()) {
                error += "Trạng thái không được để trống.<br>";
            }

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
            
            if (error.isBlank()) {
                if (bookDao.updateBook(book)) {
                    request.setAttribute("success", "Thay đổi thành công");
                } else {
                    request.setAttribute("error", "Thay đổi thất bại do trùng mã code hoặc mã isbn. <br>");
                }
            } else {
                request.setAttribute("error", error);
            }
            request.setAttribute("id", id);
        } catch (Exception e) {
            request.setAttribute("error", "The system error");
        }
        doGet(request, response);
    }
}
