<%@ page language="java" import="cs5530.*" %>
<html>
<head>
    <title>Add User Trust Rating</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>
    <h1 class="center-align">User Trust Rating</h1>
    <form action="postusertrustrating.jsp">
        <div class="input-field">
            <input name="ratedId" type="number" id="ratedId">
            <label for="ratedId">User ID</label>
        </div>
        <div class="input-field">
            <input name="rating" placeholder="0 (Not Trusted) or 1 (Trusted)" type="number" id="rating" max="1" min="0">
            <label for="rating">Rating</label>
        </div>
        <button type="submit" class="btn red darken-4">Submit Feedback</button>
    </form>
</div>
</body>
</html>
