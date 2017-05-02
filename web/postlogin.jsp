<%@ page language="java" import="uotel.*" %>
<html>
<head>
    <title>Post Login</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <%
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        DBConn conn = new DBConn();
        int userId;
        try {
            userId = conn.loginUser(login, password);
            session.setAttribute("userId", userId);
            session.setAttribute("conn", conn);
            response.sendRedirect("mainmenu.jsp");
        } catch (Exception e) {
        }
    %>
    <div class="center-align" style="text-align: center">
        <h1>Bad Login</h1>
        <p>Hrmm. We don't recognize that login.</p>
        <a class="btn red darken-4" href="index.jsp">Back</a>
    </div>
</div>
</body>
</html>
