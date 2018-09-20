<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> <tiles:getAsString name="title" /></title>
<link href="resources/css/bootstrap.css" rel="stylesheet"></link>
<script src="resources/js/thirdparty/jquery-3.3.1.min.js"></script>
<script>
	var contextPath = "${pageContext.request.contextPath}";
</script>
</head>
<body>

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

	<div id="header" class="col-12">
		<tiles:insertAttribute name="header" />
	</div>

	<div class="col-3">&nbsp;</div>

	<div class="col-6">
		<tiles:insertAttribute name="menu" />
		
		<div class="col-12">
			<tiles:insertAttribute name="body" />
		</div>
	</div>

	<div class="col-3">&nbsp;</div>

	<div id="footer" class="col-12">
		<tiles:insertAttribute name="footer" />
	</div>
	<script src="resources/js/default.js"></script>
</body>
</html>
