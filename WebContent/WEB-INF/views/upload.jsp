<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Shop Upload: Upload</title>
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/upload.css">
<link rel="stylesheet" href="resources/css/pagination.css">

<script type="text/javascript">
	//Set Global Variables Here 
	var contextPath = "${pageContext.request.contextPath}";
</script>

<script id="itemTemplate" type="text/x-handlebars-template">
	<%@ include file="/WEB-INF/views/partials/itemTable.jsp" %>
</script>
</head>

<body>
	<div id="header" class="col-12">
		<h1>SHOP</h1>
	</div>

	<!-- The Modal -->
	<div id="confirmModal" class="modal col-12">
		<!-- Modal content -->
		<div class="modal-content">
			<div class="modal-header">
				<span class="close">&times;</span>
				Confirm
			</div>
			<div class="modal-body">
				<p>Are you sure?</p>
			</div>
			<div class="modal-footer">
				<button id="modal-ok">ok</button>
				<button id="modal-cancel">cancel</button>
			</div>
		</div>
	</div>

	<div class="col-3">&nbsp;</div>

	<div class="col-6">
		<div id="main-navbar" class="col-12">
			<ul>
				<li><a href="content">Home</a></li>
				<li><a href="login">Login</a></li>
				<li><a class="active">Upload</a></li>
			</ul>
		</div>
		<div id="content" class="col-12">
			<div class="col-1">&nbsp;</div>
			<div class="col-10">
				<div class="title">Upload Items</div>

				<form id="uploadform" type="file" method="POST"
					action="${pageContext.request.contextPath}/uploadItems"
					enctype="multipart/form-data" class="col-12">
					<div class="col-12">
						<input id="uploadFileInput" class="col-5" type="file" name="file"
							accept=".txt">
						<div class="col-7">&nbsp;</div>
					</div>
					<input id="uploadFileSubmit" type="submit">
					<div class="col-12">
						<p id="uploadResult"></p>
					</div>
				</form>
			</div>
			<div class="col-1">&nbsp;</div>
		
			<div class="col-12">
				<div id='loader'></div>
				<div class="col-1">&nbsp;</div>
				<div id="itemTableContainer" class="col-10"></div>
				<div class="col-1">&nbsp;</div>
			</div>
			<div class="col-12">
				<div class="col-1">&nbsp;</div>
				<div id="pagination" class="col-10"></div>
				<div class="col-1">&nbsp;</div>
			</div>
		</div>
	</div>

	<div class="col-3">&nbsp;</div>

	<div id="footer" class="col-12">
		<h2>Copyright 2018</h2>
	</div>

	<script src="resources/js/thirdparty/jquery-3.3.1.min.js"></script>
	<script src="resources/js/thirdparty/handlebars-v4.0.11.js"></script>
	<script src="resources/js/thirdparty/pagination.min.js"></script>
	<script src="resources/js/upload.js"></script>
</body>
</html>