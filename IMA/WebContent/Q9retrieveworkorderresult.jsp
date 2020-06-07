<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Retrieve Work Order</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript">
</script>
</head>
<body>

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Device Work Order" />
</jsp:include>

	<div class="context">
		<br>

<jsp:useBean id="device" class="main.java.com.ima.dev.dto.Q9DeviceInfo" scope="request">
</jsp:useBean>

	<form action="Q9RetrieveWorkOrderServlet" method="get">
	<table border="1" width="1024px">
		<tr>
			<td colspan="3" valign="middle" bgcolor="#001084"><h1>Device Work Order</h1></td>
		</tr>
		<tr>
			<td width="200px"><h2>Serial:</h2></td>
			<td><h4><jsp:getProperty property="serial" name="device" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>Device Type:</h2></td>
			<td><h4><jsp:getProperty property="deviceType" name="device" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>Device Placeid:</h2></td>
			<td><h4><jsp:getProperty property="placeId" name="device" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>Status:</h2></td>
			<td><h4><jsp:getProperty property="status" name="device" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>Work Order Prefix:</h2></td>
			<td><h4><jsp:getProperty property="woreferprefix" name="device" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>Device Installation Work Order:</h2></td>
			<td><h4><jsp:getProperty property="worefernumber" name="device" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>Creation Date:</h2></td>
			<td><h4><jsp:getProperty property="creationdate" name="device" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>Last Update Date:</h2></td>
			<td><h4><jsp:getProperty property="lastupdate" name="device" /></h4></td>
		</tr>
	</table>
	</form>
	</div>
	<div class="bottom"></div>


</body>
</html>