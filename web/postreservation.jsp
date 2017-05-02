%@ page language="java" import="cs5530.*, java.util.*, java.text.SimpleDateFormat, java.sql.*" %>
<%
   if (session.getAttribute("userId") == null ||
            session.getAttribute("conn") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    int userId = (int) session.getAttribute("userId");
    DBConn conn = (DBConn) session.getAttribute("conn");

    String propId = request.getParameter("propertyId");
    String startDate = request.getParameter("startDate");
    String endDate=  request.getParameter("endDate");
    String guests = request.getParameter("guests");


Reservation.Order order = new Reservation.Order();
order.userId = userId;
   try {
       order.propertyId = Integer.parseInt(propId);
   } catch (Exception e) {
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>Property ID must be a number.</td>
</tr>
<%
	return;
   }
if (!conn.propertyExists(order.propertyId)) {
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>No such property</td>
</tr>
<%
	return;
}

    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
java.util.Date date;
try {
    date = df.parse(endDate);
} catch (Exception e) {
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>Bad start date.</td>
</tr>
<%
	return;
}
order.startDate = new java.sql.Date(date.getTime());
try {
    date = df.parse(endDate);
} catch (Exception e) {
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>Bad end date.</td>
</tr>
<%
	return;
}
order.endDate = new java.sql.Date(date.getTime());

List<Listing> listings;
try {
listings = conn.getListings(order.propertyId);
} catch (SQLException e) {
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>No database connection.</td>
</tr>
<%
	return;
}
boolean listingExists = false;
for (Listing l : listings) {
    if ((l.startDate.before(order.startDate) || l.startDate.equals(order.startDate)) &&
	(l.endDate.equals(order.endDate) || l.endDate.after(order.endDate))) {
	listingExists = true;
	break;
    }
}
if (!listingExists) {
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>No listing matching given dates.</td>
</tr>
<%
	return;
}

try {
    order.guests = Integer.parseInt(guests);
} catch (Exception e) {
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>Guests must be a number.</td>
</tr>
<%
	return;
}
try {
    conn.createReservation(order);
} catch (SQLException e) {
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>No database connection.</td>
</tr>
<%
	return;
}
%>
<tr>
  <td><%=propId%></td>
  <td><%=startDate%></td>
  <td><%=endDate%></td>
  <td><%=guests%></td>
  <td>Recorded!</td>
</tr>
