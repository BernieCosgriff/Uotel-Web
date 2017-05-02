<%@ page language="java" import="cs5530.*" %>
<%@ page import="uotel.*" %>
<%@ page import="java.sql.SQLException" %>
<html>
<head>
    <title>Post Feedback Rating</title>
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
    Integer userId = (int) session.getAttribute("userId");
    Integer feedbackId = Integer.parseInt(request.getParameter("fid"));
    Integer rating = Integer.parseInt(request.getParameter("fRating"));
    DBConn conn = (DBConn) session.getAttribute("conn");
    try {
        boolean success = conn.createPropertyFeedbackUsefulnessRating(userId, feedbackId, rating);
        if(success){
%>
<h2 style="text-align: center">Feedback Recorded!</h2>
<%
        } else {
%>
<h1 style="text-align: center">Oops!</h1>
<p style="text-align: center">You've already rated that feedback.</p>
<%
        }
    } catch (SQLException e){
        response.sendRedirect("dberror.jsp");
    }
%>
<div class="row">
    <a class="btn col s2 push-s5" href="mainmenu.jsp">Main Menu</a>
</div>
</div>
</body>
</html>
