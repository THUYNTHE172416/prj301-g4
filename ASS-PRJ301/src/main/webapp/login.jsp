
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("pageTitle", "Đăng nhập");
  // Không cần set active để menu không highlight mục nào trên trang login
%>
<%@ include file="view/header.jsp" %>

<div class="row justify-content-center">
  <div class="col-12 col-md-7 col-lg-5">
    <div class="card border-0 shadow-sm" style="border-radius: 14px;">
      <div class="card-body p-4 p-md-5">
        <div class="d-flex align-items-center mb-3">
          <div class="me-2" style="width:38px;height:38px;border-radius:10px;display:grid;place-items:center;background:linear-gradient(135deg,#4f46e5,#06b6d4);color:#fff;">📚</div>
          <h1 class="h5 mb-0">BookStore — Đăng nhập</h1>
        </div>

        <!-- Thông báo lỗi/thành công (tuỳ bạn set trong request) -->
        <c:if test="${not empty error}">
          <div class="alert alert-danger py-2 mb-3"><c:out value="${error}"/></div>
        </c:if>
        <c:if test="${not empty message}">
          <div class="alert alert-success py-2 mb-3"><c:out value="${message}"/></div>
        </c:if>

        <form method="post" action="<c:out value='${pageContext.request.contextPath}/auth/login'/>" novalidate>
          <input type="hidden" name="next" value="<c:out value='${empty param.next ? "/dashboard.jsp" : param.next}'/>"/>

          <div class="mb-3">
            <label for="username" class="form-label">Tên đăng nhập</label>
            <input type="text" class="form-control" id="username" name="username" placeholder="vd: staff01" required>
          </div>

          <div class="mb-3">
            <label for="password" class="form-label">Mật khẩu</label>
            <div class="input-group">
              <input type="password" class="form-control" id="password" name="password" placeholder="••••••••" required>
              <button class="btn btn-outline-secondary" type="button" id="togglePwd">Hiện</button>
            </div>
          </div>

          <div class="mb-3">
            <label for="role" class="form-label">Vai trò</label>
            <select id="role" name="role" class="form-select">
                  <option value="STAFF" selected>Nhân viên (STAFF)</option>
                <option value="MANAGER">Chủ cửa hàng (MANAGER)</option>
            </select>
          </div>
          <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
        </form>

      

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
  const toggle = document.getElementById('togglePwd');
  const pwd = document.getElementById('password');
  toggle?.addEventListener('click', () => {
    const isPw = pwd.type === 'password';
    pwd.type = isPw ? 'text' : 'password';
    toggle.textContent = isPw ? 'Ẩn' : 'Hiện';
  });
  // Prefill demo
  function prefill(u,p,r){
    document.getElementById('username').value = u;
    document.getElementById('password').value = p;
    document.getElementById('role').value = r;
  }
</script>
</body>
</html>