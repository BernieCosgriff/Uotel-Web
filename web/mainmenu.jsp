<%@ page language="java" import="cs5530.*" %>
<html>
<head>
    <title>Main Menu</title>
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
    <%@include file="header.jsp"%>
    <div class="collection with-header">
        <div class="collection-header" style="text-align: center"><h1>Uotel</h1></div>
        <a class="collection-item" href="addreservation.jsp">Add a Reservation</a>
        <a class="collection-item" href="viewreservations.jsp">View Your Reservations</a>
        <a class="collection-item" href="addproperty.jsp">Record a New Property</a>
        <a class="collection-item" href="viewproperties.jsp">View Your Properties</a>
        <a class="collection-item" href="updateproperty.jsp">Update One of Your Properties</a>
        <a class="collection-item" href="addlisting.jsp">Add a Property Listing</a>
        <a class="collection-item" href="viewlistings.jsp">View Your Property Listings</a>
        <a class="collection-item" href="addstay.jsp">Record a Recent Stay or Stays</a>
        <a class="collection-item"a href="viewstays.jsp">View Your Stays</a>
        <a class="collection-item"a href="addfavorite.jsp">Add a Favorite Property</a>
        <a class="collection-item"a href="viewfavorites.jsp">View Your Favorite Properties</a>
        <a class="collection-item"a href="addfeedback.jsp">Add Property Feedback</a>
        <a class="collection-item"a href="viewfeedback.jsp">View Your Property Feedback</a>
        <a class="collection-item"a href="viewpropertyfeedback.jsp">View and Rate a Property's Feedback</a>
        <a class="collection-item"a href="addusertrustrating.jsp">Add a User Trust Rating</a>
        <a class="collection-item"a href="browseproperties.jsp">Browse Properties</a>
        <a class="collection-item"a href="statistics.jsp">View Property Statistics</a>
    </div>
</div>
</body>
</html>
