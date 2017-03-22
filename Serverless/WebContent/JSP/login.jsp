<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
	<style type="text/css">
		html, body { height: 100%; width: 100%; margin: 0;padding:0;}
		header { height: 15%; width: 100%;}
		footer { height: 9%; width: 100%;}
		table {height:100%;width:60%}
		td {padding:3% 3% 3% 15%;font-size:120%}
		input {font-size:90%}	
	</style>
	<script> 
	function load_home() 
	{
     	document.getElementById("headerContent").innerHTML='<object style="height:100%;width:100%" type="text/html" data="header.html" ></object>';
     	document.getElementById("footerContent").innerHTML='<object style="height:100%;width:100%" type="text/html" data="footer.html" ></object>';
	} 
	</script> 
</head>
<body onload="load_home()">
	<header id="headerContent"></header>
	<div style="height:75.5%">
		<br><br>
		<div style="margin:0 auto;height:80%;width:70%;border:1px solid black;">
			<center><h2>Sign In</h2>
			<br>
			<form name="form1" action="LoginServlet">
				<table>
					<tr>
						<td>Username:</td>
						<td><input type="text" name="username" value="" required></td>	
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="password" value="" required></td>
					</tr>
					<tr>
						<td colspan="2" style="padding:5% 9% 3% 3%" align="right">
							<input type="submit" value=	"Login">
						</td>
					</tr>
					<tr>
						<td colspan="2" style="padding:5% 9% 3% 3%" align="right">
							<a href="">Forgot Password ?</a>&nbsp;&nbsp;&nbsp;
							<a href="registration.html">Sign Up</a>
						</td>
					</tr>
				</table>
			</form>
			</center>
		</div>
	</div>
	<footer id="footerContent"></footer>
</body>
</html>