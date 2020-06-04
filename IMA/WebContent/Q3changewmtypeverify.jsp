<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change Meter Type</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
function check()
{
<c:set var="mtype" value="${customerInfo.meterType}"/>
var meterlbl = '<c:out value="${mtype}"/>';
var meterddboxIndex = document.getElementById("meters").selectedIndex;
var meterddboxText = document.getElementById("meters").options[meterddboxIndex].text;
if(meterlbl==meterddboxText)
{
	alert("Different Meter Type needs to be selected before proceeding");
	return false;
}else{
	//proceed
	return true;
}
}
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
			<h3>Water > <a href="Q3changewmtype.jsp">Change Type</a> > Water Meter Verify</h3>
		</div>
		<br style="clear: left;" />
	</div>
	
	<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Water Meter Verify" />
</jsp:include>	
	<div class="context">
<br>

<jsp:useBean id="customerInfo" class="main.com.ima.dev.dto.Q3CustomerInfo" scope="request">
</jsp:useBean>

<form action="Q3UpdateWaterMeterType" method="get" onsubmit="return check()">
	<table border="1" width="1024px">
			<tr>
				<td colspan="6" valign="middle" bgcolor="#001084"><h1>Installation Details</h1></td>
			</tr>
		<tr>
			<td><h2>WorkOrder ID:</h2></td>
			<td colspan="5"><h4><jsp:getProperty property="workorderID" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td><h2>First Name:</h2></td>
			<td colspan="5"><h4><jsp:getProperty property="firstName" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td><h2>Last Name:</h2></td>
			<td colspan="5"><h4><jsp:getProperty property="lastName" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td><h2>House Name:</h2></td>
			<td colspan="5"><h4><jsp:getProperty property="houseNumber" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td><h2>Number:</h2></td>
			<td colspan="5"><h4><jsp:getProperty property="houseName" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td><h2>Street:</h2></td>
			<td colspan="5"><h4><jsp:getProperty property="street" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td><h2>City:</h2></td>
			<td colspan="5"><h4><jsp:getProperty property="city" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td width="180px"><h2>Meter Serial:</h2></td>
			<td width="332px"><h4><jsp:getProperty property="meterSerial" name="customerInfo" /></h4></td>
			<td width="180px"><h2>Meter Type:</h2></td>
			<td width="332px" colspan="3"><h4><jsp:getProperty property="meterType" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td colspan="2"> </td>
			<td><h2>Change Meter Type</h2></td>
			<td colspan="3">
      		<select name="meters">
    		<c:forEach items="${meterTypes}" var="meter">
    			<c:choose>
    			<c:when test ="${meter.value==customerInfo.meterType}">
    				<option selected="selected" value="${meter.key}">${meter.value}</option>
  				</c:when>
  				<c:otherwise>
					<option value="${meter.key}">${meter.value}</option>
  				</c:otherwise>
				</c:choose>
    		</c:forEach>
			</select>
            </td>
		</tr>
		<tr>
			<td width="180px"><h2>RF Serial:</h2></td>
			<td width="332px"><h4><jsp:getProperty property="rfSerial" name="customerInfo" /></h4></td>
			<td width="180px"><h2>RF Type:</h2></td>
			<td width="332px" colspan="3"><h4><jsp:getProperty property="rfType" name="customerInfo" /></h4></td>
		</tr>
		<tr>
			<td colspan="6" align="right"><input class="btn" type="submit" value="Submit" /></td>		
		</tr>
	</table>
	<input type="hidden" name="hiddenPlaceID" value="${customerInfo.placeID}" />
	<input type="hidden" name="hiddenmeterSerial" value="${customerInfo.meterSerial}" />
	<input type="hidden" name="hiddenOldMeterType" value="${customerInfo.meterType}" />
	<input type="hidden" name="hiddenOldMeterTypeID" value="${customerInfo.meterTypeID}" />
	<input type="hidden" name="hiddenrfTypeID" value="${customerInfo.rfTypeID}" />
	<input type="hidden" name="hiddenwoID" value="${customerInfo.woID}" />
	</form>
</div>
	
<div class="bottom"></div>
		

</body>
</html>