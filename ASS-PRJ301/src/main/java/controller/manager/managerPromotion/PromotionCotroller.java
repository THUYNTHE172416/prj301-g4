/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.manager.managerPromotion;

import dal.PromotionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Promotion;

/**
 *
 * @author Nguyen Van Manh
 */
public class PromotionCotroller extends HttpServlet {

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
            out.println("<title>Servlet PromotionCotroller</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PromotionCotroller at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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

        String action = request.getParameter("action");
        String sid = request.getParameter("id");
        String keyword = request.getParameter("keyword");

        PromotionDAO dao = new PromotionDAO();

        // parse id ngay tại chỗ (không dùng helper)
        Long id = null;
        if (sid != null && !sid.isBlank()) {
            try {
                id = Long.valueOf(sid);
            } catch (NumberFormatException ignored) {
            }
        }

        // Toggle Active
        if ("toggleActive".equals(action) && id != null) {
            var p = dao.getById(id);
            if (p != null) {
                dao.setActive(id, !Boolean.TRUE.equals(p.getActive()));
            }
            response.sendRedirect("promotion");
            return;
        }

        // Delete
        if ("delete".equals(action) && id != null) {
            dao.delete(id);
            response.sendRedirect("promotion");
            return;
        }

        // Edit (nếu có trang riêng thì forward; không thì set "p" để JSP dùng)
        if ("edit".equals(action) && id != null) {
            var p = dao.getById(id);
            if (p == null) {
                response.sendRedirect("promotion?msg=not_found");
                return;
            }
            request.setAttribute("p", p);
            // request.getRequestDispatcher("/promotion-edit.jsp").forward(request, response); return;
        }

        // Create (xử lý qua GET cho form modal method="get")
        if ("create".equals(action)) {
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            String type = request.getParameter("type");   // PERCENT | FIXED
            String sval = request.getParameter("value");  // double
            String sdt = request.getParameter("start");  // yyyy-MM-dd'T'HH:mm
            String edt = request.getParameter("end");    // yyyy-MM-dd'T'HH:mm
            boolean active = request.getParameter("active") != null;

            // validate tối thiểu ngay tại chỗ
            if (code == null || code.isBlank()
                    || name == null || name.isBlank()
                    || type == null || type.isBlank()
                    || sval == null || sval.isBlank()
                    || sdt == null || sdt.isBlank()
                    || edt == null || edt.isBlank()) {

                request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
                request.setAttribute("openCreate", true);
                var listErr = (keyword == null || keyword.isBlank()) ? dao.getAll() : dao.list(keyword, 200);
                request.setAttribute("getall", listErr);
                request.setAttribute("active", "promotion");
                request.getRequestDispatcher("/promotion.jsp").forward(request, response);
                return;
            }

            try {
                double value = Double.parseDouble(sval);
                var F = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

                var p = new model.Promotion();
                p.setCode(code.trim());
                p.setName(name.trim());
                p.setType(type.trim());
                p.setValue(value);
                p.setStartDate(java.time.LocalDateTime.parse(sdt, F));
                p.setEndDate(java.time.LocalDateTime.parse(edt, F));
                p.setActive(active);
                p.setCreatedAt(java.time.LocalDateTime.now());
                p.setUpdatedAt(java.time.LocalDateTime.now());

                dao.create(p);
                response.sendRedirect("promotion?msg=created");
                return;

            } catch (Exception ex) {
                request.setAttribute("error", "Tạo voucher thất bại (sai định dạng hoặc trùng mã).");
                request.setAttribute("openCreate", true);
                var listErr = (keyword == null || keyword.isBlank()) ? dao.getAll() : dao.list(keyword, 200);
                request.setAttribute("getall", listErr);
                request.setAttribute("active", "promotion");
                request.getRequestDispatcher("/promotion.jsp").forward(request, response);
                return;
            }
        }

        // Mặc định: list
        var list = (keyword == null || keyword.isBlank()) ? dao.getAll() : dao.list(keyword, 200);
        request.setAttribute("getall", list);
        request.setAttribute("active", "promotion");
        request.getRequestDispatcher("/promotion.jsp").forward(request, response);
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
        processRequest(request, response);
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
