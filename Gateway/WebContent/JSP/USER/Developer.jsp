<!DOCTYPE html>
<html>
<head>
	<title>User</title>
	<style type="text/css">
		html, body { height: 100%; width: 100%; margin: 0;padding:0;}
		header { height: 15%; width: 100%;margin: 0;padding:0;}
		#headerContent{margin: 0;padding:0;}
		footer { height: 9%; width: 100%;}
		td {padding:2% 2% 2% 5%;font-size:120%}
		input {font-size:90%}
		.sidebar
		{
			width:16%;
			height:100%;
			float:left;
			background-color :#171717;
			margin:0;
			padding:0;
		}
		.content
		{
			width:84%;
			height:100%;
			float:left;
			margin-left:0 auto;
			margin-top:0 auto;
		}
		ul#nav
		{
			padding:0;
			margin:0;
		}
		ul#nav li
		{
			list-style: none;
		}
		ul#nav li a
		{
			color: #ccc;
			display:block;
			padding: 17px;
			font-size: 1.2em;
			border-bottom: 1px solid
		}
		ul#nav li a:hover
		{
			background-color:#030303;
			color:#fff; 
			padding-left: 30px;
		}
		ul#nav li a.selected
		{
			background-color:#030303;
			color:#fff; 
		}	
	</style>
	<script> 
	function load_home() 
	{
     	document.getElementById("headerContent").innerHTML='<object style="height:100%;width:100%" type="text/html" data="../header.jsp" ></object>';
     	document.getElementById("footerContent").innerHTML='<object style="height:100%;width:100%" type="text/html" data="../footer.jsp" ></object>';
	} 
	</script> 
</head>
<body onload="load_home()">
	<header id="headerContent"></header>
	<div style="height:75.5%;margin:0;padding:0">
		<div class ="sidebar">
			<ul id="nav">
				<li><a href="/Serverless/JSP/USER/UserHome.jsp"> Home</a></li>
				<li><a href="#"> Tutorial</a></li>
				<li><a class="selected" href="/Serverless/JSP/USER/Developer.jsp"> Developer Services</a></li>
				<li><a href="/Serverless/Config"> Configuration Services</a></li>
				<li><a href="/Serverless/ViewService"> View Services</a></li>
				<li><a href="/Serverless/JSP/USER/Contact.jsp"> Contact Us</a></li>
				<li><a href="/Serverless/Logout"> Logout</a></li>
			</ul>
		</div>
		<div class="content">
			<h2 style="margin:2%">Add New Developer Service</h2>
			<br><br>
			<p style="color:red;margin:2%">${param.message}</p>
			<div style="margin:2% auto;padding:2%;width:70%;border:1px solid black;">
			<center><form name="form1" method="post" enctype="multipart/form-data" action="/Serverless/Developer">
					<table width="80%">
						<tr>
							<td>Name:</td>
							<td><input type="text" name="name" value="" required></td>	
						</tr>
						<tr>
							<td>Upload Code :</td>
							<td><input type="file" name="jar"></td>
						</tr>
						<tr>
							<td colspan="2" style="padding:5% 3% 3% 3%"><center>
								<input type="submit" value="DEPLOY">
							</center></td>
						</tr>
					</table>
				</form></center>
				</div>
		</div>
	</div>
	<footer id="footerContent"></footer>
</body>
</html>