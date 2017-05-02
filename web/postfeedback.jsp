<%@ page language="java" import="cs5530.*, java.util.*, java.sql.*" %>
<html>
<head>
    <title>Post Feedback</title>
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
String score = request.getParameter("score");
String comment = request.getParameter("comment");

int userId = (int) session.getAttribute("userId");
DBConn conn = (DBConn) session.getAttribute("conn");


try {
    int propIdInt;
    try {
    propIdInt = Integer.parseInt(propId);
    } catch (NumberFormatException e) {
%>
   <h1>Oh no!</h1>
   <p>Be sure to enter a valid property ID.</p>
   <a href="mainmenu.jsp">Back</a>
<% 
	    return;
    }
    if (!conn.propertyExists(propIdInt)) {
%>
   <h1>Oh no!</h1>
   <p>It looks like you don't own a property with that ID.</p>
   <a href="mainmenu.jsp">Back</a>
<%
         return;
    }
    if (conn.feedbackExists(userId, propIdInt)) {
%>
   <h1>Oh no!</h1>
   <p>You've already given feedback for this property.</p>
   <a href="mainmenu.jsp">Back</a>
<%
         return;
    }
    int scoreInt;
    try {
	scoreInt = Integer.parseInt(score);
    } catch (NumberFormatException e) {
%>
   <h1>Oh no!</h1>
   <p>Remember to give a numerical score.</p>
   <a href="mainmenu.jsp">Back</a>
<%
	    return;
    }
    java.sql.Date feedbackDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    conn.createFeedback(userId, propIdInt, feedbackDate, scoreInt, comment);
} catch (SQLException e) {
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
