<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
</head>
<body>
	<div id="main-navbar" class="col-12">
		<security:authorize access="isAuthenticated()" var="isLoggedIn" />
		<c:choose>
			<c:when test="${isLoggedIn}">
				<ul>
					<li><a id="menu-home" href="content">Home</a></li>
					<li><a id="menu-upload" href="upload">Upload</a></li>
					Hello, <security:authentication property="principal.username" />
					<form name="logoutForm" method="post" action="${pageContext.request.contextPath}/logout">
						<input name="logout" type="submit" value="logout">
					</form>
				</ul>
			</c:when>
			<c:otherwise>
				<ul>
					<li><a id="menu-home" href="content">Home</a></li>
					<li><a id="menu-login" href="login">Login</a></li>
				</ul>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>