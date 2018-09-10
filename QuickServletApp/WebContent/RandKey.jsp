<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<style>
body {
    background-image: url("Temp.jpg");
}
</style>
<center>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Random Key</title>
</head>

<body>
<%
	if(session.getAttribute("teacher") == null){
		response.sendRedirect("Teacher.jsp");
	}
%>
</body><font size="14" color="white">

<%Object randomKey = request.getAttribute("randomKey");%>
<%=randomKey%>

<input type="hidden" name="FormName" value="RandKey" />
</font>
<br><br/>
<form action="QuickServlet" method="post">
		  <button type="sumbit" value="KeyReset" name="FormName">RESET</button>
		  </form>
<br><br/>
<font size="8" color="white">

<%Object timeLimit = request.getAttribute("timeLimit");%>
<%=timeLimit%>

<input type="hidden" name="FormName" value="TimeLimit" />
</font>

<br><br/>
<a href="https://docs.google.com/spreadsheets/d/1HEkPX-wEowUAOSH3rAzwLOndnAMZ_WsCkxR_aonbyu8/edit#gid=0">Manual Entry</a>
<br><br/>
</center>
</body>
</html>