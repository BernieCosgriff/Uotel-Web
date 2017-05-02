<%@ page language="java" import="cs5530.*, java.util.*, java.text.SimpleDateFormat, java.sql.*, java.math.BigDecimal" %>
<%
   if (session.getAttribute("userId") == null ||
            session.getAttribute("conn") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    int userId = (int) session.getAttribute("userId");
    DBConn conn = (DBConn) session.getAttribute("conn");

    String reservationId = request.getParameter("reservationId");
    String totalCost = request.getParameter("totalCost");

Stay.Order order = new Stay.Order();
   try {
       order.reservationId = Integer.parseInt(reservationId);
   } catch (Exception e) {
%>
<tr>
  <td><%=reservationId%></td>
  <td><%=totalCost%></td>
  <td>Reservation ID must be a number.</td>
</tr>
<%
	return;
   }
try {
if (!conn.reservationExists(userId, order.reservationId)) {
%>
<tr>
  <td><%=reservationId%></td>
  <td><%=totalCost%></td>
  <td>No such reservation.</td>
</tr>
<%
	return;
}
if (conn.stayExists(order.reservationId)) {
%>
<tr>
  <td><%=reservationId%></td>
  <td><%=totalCost%></td>
  <td>You've already recorded a stay for this reservation.</td>
</tr>
<%
	return;
}
} catch (SQLException e) {
%>
<tr>
  <td><%=reservationId%></td>
  <td><%=totalCost%></td>
  <td>No database connection.</td>
</tr>
<%
	return;
}
try {
   order.totalCost = new BigDecimal(totalCost);
} catch (Exception e) {
%>
<tr>
  <td><%=reservationId%></td>
  <td><%=totalCost%></td>
  <td>Total cost must be of the form xx.xx.</td>
</tr>
<%
	return;
}
try {
    conn.createStay(order);
} catch (Exception e) {
%>
<tr>
  <td><%=reservationId%></td>
  <td><%=totalCost%></td>
  <td>No database connection.</td>
</tr>
<%
	return;
}
%>
<tr>
  <td><%=reservationId%></td>
  <td><%=totalCost%></td>
  <td>Recorded!</td>
</tr>
