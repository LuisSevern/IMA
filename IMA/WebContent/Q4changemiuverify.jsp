<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change MIU Serial</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
function tx4confirm(){
	var ondeo = '<c:out value="${customerInfo.rfType}"/>';
	if(ondeo == 'Ondeo.Transmitter-Tx4')
	{
		var r=confirm("Note that by changing this MIU Serial Number, you will be affecting all meters related to this MIU! Click OK to continue or Cancel to stop change.");
		if(r==true)
		{
		  	return true;
		}
		else
		{
		  	return false;
		}
	}
}
function ChangeCase(elem)
{
    elem.value = elem.value.toUpperCase();
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
			<h3>
				Water > <a href="Q4changewmmiu.jsp">Change Serial</a> > Water MIU Serial Verify 
			</h3>
		</div>
		<br style="clear: left;" />
	</div>

	<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Water MIU Serial Verify" />
</jsp:include>	
	

	<div class="context">
		<br>

		<jsp:useBean id="customerInfo" class="main.java.com.ima.dev.dto.Q3CustomerInfo"
			scope="request">
		</jsp:useBean>

		<form action="Q4UpdateMiuModuleServlet" method="get">
			<table border="1" width="1024px">
			<tr>
				<td colspan="4" valign="middle" bgcolor="#001084"><h1>Installation Details</h1></td>
			</tr>
				<tr>
					<td><h2>WorkOrder ID:</h2></td>
					<td colspan="4"><h4><jsp:getProperty property="workorderID"
							name="customerInfo" /></h4></td>
				</tr>
				<tr>
					<td><h2>First Name:</h2></td>
					<td colspan="4"><h4><jsp:getProperty property="firstName" name="customerInfo" /></h4></td>
				</tr>
				<tr>
					<td><h2>Last Name:</h2></td>
					<td colspan="4"><h4><jsp:getProperty property="lastName" name="customerInfo" /></h4></td>
				</tr>
				<tr>
					<td><h2>House Name:</h2></td>
					<td colspan="4"><h4><jsp:getProperty property="houseNumber"
							name="customerInfo" /></h4></td>
				</tr>
				<tr>
					<td><h2>Number:</h2></td>
					<td colspan="4"><h4><jsp:getProperty property="houseName" name="customerInfo" /></h4></td>
				</tr>
				<tr>
					<td><h2>Street:</h2></td>
					<td colspan="4"><h4><jsp:getProperty property="street" name="customerInfo" /></h4></td>
				</tr>
				<tr>
					<td><h2>City:</h2></td>
					<td colspan="4"><h4><jsp:getProperty property="city" name="customerInfo" /></h4></td>
				</tr>
				<tr>
					<td><h2>Meter Serial:</h2></td>
					<td><h4><jsp:getProperty property="meterSerial"
							name="customerInfo" /></h4></td>
					<td><h2>Meter Type:</h2></td>
					<td><h4><jsp:getProperty property="meterType" name="customerInfo" /></h4></td>
				</tr>
				<tr>
					<td><h2>RF Serial:</h2></td>
					<td><c:set var="miuvar" value="${customerInfo.rfSerial}" /> <input
						class="txt" type="text" name="changedRFSerial"
						value="<c:out value="${miuvar}"/>" onkeyup="ChangeCase(this);"/>
					</td>
					<td><h2>RF Type:</h2></td>
					<td><h4><jsp:getProperty property="rfType" name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="right"><input class="btn" type="submit"
						value="Submit" onclick="return tx4confirm()"/>
					</td>
				</tr>
			</table>
			<input type="hidden" name="hiddenWoID" value="${customerInfo.woID}" />
			<input type="hidden" name="hiddenOldRFSerial" value="${customerInfo.rfSerial}" />
			<input type="hidden" name="hiddenRFTypeName" value="${customerInfo.rfType}" />
			<input type="hidden" name="hiddenRFTypeID" value="${customerInfo.rfTypeID}" />
			<input type="hidden" name="hiddenworkorderid" value="${customerInfo.workorderID}" />
		</form>
	</div>
	<div class="bottom"></div>
		

</body>
</html>