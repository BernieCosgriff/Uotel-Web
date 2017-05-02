<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
    <title>Post Property</title>
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
	String name = request.getParameter("name");
	String phoneNumber = request.getParameter("phoneNumber");
	String url = request.getParameter("url");
	String yearBuilt = request.getParameter("yearBuilt");
	String category = request.getParameter("category");
	String keywords = request.getParameter("keywords");
	String street = request.getParameter("street");
	String unitNumber = request.getParameter("unitNumber");
	String city = request.getParameter("city");
	String state = request.getParameter("state");
	String country = request.getParameter("country");
	String zipcode = request.getParameter("zipcode");

        int userId = (int)session.getAttribute("userId");
	DBConn conn = (DBConn)session.getAttribute("conn");
	try {
	    List<String> categories = conn.getPropertyCategories();
	    int categoryId = categories.contains(category) ? 
		conn.getPropertyCategoryId(category) :
		conn.createPropertyCategory(category);
	    int addrId = conn.createAddress(street, unitNumber, zipcode, city, state, country);
	    int propId = conn.createProperty(name, userId, addrId, categoryId, phoneNumber, url, yearBuilt);
	    List<String> keywordList = Arrays.asList(keywords.split(" "));
	    for (String kw : keywordList) {
		conn.addPropertyKeyword(propId, kw);
	    }
	} catch (Exception e) {
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
