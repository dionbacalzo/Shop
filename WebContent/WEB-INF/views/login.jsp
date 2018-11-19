<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="resources/css/login.css">
</head>
<body>
	<div id="content">
		<div class="col-12">

			<div class="col-2">&nbsp;</div>
			<div class="col-8">
				<h4>Login</h4>
				<form method="POST" id="loginForm"
					action="${pageContext.request.contextPath}/loginUser">
					<%--<input type="hidden" name="redirectId" value="${param.redirectId}" />  --%>
					<div class="col-12">
						<div class="col-2">User Name</div>
						<div class="col-10">
							<input type="text" name="username" />
						</div>
					</div>
					<div class="col-12">
						<div class="col-2">Password</div>
						<div class="col-10">
							<input type="password" name="password" />
						</div>
					</div>
					<div class="col-12">
						<div class="col-3">Remember Me:</div>
						<div class="col-9">
							<input type="checkbox" name="rememberMe" />
						</div>
					</div>
					<div class="col-12">
						<div class="col-2">
							<input id="loginSubmit" type="submit" value="Submit" />
						</div>
						<div class="col-10"> 
							<a href="${pageContext.request.contextPath}/">Cancel</a>
						</div>
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
				<h4>Sign Up</h4>
				<form method="POST" id="signupForm"
					action="${pageContext.request.contextPath}/signupUser">
					<%--<input type="hidden" name="redirectId" value="${param.redirectId}" />  --%>
					<div class="col-12">
						<div class="col-2">User Name</div>
						<div class="col-10">
							<input type="text" name="userName" />
						</div>
					</div>
					<div class="col-12">
						<div class="col-2">Password</div>
						<div class="col-10">
							<input type="password" name="password" />
						</div>
					</div>

					<div class="col-12">
						<div class="col-2">Role</div>
						<div class="col-10">
							<select name="role">
								<option value="USER">Basic</option>
								<option value="ADMIN">Admin</option>
							</select>
						</div>
					</div>

					<div class="col-12">
						<div class="col-2">
							<input id="signupSubmit" type="submit" value="Submit" />
						</div>
						<div class="col-10"> 
							<a href="${pageContext.request.contextPath}/">Cancel</a>
						</div>
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

	<script src="resources/js/thirdparty/jquery-3.3.1.min.js"></script>
	<script src="resources/js/login.js"></script>
</body>
</html>
