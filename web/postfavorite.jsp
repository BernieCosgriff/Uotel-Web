<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
    <title>Post Favorite</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<%
    if (session.getAttribute("userId") == null ||
        session.getAttribute("conn") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<div class="container">
  <%
String propId = request.getParameter("propertyId");
int userId = (int) session.getAttribute("userId");
DBConn conn = (DBConn) session.getAttribute("conn");
try {
    int propIdInt = Integer.parseInt(propId);
    if (!conn.propertyExists(propIdInt)) {
%>
  <h1>Oh no!</h1>
  <p>That property doesn't exist. Be sure you have the right ID.</p>
  <a href="mainmenu.jsp">Back</a>
<%
        return;
   }
    if (conn.favoriteExists(userId, propIdInt)) {
%>
  <h1>Oh no!</h1>
  <p>You've already favorited that property!</p>
  <a href="mainmenu.jsp">Back</a>
<%
       return;
    }
    conn.createFavoriteProperty(userId, propIdInt);
} catch (NumberFormatException e) {
%>
  <h1>Oh no!</h1>
  <p>Be sure to enter a property ID.</p>
  <a href="mainmenu.jsp">Back</a>
<%
    return;
} catch (Exception e) {
   response.sendRedirect("dberror.jsp");
}
%>
<h1 style="text-align: center">Done!</h1>
<div class="row">
    <a class="btn col s2 push-s5" href="mainmenu.jsp">Main Menu</a>
</div>
</div>
</body>
</html>
