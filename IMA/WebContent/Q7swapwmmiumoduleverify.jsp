<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Swap MIU Module Verify</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript">
</script>
</head>
<body>

<!--
<div class="topheader"></div>
	<div class="topmenuA"></div>
<div class="horizontalcssmenu">
				<ul id="cssmenu1">
			<li style="border-left: 1px solid #202020;"><a href="">Logout</a>
			</li>
			<li><a href="#">Electricity</a>
				<ul>
					<li><a href="Q1changeinstallpoint.jsp">Swap Install Point</a>
					</li>
					<li><a href="Q2changeinstallpointsingle.jsp">Change Install Point</a>
					</li>
				</ul></li>
			<li><a href="#">Water</a>
				<ul>
					<li><a href="Q3changewmtype.jsp">Change MIU/Meter Type</a>
					</li>
					<li><a href="Q4changewmmiu.jsp">Change MIU/Meter Serial</a>
					</li>
					<li><a href="Q5changemiuport.jsp">Change MIU Port</a>
					</li>
					<li><a href="Q6changeinstallpointwater.jsp">Swap Install Point</a>
					</li>
					<li><a href="Q8changeinstallpointsinglewater.jsp">Change Install Point</a>
					</li>
					<li><a href="Q7swapwmmiumodule.jsp">Swap Module Connection</a>
					</li>
				</ul></li>
		</ul>
		<div class="toptitle">
			<h3>Water > <a href="Q7swapwmmiumodule.jsp">Swap Module Connections</a> > Work Order Details</h3>
		</div>
		<br style="clear: left;" />
	</div>

<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Work Order Details" />
</jsp:include>		

	<div class="context">
		<br>

<jsp:useBean id="woInfo" class="main.com.ima.dev.dto.Q7SwapModule" scope="request">
</jsp:useBean>

<jsp:useBean id="woInfo2" class="main.com.ima.dev.dto.Q7SwapModule" scope="request">
</jsp:useBean>
<form action="Q7UpdateMIUMeterRelationServlet" method="get">
	<table border="1" width="1024px">
		<tr>
			<td colspan="3" valign="middle" bgcolor="#001084"><h1>Water WorkOrder Details</h1></td>
		</tr>
		<tr>
			<td width="200px"><h2>WorkOrder ID:</h2></td>
			<td><h4><jsp:getProperty property="workorder" name="woInfo" /></h4></td>
			<td><h4><jsp:getProperty property="workorder" name="woInfo2" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>Meter Serial:</h2></td>
			<td><h4><jsp:getProperty property="metersn" name="woInfo" /></h4></td>
			<td><h4><jsp:getProperty property="metersn" name="woInfo2" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>MIU Serial:</h2></td>
			<td><h4><jsp:getProperty property="miusn" name="woInfo" /></h4></td>
			<td><h4><jsp:getProperty property="miusn" name="woInfo2" /></h4></td>
		</tr>
		<tr>
			<td width="200px"><h2>MIU Port:</h2></td>
			<td><h4><jsp:getProperty property="miuport" name="woInfo" /></h4></td>
			<td><h4><jsp:getProperty property="miuport" name="woInfo2" /></h4></td>
		</tr>
		<tr>
			<td colspan="3" align="right"><input class="btn" type="submit" value="Swap" /></td>		
		</tr>
		</table>
			<input type="hidden" name="target" value="${woInfo.target}" />
			<input type="hidden" name="devrelid" value="${woInfo.devrelid}" />
			<input type="hidden" name="target2" value="${woInfo2.target}" />
			<input type="hidden" name="devrelid2" value="${woInfo2.devrelid}" />
		</form>
	</div>
	<div class="bottom"></div>
		

</body>
</html>