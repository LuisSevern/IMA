<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change MIU Type</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
function check() {
	var miulbl = document.getElementById('hiddenrfTypeID').value;
	var miuddboxIndex = document.getElementById("mius").value;
	if(miulbl==miuddboxIndex){
		alert("Different MIU Type needs to be selected before proceeding");
		return false;
	}else{
		if (miuddboxIndex == 16){
			if (portSelected == 0){
				alert ("Please select one port to place the meter: " + document.getElementById('hiddenMeterSerialNumber').value);
				return false;
			} else if (portSelected > 1){
				alert ("Please select only one port for the meter: " + document.getElementById('hiddenMeterSerialNumber').value);
				return false;
			}
		}
		document.form.submit();
		//proceed
/*		var r=confirm("Are you sure you want to change from " +  miulbl  + " to " + miuddboxText);
			if(r==true){
		  		return true;
			}
			else{
		  		return false;
			}*/
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
			<h3>
				Water > <a href="Q3changewmtype.jsp">Change Type</a> > Water MIU
				Verify
			</h3>
		</div>
		<br style="clear: left;" />
	</div>

	<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Change Type" />
</jsp:include>


	<div class="context">
		<br>

		<jsp:useBean id="customerInfo" class="main.java.com.ima.dev.dto.Q3CustomerInfo"
			scope="request">
		</jsp:useBean>

		<form name="form" id="form" action="Q3UpdateMiuModuleTypeServlet" method="get">
			<table border="1" width="1024px">
				<tr>
					<td colspan="5" valign="middle" bgcolor="#001084"><h1>Installation
							Details</h1>
					</td>
				</tr>
				<tr>
					<td><h2>WorkOrder ID:</h2>
					</td>
					<td colspan="5"><h4><jsp:getProperty
								property="workorderID" name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td><h2>First Name:</h2>
					</td>
					<td colspan="5"><h4><jsp:getProperty property="firstName"
								name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td><h2>Last Name:</h2>
					</td>
					<td colspan="5"><h4><jsp:getProperty property="lastName"
								name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td><h2>House Name:</h2>
					</td>
					<td colspan="5"><h4><jsp:getProperty
								property="houseNumber" name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td><h2>Number:</h2>
					</td>
					<td colspan="5"><h4><jsp:getProperty property="houseName"
								name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td><h2>Street:</h2>
					</td>
					<td colspan="5"><h4><jsp:getProperty property="street"
								name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td><h2>City:</h2>
					</td>
					<td colspan="5"><h4><jsp:getProperty property="city"
								name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td width="180px"><h2>Meter Serial:</h2>
					</td>
					<td width="332px"><h4><jsp:getProperty
								property="meterSerial" name="customerInfo" /></h4>
					</td>
					<td width="180px"><h2>Meter Type:</h2>
					</td>
					<td width="332px" colspan="2"><h4><jsp:getProperty
								property="meterType" name="customerInfo" /></h4>
					</td>
				</tr>
				<tr>
					<td><h2>MIU Serial:</h2>
					</td>
					<td><h4><jsp:getProperty property="rfSerial"
								name="customerInfo" /></h4></td>
					<td><h2>MIU Type:</h2>
					</td>
					<td width="200px" colspan="2"><h4><jsp:getProperty
								property="rfType" name="customerInfo" /></h4> 
								</td>
				</tr>
				<tr>
					<td colspan="2">
						<div id="divPorts" style="display: none;">
							<c:forEach var='currentMeter' items='${customerInfo.waterMetersAssociated}' varStatus="status">
								<c:if test="${currentMeter == '-1'}">
									<input type="checkbox" name="portName_<c:out value='${status.count}'/>" id="portName_<c:out value='${status.count}'/>" 
										value="${currentMeter}" onchange="selectPort(this.id);"> 
								</c:if>
								<c:if test="${currentMeter != '-1'}">
									<input type="checkbox" name="portName_<c:out value='${status.count}'/>" id="portName_<c:out value='${status.count}'/>" 
										value="${currentMeter}" onchange="selectPort(this.id);" checked  disabled> 
								</c:if>
								port <c:out value='${status.count}'/> 							
							</c:forEach>
						</div>
					</td>
					<td><h2>Change MIU Type</h2>
					</td>
					<td><select name="mius" id='mius' onchange="showPorts(this.value)">
							<c:forEach items="${miuTypes}" var="miu">

								<c:choose>
									<c:when test="${miu.value==customerInfo.rfType}">
										<option selected="selected" value="${miu.key}">${miu.value}</option>
									</c:when>
									<c:otherwise>
										<option value="${miu.key}">${miu.value}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
					</select>
					</td>
				</tr>				
				<tr>
					<td colspan="6" align="right"><input class="btn" type="button"
						value="Submit" onclick="return check();"/></td>
				</tr>
			</table>
			<input type="hidden" name="hiddenrfSerial" value="${customerInfo.rfSerial}" />
			<input type="hidden" name="hiddenoldrfName" value="${customerInfo.rfType}" />
			<input type="hidden" name="hiddenrfTypeID" id='hiddenrfTypeID' value="${customerInfo.rfTypeID}" />
			<input type="hidden" name="hiddenwoID" value="${customerInfo.woID}" />
			<input type="hidden" name="hiddenworkorderid" value="${customerInfo.workorderID}" />
			<input type="hidden" name="hiddendevrelid" value="${customerInfo.relationshipID}" />
			
			<input type="hidden" name="hiddenMeterSerialNumber" id="hiddenMeterSerialNumber" value="${customerInfo.meterSerial}" />
			<input type="hidden" name="hiddenMeterTypeId" value="${customerInfo.meterTypeID}" />
		</form>
	</div>

	<div class="bottom"></div>

	<script type="text/javascript">
		var portSelected = 0;
		function showPorts(valueSelected){
			if (valueSelected == 16){
				document.getElementById("divPorts").style.display = 'block';
			} else {
				document.getElementById("divPorts").style.display = 'none';
			}
		}
		function selectPort(port){
			if (document.getElementById(port).checked){
				portSelected = portSelected + 1;
			} else {
				portSelected = portSelected - 1;
			}
		}
	</script>
</body>
</html>