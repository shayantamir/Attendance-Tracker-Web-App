<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import = "java.io.*,java.util.*, javax.servlet.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
body {
    background-image: url("Temp.jpg");
}
</style>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<center>
<font color = "white">Attendance Logged for Student ID# </font>

<font color = "red"> <%= request.getAttribute("stuID") %> on</font>

<font color="red">
	<%
         Date date = new Date();
         out.print( "<h2 align = \"center\">" +date.toString()+"</h2>");
      %></font>
<font color = "white">using key</font>

<font color="red"> <%= request.getAttribute("key") %></font>
</center>
</body>
</html>