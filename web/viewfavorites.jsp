<%@ page language="java" import="cs5530.*, java.util.*" %>
<%@ page import="uotel.*" %>
<html>
<head>
    <title>View Favorites</title>
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
    <h1 style="text-align: center">Your Favorites</h1>
    <%
        int userId = (int) session.getAttribute("userId");
        DBConn conn = (DBConn) session.getAttribute("conn");
        try {
            List<Property> favorites = conn.getUserFavorites(userId);
       %>
    <table class="striped">
      <thead>
        <tr>
          <th>Property ID</th>
          <th>Property Name</th>
        </tr>
      </thead>
      <tbody>
        <%
            for (Property fav : favorites) {
        %>
	<tr>
	  <td><%=fav.id%></td>
	  <td><%=fav.name%></td>
	</tr>	  
<%
	    }
%>
</tbody>
</table>
<%
	} catch (Exception e) {
   %>
         <h1>Oh no!</h1>
	 <p>We can't connect to the database right now.</p>
	 <a href="mainmenu.jsp">Back</a>
   <%
      }
      %>
</div>
</body>
</html>
