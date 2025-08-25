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

@WebServlet(name="bookservice", urlPatterns = "/management-book")
public class BookController extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        BookDAO bookDAO = new BookDAO();
        List<Book> listBook = new ArrayList<>();
        listBook = bookDAO.getAllBook();
        
        //Delete
        String id = request.getParameter("id");
        String mode = request.getParameter("mode");
        
        if (id != null && mode != null && mode.equals("2")) {
            // change status
            try {
                int bookId = Integer.parseInt(id);
                bookDAO.deleteBook(bookId);
                
                request.setAttribute("success", "Xóa sản phẩm thành công");
            } catch (Exception e) {
                request.setAttribute("error", "Lỗi hệ thống không thể xóa sản phẩm");
            }
        }
        

        request.setAttribute("listBook", listBook);
        request.getRequestDispatcher("book.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String search = request.getParameter("search");
        BookDAO bookDAO = new BookDAO();
        
        List<Book> filter = new ArrayList<>();
        filter = bookDAO.getAllBookByKeyword(search);
        
        request.setAttribute("listBook", filter);
        request.setAttribute("search", search);
        request.getRequestDispatcher("book.jsp").forward(request, response);
    }
    
}
