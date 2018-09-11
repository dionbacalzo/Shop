//upload form

var REPORT_ROW_TOTAL = 10;
var $window = $(window);
var $uploadform = $("#uploadform");
var $uploadFileInput = $("#uploadFileInput");
var $uploadResult = $("#uploadResult");
var $itemTableContainer = $("#itemTableContainer");
var $pagination = $('#pagination');
var $loader = $('#loader');
var successCss = "success";
var warningCss = "warning";
//Get the modal
var modal = $('#confirmModal');

function initializeModal(){
	// Get the <span> element that closes the modal
	var closeButton = $(".close");
	// When the user clicks on <span> (x), close the modal
	closeButton.click(function(){
	    modal.hide();
	    $("#modal-ok").unbind();
	});
	//When the user clicks on cancel button, close the modal
	$("#modal-cancel").click(function(){
		modal.hide();
		$("#modal-ok").unbind();
	});
}

function initializeModalFunction($button, message, callback){
	// When the user clicks the button, open the modal 
	$button.click(function(){
		if(message !== undefined && message !== "") {
			modal.find(".modal-body p").text(message);
		}
		modal.show();
	    $("#modal-ok").click(function() {
			callback();
			modal.hide();
			$(this).unbind();
		});
	});
	
}

//serialize data function
function objectifyForm(formArray) {
	var returnArray = {};
	for (var i = 0; i < formArray.length; i++) {
		returnArray[formArray[i]['name']] = formArray[i]['value'];
	}
	return returnArray;
}

function initializeUploadForm() {
	$uploadform.submit(function(event) {
		event.preventDefault();

		$uploadResult.text("");

		// add css display for the result
		
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

//delete item immediately
function deleterow($element){
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
}

function initializeDelete($element){
	$element.parents('tr').detach();
}

function initializeEdit($element, field){
	$element.on('input', function(){
		$this = $(this);
		var inputCounterpart = $element.parents('tr').find("input[name$=\""+ field +"\"]")[0]
		inputCounterpart.setAttribute("value", $this[0].textContent.trim());
	});
}

function initializeContentTable() {
	
	//initialize edit
	$("#uploadItemTable td").each(function(index, element) {
		$(element).each(function(i, cell) {
			if(cell.className.indexOf("itemAttr-")){
				var field = cell.className.replace('itemAttr-','');
				initializeEdit($(cell), field)
			}
		});
	});
	
	$(".deleteButton").each(function(index, element) {
		var $element = $(element);
		initializeModalFunction($element, "Are you sure you want to delete this?", function(){
			$element.parents('tr').detach();
		});
		//initializeDelete($element);
	});
	
	$("#addItem").click(function(){
		var table = $("#uploadItemTable").find("tbody")[0];
		var row = table.insertRow(0);
		
		var cellList = ["title", "price", "type", "manufacturer", "releaseDate"];
		
		// Insert a cell in the row at index 0
		for (var i = 0; i < cellList.length; i++){
			var currentCell = row.insertCell(i);
			currentCell.setAttribute('contenteditable', "true");
			initializeEdit($(currentCell), cellList[i]);
		}
		deleteCell = row.insertCell(cellList.length);
		var deleteButton = document.createElement("button");
		deleteButton.className = "deleteButton";
		//initializeDelete($(deleteButton));
		initializeModalFunction($(deleteButton), "Are you sure you want to delete this?", function(){
			$(deleteButton).parents('tr').detach();
		});
		deleteCell.appendChild(deleteButton);
		
		hiddenClass = "hidden";
		var formCell = row.insertCell(cellList.length+1);
		arr = formCell.className.split(" ");
	    if (arr.indexOf(hiddenClass) == -1) {
	    	formCell.className += " " + hiddenClass;
	    };
	    
	    var form = document.createElement("form");
	    form.className = hiddenClass;
	    form.setAttribute('method',"post");
	    form.setAttribute('action', contextPath);
	    
	    var inputList = ["id", "title", "price", "type", "manufacturer", "releaseDate"];
	    
	    for (var i=0; i < inputList.length; i++) {
	    	var input = document.createElement("input"); //input element, text
		    input.className = hiddenClass;
		    input.setAttribute('type',"text");
		    input.setAttribute('name', inputList[i]);
		    
		    form.appendChild(input);
	    }
	    
	    formCell.appendChild(form)
	});
	
	var $saveContent = $("#saveContent");
	initializeModalFunction($saveContent, "Are you sure you want to update?", function(){
		//initialize edit
		var itemList = [];
		$("#uploadItemTable tr form").each(function(index, element) {
			var item = objectifyForm($(element).serializeArray());
			itemList.push(item);
		});
		if(itemList.length > 0) {
			$.ajax({
				url : contextPath + "/save",
				type : "POST",
				data : JSON.stringify(itemList),
				contentType : "application/json; charset=utf-8",
				beforeSend : function() {
					$loader.show();
				},
				complete : function() {
					$loader.hide();
				},
				success : function(data) {
					updateTable(data);
					$uploadResult.addClass(successCss);
					$uploadResult.text("Successfully saved");
				},
				error : function(data) {
					$uploadResult.addClass(warningCss);
					$uploadResult.text("Unable to save");
					// reset input form
					$uploadFileInput.val("");
					// reload history
					showItems();
				}
			});
		} else {
			//TODO: add error empty list
		}
	});
}

function updateTable(data){
	if (data !== undefined && JSON.parse(data).length !== 0) {
		// var files = {"itemList" : JSON.parse(data)};
		var files = JSON.parse(data);
		var source = $("#itemTemplate").html();
		var template = Handlebars.compile(source);
		if (files.itemList && files.itemList.length - 1 < REPORT_ROW_TOTAL) {
			// $itemTableContainer.html(template(files));
			// initializeContentTable();
			$pagination.pagination({
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
					initializeContentTable();
				}
			});
		} else {
			//TODO: pagination does not work with save, edit function
			$pagination.pagination({
				dataSource : files.itemList,
				showNavigator : true,
				formatNavigator : '<span>Page <%= currentPage %> of <%= totalPage %>, Total Results: <%= totalNumber%> </span>',
				pageSize : REPORT_ROW_TOTAL,
				callback : function(data, pagination) {
					 var dataHtml = template({
						 itemList: data 
					 });
					//var dataHtml = template(data);
					$itemTableContainer.html(dataHtml);
					initializeContentTable();
				}
			});
		}
	} else {
		$itemTableContainer.html("");
		$pagination.html("");
	}
}

function showItems() {
	$itemTableContainer.html("");
	$pagination.html("");
	$.ajax({
		url : contextPath + "/viewListUnparsed",
		cache : false,
		beforeSend : function() {
			$loader.show();
		},
		complete : function() {
			$loader.hide();
		},
		success : function(data) {
			updateTable(data);
		},
		error : function(data) {
			$uploadResult.addClass(warningCss);
			$uploadResult.text("Unable to show items");
			// reset input form
			$uploadFileInput.val("");
		}
	});
}


$(document).ready(function() {
	initializeUploadForm();
	showItems();
	initializeModal();
});
