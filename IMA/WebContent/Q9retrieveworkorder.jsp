<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Retrieve Work Order</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript">
</script>
<script language="javascript" type="text/javascript">
function validate(evt) {
	  var theEvent = evt || window.event;
	  var key = theEvent.keyCode || theEvent.which;
	  key = String.fromCharCode( key );
	  var regex = /[0-9]|\./;
	  if( !regex.test(key) ) {
	    theEvent.returnValue = false;
	    if(theEvent.preventDefault) theEvent.preventDefault();
	  }
}
function checklength(arg1){
	var text = document.getElementById(arg1).value;
	if(text.length <= 0) 
	{ 
		alert('Device Serial Number is needed.'); 
		return false; 
	}
	return true;
}
</script>
</head>
<body>

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Device Work Order" />
</jsp:include>

	<div class="context">
		<br>

		<form action="Q9RetrieveWorkOrderServlet" method="get" onsubmit="return checklength('deviceSerialNumber')">
			<table border="1">
				<tr>
					<td colspan="2" width="500px" valign="middle" bgcolor="#001084"
						height="14px"><h1>WorkOrder Details:</h1>
					</td>
				</tr>
				<tr>
					<td><h2>Device Serial Number:</h2>
					</td>
					<td>
						<input class="txt" type="text" name="deviceSerialNumber" id="deviceSerialNumber"/>
					</td>
				</tr>
				<tr>
					<td colspan=2 align="right">
						<input class="btn" type="submit" value="Submit" />
				</tr>
			</table>
			<h5><c:out value="${requestScope.errormsg}"/></h5>
		</form>
	</div>
	<div class="bottom"></div>

	
</body>
</html>
