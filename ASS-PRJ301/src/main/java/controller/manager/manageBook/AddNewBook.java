package controller.manager.manageBook;

import dal.AuthorDAO;
import dal.BookDAO;
import dal.CategoryDAO;
import dal.PublisherDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Author;
import model.Book;
import model.Category;
import model.Publisher;
import model.Users;

@WebServlet(name = "AddNewBook", urlPatterns = {"/add-new-book"})
public class AddNewBook extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Users user = (Users) request.getSession().getAttribute("currentUser");
        if (user == null || user.getRole().equals("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        CategoryDAO categoryDao = new CategoryDAO();
        List<Category> categoryList = new ArrayList<>();

        PublisherDAO publisherDAO = new PublisherDAO();
        List<Publisher> publisherList = new ArrayList<>();
        try {
            categoryList = categoryDao.getAllCategory();
            publisherList = publisherDAO.getAllPublisher();

            request.setAttribute("categoryList", categoryList);
            request.setAttribute("publisherList", publisherList);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "The system error");
        }
        request.getRequestDispatcher("add-book.jsp").forward(request, response);
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
        PublisherDAO publisherDAO = new PublisherDAO();

        String error = "";

        try {
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
            }

            // price 
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

            // min stock
            String minStock = request.getParameter("minStock");
            if (minStock == null || minStock.isBlank()) {
                error += "Tồn kho tối thiểu không được để trống.<br>";
            }
            Integer minStockBook = null;
            try {
                if (minStock != null && !minStock.isBlank()) {
                    minStockBook = Integer.parseInt(minStock);
                    if (minStockBook <= 0) {
                        error += "Tồn kho tối thiểu phải > 0.<br>";
                    }
                }
            } catch (NumberFormatException e) {
                error += "Tồn kho tối thiểu phải là số hợp lệ.<br>";
            }

            // kiem tra stock > min stock
            if (error.isBlank()) {
                if (stockQtyBook <= minStockBook) {
                    error += "Số lượng tồn kho phải lớn hơn số lượng tồn kho tối thiểu.<br>";
                }
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

            //publisher
            String publisherId = request.getParameter("publisherId");
            if (publisherId == null || publisherId.isBlank()) {
                error += "Nhà xuất bản không được để trống.<br>";
            }
            Publisher publisher = null;
            try {
                if (publisherId != null && !publisherId.isBlank()) {
                    publisher = publisherDAO.getPublisherById(Integer.parseInt(publisherId));
                    if (publisher == null) {
                        error += "Nhà xuất bản không tồn tại.<br>";
                    }
                }
            } catch (NumberFormatException e) {
                error += "ID nhà xuất bản không hợp lệ.<br>";
            }

            // image
            String coverUrl = request.getParameter("coverUrl");
            //description
            String description = request.getParameter("description");

            //status
            String status = "ACTIVE";

            // Nếu không có lỗi thì persist book
            if (error.isBlank()) {
                Book book = new Book();
                
                book.setCode(code);
                book.setTitle(title);
                book.setIsbn(isbn);
                book.setPrice(priceBook);
                book.setStockQty(stockQtyBook);
                book.setMinStock(minStockBook);
                book.setCoverUrl(coverUrl);
                book.setDescription(description);
                book.setStatus(status);
                book.setVersion(1L);
                book.setCategory(category);
                book.setPublisher(publisher);
                book.setCreatedAt(LocalDateTime.now());
                book.setUpdatedAt(LocalDateTime.now());

                if (bookDao.addNewBook(book)) {
                    request.setAttribute("success", "Thêm một sách mới thành công!");
                } else {
                    request.setAttribute("error", "Lỗi không thể thêm sách mới do trùng mã code hoặc isbn");
                }
            } else {
                request.setAttribute("error", error);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }

        doGet(request, response);
    }
}
