<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
    <title>Post Property Update</title>
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
	String propId = request.getParameter("propId");
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
	    int propIdInt = Integer.parseInt(propId);
	    if (!conn.propertyExists(propIdInt)) {
%>
<h1>No Such Property</h1>
<p>Make sure you entered the property ID!</p>
<a href="updateproperty.jsp">Back</a>
<%
		    return;
	    }
	    Property prop = conn.getProperty(propIdInt);
	    if (name.length() > 0) {
		prop.name = name;
	    }
	    if (phoneNumber.length() > 0) {
		prop.phoneNumber = phoneNumber;
	    }
	    if (url.length() > 0) {
		prop.url = url;
	    }
	    if (yearBuilt.length() > 0) {
		prop.yearBuilt = yearBuilt;
	    }
	    if (category.length() > 0) {
		List<String> categories = conn.getPropertyCategories();
		int catId = categories.contains(category) ? 
		    conn.getPropertyCategoryId(category) :
		    conn.createPropertyCategory(category);
		prop.category = new Category(catId, "");
	    }
	    if (keywords.length() > 0) {
		List<String> currKeywords = conn.getPropertyKeywords(propIdInt);
		for (String kw : currKeywords) {
		    conn.deletePropertyKeyword(propIdInt, kw);
		}
		List<String> newKeywords = Arrays.asList(keywords.split(" "));
		for (String kw : newKeywords) {
		    conn.addPropertyKeyword(propIdInt, kw);
		}
	    }
	    if (street.length() > 0 || unitNumber.length() > 0 ||
		city.length() > 0 || state.length() > 0 || country.length() > 0 ||
		zipcode.length() > 0) {
		Address updatedAddr = prop.address;
		if (street.length() > 0) updatedAddr.street = street;
		if (unitNumber.length() > 0) updatedAddr.unitNumber = unitNumber;
		if (city.length() > 0) updatedAddr.city = city;
		if (state.length() > 0) updatedAddr.state = state;
		if (country.length() > 0) updatedAddr.country = country;
		if (zipcode.length() > 0) updatedAddr.zipcode = zipcode;
		int addrId = conn.createAddress(updatedAddr.street, updatedAddr.unitNumber,
						updatedAddr.zipcode, updatedAddr.city,
						updatedAddr.state, updatedAddr.country);
		updatedAddr.id = addrId;
	    }
            conn.updateProperty(propIdInt, prop);
	} catch (Exception e) {
            response.sendRedirect("dberror.jsp");
	}
      %>
<h1 style="text-align: center">Updated!</h1>
<div class="row">
    <a class="btn col s2 push-s5" href="mainmenu.jsp">Main Menu</a>
</div>
</div>
</body>
</html>
