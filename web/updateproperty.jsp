<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
    <title>Update Property</title>
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
    <h1>Update Property</h1>
    <p>
        Enter the ID and updated fields for the property. <br>
        You only have to enter the fields you want to update. <br>
        View your properties below.
    </p>

    <form class="row" name="add_property" method=post action="postupdateproperty.jsp">
        <div class="input-field">
            <label>Property ID</label> <br>
            <input type=text name="propId" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Name</label> <br>
            <input type=text name="name" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Phone Number</label> <br>
            <input type=text name="phoneNumber" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>URL</label> <br>
            <input type=text name="url" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Year Built</label> <br>
            <input type=text name="yearBuilt" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Category</label> <br>
            <input type=text name="category" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Keywords (space separated)</label> <br>
            <input type=text name="keywords" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Street</label> <br>
            <input type=text name="street" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>House/Unit Number</label> <br>
            <input type=text name="unitNumber" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>City</label> <br>
            <input type=text name="city" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>State</label> <br>
            <input type=text name="state" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Country</label> <br>
            <input type=text name="country" length=10/> <br> <br>
        </div>
        <div class="input-field">
            <label>Zip Code</label> <br>
            <input type=text name="zipcode" length=10/> <br> <br>
        </div>
        <button type="submit" class="btn red darken-4">Create</button>
    </form>
</div>
<div class="container">
    <h1>Your Properties</h1>
    <%
        int userId = (int) session.getAttribute("userId");
        DBConn conn = (DBConn) session.getAttribute("conn");
        try {
            List<Property> properties = conn.getUserProperties(userId);
    %>
    <table class="bordered">
        <thead>
        <tr>
            <th>Name</th>
            <th>ID</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Property prop : properties) {
        %>
        <tr>
            <td><%= prop.name %>
            </td>
            <td><%= prop.id %>
            </td>
        <tr>
                <%
       }
%>
        </tbody>
    </table>
    <%
        } catch (Exception e) {
        }
    %>
</div>
</body>
</html>
