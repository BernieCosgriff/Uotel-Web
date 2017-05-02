<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
    <title>Add Property</title>
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
    <h1>Record Property</h1>
    <form class="row" name="add_property" method=post action="postproperty.jsp">
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
        <button class="btn red darken-4" type="submit">Create</button>
    </form>
</div>
</body>
</html>
