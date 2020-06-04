<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change Install Point</title>
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
			<h3>Water > <a href="Q8changeinstallpointsinglewater.jsp">Change Install Point</a> > Customer Details Verify</h3>
		</div>
		<br style="clear: left;" />
	</div>
	<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Customer Details Verify" />
</jsp:include>		

	
	
	<div class="context"><br>
	<jsp:useBean id="customerInfo" class="main.com.ima.dev.dto.Q6MWCustomerInfo"
		scope="request">
	</jsp:useBean>
	<jsp:useBean id="customerInfo2" class="main.com.ima.dev.dto.Q6MWCustomerInfo"
		scope="request">
	</jsp:useBean>
	<form action="Q8UpdateInstallPointSingleServlet" method="get">
			<table border="1">
				<tr>
					<td width="250px"></td>
					<td width="387px" bgcolor="#001084"><h1>Used Workorder:</h1></td>
					<td width="387px" bgcolor="#001084"><h1>Correct Workorder:</h1></td>
				</tr>
				<tr>
					<td><h2>WorkOrder ID:</h2></td>
					<td><h4><jsp:getProperty property="workOrderID"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="workOrderID"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>First Name:</h2></td>
					<td><h4><jsp:getProperty property="customerFirstName"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="customerFirstName"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Last Name:</h2></td>
					<td><h4><jsp:getProperty property="customerLastName"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="customerLastName"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>House Number:</h2></td>
					<td><h4><jsp:getProperty property="premiseNumber"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="premiseNumber"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>House Name:</h2></td>
					<td><h4><jsp:getProperty property="premiseHouseName"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="premiseHouseName"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Street:</h2></td>
					<td><h4><jsp:getProperty property="premiseStreet"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="premiseStreet"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>City:</h2></td>
					<td><h4><jsp:getProperty property="premiseCity"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="premiseCity"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Installed Meter SN:</h2></td>
					<td><h4><jsp:getProperty property="meterSerial"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="meterSerial"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td><h2>Installed MIU SN:</h2></td>
					<td><h4><jsp:getProperty property="miuSerial"
							name="customerInfo" /></h4></td>
					<td><h4><jsp:getProperty property="miuSerial"
							name="customerInfo2" /></h4></td>
				</tr>
				<tr>
					<td colspan="3" align="right"><input class="btn" type="submit"
						value="Submit" />
					</td>
				</tr>
			</table>
			<input type="hidden" name="hiddenMeterSN" value="${customerInfo.meterSerial}" />
			<input type="hidden" name="hiddenInstallPoint" value="${customerInfo2.installPoint}" />
			<input type="hidden" name="hiddenUsedSN" value="${customerInfo.workOrderID}" />
			<input type="hidden" name="hiddenUsedIP" value="${customerInfo.installPoint}" />
			<input type="hidden" name="hiddenCorrectSN" value="${customerInfo2.workOrderID}" />
			<input type="hidden" name="hiddenDeviceType" value="${customerInfo.deviceType}" />
			<input type="hidden" name="hiddenPlaceID" value="${customerInfo2.placeID}" />		
		</form>
	</div>
	<div class="bottom"></div>

	
</body>
</html>