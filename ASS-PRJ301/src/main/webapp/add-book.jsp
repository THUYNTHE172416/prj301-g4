<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("pageTitle", "Thêm sách mới");
    request.setAttribute("active", "books");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">➕ Thêm sách mới</h2>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="/ass-g6/add-new-book" method="post" >

            <!--code, isbn, title-->
            <div class="row mb-3">
                <div class="col-md">
                    <label class="form-label fw-bold">Mã sách</label>
                    <input type="text" name="code" class="form-control" placeholder="Nhập mã sách" required />
                </div>
                <div class="col-md">
                    <label class="form-label fw-bold">ISBN</label>
                    <input type="text" name="isbn" class="form-control" placeholder="Nhập ISBN"/>
                </div>
                <div class="col-md">
                    <label class="form-label fw-bold">Tên sách</label>
                    <input type="text" name="title" class="form-control" placeholder="Nhập tên sách" required />
                </div>
            </div>

            <!--price, stockQty and minStock-->
            <div class="row mb-3">
                <div class="col-md">
                    <label class="form-label fw-bold">Giá bán</label>
                    <input type="number" step="0.01" name="price" class="form-control" placeholder="Nhập giá" required />
                </div>
                <div class="col-md">
                    <label class="form-label fw-bold">Số lượng tồn kho</label>
                    <input type="number" name="stockQty" step="1" class="form-control" placeholder="Nhập số lượng tồn kho" required />
                </div>
                <div class="col-md">
                    <label class="form-label fw-bold">Số lượng tồn kho tối thiểu</label>
                    <input type="number" name="minStock" step="1" class="form-control" placeholder="Nhập số lượng tồn kho tối thiểu" required />
                </div>
            </div>

            <!--categoryId, publisherId, coverUrl-->
            <div class="row mb-3">
                <div class="col-md">
                    <label class="form-label fw-bold">Thể loại</label>
                    <select name="categoryId" class="form-select" required>
                        <option value="">-- Chọn thể loại --</option>
                        <c:forEach var="cat" items="${categoryList}">
                            <option value="${cat.id}">${cat.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md">
                    <label class="form-label fw-bold">Nhà xuất bản</label>
                    <select name="publisherId" class="form-select">
                        <option value="">-- Chọn NXB --</option>
                        <c:forEach var="pub" items="${publisherList}">
                            <option value="${pub.id}">${pub.name}</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="col-md">
                    <label class="form-label fw-bold">Ảnh bìa</label>
                    <input type="text" name="coverUrl"  class="form-control" placeholder="Nhập url hình ảnh"/>
                </div>
            </div>

            
            <!--description-->
            <div class="mb-3">
                <label class="form-label fw-bold">Mô tả</label>
                <textarea name="description" rows="4" class="form-control"></textarea>
            </div>

            <div class="d-flex justify-content-between">
                <a href="/ass-g6/management-book" class="btn btn-secondary">⬅ Quay lại</a>
                <button type="submit" class="btn btn-success">💾 Thêm sách</button>
            </div>
        </form>
    </div>
</div>

<!-- Footer -->
<footer class="bg-dark text-white-50 py-3 mt-4">
    <div class="container small text-center">
        © 2025 BookStore
    </div>
</footer>

<!-- SweetAlert2 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<c:if test="${not empty error}">
    <script>
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            html: '${error}',
            confirmButtonText: 'OK'
        });
    </script>
</c:if>
<c:if test="${not empty success}">
    <script>
        Swal.fire({
            icon: 'success',
            title: 'Thành công',
            text: '${success}',
            timer: 2000,
            showConfirmButton: false
        }).then(() => {
            window.location.href = '/ass-g6/management-book';
        });
    </script>
</c:if>
</body>
</html>
