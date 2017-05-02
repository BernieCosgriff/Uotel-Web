<%@ page language="java" import="cs5530.*, java.util.*" %>
<%@ page import="uotel.*" %>

<%
    if (session.getAttribute("userId") == null ||
            session.getAttribute("conn") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<div class="center-align">
    <h1 style="text-align: center">Properties</h1>
    <%
        String option = request.getParameter("option");
        Integer numProps = Integer.parseInt(request.getParameter("numProps"));
        DBConn conn = (DBConn) session.getAttribute("conn");
        TreeMap<String, List<Property>> propertyMap = null;
        try {
            switch (option) {
                case "expensive":
                    propertyMap = conn.getMostExpensivePropertiesPerCategory();
                    break;
                case "rating":
                    propertyMap = conn.getHighestRatedPropertiesPerCategory();
                    break;
                case "popular":
                    propertyMap = conn.getMostPopularPropertiesPerCategory();
                    break;
                default:
                    return;
            }
        } catch (Exception e) {
//        response.sendRedirect("error.jsp");
        }
    %>
    <table class="striped">
        <thead>
        <tr>
            <th>Category</th>
            <th>ID</th>
            <th>Name</th>
            <th>Owner ID</th>
            <th>Address</th>
            <th>Category</th>
            <th>Phone Number</th>
            <th>URL</th>
            <th>Year Built</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (String key : propertyMap.navigableKeySet()) {
                List<Property> properties = propertyMap.get(key);
                for (int i = 0; i < Math.min(numProps, properties.size()); i++) {
                    Property prop = properties.get(i);
        %>
        <tr>
            <td><%=key%>
            </td>
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
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
<%
%>
