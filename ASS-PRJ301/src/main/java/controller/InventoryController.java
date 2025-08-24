package Controllers;

import dal.InventoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import model.Book;

@WebServlet(name = "InventoryController", urlPatterns = {"/inventory"})
public class InventoryController extends HttpServlet {

    private final InventoryDAO inventoryDao = new InventoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sess = request.getSession();

        // Lấy filter từ query hoặc fallback session (để không cần thêm hidden input trong JSP)
        String name = request.getParameter("name");
        String status = request.getParameter("status"); // "", "low", "ok"

        if (name == null) {
            name = (String) sess.getAttribute("inv_name");
        } else {
            sess.setAttribute("inv_name", name);
        }

        if (status == null) {
            status = (String) sess.getAttribute("inv_status");
        } else {
            sess.setAttribute("inv_status", status);
        }

        // Chuẩn hóa status hợp lệ
        if (status != null && !(status.isBlank()
                || "low".equalsIgnoreCase(status)
                || "ok".equalsIgnoreCase(status))) {
            status = "";
            sess.setAttribute("inv_status", "");
        }

        List<Book> books = inventoryDao.findBooks(name, status);

        request.setAttribute("name", name);
        request.setAttribute("status", status);
        request.setAttribute("books", books);

        request.setAttribute("recentMovements", inventoryDao.findRecentMovements(50));

        request.getRequestDispatcher("inventory.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession sess = request.getSession();

        // Ưu tiên đọc từ form; nếu không có thì lấy từ session để giữ filter sau POST
        String keepName = request.getParameter("keepName");
        if (keepName == null) keepName = (String) sess.getAttribute("inv_name");

        String keepStatus = request.getParameter("keepStatus");
        if (keepStatus == null) keepStatus = (String) sess.getAttribute("inv_status");

        StringBuilder keep = new StringBuilder();
        if (keepName != null && !keepName.isBlank()) {
            keep.append("?name=").append(URLEncoder.encode(keepName, StandardCharsets.UTF_8));
        }
        if (keepStatus != null && !keepStatus.isBlank()) {
            keep.append(keep.length() == 0 ? "?" : "&")
                .append("status=").append(keepStatus);
        }

        try {
            // 1) Nhập / Xuất từ bảng sách
            String op = request.getParameter("op"); // import | export (2 nút submit)
            if (op != null) {
                Long bookId = Long.valueOf(request.getParameter("bookId"));
                int qty = Math.max(1, Integer.parseInt(request.getParameter("qty")));

                if ("import".equalsIgnoreCase(op)) {
                    inventoryDao.addMovement(bookId, "IMPORT", qty, "Nhập thủ công +" + qty);
                    sess.setAttribute("success", "Đã nhập +" + qty + " vào kho.");
                } else if ("export".equalsIgnoreCase(op)) {
                    // (tuỳ chọn) chặn âm kho ở DAO trước khi trừ
                    inventoryDao.addMovement(bookId, "ADJUST", -qty, "Xuất/giảm -" + qty);
                    sess.setAttribute("success", "Đã xuất/giảm -" + qty + " khỏi kho.");
                }
            }

            // 2) Xóa movement ở “Biến động gần đây”
            String action = request.getParameter("action"); // delete
            if ("delete".equalsIgnoreCase(action)) {
                Long movementId = Long.valueOf(request.getParameter("id"));
                inventoryDao.deleteMovement(movementId);
                sess.setAttribute("success", "Đã xóa movement #" + movementId);
            }

        } catch (Exception e) {
            sess.setAttribute("error", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/inventory" + keep);
    }
}
