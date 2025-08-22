<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
  request.setAttribute("pageTitle", "Tạo tài khoản");
  // Không set "active" để tránh highlight mục nào trên sidebar
%>
<%@ include file="view/header.jsp" %>

<div class="row justify-content-center">
  <div class="col-12 col-md-8 col-lg-6">
    <div class="card border-0 shadow-sm" style="border-radius:14px;">
      <div class="card-body p-4 p-md-5">
        <div class="d-flex align-items-center justify-content-between mb-3">
          <div class="d-flex align-items-center">
            <div class="me-2"
                 style="width:38px;height:38px;border-radius:10px;display:grid;place-items:center;
                        background:linear-gradient(135deg,#4f46e5,#06b6d4);color:#fff;">
              ✨
            </div>
            <h1 class="h5 mb-0">Tạo tài khoản mới</h1>
          </div>
          <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/login.jsp">
            Đã có tài khoản?
          </a>
        </div>

        <!-- Thông báo -->
        <c:if test="${not empty error}">
          <div class="alert alert-danger py-2 mb-3"><c:out value="${error}"/></div>
        </c:if>
        <c:if test="${not empty message}">
          <div class="alert alert-success py-2 mb-3"><c:out value="${message}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/auth/register" novalidate>
          <!-- Redirect sau đăng ký thành công (mặc định về dashboard.jsp) -->
          <input type="hidden" name="next" value="<c:out value='${empty param.next ? "/dashboard.jsp" : param.next}'/>" />
          <!-- CSRF nếu có -->
          <!-- <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/> -->

          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label" for="fullName">Họ và tên</label>
              <input type="text" class="form-control" id="fullName" name="fullName"
                     placeholder="VD: Nguyễn Văn A" required>
            </div>
            <div class="col-md-6">
              <label class="form-label" for="username">Tên đăng nhập</label>
              <input type="text" class="form-control" id="username" name="username"
                     placeholder="vd: staff01" required>
            </div>

            <div class="col-md-6">
              <label class="form-label" for="email">Email</label>
              <input type="email" class="form-control" id="email" name="email"
                     placeholder="you@example.com" required>
            </div>
            <div class="col-md-6">
              <label class="form-label" for="phone">Số điện thoại (tuỳ chọn)</label>
              <input type="tel" class="form-control" id="phone" name="phone" placeholder="09xxxxxxxx">
            </div>

            <div class="col-md-6">
              <label class="form-label" for="password">Mật khẩu</label>
              <div class="input-group">
                <input type="password" class="form-control" id="password" name="password" placeholder="••••••••" required>
                <button class="btn btn-outline-secondary" type="button" id="togglePwd">Hiện</button>
              </div>
              <div class="form-text">Tối thiểu 8 ký tự, nên có chữ hoa, số và ký tự đặc biệt.</div>
            </div>
            <div class="col-md-6">
              <label class="form-label" for="confirm">Nhập lại mật khẩu</label>
              <div class="input-group">
                <input type="password" class="form-control" id="confirm" name="confirm" placeholder="••••••••" required>
                <button class="btn btn-outline-secondary" type="button" id="toggleConfirm">Hiện</button>
              </div>
            </div>

            <div class="col-md-6">
              <label class="form-label" for="role">Vai trò</label>
              <select id="role" name="role" class="form-select">
                <option value="STAFF" selected>Nhân viên (STAFF)</option>
                <option value="OWNER">Chủ cửa hàng (OWNER)</option>
              </select>
            </div>
            <div class="col-md-6">
              <label class="form-label" for="displayName">Tên hiển thị (tuỳ chọn)</label>
              <input type="text" class="form-control" id="displayName" name="displayName" placeholder="Tên hiển thị trên hệ thống">
            </div>

            <div class="col-12">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" value="true" id="agree" name="agree" required>
                <label class="form-check-label" for="agree">
                  Tôi đồng ý với <a href="#" class="text-decoration-none">Điều khoản</a> & <a href="#" class="text-decoration-none">Chính sách</a>.
                </label>
              </div>
            </div>

            <div class="col-12 mt-2">
              <button type="submit" class="btn btn-primary w-100">Tạo tài khoản</button>
            </div>
          </div>
        </form>

        <!-- Gợi ý nhanh tài khoản demo (tuỳ chọn, có thể xoá) -->
        <div class="mt-4">
          <div class="small text-uppercase text-muted mb-2">Tạo nhanh tài khoản demo</div>
          <div class="d-flex gap-2 flex-wrap">
            <button class="btn btn-sm btn-outline-primary" type="button"
                    onclick="prefill('Nguyễn Chủ','owner','owner@demo.local','0900000000','OWNER')">OWNER</button>
            <button class="btn btn-sm btn-outline-secondary" type="button"
                    onclick="prefill('Lê Nhân Viên','staff01','staff01@demo.local','0911111111','STAFF')">STAFF</button>
          </div>
        </div>

      </div>
    </div>
  </div>
</div>

</div> <!-- đóng .content mở ở headerMenu.jsp -->

<footer class="bg-dark text-white-50 py-3 mt-4">
  <div class="container small text-center">© 2025 BookStore</div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
  // Hiện/ẩn mật khẩu
  function toggleField(btnId, inputId){
    const btn = document.getElementById(btnId);
    const input = document.getElementById(inputId);
    btn?.addEventListener('click', () => {
      const isPw = input.type === 'password';
      input.type = isPw ? 'text' : 'password';
      btn.textContent = isPw ? 'Ẩn' : 'Hiện';
    });
  }
  toggleField('togglePwd','password');
  toggleField('toggleConfirm','confirm');

  // Điền nhanh demo
  function prefill(fullname, username, email, phone, role){
    document.getElementById('fullName').value = fullname;
    document.getElementById('username').value = username;
    document.getElementById('email').value = email;
    document.getElementById('phone').value = phone;
    document.getElementById('role').value = role;
    document.getElementById('password').value = '12345678';
    document.getElementById('confirm').value  = '12345678';
  }
</script>
</body>
</html>
