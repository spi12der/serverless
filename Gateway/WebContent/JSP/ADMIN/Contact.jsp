<!DOCTYPE html>
<html>
<head>
	<title>Admin</title>
	<style type="text/css">
		html, body { height: 100%; width: 100%; margin: 0;padding:0;}
		header { height: 15%; width: 100%;margin: 0;padding:0;}
		#headerContent{margin: 0;padding:0;}
		footer { height: 9%; width: 100%;}
		table {height:100%;width:60%}
		td {padding:3% 3% 3% 15%;font-size:120%}
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
     	document.getElementById("headerContent").innerHTML='<object style="height:100%;width:100%" type="text/html" data="/Serverless/JSP/header.jsp" ></object>';
     	document.getElementById("footerContent").innerHTML='<object style="height:100%;width:100%" type="text/html" data="/Serverless/JSP/footer.jsp" ></object>';
	} 
	</script> 
</head>
<body onload="load_home()">
	<header id="headerContent"></header>
	<div style="height:75.5%;margin:0;padding:0">
		<div class ="sidebar">
			<ul id="nav">
				<li><a href="/Serverless/JSP/ADMIN/AdminHome.jsp"> Home</a></li>
				<li><a href="/Serverless/ServerDetails"> Manage Server</a></li>
				<li><a href="/Serverless/JSP/ADMIN/AddServer.jsp"> Add Server</a></li>
				<li><a href="/Serverless/VM">Manage VM</a></li>
				<li><a href="/Serverless/Logs"> Platform Logs</a></li>
				<li><a href="/Serverless/JSP/ADMIN/AddAdmin.jsp"> Add Admin</a></li>
				<li><a class="selected" href="/Serverless/JSP/ADMIN/Contact.jsp"> Contact Us</a></li>
				<li><a href="/Serverless/Logout"> Logout</a></li>
			</ul>
		</div>
		<div class="content">
			<img src="../../IMG/iiit.png" width="80%" height="70%" style="margin:8%">
		</div>
	</div>
	<footer id="footerContent"></footer>
</body>
</html>