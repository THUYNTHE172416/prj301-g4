<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("pageTitle", "Chỉnh sửa sách");
    request.setAttribute("active", "books");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">✏️ Chỉnh sửa sách</h2>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="/ass-g6/edit-book" method="post">
            <!-- Hidden ID -->
            <input type="hidden" name="bookId" value="${id}"/>

            <!--code and isbn-->
            <div class="row mb-3">
                <div class="col-md-6">
                    <label class="form-label fw-bold">Mã sách</label>
                    <input type="text" name="code" value="${book.code}" class="form-control" readonly />
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-bold">ISBN</label>
                    <input type="text" name="isbn" value="${book.isbn}" class="form-control" />
                </div>
            </div>

            <!--title and category-->
            <div class="row mb-3">
                <div class="col-md-6">
                    <label class="form-label fw-bold">Tên sách</label>
                    <input type="text" name="title" value="${book.title}" class="form-control" required />
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-bold">Thể loại</label>
                    <select name="categoryId" class="form-select" required>
                        <c:forEach var="cat" items="${categoryList}">
                            <option value="${cat.id}" ${book.category.id == cat.id ? 'selected' : ''}>
                                ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <!--price and stock quantity-->
            <div class="row mb-3">
                <div class="col-md-6">
                    <label class="form-label fw-bold">Giá bán</label>
                    <input type="number" name="price" step="0.01" value="${book.price}" class="form-control" required />
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-bold">Số lượng tồn kho</label>
                    <input type="number" name="stockQty" value="${book.stockQty}" class="form-control" required />
                </div>
            </div>
                
            <!--image-->
            <div class="mb-3">
                <label class="form-label fw-bold">Ảnh bìa</label><br>
                <img src="${book.coverUrl}" alt="${book.title}" style="height:100px;" class="mb-2 d-block"/>
                <input type="text" name="coverUrl" class="form-control" value="${book.coverUrl}" placeholder="Nhập link hình ảnh"/>
            </div>

            <!--description-->
            <div class="mb-3">
                <label class="form-label fw-bold">Mô tả</label>
                <textarea name="description" rows="4" class="form-control">${book.description}</textarea>
            </div>

            <!--status-->
            <div class="mb-3">
                <label class="form-label fw-bold">Trạng thái</label>
                <select name="status" class="form-select">
                    <option value="ACTIVE" ${book.status eq 'ACTIVE' ? 'selected' : ''}>Hoạt động</option>
                    <option value="INACTIVE" ${book.status eq 'INACTIVE' ? 'selected' : ''}>Ngừng kinh doanh</option>
                </select>
            </div>
            
            <!--back and save-->
            <div class="d-flex justify-content-between">
                <a href="/ass-g6/management-book" class="btn btn-secondary">⬅ Quay lại</a>
                <button type="submit" class="btn btn-primary">💾 Lưu thay đổi</button>
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
            text: '${error}',
            showConfirmButton: false,
            timer: 2000
        }).then(() => {
            window.location.href = '/ass-g6/management-book';
        });
    </script>
</c:if>

<c:if test="${not empty success}">
    <script>
        Swal.fire({
            icon: 'success',
            title: 'Thành công',
            text: '${success}',
            confirmButtonText: 'OK'
        });
    </script>
</c:if>
</body>
</html>
