//upload form

var REPORT_ROW_TOTAL = 10;
var $uploadform = $("#uploadform");
var $uploadResult = $("#uploadResult");
var $itemTableContainer = $("#itemTableContainer");
var $pagination = $('#pagination');
var $loader = $('#loader');
var successCss = "success";
var warningCss = "warning";

function initializeUploadForm() {
	$uploadform.submit(function(event) {
		event.preventDefault();

		$uploadResult.text("");

		// add css display for the result
		var $uploadFileInput = $("#uploadFileInput");
		if ($uploadResult.hasClass(warningCss)) {
			$uploadResult.removeClass(warningCss);
		}
		if ($uploadResult.hasClass(successCss)) {
			$uploadResult.removeClass(successCss);
		}

		// upload load validation before submit
		if ($uploadFileInput.val().length === 0) {
			$uploadResult.addClass(warningCss);
			$uploadResult.text("No file found");

		} else {
			var formData = new FormData();
			formData.append("file", $uploadFileInput[0].files[0]);

			$.ajax({
				url : $uploadform.attr("action"),
				data : formData,
				contentType : false,
				processData : false,
				cache : false,
				method : 'POST',
				type : 'POST', // For jQuery < 1.9
				beforeSend : function() {
					$loader.show();
				},
				complete : function() {
					$loader.hide();
				},
				success : function(data) {
					$uploadResult.addClass(successCss);
					$uploadResult.text(data);
					// reset input form
					$uploadFileInput.val("");
					// reload history
					showItems();
				},
				error : function(data) {
					$uploadResult.addClass(warningCss);
					$uploadResult.text("Unable to upload file");
					// reset input form
					$uploadFileInput.val("");
					// reload history
					showItems();
				}
			});
		}
	});
}

function initializeButtons() {
	$(".deleteButton").each(function(index, element) {
		var $element = $(element);
		$element.click(function() {
			var form = $element.closest('tr').find('form');
			var item = objectifyForm(form.serializeArray());

			$.ajax({
				url : form.attr("action") + "/delete",
				type : "POST",
				data : JSON.stringify(item),
				contentType : "application/json; charset=utf-8",
				beforeSend : function() {
					$loader.show();
				},
				complete : function() {
					$loader.hide();
				},
				success : function(data) {
					$uploadResult.text("");
					$uploadResult.addClass(successCss);
					$uploadResult.text(data);
					showItems();
				},
				error : function(data) {
					$uploadResult.addClass(warningCss);
					$uploadResult.text("Unable to delete item " + item.title);
					// reset input form
					$uploadFileInput.val("");
					// reload history
					showItems();
				}
			});

		});
	});
}

// serialize data function
function objectifyForm(formArray) {
	var returnArray = {};
	for (var i = 0; i < formArray.length; i++) {
		returnArray[formArray[i]['name']] = formArray[i]['value'];
	}
	return returnArray;
}

function showItems() {
	$itemTableContainer.html("");
	$pagination.html("");
	$
			.ajax({
				url : contextPath + "/viewListUnparsed",
				cache : false,
				success : function(data) {
					if (data !== undefined && JSON.parse(data).length !== 0) {
						// var files = {"itemList" : JSON.parse(data)};
						var files = JSON.parse(data);
						var source = $("#itemTemplate").html();
						var template = Handlebars.compile(source);

						if (files.itemList
								&& files.itemList.length - 1 < REPORT_ROW_TOTAL) {
							// $itemTableContainer.html(template(files));
							// initializeButtons();
							$pagination
									.pagination({
										dataSource : files.itemList,
										showNavigator : true,
										formatNavigator : 'Total Results: <%= totalNumber%> </span>',
										showPageNumbers : false,
										showPrevious : false,
										showNext : false,
										showNavigator : true,
										pageSize : REPORT_ROW_TOTAL,
										callback : function(data, pagination) {
											var dataHtml = template({
												itemList : data
											});
											$itemTableContainer.html(dataHtml);
											initializeButtons();
										}
									});
						} else {
							$pagination
									.pagination({
										dataSource : files.itemList,
										showNavigator : true,
										formatNavigator : '<span>Page <%= currentPage %> of <%= totalPage %>, Total Results: <%= totalNumber%> </span>',
										pageSize : REPORT_ROW_TOTAL,
										callback : function(data, pagination) {
											// var dataHtml = template({
											// itemList: data });
											var dataHtml = template(data);
											$itemTableContainer.html(dataHtml);
											initializeButtons();
										}
									});
						}
					} else {
						$itemTableContainer.html("");
						$pagination.html("");
					}
				}
			});
}

$(document).ready(function() {
	initializeUploadForm();
	showItems();
});
