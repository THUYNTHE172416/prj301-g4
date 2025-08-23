<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("pageTitle", "Chỉnh sửa sách");
    request.setAttribute("active", "books");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">✏️ Chỉnh sửa sách</h2>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="/ass-g6/edit-book" method="post" enctype="multipart/form-data">
            <!-- Hidden ID -->
            <input type="hidden" name="id" value="${book.id}"/>

            <div class="row mb-3">
                <div class="col-md-6">
                    <label class="form-label">Mã sách</label>
                    <input type="text" name="code" value="${book.code}" class="form-control" readonly />
                </div>
                <div class="col-md-6">
                    <label class="form-label">ISBN</label>
                    <input type="text" name="isbn" value="${book.isbn}" class="form-control" />
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Tên sách</label>
                <input type="text" name="title" value="${book.title}" class="form-control" required />
            </div>

            <div class="row mb-3">
                <div class="col-md-6">
                    <label class="form-label">Giá bán</label>
                    <input type="number" name="price" step="0.01" value="${book.price}" class="form-control" required />
                </div>
                <div class="col-md-6">
                    <label class="form-label">Số lượng tồn</label>
                    <input type="number" name="stockQty" value="${book.stockQty}" class="form-control" required />
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-6">
                    <label class="form-label">Thể loại</label>
                    <select name="categoryId" class="form-select" required>
                        <c:forEach var="cat" items="${listCategory}">
                            <option value="${cat.id}" ${book.category.id == cat.id ? 'selected' : ''}>
                                ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Nhà xuất bản</label>
                    <select name="publisherId" class="form-select">
                        <c:forEach var="pub" items="${listPublisher}">
                            <option value="${pub.id}" ${book.publisher.id == pub.id ? 'selected' : ''}>
                                ${pub.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Ảnh bìa</label><br>
                <img src="${book.coverUrl}" alt="cover" style="height:100px;" class="mb-2 d-block"/>
                <input type="file" name="coverFile" class="form-control" />
            </div>

            <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea name="description" rows="4" class="form-control">${book.description}</textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">Trạng thái</label>
                <select name="status" class="form-select">
                    <option value="ACTIVE" ${book.status eq 'ACTIVE' ? 'selected' : ''}>Hoạt động</option>
                    <option value="INACTIVE" ${book.status eq 'INACTIVE' ? 'selected' : ''}>Ngừng kinh doanh</option>
                </select>
            </div>

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
            window.location.href = '/management-book';
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
