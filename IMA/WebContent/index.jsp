<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<c:choose>
		<c:when test="${sessionScope.username==null}">
			<jsp:forward page="Login.jsp"></jsp:forward>
		</c:when>
		<c:otherwise>
			<jsp:forward page="Login.jsp"></jsp:forward>
		</c:otherwise>
	</c:choose>
</body>
</html>