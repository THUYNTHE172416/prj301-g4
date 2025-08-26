package controller.manager.manageBook;

import dal.BookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.Users;

@WebServlet(name = "bookservice", urlPatterns = "/management-book")
public class BookController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Users user = (Users) request.getSession().getAttribute("currentUser");
        if (user == null || user.getRole().equals("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        int page = 1;
        int pageSize = 5; // mặc định 5

        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            }
        } catch (Exception e) {
            page = 1;
            pageSize = 5;
        }

        BookDAO bookDAO = new BookDAO();

        // tổng số sách
        int totalBooks = bookDAO.getTotalBooks();
        // tổng số trang
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        // Delete
        String id = request.getParameter("id");
        String mode = request.getParameter("mode");
        if (id != null && mode != null && mode.equals("2")) {
            try {
                int bookId = Integer.parseInt(id);
                bookDAO.deleteBook(bookId);
                request.setAttribute("success", "Xóa sản phẩm thành công");
            } catch (Exception e) {
                request.setAttribute("error", "Lỗi hệ thống không thể xóa sản phẩm");
            }
            request.removeAttribute("id");
            request.removeAttribute("mode");
        }

        List<Book> listBook = bookDAO.getAllBook(page, pageSize);

        String[] cols = request.getParameterValues("col");
        if (cols == null) {
            cols = new String[]{"code", "cover", "category", "price", "stock"}; // mặc định tất cả
        }
        request.setAttribute("col", cols);

        request.setAttribute("pageSize", pageSize);
        request.setAttribute("listBook", listBook);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("book.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Users user = (Users) request.getSession().getAttribute("currentUser");
        if (user == null || user.getRole().equals("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String search = request.getParameter("search");
        BookDAO bookDAO = new BookDAO();

        List<Book> filter = new ArrayList<>();
        filter = bookDAO.getAllBookByKeyword(search);

        request.setAttribute("listBook", filter);
        request.setAttribute("search", search);
        request.getRequestDispatcher("book.jsp").forward(request, response);
    }

}
