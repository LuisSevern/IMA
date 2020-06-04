<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change MIU Port</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
function checklength(arg1){
	var text = document.getElementById(arg1).value;
	if(text.length <= 0) 
	{ 
		alert('RFID is needed.'); 
		return false; 
	}
	return true;
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
			<h3>Water > Change MIU Port</h3>
		</div>
		<br style="clear: left;" />
	</div>

	<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Change MIU Port" />
</jsp:include>


	<div class="context">
		<br>

		<form action="Q5ViewMiuMeterRelationServlet" method="get" onsubmit="return checklength('rfid')">
			<table border="1">
				<tr>
					<td colspan="2" valign="middle" bgcolor="#001084" height="14px"><h1>MIU
							Details:</h1>
					</td>
				</tr>
				<tr>
					<td width="200px"><h2>Enter MIU Serial:</h2>
					</td>
					<td><input class="txt" type="text" name="rfid" id="rfid" /></td>
				</tr>
				<tr>
					<td colspan=2 align="right"><input class="btn" type="submit"
						name="changemiuport" value="RF Details" />
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