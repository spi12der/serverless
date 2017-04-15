<!DOCTYPE html>
<html>
<head>
	<title>Admin</title>
	<style type="text/css">
		html, body { height: 100%; width: 100%; margin: 0;padding:0;}
		header { height: 15%; width: 100%;margin: 0;padding:0;}
		#headerContent{margin: 0;padding:0;}
		footer { height: 9%; width: 100%;}
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

		#myInput {
		  background-image: url('/Serverless/IMG/searchicon.png');
		  background-position: 10px 10px;
		  background-repeat: no-repeat;
		  width: 80%;
		  font-size: 16px;
		  padding: 12px 20px 12px 40px;
		  border: 1px solid #ddd;
		  margin-top: 2%;
		  margin-left:5%;
		  margin-bottom: 12px;
		}

		#myTable {
		  border-collapse: collapse;
		  border: 1px solid #ddd;
		  width: 86%;
		  font-size: 18px;
		  margin-left:5%;
		}

		#myTable th, #myTable td {
		  text-align: left;
		  padding: 12px;
		}

		#myTable tr {
		  border-bottom: 1px solid #ddd;
		}

		#myTable tr.header, #myTable tr:hover {
		  background-color: #f1f1f1;
		}	
	</style>
	<script>
		function makeTable(response)
		{
		    //var response='{"details":[{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Available"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Available"},{"port":"8080","ip":"10.0.2.102","status":"Available"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Available"},{"port":"8080","ip":"10.0.2.102","status":"Available"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Available"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Available"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"},{"port":"8080","ip":"10.0.2.102","status":"Occupied"}]}';
		    var message=JSON.parse(response);
		    var table = document.createElement('table');
		    table.id="myTable";
		    var tr = document.createElement('tr');   
		    var td1 = document.createElement('th');
		    var td2 = document.createElement('th');
		    var td3 = document.createElement('th');
		    var text1 = document.createTextNode("DEBUG LEVEL");
		    var text2 = document.createTextNode("MODULE");
		    var text3 = document.createTextNode("LOG");
		    td1.appendChild(text1);
		    td2.appendChild(text2);
		    td3.appendChild(text3);
		    tr.appendChild(td1);
		    tr.appendChild(td2);
		    tr.appendChild(td3);
		    table.appendChild(tr);
		    for (var i = 0; i < message.details.length; i++)
		    {
		        var tr = document.createElement('tr');   
		        var td1 = document.createElement('td');
		        var td2 = document.createElement('td');
		        var td3 = document.createElement('td');
		        var text1 = document.createTextNode(message.details[i].debug);
		        var text2 = document.createTextNode(message.details[i].module);
		        var text3 = document.createTextNode(message.details[i].log);
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
				<li><a href="/Serverless/ServerDetails"> Manage Server</a></li>
				<li><a href="/Serverless/JSP/ADMIN/AddServer.jsp"> Add Server</a></li>
				<li><a href="/Serverless/VM">Manage VM</a></li>
				<li><a class="selected" href="/Serverless/Logs"> Platform Logs</a></li>
				<li><a href="/Serverless/JSP/ADMIN/AddAdmin.jsp"> Add Admin</a></li>
				<li><a href="/Serverless/JSP/ADMIN/Contact.jsp"> Contact Us</a></li>
				<li><a href="/Serverless/Logout"> Logout</a></li>
			</ul>
		</div>
		<div class="content" id="displayArea">
			<input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for module.." title="Type in a name">
			<script>
				function myFunction() {
				  var input, filter, table, tr, td, i;
				  input = document.getElementById("myInput");
				  filter = input.value.toUpperCase();
				  table = document.getElementById("myTable");
				  tr = table.getElementsByTagName("tr");
				  for (i = 0; i < tr.length; i++) {
				    td = tr[i].getElementsByTagName("td")[1];
				    if (td) {
				      if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
				        tr[i].style.display = "";
				      } else {
				        tr[i].style.display = "none";
				      }
				    }       
				  }
				}
			</script>
		</div>
	</div>
	<footer id="footerContent"></footer>
</body>
</html>