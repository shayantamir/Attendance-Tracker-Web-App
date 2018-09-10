<html>
<head>
<style>
body {
    background-image: url("Temp.jpg");
}
</style>
<title>Teacher Sign In</title>

</head>
<center>
	<font color="white">
		<body>
		<h1><font size="14">CSUS Attendance</font></h1>
		<font size="4">Please Sign In</font>
		  <fieldset>
		<legend><font  color="white"></font></legend> 
		<br/><br/>
		  <form action="QuickServlet" method="post">
		  User Name: <input type="text" size="10" name="userTeacher" id="x"/>
		  &nbsp;&nbsp;
		  Password <input type="password" size="10" name="passTeacher"/>
		  &nbsp;&nbsp;
		  <br/><br/>
		  <button type="submit" value="TeacherLogin" name="FormName">Submit</button>
		  </form>
		  </fieldset>
	</font>
</center>	
</body>
</html>