<html>
<head>
<style>
body {
    background-image: url("Temp.jpg");
}
</style>
</head>
<body>
	<font color="white">
	<center>
		<h1>CSUS Attendance</h1>
		<p>Please enter your Student ID# and the Attendance Key</p>

   		<!-- <a href="/QuickServlet">Click here to send GET request</a>-->
    	<br/><br/>
    	<fieldset>
			<legend><font  color="white">Sign In </font></legend> 
			<br/><br/>
		<form action="QuickServlet?action=CSC20,1996570317" method="post">
        	ID#: <input type="text" size="10" name="Student ID"/>
        	KEY <input type="text" size="10" name="Key"/>
        	<input type="hidden" value="StudentEntry" name="FormName"/>
			<br/><br/>
			<legend><font  color="white">Attendance Note </font></legend>
			<textarea name="Comment"  rows="5" cols="50" maxlength="140"></textarea>
			<br/><br/>
        	<button type="submit" value="StudentEntry" name="FormName">Submit</button>
        </form>
        <fieldset>
      </center>
</font>      
    
</body>
</html>