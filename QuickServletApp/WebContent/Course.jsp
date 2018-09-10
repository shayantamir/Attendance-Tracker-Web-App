<html>
<head>
<style>
body {
    background-image: url("Temp.jpg");
}
</style>
<center>
<title>Select Course</title>
</head>
<font color="white">
<body>
</body>
<%
	if(session.getAttribute("teacher") == null){
		response.sendRedirect("Teacher.jsp");
	}
%>
<h1>
	<font size="14">CSUS Attendance</font></h1>
	<font size="4">Welcome Professor</font>
    <form action="QuickServlet" method="post">
    <fieldset>
    	<p>
    		<label>Course Selection:</label>
    		<select id = "myList" name = "myList">
    			<option name ="CSC 20" value = "CSC20"> CSC 20</option>
    			<option name ="CSC 130" value = "CSC130"> CSC 130</option>
    			<option name ="CSC 131" value = "CSC131"> CSC 131</option>
    			<option name ="CSC 133" value = "CSC133"> CSC 133</option>
    			<option name ="CSC 135" value = "CSC135"> CSC 135</option>
    		</select>
    	</p>
    	<input type="submit" name="FormName" value="CourseSelect"/>

    	</form>
    </fieldset>
    </font>
    </center>
</body>
</html>