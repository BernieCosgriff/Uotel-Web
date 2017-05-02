<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
</head>
<body>
<%
	session.invalidate();
	response.sendRedirect("index.jsp");
%>
</body>
</html>
