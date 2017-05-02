<%@ page language="java" import="cs5530.*" %>
<html>
<head>
    <title>Post New User</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
  <%
     String firstName = request.getParameter("firstName");
     String lastName = request.getParameter("lastName");
     String login = request.getParameter("login");
     String password = request.getParameter("password");
     String phoneNumber = request.getParameter("phoneNumber");
     String street = request.getParameter("street");
     String unitNumber = request.getParameter("unitNumber");
     String city = request.getParameter("city");
     String state = request.getParameter("state");
     String country = request.getParameter("country");
     String zipcode = request.getParameter("zipcode");
     
     try {
         DBConn conn = new DBConn();
         if (conn.loginExists(login)) {
     %>
  <h1>That login exists</h1>
  <p>Somebody already used it. Sorry.</p>
  <a href="index.jsp">Back</a>
  <%
             return;
         }
         int addrId = conn.createAddress(street, unitNumber, zipcode, city, state, country);
         int userId = conn.createUser(firstName, lastName, login, password, phoneNumber, addrId);
         session.setAttribute("userId", userId);
         session.setAttribute("conn", conn);
         response.sendRedirect("mainmenu.jsp");
     } catch (Exception e) {
     }
     %>
  <h1>Oh no!</h1>
  <p>We can't connect to the database right now.</p>
  <a href="index.jsp">Back</a>
</div>
</body>
</html>
