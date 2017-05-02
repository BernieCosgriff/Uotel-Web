<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
    <title>Add Feedback</title>
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
    <%@include file="header.jsp" %>
    <h1>Add Feedback</h1>
    <form class="row" name="add_feedback" method=post action="postfeedback.jsp">
        <div class="input-field">
            <label>Property ID</label> <br>
            <input type=text name="propertyId" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Score (0 - 10)</label> <br>
            <input type=text name="score" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Comment (optional)</label> <br>
            <input type=text name="comment" length=10/> <br> <br>
        </div>
        <button type="submit" class="btn red darken-4">Create</button>
    </form>
</div>
</body>
</html>
