<%@ page language="java" import="cs5530.*, java.util.*, java.text.SimpleDateFormat, java.sql.*, java.math.BigDecimal" %>
<html>
<head>
    <title>Post Listing</title>
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
String startDate = request.getParameter("startDate");
String endDate = request.getParameter("endDate");
String pricePerNight = request.getParameter("pricePerNight");

int userId = (int) session.getAttribute("userId");
DBConn conn = (DBConn) session.getAttribute("conn");

try {
    int propIdInt = Integer.parseInt(propId);
    List<Property> userProperties = conn.getUserProperties(userId);
    boolean exists = false;
    for (Property prop : userProperties) {
	if (prop.id == propIdInt) {
	    exists = true;
	    break;
	}
    }
    if (!exists) {
%>
   <h1>Oh no!</h1>
   <p>It looks like you don't own a property with that ID.</p>
   <a href="mainmenu.jsp">Back</a>
<%
         return;
    }
    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    java.util.Date date = df.parse(startDate);
    if (date == null) {
%>
   <h1>Oh no!</h1>
   <p>Make sure to enter those dates in MM-dd-yyy format.</p>
   <a href="mainmenu.jsp">Back</a>
<%
	return;
    }
    java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
    date = df.parse(endDate);
    if (date == null) {
%>
   <h1>Oh no!</h1>
   <p>Make sure to enter those dates in MM-dd-yyy format.</p>
   <a href="mainmenu.jsp">Back</a>
<%
        return;
    }
    java.sql.Date sqlEndDate = new java.sql.Date(date.getTime());
    java.math.BigDecimal ppn = new BigDecimal(pricePerNight);
    conn.addListing(propIdInt, sqlStartDate, sqlEndDate, ppn);
    response.sendRedirect("mainmenu.jsp");
    return;
} catch (NumberFormatException e) {
 %>
   <h1>Oh no!</h1>
   <p>Make sure to enter a valid property ID and price per night.</p>
   <a href="mainmenu.jsp">Back</a>
<% 
   return;
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
