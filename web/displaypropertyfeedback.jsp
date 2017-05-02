<%@ page language="java" import="cs5530.*" %>
<%@ page import="uotel.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
    <%
        Integer propertyId = Integer.parseInt(request.getParameter("pid"));
        Integer numProps = Integer.parseInt(request.getParameter("numProps"));
        DBConn conn = (DBConn) session.getAttribute("conn");
        List<Feedback> feedbacks = null;
        try {
            if(numProps != 0) {
                feedbacks = conn.getTopRatedPropertyFeedback(propertyId, numProps);
            } else {
                feedbacks = conn.getPropertyFeedback(propertyId);
            }
        } catch (SQLException e) {
            response.sendRedirect("error.jsp");
        }
    %>
    <table class="striped">
        <thead>
        <tr>
            <th>
                Feedback ID
            </th>
            <th>
                Rater ID
            </th>
            <th>
                Rater Login
            </th>
            <th>
                Property ID
            </th>
            <th>
                Property Name
            </th>
            <th>
                Property Address
            </th>
            <th>
                Date
            </th>
            <th>
                Score
            </th>
            <th>
                Comment
            </th>
        </tr>
        </thead>
        <tbody>
        <% for(Feedback feedback : feedbacks) { %>
        <tr>
            <td>
                <%= feedback.id %>
            </td>
            <td>
                <%= feedback.userId %>
            </td>
            <td>
                <%= feedback.userLogin %>
            </td>
            <td>
                <%= feedback.property.id %>
            </td>
            <td>
                <%= feedback.property.name %>
            </td>
            <td>
                <%= feedback.property.address %>
            </td>
            <td>
                <%= feedback.date %>
            </td>
            <td>
                <%= feedback.score %>
            </td>
            <td>
                <%= feedback.comment %>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
