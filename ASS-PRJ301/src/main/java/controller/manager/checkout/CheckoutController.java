/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.checkout;

import dal.BookDAO;
import dal.CustomerDAO;
import dal.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.CartItem;
import model.Customer;
import model.Order;
import model.OrderItemInput;
import model.CartItem;
import model.Users;

/**
 *
 * @author Nguyen Van Manh
 */
public class CheckoutController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CheckoutController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    // ==== Helpers ====

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private CartItem findItem(List<CartItem> cart, String code) {
        if (code == null) {
            return null;
        }
        for (CartItem o : cart) {
            if (code.equalsIgnoreCase(o.getCode())) {
                return o;
            }
        }
        return null;
    }

    private BigDecimal subtotal(List<CartItem> cart) {
        BigDecimal s = BigDecimal.ZERO;
        for (CartItem o : cart) {
            s = s.add(o.getLineTotal());
        }
        return s;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String key = request.getParameter("key");
        String type = request.getParameter("type"); // "code" | "title"
        HttpSession session = request.getSession();
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Users currentUser = (Users) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        Long userId = currentUser.getId();       // hoặc getUserId() tuỳ field bạn đặt
        String role = currentUser.getRole();
// Kiểm tra quyền
        if (!"MANAGER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }
// nếu tới đây tức là Manager và có userId
        System.out.println("Đăng nhập bởi userId = " + userId);
        // ✅ lấy giỏ theo CartItem, key "cart"
        List<CartItem> cart = getCart(session);
        // ✅ Lấy customer đã chọn từ session
        Customer selectedCustomer = (Customer) session.getAttribute("selectedCustomer");
        request.setAttribute("selectedCustomer", selectedCustomer);
        List<Customer> results = (List<Customer>) session.getAttribute("results");
        request.setAttribute("results", results);
        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            request.setAttribute("msg", msg);
            session.removeAttribute("msg"); // đọc xong xoá để không lặp
        }
        // (tùy chọn) tính tổng
        BigDecimal subtotal = subtotal(cart);
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal grand = subtotal.subtract(discount);

        // dữ liệu gợi ý bên trái
        OrderDAO dao = new OrderDAO();
        BookDAO bdao = new BookDAO();
        List<Book> listBook;

        if (key != null && !key.isBlank()) {
            if ("code".equalsIgnoreCase(type)) {
                Book b = dao.findByCode(key);
                listBook = new ArrayList<>();
                if (b != null) {
                    listBook.add(b);
                }
            } else {
                listBook = dao.searchByTitle(key);
            }
        } else {
            listBook = bdao.getAllBook();
        }

        // ✅ attributes cho JSP
        request.setAttribute("listBook", listBook);
        request.setAttribute("cart", cart);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("discount", discount);
        request.setAttribute("grandTotal", grand);
        request.setAttribute("key", key);
        request.setAttribute("type", type);

        request.getRequestDispatcher("pos.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String phone = request.getParameter("phone");
        String name = request.getParameter("name");
        String action = request.getParameter("action"); // add|inc|dec|remove|clear
        String code = request.getParameter("id");
        String payment = request.getParameter("paymentMethod");
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String userRole = currentUser.getRole();
        if (!"Manager".equalsIgnoreCase(userRole) && !"admin".equalsIgnoreCase(userRole)) {

            request.setAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
            request.getRequestDispatcher("/access-denied.jsp").forward(request, response);
            return;
        }
        Long userId = currentUser.getId();
        OrderDAO dao = new OrderDAO();
        CustomerDAO cus = new CustomerDAO();
        List<CartItem> cart = getCart(session);
        CartItem item = findItem(cart, code); //tim san pham xem có trong giỏ hàng không
        switch (action) {
            case "add": {
                Book b = dao.findByCode(code);
                if (b != null) {
                    if (item == null) {
                        cart.add(new CartItem(
                                b.getId(), b.getCode(), b.getTitle(),
                                BigDecimal.valueOf(b.getPrice()), 1
                        ));
                    } else {
                        item.setQty(item.getQty() + 1);
                    }
                }
                break;
            }
            case "inc": {
                if (item != null) {
                    item.setQty(item.getQty() + 1);
                }
                break;
            }
            case "dec": {
                if (item != null) {
                    int q = item.getQty() - 1;
                    if (q <= 0) {
                        cart.remove(item);
                    } else {
                        item.setQty(q);
                    }
                }
                break;
            }
            case "remove": {
                if (item != null) {
                    cart.remove(item);
                }
                break;
            }
            case "clear": {
                cart.clear();
                break;
            }
            case "findCustom": {
                // Xoá msg cũ nếu có
                session.removeAttribute("msg");

                if (phone != null && !phone.isBlank()) {
                    Customer c = cus.findByPhone(phone.trim());
                    if (c != null) {
                        session.setAttribute("selectedCustomer", c);
                        session.removeAttribute("results"); // clear list cũ
                        break; // ✅ đã tìm thấy -> thoát
                    } else {
                        session.setAttribute("msg", "Không tìm thấy khách hàng với số: " + phone);
                    }
                }

                // Nếu không có phone hoặc không tìm thấy -> thử tìm theo tên
                List<Customer> results = new ArrayList<>();
                if (name != null && !name.isBlank()) {
                    results = cus.findByName(name.trim()); // trả về list
                }
                session.setAttribute("results", results);

                // Nếu chỉ có 1 kết quả -> auto chọn
                if (results.size() == 1) {
                    session.setAttribute("selectedCustomer", results.get(0));
                    session.removeAttribute("results");
                } else {
                    // nhiều hoặc 0 kết quả -> để JSP hiển thị list
                    session.removeAttribute("selectedCustomer"); // tuỳ bạn: có thể giữ KH cũ
                }
                break;
            }

            case "pickCustom": { // chọn 1 KH từ danh sách
                String cid = request.getParameter("customerId");
                if (cid != null && !cid.isBlank()) {
                    try {
                        Long id = Long.valueOf(cid);
                        Customer chosen = cus.findById(id).orElse(null);
                        if (chosen != null) {
                            session.setAttribute("selectedCustomer", chosen);
                            session.removeAttribute("results");
                            session.removeAttribute("msg");
                        }
                    } catch (NumberFormatException ignore) {
                    }
                }
                break;
            }
            case "clearCustom": {
                // Bỏ chọn khách hàng hiện tại
                session.removeAttribute("selectedCustomer");
                // (tuỳ chọn) cũng xoá danh sách kết quả tìm trước đó
                // session.removeAttribute("results");
                // (tuỳ chọn) đặt thông điệp
                session.setAttribute("msg", "Đã bỏ chọn khách hàng.");
                break;
            }
            case "pay": {
                if (cart.isEmpty()) {
                    response.sendRedirect("checkout?msg=empty");
                    return;
                }
                // ✅ Lấy customer đã chọn từ session
                Customer selected = (Customer) session.getAttribute("selectedCustomer");
                Long customerId = null;
                if (selected != null) {
                    customerId = selected.getId();
                }
                // Chuẩn bị Order
                Order o = new Order();
                o.setOrderCode("ORD-" + System.currentTimeMillis());
                if ("CASH".equals(payment)) {
                    o.setPaymentMethod("CASH");
                } else {
                    o.setPaymentMethod("CARD");
                }
                o.setStatus("PAID");
                o.setPaymentStatus("PAID");
                o.setCashierUserId(userId);   // sửa theo thực tế
                o.setCustomerId(customerId);      // sửa theo thực tế
                o.setDiscount(0f);        // hoặc lấy từ mã KM đã áp dụng

                // Map cart -> items cho DAO
                List<OrderItemInput> items = new java.util.ArrayList<>();
                for (CartItem ci : cart) {
                    items.add(new model.OrderItemInput(
                            ci.getBookId(),
                            ci.getQty(),
                            ci.getUnitPrice() // có thể null nếu muốn dùng giá DB
                    ));
                }
                try {
                    Order saved = dao.checkout(o, items);

                    // Clear cart + chuyển sang hóa đơn
                    session.removeAttribute("cart");
                    response.sendRedirect("invoice?id=" + saved.getId());
                    return;
                } catch (RuntimeException ex) {
                    // Trường hợp hết hàng / race-condition / lỗi DB
                    ex.printStackTrace();
                    response.sendRedirect("checkout?msg=pay_error");
                    return;
                }
            }
            default:
                break;
        }

        // lưu lại (nếu cần có thể set subtotal/discount vào session)
        session.setAttribute("cart", cart);

        // PRG
        response.sendRedirect("checkout");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
