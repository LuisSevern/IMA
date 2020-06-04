<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change MIU Port</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript">
</script>
<script language="javascript">
/*function init() {
	if (!document.getElementById) return;
	var submitObj = document.getElementById('sub');
	submitObj.disabled = true;
}*/
function enableDisable(bEnable, textBoxID) {
		document.getElementById(textBoxID).disabled = !bEnable;
}
function init() {
	document.getElementById('sub').disabled = false;
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
				Water > <a href="Q5changemiuport.jsp">Change MIU Port</a> > MIU Port
				Details
			</h3>
		</div>
		<br style="clear: left;" />
	</div>

	<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > MIU Port Details" />
</jsp:include>


	<div class="context">
		<br>
		<form action="Q5UpdateMiuMeterRelationServlet" method="get">
			<table border="1" width="1024px">
				<tr>
					<td colspan="5" valign="middle" bgcolor="#001084"><h1>Installation
							Details</h1>
					</td>
				</tr>
				<tr>
					<td><h2>Device Relation:</h2>
					</td>
					<td><h2>MIU Serial:</h2>
					</td>
					<td><h2>Meter Serial:</h2>
					</td>
					<td><h2>Port:</h2>
					</td>
					<td><h2>Change Port?</h2>
					</td>
				</tr>
				<c:forEach items="${miuInfo}" var="current">
					<tr>
						<c:set var="miuSS" value="${fn:substring(current.miuSerial, 1, 2)}" />
						<c:set var="type" value="${current.miuType}" />
						<td><h4><c:out value="${current.id}~${current.woid}" /></h4>
						</td>
						<td><h4><c:out value="${current.miuSerial}" /> - <c:out value="${current.miuType}" /></h4>
						</td>
						<td><h4><c:out value="${current.meterSerial}" /></h4>
						</td>
						<td><h4><c:out value="${current.port}" /></h4>
						</td>
						<td>
							<c:if test="${current.miuType == 'Ondeo.Transmitter-Tx4' && miuSS=='5'}">							
							<input type="checkbox" name="chk" value="${current.id}~${current.woid}"
							onclick="enableDisable(this.checked, '${current.id}${current.woid}')">
								<select name="${current.id}~${current.woid}"  id="${current.id}${current.woid}"  disabled="disabled">
									<option value='1'>1</option>
									<option value='2'>2</option>
									<option value='3'>3</option>
									<option value='4'>4</option>
								</select>
								<script>window.onload = init;</script>			
							</c:if></td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="3">
      				
      <c:choose>
      <c:when test="${type=='Ondeo.Transmitter-Tx1' && miuSS!='5'}">
      		<h5>Only Tx4 Modules have different Ports, Tx1 Modules have a single port and cannot be changed</h5>
      </c:when>
      <c:when test="${type=='Ondeo.Transmitter-Tx1' && miuSS=='5'}">
      		<h5>Device Type is Tx1 while Serial Number represents a Tx4 MIU - please Confirm prior to modifications</h5>
      </c:when>
      <c:when test="${type=='Ondeo.Transmitter-Tx4' && miuSS!='5'}">
      		<h5>Device Type is Tx4 while Serial Number represents a Tx1 MIU - please Confirm prior to modifications</h5>
      </c:when>
      <c:otherwise>
      <!-- otherwise no message -->
      </c:otherwise>
	  </c:choose>	
	  
					</td>
					<td colspan="2" align="right"><input class="btn" name="sub" id="sub" type="submit" disabled="disabled"
						value="Submit" />
					</td>
				</tr>
			</table>
			<h5>
				<c:out value="${requestScope.errormsg}" />
			</h5>
		</form>
	</div>
	<div class="bottom"></div>
		

</body>
</html>