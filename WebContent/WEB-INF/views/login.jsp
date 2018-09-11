<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/login.css">

<script type="text/javascript">
	//Set Global Variables Here 
	var contextPath = "${pageContext.request.contextPath}";
</script>

<title>Shop Display: Login</title>
</head>
<body>
	<div id="header" class="col-12">
		<h1>SHOP</h1>
	</div>

	<div class="col-3">&nbsp;</div>

	<div class="col-6">
		<div id="main-navbar" class="col-12">
			<ul>
				<li><a href="content">Home</a></li>
				<li><a class="active">Login</a></li>
				<li><a href="upload">Upload</a></li>
			</ul>
		</div>
		
		<div class="col-12">
			<div id="content">
				<div class="col-12">
				
				<div class="col-2">&nbsp;</div>
				<div class="col-8">
					<h4> Login </h4>
					<form method="POST" id="loginForm"
						action="${pageContext.request.contextPath}/loginUser">
						<%--<input type="hidden" name="redirectId" value="${param.redirectId}" />  --%>
						<div class="col-12">
							<div class="col-2">
								User Name
							</div>
							<div class="col-10">
								<input type="text" name="userName" />
							</div>
						</div>
						<div class="col-12">
							<div class="col-2">
								Password
							</div>
							<div class="col-10">
								<input type="password" name="password" />
							</div>
						</div>
						<div class="col-12">
							<input id="loginSubmit" type="submit" value="Submit" /> 
							<a href="${pageContext.request.contextPath}/">Cancel</a>
						</div>
						<div class="col-12">
							<p id="loginResult"></p>
						</div>
					</form>
				</div>
				<div class="col-2">&nbsp;</div>
				</div>
				
				<div class="col-12">
				<div class="col-2">&nbsp;</div>
				<div class="col-8">
					<h4> Sign Up </h4>
					<form method="POST" id="signupForm"
						action="${pageContext.request.contextPath}/signupUser">
						<%--<input type="hidden" name="redirectId" value="${param.redirectId}" />  --%>
						<div class="col-12">
							<div class="col-2">
								User Name
							</div>
							<div class="col-10">
								<input type="text" name="userName" />
							</div>
						</div>
						<div class="col-12">
							<div class="col-2">
								Password
							</div>
							<div class="col-10">
								<input type="password" name="password" />
							</div>
						</div>
						<div class="col-12">
							<input id="signupSubmit" type="submit" value="Submit" /> 
							<a href="${pageContext.request.contextPath}/">Cancel</a>
						</div>
						<div class="col-12">
							<p id="signupResult"></p>
						</div>
					</form>
				</div>
				<div class="col-2">&nbsp;</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-3">&nbsp;</div>

	<div id="footer" class="col-12">
		<h2>Copyright 2018</h2>
	</div>
	
	<script src="resources/js/thirdparty/jquery-3.3.1.min.js"></script>
	<script src="resources/js/login.js"></script>
</body>
</html>
