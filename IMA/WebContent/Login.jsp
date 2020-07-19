<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Form</title>
<link href="stylesheet/AMMCSSlogin.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<form action="checkCredentialServlet" method="post">
	<h2>v702020</h2>
		<div class="topheader"></div>		
		<div class="topcontent">			
			<div class="innerdiv">
			<br>
			<table border="0" cellpadding="10">
			<tr>
				<td>
					<h1>Username:</h1></td>
				<td><input class="txt" type="text" name="username"><br>
				</td>
			</tr>
			<tr>
				<td>
					<h1>Password:</h1></td>
				<td><input class="txt" type="password" name="password"><br>
				<h3><c:out value="${requestScope.errormsg}"/></h3></td>
			</tr>
			<tr>
				<td colspan="2">
				<input class="btn" type="submit" value="Submit"></td>
			</tr>
		</table>
			</div>
		</div>
		<div class="topfooter"></div>		
	</form>
</body>
</html>