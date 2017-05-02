<%@ page language="java" import="cs5530.*" %>
<%@ page import="uotel.*" %>
<%@ page import="java.sql.SQLException" %>
<html>
<head>
    <title>Post User Trust Rating</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <div class="center-align">
<%
    if (session.getAttribute("userId") == null ||
            session.getAttribute("conn") == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    int ratedId;
    int rating;
    try {
        ratedId = Integer.parseInt(request.getParameter("ratedId"));
	rating = Integer.parseInt(request.getParameter("rating"));
    } catch (NumberFormatException e) {
%>
<h1>Oh no!</h1>
<p>Be sure to give the user ID and rating.</p>
<a href="mainmenu.jsp">Back</a>
<%
        return;
    }

    int userId = (int) session.getAttribute("userId");
    DBConn conn = (DBConn) session.getAttribute("conn");
    try {
        if(!conn.userExists(ratedId)) {
%>
    <h1>That user doesn't exist.</h1>
<%
        } else if(conn.userTrustRatingExists(userId, ratedId)) {
%>
    <h1>Looks like you already rated this user!</h1>
    <%
        }else {
            conn.addUserTrustRating(userId, ratedId, rating);
            %>
    <h1>Rating recorded!</h1>
    <%
        }
    } catch (SQLException e) {
        response.sendRedirect("error.jsp");
    }
%>
    <a href="mainmenu.jsp" class="btn red darken-4">Main Menu</a>
    </div>
</div>
</body>
</html>
