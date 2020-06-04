<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Swap Module Connections</title>
<link href="stylesheet/AMMCSSW.css" rel="stylesheet" type="text/css" />
<link href="stylesheet/hMenu.css" rel="stylesheet" type="text/css" />
<script src="js/csshorizontalmenu.js" type="text/javascript">
	
</script>
<script language="javascript" type="text/javascript">
	function validate(evt) {
		var theEvent = evt || window.event;
		var key = theEvent.keyCode || theEvent.which;
		key = String.fromCharCode(key);
		var regex = /[0-9]|\./;
		if (!regex.test(key)) {
			theEvent.returnValue = false;
			if (theEvent.preventDefault)
				theEvent.preventDefault();
		}
	}
	function checklength(arg1, arg2){
		var text = document.getElementById(arg1).value;
		var text2 = document.getElementById(arg2).value;
		if(text.length <= 0 || text2.length <= 0) 
		{ 
			alert('Both WorkOrder IDs are needed.'); 
			return false; 
		}
		return true;
	}
	
	var ignoreSuffix = false;
	function changeSuffix(){
		d = document.getElementById("waterWoprefix_id").value;
		if (!ignoreSuffix){
			if(d == 1){
				document.getElementById("suffix").disabled = true;
				document.getElementById("suffix").value = 1;
			}if(d == 3){
				document.getElementById("suffix").disabled = false;
			}
		}
	}

	function changeWorkOrderInputs() {
		document.getElementById("waterWoprefix_id").value = 1;
		document.getElementById("suffix").value = 1;	
		if (document.getElementById("noSuffix").checked){
			ignoreSuffix = true;
			document.getElementById("suffix").style.display = 'none';
			document.getElementById("suffix").disabled = true;		
		} else {
			ignoreSuffix = false;
			document.getElementById("suffix").style.display = '';
			document.getElementById("suffix").disabled = true;		
		}
	}
	
	var ignoreSuffix2 = false;
	function changeSuffix2(){
		d = document.getElementById("waterWoprefix2_id").value;
		if (!ignoreSuffix2){
			if(d == 1){
				document.getElementById("suffix2").disabled = true;
				document.getElementById("suffix2").value = 1;
			}if(d == 3){
				document.getElementById("suffix2").disabled = false;
			}
		}
	}
	
	function changeWorkOrderInputs2() {
		document.getElementById("waterWoprefix2_id").value = 1;
		document.getElementById("suffix2").value = 1;	
		if (document.getElementById("noSuffix2").checked){
			ignoreSuffix2 = true;
			document.getElementById("suffix2").style.display = 'none';
			document.getElementById("suffix2").disabled = true;		
		} else {
			ignoreSuffix2 = false;
			document.getElementById("suffix2").style.display = '';
			document.getElementById("suffix2").disabled = true;		
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
			<h3>Water > Swap Module Connections</h3>
		</div>
		<br style="clear: left;" />
	</div>

	<div class="topmenuC"></div>
-->

<jsp:include page="menu.jsp" flush="true">
	<jsp:param name="breadCrumb" value="Water > Swap Module Connections" />
</jsp:include>

	<div class="context">
		<br>
		<form action="Q7WaterDetailsByWorkOrderServlet" method="get" onsubmit="return checklength('woRefNumber','woRefNumber2')">
			<table border="1" width="1024px">
				<tr>
					<td colspan="2" width="487px" valign="middle" bgcolor="#001084"
						height="14px"><h1>Module Details:</h1>
					</td>
					<td width="50px" height="14px"></td>
					<td colspan="2" width="487px" valign="middle" bgcolor="#001084"
						height="14px"><h1>Module Details:</h1>
					</td>
				</tr>
				<tr>
					<td valign="middle" valign="bottom"><h2>WorkOrder ID:</h2>
					</td>
					
					<td>
						<table>
							<tr>
								<td>
									<h2>No Suffix</h2>
								</td>
								<td>
									<input type="checkbox" name="noSuffix" id="noSuffix" onclick="changeWorkOrderInputs()" >									
								</td>
							</tr>
							<tr>
								<td> 
									<select name="woRefPrefix" id="waterWoprefix_id" onchange="changeSuffix()">
										<option value="1">EXT</option>
										<option value="3">DUP</option>
									</select> 
								</td>
								<td>
									<input class="txt" type="text" name="woRefNumber" id="woRefNumber" onkeypress='validate(event)'/>
								</td>
								<td>
								</td>
								<td width="100px"> 
									<select name="suffix" id="suffix" disabled="disabled">
										<option value="1">01</option>
										<option value="2">02</option>
										<option value="3">03</option>
										<option value="4">04</option>
										<option value="5">05</option>
									</select>
								</td>
							</tr>
						</table>							
					</td>
										
					<td width="50px"></td>
					<td><h2>WorkOrder ID:</h2>
					</td>
					
					<td>
						<table>
							<tr>
								<td>
									<h2>No Suffix</h2>
								</td>
								<td>
									<input type="checkbox" name="noSuffix2" id="noSuffix2" onclick="changeWorkOrderInputs2()" >									
								</td>
							</tr>
							<tr>
								<td> 
									<select name="woRefPrefix2" id="waterWoprefix2_id" onchange="changeSuffix2()">
										<option value="1">EXT</option>
										<option value="3">DUP</option>
									</select> 
								</td>
								<td>
									<input class="txt" type="text" name="woRefNumber2" id="woRefNumber2" onkeypress='validate(event)'/>
								</td>
								<td>
								</td>
								<td width="100px"> 
									<select name="suffix2" id="suffix2" disabled="disabled">
										<option value="1">01</option>
										<option value="2">02</option>
										<option value="3">03</option>
										<option value="4">04</option>
										<option value="5">05</option>
									</select>
								</td>
							</tr>
						</table>							
					</td>
					
				</tr>
				<tr>
					<td colspan="6" align="right"><input class="btn" type="submit"
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