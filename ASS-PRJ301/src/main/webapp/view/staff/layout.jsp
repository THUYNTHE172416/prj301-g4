<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Qu?n tr? h? th?ng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="dashboard.jsp">? BookStore Admin</a>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-2 bg-dark text-light vh-100 p-3">
            <h5>? Menu</h5>
            <ul class="nav flex-column">
                <li class="nav-item"><a href="book" class="nav-link text-light">? Qu?n lý sách</a></li>
                <li class="nav-item"><a href="category" class="nav-link text-light">?? Qu?n lý th? lo?i</a></li>
                <li class="nav-item"><a href="author" class="nav-link text-light">?? Qu?n lý tác gi?</a></li>
            </ul>
        </div>

        <!-- N?i dung chính -->
        <div class="col-md-10 p-4">
            <jsp:include page="${contentPage}" />
        </div>
    </div>
</div>

</body>
</html>
