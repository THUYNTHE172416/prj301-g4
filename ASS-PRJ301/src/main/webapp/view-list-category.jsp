<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("pageTitle", "Quản lý thể loại");
    request.setAttribute("active", "categories");
%>
<%@ include file="view/header.jsp" %>

<h2 class="mb-4">📚 Danh sách thể loại</h2>

<!-- Nút mở modal Thêm -->
<div class="mb-3">
    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addModal">➕ Thêm thể loại</button>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr class="row">
                    <th class="col-md-1 text-center">ID</th>
                    <th class="col-md-2 text-center">Tên</th>
                    <th class="col-md-2 text-center">Slug</th>
                    <th class="col-md text-center">Mô tả</th>
                    <th class="col-md-2 text-center">Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cat" items="${data}">
                    <tr class="row">
                        <td class="col-md-1">${cat.id}</td>
                        <td class="col-md-2">${cat.name}</td>
                        <td class="col-md-2">${cat.slug}</td>
                        <td class="col-md">${cat.description}</td>
                        <td class="col-md-2 d-flex justify-content-between">
                            <button class="btn btn-sm btn-warning"
                                    data-bs-toggle="modal"
                                    data-bs-target="#editModal"
                                    data-id="${cat.id}"
                                    data-name="${cat.name}"
                                    data-slug="${cat.slug}"
                                    data-description="${cat.description}">
                                ✏ Sửa
                            </button>

                            <a href="/ass-g6/category?id=${cat.id}&mode=2" 
                               class="btn btn-sm btn-danger"
                               onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">
                                🗑 Delete
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Modal Thêm Category -->
<div class="modal fade" id="addModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/ass-g6/category/store" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">➕ Thêm thể loại mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Tên thể loại</label>
                        <input type="text" name="name" class="form-control" required/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Slug</label>
                        <input type="text" name="slug" class="form-control" required/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Mô tả</label>
                        <textarea name="description" rows="3" class="form-control"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-success">💾 Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal Sửa Category -->
<div class="modal fade" id="editModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/ass-g6/category/update" method="post">
                <div class="modal-header">
                    <h5 class="modal-title">✏ Chỉnh sửa thể loại</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" id="edit-id"/>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Tên thể loại</label>
                        <input type="text" name="name" id="edit-name" class="form-control" required/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Slug</label>
                        <input type="text" name="slug" id="edit-slug" class="form-control" required/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Mô tả</label>
                        <textarea name="description" id="edit-description" rows="3" class="form-control"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-warning">💾 Cập nhật</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Script điền dữ liệu vào modal Edit -->
<script>
    const editModal = document.getElementById('editModal');
    editModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const id = button.getAttribute('data-id');
        const name = button.getAttribute('data-name');
        const slug = button.getAttribute('data-slug');
        const description = button.getAttribute('data-description');

        document.getElementById('edit-id').value = id;
        document.getElementById('edit-name').value = name;
        document.getElementById('edit-slug').value = slug;
        document.getElementById('edit-description').value = description;
    });
</script>

<!-- SweetAlert2 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<c:if test="${not empty error}">
    <script>
    Swal.fire({
        icon: 'error',
        title: 'Lỗi',
        html: '${error}',
        confirmButtonText: 'OK'
    }).then(() => {
        window.location.href = '/ass-g6/category';
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
            window.location.href = '/ass-g6/category';
        });
    </script>
</c:if>
