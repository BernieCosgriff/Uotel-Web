<%@ page language="java" import="cs5530.*, java.util.*" %>
<%@ page import="uotel.*" %>
<html>
<head>
    <title>View Properties</title>
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
    <h1 style="text-align: center">Your Properties</h1>
    <%
        int userId = (int) session.getAttribute("userId");
        DBConn conn = (DBConn) session.getAttribute("conn");
        try {
            List<Property> properties = conn.getUserProperties(userId);
    %>
    <table class="striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Address</th>
            <th>Category</th>
            <th>Phone Number</th>
            <th>URL</th>
            <th>Year Built</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Property prop : properties) {
        %>
        <tr>
            <td><%=prop.id%>
            </td>
            <td><%=prop.name%>
            </td>
            <td><%=prop.address.toString()%>
            </td>
            <td><%=prop.category%>
            </td>
            <td><%=prop.phoneNumber%>
            </td>
            <td><%=prop.url%>
            </td>
            <td><%=prop.yearBuilt%>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%

    } catch (Exception e) {
    %>
    <h1>Oh snap!</h1>
    <p>We can't connect to the database.</p>
    <a href="mainmenu.jsp">Back</a>
    <%
            return;
        }
    %>

</div>
</body>
</html>
