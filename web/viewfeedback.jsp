<%@ page language="java" import="cs5530.*" %>
<%@ page import="uotel.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<html>
<head>
    <title>View Feedback</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
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
    <h1 style="text-align: center">Your Feedback</h1>
    <%
        int userId = (int) session.getAttribute("userId");
        DBConn conn = (DBConn) session.getAttribute("conn");
        List<Feedback> feedbacks = null;
        try {
            feedbacks = conn.getAllUserFeedback(userId);
        } catch (SQLException e) {
            response.sendRedirect("error.jsp");
            return;
        }
    %>
    <table class="striped">
        <thead>
        <tr>
            <th>
                Feedback ID
            </th>
            <th>
                Property ID
            </th>
            <th>
                Property Name
            </th>
            <th>
                Property Address
            </th>
            <th>
                Date
            </th>
            <th>
                Score
            </th>
            <th>
                Comment
            </th>
        </tr>
        </thead>
        <tbody>
        <% for (Feedback feedback : feedbacks) { %>
        <tr>
            <td>
                <%= feedback.id %>
            </td>
            <td>
                <%= feedback.property.id %>
            </td>
            <td>
                <%= feedback.property.name %>
            </td>
            <td>
                <%= feedback.property.address %>
            </td>
            <td>
                <%= feedback.date %>
            </td>
            <td>
                <%= feedback.score %>
            </td>
            <td>
                <%= feedback.comment %>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
