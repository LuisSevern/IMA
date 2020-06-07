<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Swap Install Point Verification</title>
<link href="stylesheet/AMMCSS.css" rel="stylesheet" type="text/css" />
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
			<h3>Electricity > <a href="Q1changeinstallpoint.jsp">Swap Install Point</a> > Verify</h3>
		</div>
		<br style="clear: left;" />
	</div>

	<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Electricity > Swap Install Point" />
</jsp:include>


	<div class="context">
		<br>

		<jsp:useBean id="premiseInfo" class="main.java.com.ima.dev.dto.Q1Premise"
			scope="request">
		</jsp:useBean>

		<jsp:useBean id="premiseInfo2" class="main.java.com.ima.dev.dto.Q1Premise"
			scope="request">
		</jsp:useBean>

		<form action="Q1ChangeInstallPointServlet" method="get">
			<table border="1" width="1024px">
				<tr>
					<td width="250px"></td>
					<td width="387px" bgcolor="#216d4a"><h1>Premise A:</h1></td>
					<td width="387px" bgcolor="#216d4a"><h1>Premise B:</h1></td>
				</tr>
				<tr>
					<td><h2>WorkOrder ID:</h2></td>
					<td><h4><jsp:getProperty property="WORK_ORDER_ID"
							name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="WORK_ORDER_ID"
							name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>First Name:</h2></td>
					<td><h4><jsp:getProperty property="FIRSTNAME" name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="FIRSTNAME" name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Last Name:</h2></td>
					<td><h4><jsp:getProperty property="LASTNAME" name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="LASTNAME" name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>House Name:</h2></td>
					<td><h4><jsp:getProperty property="HOUSENAME" name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="HOUSENAME" name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Number:</h2></td>
					<td><h4><jsp:getProperty property="NUMBER" name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="NUMBER" name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Street:</h2></td>
					<td><h4><jsp:getProperty property="STREET" name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="STREET" name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>City:</h2></td>
					<td><h4><jsp:getProperty property="CITY" name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="CITY" name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Serial:</h2></td>
					<td><h4><jsp:getProperty property="SERIAL" name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="SERIAL" name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>TypeName:</h2></td>
					<td><h4><jsp:getProperty property="TYPENAME" name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="TYPENAME" name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Last Update:</h2></td>
					<td><h4><jsp:getProperty property="LASTUPDATEDATE"
							name="premiseInfo" /></h4></td>
					<td><h4><jsp:getProperty property="LASTUPDATEDATE"
							name="premiseInfo2" /></h4></td>
				</tr>
				<tr>
					<td colspan="3" align="right"><input class="btn" type="submit"
						value="Submit" /></td>
				</tr>
			</table>
			<input type="hidden" name="id1" value="${premiseInfo.ASSETID}" />
			<input type="hidden" name="id2" value="${premiseInfo2.ASSETID}" />			
			<input type="hidden" name="workOrderId1" value="${premiseInfo.WORK_ORDER_ID}">
			<input type="hidden" name="workOrderId2" value="${premiseInfo2.WORK_ORDER_ID}">
			<input type="hidden" name="meterSeralNumber1" value="${premiseInfo.SERIAL}">
			<input type="hidden" name="meterSeralNumber2" value="${premiseInfo2.SERIAL}">
		</form>
	</div>

	<div class="bottom">
		<img src="images/homepage-img4.jpg" alt="some_text" />
	</div>


</body>
</html>