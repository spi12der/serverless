<!DOCTYPE html>
<html>
<head>
	<title>Admin</title>
	<style type="text/css">
		html, body { height: 100%; width: 100%; margin: 0;padding:0;}
		header { height: 15%; width: 100%;margin: 0;padding:0;}
		#headerContent{margin: 0;padding:0;}
		footer { height: 9%; width: 100%;}
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
			overflow: scroll;
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
		table {
		    border-collapse: collapse;
		}

		td {
		    padding: 8px;
		    text-align: left;
		    border-bottom: 1px solid #ddd;
		}

		tr:hover{background-color:#E5E8E8}	
	</style>
	<script> 
	function makeTable(response)
	{
		var message=JSON.parse(response);
		var table = document.createElement('table');
		table.style.marginTop="5%";
		table.style.marginLeft="10%";
		table.width="70%";
		var tr = document.createElement('tr');   
		var td1 = document.createElement('td');
		var td2 = document.createElement('td');
		var td3 = document.createElement('td');
		var text1 = document.createTextNode("IP");
	    var text2 = document.createTextNode("USERNAME");
	    var text3 = document.createTextNode("STATUS");
	    td1.appendChild(text1);
	    td2.appendChild(text2);
	    td3.appendChild(text3);
	    tr.appendChild(td1);
	    tr.appendChild(td2);
	    tr.appendChild(td3);
	    tr.style.color="white";
	    tr.style.backgroundColor="#444444";
	    table.appendChild(tr);
		for (var i = 0; i < message.details.length; i++)
		{
		    var tr = document.createElement('tr');   
		    var td1 = document.createElement('td');
		    var td2 = document.createElement('td');
		    var td3 = document.createElement('td');
		    var text1 = document.createTextNode(message.details[i].ip);
		    var text2 = document.createTextNode(message.details[i].username);
		    var text3 = document.createTextNode(message.details[i].status);
		    td1.appendChild(text1);
		    td2.appendChild(text2);
		    td3.appendChild(text3);
		    tr.appendChild(td1);
		    tr.appendChild(td2);
		    tr.appendChild(td3);
		    table.appendChild(tr);
		}
		document.getElementById("displayArea").appendChild(table);
	}
	
	function load_home() 
	{
     	document.getElementById("headerContent").innerHTML='<object style="height:100%;width:100%" type="text/html" data="/Serverless/JSP/header.jsp" ></object>';
     	document.getElementById("footerContent").innerHTML='<object style="height:100%;width:100%" type="text/html" data="/Serverless/JSP/footer.jsp" ></object>';
     	var response='${message}';
     	makeTable(response);		
	} 
	</script> 
</head>
<body onload="load_home()">
	<header id="headerContent"></header>
	<div style="height:75.5%;margin:0;padding:0">
		<div class ="sidebar">
			<ul id="nav">
				<li><a href="/Serverless/JSP/ADMIN/AdminHome.jsp"> Home</a></li>
				<li><a class="selected" href="/Serverless/ServerDetails"> Manage Server</a></li>
				<li><a href="/Serverless/JSP/ADMIN/AddServer.jsp"> Add Server</a></li>
				<li><a href="/Serverless/VM">Manage VM</a></li>
				<li><a href="/Serverless/Logs"> Platform Logs</a></li>
				<li><a href="/Serverless/JSP/ADMIN/AddAdmin.jsp"> Add Admin</a></li>
				<li><a href="/Serverless/JSP/ADMIN/Contact.jsp"> Contact Us</a></li>
				<li><a href="/Serverless/Logout"> Logout</a></li>
			</ul>
		</div>
		<div class="content" id="displayArea">
			
		</div>
	</div>
	<footer id="footerContent"></footer>
</body>
</html>