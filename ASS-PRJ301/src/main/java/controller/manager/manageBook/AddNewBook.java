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

@WebServlet(name = "AddNewBook", urlPatterns = {"/add-new-book"})
public class AddNewBook extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO categoryDao = new CategoryDAO();
        List<Category> categoryList = new ArrayList<>();
        AuthorDAO authorDAO = new AuthorDAO();
        List<Author> authorList = new ArrayList<>();
        PublisherDAO publisherDAO = new PublisherDAO();
        List<Publisher> publisherList = new ArrayList<>();
        try {
            categoryList = categoryDao.getAllCategory();
            authorList = authorDAO.getAllAuthor();
            publisherList = publisherDAO.getAllPublisher();
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("authorList", authorList);
            request.setAttribute("publisherList", publisherList);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "The system error");
        }
        request.getRequestDispatcher("add-book.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookDAO bookDao = new BookDAO();
        CategoryDAO categoryDao = new CategoryDAO();
        PublisherDAO publisherDAO = new PublisherDAO();

        Book book = new Book();
        StringBuilder error = new StringBuilder();

        try {
            // Lấy tham số
            String code = request.getParameter("code");
            String isbn = request.getParameter("isbn");
            String title = request.getParameter("title");
            String priceStr = request.getParameter("price");
            String stockQtyStr = request.getParameter("stockQty");
            String minStockStr = request.getParameter("minStock");
            String categoryIdStr = request.getParameter("categoryId");
            String publisherIdStr = request.getParameter("publisherId");
            String coverUrl = request.getParameter("coverUrl");
            String[] authors = request.getParameterValues("author");
            String description = request.getParameter("description");
            String status = request.getParameter("status");

            // Validate cơ bản (null/blank)
            if (code == null || code.isBlank()) {
                error.append("Mã sách không được để trống.<br>");
            }
            if (isbn == null || isbn.isBlank()) {
                error.append("ISBN không được để trống.<br>");
            }
            if (title == null || title.isBlank()) {
                error.append("Tên sách không được để trống.<br>");
            }
            if (priceStr == null || priceStr.isBlank()) {
                error.append("Giá sách không được để trống.<br>");
            }
            if (stockQtyStr == null || stockQtyStr.isBlank()) {
                error.append("Số lượng không được để trống.<br>");
            }
            if (minStockStr == null || minStockStr.isBlank()) {
                error.append("Tồn kho tối thiểu không được để trống.<br>");
            }
            if (categoryIdStr == null || categoryIdStr.isBlank()) {
                error.append("Danh mục không được để trống.<br>");
            }
            if (publisherIdStr == null || publisherIdStr.isBlank()) {
                error.append("Nhà xuất bản không được để trống.<br>");
            }
            if (authors == null || authors.length == 0) {
                error.append("Phải chọn ít nhất một tác giả.<br>");
            }
            if (status == null || status.isBlank()) {
                error.append("Trạng thái không được để trống.<br>");
            }

            // Parse số (nếu có giá trị)
            Float priceBook = null;
            Integer stockQtyBook = null;
            Integer minStockBook = null;
            Category category = null;
            Publisher publisher = null;

            try {
                if (priceStr != null && !priceStr.isBlank()) {
                    priceBook = Float.parseFloat(priceStr);
                    if (priceBook < 0) {
                        error.append("Giá sách phải >= 0.<br>");
                    }
                }
            } catch (NumberFormatException e) {
                error.append("Giá sách phải là số hợp lệ.<br>");
            }

            try {
                if (stockQtyStr != null && !stockQtyStr.isBlank()) {
                    stockQtyBook = Integer.parseInt(stockQtyStr);
                    if (stockQtyBook < 0) {
                        error.append("Số lượng phải >= 0.<br>");
                    }
                }
            } catch (NumberFormatException e) {
                error.append("Số lượng phải là số hợp lệ.<br>");
            }

            try {
                if (minStockStr != null && !minStockStr.isBlank()) {
                    minStockBook = Integer.parseInt(minStockStr);
                    if (minStockBook < 0) {
                        error.append("Tồn kho tối thiểu phải >= 0.<br>");
                    }
                }
            } catch (NumberFormatException e) {
                error.append("Tồn kho tối thiểu phải là số hợp lệ.<br>");
            }

            try {
                if (categoryIdStr != null && !categoryIdStr.isBlank()) {
                    category = categoryDao.getCategoryById(Integer.parseInt(categoryIdStr));
                    if (category == null) {
                        error.append("Danh mục không tồn tại.<br>");
                    }
                }
            } catch (NumberFormatException e) {
                error.append("ID danh mục không hợp lệ.<br>");
            }

            try {
                if (publisherIdStr != null && !publisherIdStr.isBlank()) {
                    publisher = publisherDAO.getPublisherById(Integer.parseInt(publisherIdStr));
                    if (publisher == null) {
                        error.append("Nhà xuất bản không tồn tại.<br>");
                    }
                }
            } catch (NumberFormatException e) {
                error.append("ID nhà xuất bản không hợp lệ.<br>");
            }

            // Nếu không có lỗi thì persist book
            if (error.length() == 0) {
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

                bookDao.addNewBook(book, authors);

                request.setAttribute("success", "Thêm một sách mới thành công!");
            } else {
                request.setAttribute("error", error.toString());
            }

        } catch (Exception e) {
            e.printStackTrace(); // hoặc logger.error(...)
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }

        doGet(request, response);
    }
}
