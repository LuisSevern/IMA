<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
<link href="stylesheet/AMMCSS.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript">
	
</script>
</head>
<body>
	<div class="topheader"></div>
	<div class="topmenuA"></div>

	<div class="horizontalcssmenu">
		<ul id="cssmenu1">
			<li style="border-left: 1px solid #202020;"><a href="">Home</a>
			</li>
			<li><a href="#">Electricity</a>
				<ul>
					<li><a href="Q1changeinstallpoint.jsp">Swap Install Point</a>
					</li>
					<li><a href="Q2changeinstallpointsingle.jsp">Change
							Install Point</a></li>
				</ul>
			</li>
			<li><a href="#">Water</a>
				<ul>
					<li><a href="Q3changewmtype.jsp">Change MIU/Meter Type</a></li>
					<li><a href="Q4changewmmiu.jsp">Change MIU/Meter Serial</a></li>
					<li><a href="Q5changemiuport.jsp">Change MIU Port</a></li>
					<li><a href="Q6changeinstallpointwater.jsp">Change Install
							Point</a></li>
					<li><a href="Q7swapinstallpointwater.jsp">Swap Install
							Point</a></li>
				</ul>
			</li>
			<li><a href="">View Details</a></li>
		</ul>
		<div class="toptitle">
			<h3>Error</h3>
		</div>
		<br style="clear: left;" />
	</div>

	<div class="topmenuC"></div>

	<div class="context">
		<br>
		<h5>Error Message:</h5>
		<h5><c:out value="${requestScope.result}"/></h5>
	</div>
	<div class="bottom"></div>
</body>
</html>