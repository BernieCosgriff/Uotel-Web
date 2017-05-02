<%@ page language="java" import="cs5530.*, java.util.*" %>
<%@ page import="uotel.*" %>
<%@ page import="java.math.BigDecimal" %>

<%
    if (session.getAttribute("userId") == null ||
            session.getAttribute("conn") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<div class="center-align">
    <%@include file="header.jsp" %>
    <h1 style="text-align: center">Properties</h1>
    <%
        DBConn.PropertySearchOptions searchOptions = new DBConn.PropertySearchOptions();
        if(request.getParameter("low") != null && !request.getParameter("low").isEmpty()){
            searchOptions.lowPrice = BigDecimal.valueOf(Double.parseDouble(request.getParameter("low")));
        }
        if(request.getParameter("high") != null && !request.getParameter("high").isEmpty()){
            searchOptions.highPrice = BigDecimal.valueOf(Double.parseDouble(request.getParameter("high")));
        }
        if(request.getParameter("city") != null && !request.getParameter("city").isEmpty()){
            searchOptions.city = request.getParameter("city");
        }
        if(request.getParameter("state") != null && !request.getParameter("state").isEmpty()){
            searchOptions.state = request.getParameter("state");
        }
        if(request.getParameter("keyword") != null && !request.getParameter("keyword").isEmpty()){
            String keyword = request.getParameter("keyword");
            searchOptions.keywords = new ArrayList<>(Arrays.asList(keyword.split(" ")));
        }
        if(request.getParameter("category") != null && !request.getParameter("category").isEmpty()){
            searchOptions.category = request.getParameter("category");
        }
        if(request.getParameter("sortBy") != null && !request.getParameter("sortBy").isEmpty()){
            String sortBy = request.getParameter("sortBy");
            if(sortBy.equals("Price")) {
                searchOptions.sortOption = DBConn.PropertySortOption.PRICE;
            } else if(sortBy.equals("Feedback")) {
                searchOptions.sortOption = DBConn.PropertySortOption.FEEDBACK;
            } else if(sortBy.equals("TrustedFeedback")) {
                searchOptions.sortOption = DBConn.PropertySortOption.TRUSTED_FEEDBACK;
            }
        }
        if(request.getParameter("order") != null && !request.getParameter("order").isEmpty()){
            String order = request.getParameter("order");
            if(order.equals("asc")) {
                searchOptions.sortOrder = DBConn.SortOrder.ASCENDING;
            } else if(order.equals("desc")) {
                searchOptions.sortOrder = DBConn.SortOrder.DESCENDING;
            }
        }
        if(request.getParameter("limit") != null && !request.getParameter("limit").isEmpty()){
           Integer limit = Integer.parseInt(request.getParameter("limit"));
           if(limit > 0){
               searchOptions.limit = limit;
           }
        }
        int userId = (int) session.getAttribute("userId");
        DBConn conn = (DBConn) session.getAttribute("conn");
        try {
            List<SearchedProperty> properties = conn.searchProperties(userId, searchOptions);
    %>
    <table class="striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Owner ID</th>
            <th>Address</th>
            <th>Category</th>
            <th>Phone Number</th>
            <th>URL</th>
            <th>Year Built</th>
	    <th>Listing Start Date</th>
	    <th>Listing End Date</th>
	    <th>Listing Price per Night</th>
	    <th>Average Feedback Rating</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (SearchedProperty prop : properties) {
	       String avgRatingStr = prop.avgRating;
	       if (avgRatingStr == null) {
	           avgRatingStr = "No feedback yet.";
	       }
        %>
        <tr>
            <td><%=prop.id%>
            </td>
            <td><%=prop.name%>
            </td>
            <td><%=prop.ownerId%>
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
            <td><%=prop.startDate%>
            </td>
            <td><%=prop.endDate%>
            </td>
            <td><%=prop.pricePerNight%>
            <td><%=avgRatingStr%>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <div style="padding: 10px" >
<a href="browseproperties.jsp" class="btn red darken-4 align-center">Search Again</a>
    </div>
</div>
    <%

    } catch (Exception e) {
            response.sendRedirect("error.jsp");
        }
    %>
