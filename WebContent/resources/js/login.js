var $loader = $('#loader');
var successCss = "success";
var warningCss = "warning";

//Login form
var $loginResult = $("#loginResult");
var $loginForm = $("#loginForm");

var $signupResult = $("#signupResult");
var $signupForm = $("#signupForm");

//serialize data function
function objectifyForm(formArray) {
	var returnArray = {};
	for (var i = 0; i < formArray.length; i++) {
		returnArray[formArray[i]['name']] = formArray[i]['value'];
	}
	return returnArray;
}

function validateFormInput(formData, $result){
	//check for empty inputs
	var hasCompleteValues = true;
	
	for (var key in formData) {
	   if (formData.hasOwnProperty(key)) {
	      if (formData[key].length === 0) {
	    	  hasCompleteValues = false;
	      }
	   }
	}
	
	if (!hasCompleteValues) {
		$result.addClass(warningCss);
		$result.text("Missing username/password");
	}
	
	return hasCompleteValues;
}

function initializeLoginForm() {
	$loginForm.submit(function(event) {
		event.preventDefault();
		
		if ($loginResult.hasClass(warningCss)) {
			$loginResult.removeClass(warningCss);
		}
		if ($loginResult.hasClass(successCss)) {
			$loginResult.removeClass(successCss);
		}
		
		$loginResult.text("");
		
		var formData = objectifyForm($loginForm.serializeArray());
		
		if(validateFormInput(formData, $loginResult)) {
			$.ajax({
				url :  $loginForm.attr("action"),
				type : "POST",
				data : JSON.stringify(formData),
				contentType : "application/json; charset=utf-8",
				beforeSend : function() {
					$loader.show();
				},
				complete : function() {
					$loader.hide();
				},
				success : function(data) {
					if(data.toUpperCase() === "unable to login".toUpperCase()){
						$loginResult.addClass(warningCss);
						$loginResult.text("Unable to log in");
					} else {
						$loginResult.addClass(successCss);
						$loginResult.text(data);
					}
				},
				error : function(data) {
					$loginResult.addClass(warningCss);
					$loginResult.text("Unable to log in");
				}
			});
		}
	});
}

function initializeSignupForm() {
	$signupForm.submit(function(event) {
		event.preventDefault();
		
		if ($signupResult.hasClass(warningCss)) {
			$signupResult.removeClass(warningCss);
		}
		if ($signupResult.hasClass(successCss)) {
			$signupResult.removeClass(successCss);
		}
		
		$signupResult.text("");
		
		var formData = objectifyForm($signupForm.serializeArray());
		
		if(validateFormInput(formData, $signupResult)) {
			$.ajax({
				url :  $signupForm.attr("action"),
				type : "POST",
				data : JSON.stringify(formData),
				contentType : "application/json; charset=utf-8",
				beforeSend : function() {
					$loader.show();
				},
				complete : function() {
					$loader.hide();
				},
				success : function(data) {
					if(data.toUpperCase() === "unable to signup".toUpperCase()){
						$signupResult.addClass(warningCss);
						$signupResult.text("Unable to log in");
					} else {
						$signupResult.addClass(successCss);
						$signupResult.text(data);
					}
				},
				error : function(data) {
					$signupResult.addClass(warningCss);
					$signupResult.text("Unable to log in");
				}
			});
		}
	});
}

$(document).ready(function() {
	initializeLoginForm();
	initializeSignupForm();
});