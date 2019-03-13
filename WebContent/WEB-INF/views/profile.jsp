<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="resources/css/profile.css">
</head>
<body>
	<div id="content">
		<div class="col-12">
			<div class="col-1">&nbsp;</div>
			<div class="col-10">
				<h4>Profile Page</h4>
				<form method="POST" id="profileForm"
					action="${pageContext.request.contextPath}/updateUser">
					<div class="col-12">
						<h4>Change Information</h4>
					</div>
					<div class="col-12">
						<div class="col-4">First Name</div>
						<div class="col-8">
							<input type="text" name="firstname" />
						</div>
					</div>
					<div class="col-12">
						<div class="col-4">Last Name</div>
						<div class="col-8">
							<input type="text" name="lastname" />
						</div>
					</div>					

					<div class="col-12">
						<div class="col-4">
							<input id="profileSubmit" type="submit" value="Update Profile" />
						</div>
						<div class="col-8">
							<a href="${pageContext.request.contextPath}/">Cancel</a>
						</div>
					</div>
					<div class="col-12">
						<p id="profileResult"></p>
					</div>
				</form>
				
				<form method="POST" id="passwordForm"
					action="${pageContext.request.contextPath}/updatePassword">
					
					<div class="col-12">
						<h4>Change Password</h4>
					</div>
					<div class="col-12">
						<div class="col-4">Current Password</div>
						<div class="col-8">
							<input type="password" name="password" />
						</div>
					</div>
					<div class="col-12">
						<div class="col-4">New Password</div>
						<div class="col-8">
							<input type="password" name="newPassword" />
						</div>
					</div>
					<div class="col-12">
						<div class="col-4">Retype New Password</div>
						<div class="col-8">
							<input type="password" name="newPasswordRetype" />
						</div>
					</div>

					<div class="col-12">
						<div class="col-4">
							<input id="passwordSubmit" type="submit" value="Update Password" />
						</div>
						<div class="col-8">
							<a href="${pageContext.request.contextPath}/">Cancel</a>
						</div>
					</div>
					<div class="col-12">
						<p id="passwordResult"></p>
					</div>
				</form>
			</div>
			<div class="col-1">&nbsp;</div>
		</div>
	</div>
	</div>

	<script src="resources/js/thirdparty/jquery-3.3.1.min.js"></script>
	<script src="resources/js/profile.js"></script>
</body>
</html>
