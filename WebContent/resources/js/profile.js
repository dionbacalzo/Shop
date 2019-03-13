var $loader = $('#loader');
var successCss = "success";
var warningCss = "warning";

var $profileResult = $("#profileResult");
var $profileForm = $("#profileForm");

var $passwordResult = $("#passwordResult");
var $passwordForm = $("#passwordForm");

// serialize data function
function objectifyForm(formArray) {
	var returnArray = {};
	for (var i = 0; i < formArray.length; i++) {
		returnArray[formArray[i]['name']] = formArray[i]['value'];
	}
	return returnArray;
}

function validateProfileFormInput(formData, $result) {
	// check for empty inputs
	var hasCompleteValues = true;

	if (formData['firstname'].length === 0) {
		if (!$result.hasClass(warningCss)) {
			$result.addClass(warningCss);
		}
		$result.text("Missing Firstname");
		hasCompleteValues = false;
	} else if (formData['lastname'].length === 0) {
		if (!$result.hasClass(warningCss)) {
			$result.addClass(warningCss);
		}
		$result.text("Missing Lastname");
		hasCompleteValues = false;
	}
	return hasCompleteValues;
}

function validatePasswordFormInput(formData, $result) {
	// check for empty inputs
	var hasCompleteValues = true;

	if (formData['password'].length === 0) {
		if (!$result.hasClass(warningCss)) {
			$result.addClass(warningCss);
		}
		$result.text("Missing password");
		hasCompleteValues = false;
	} else if (formData['newPassword'].length === 0) {
		if (!$result.hasClass(warningCss)) {
			$result.addClass(warningCss);
		}
		$result.text("Missing New password");
		hasCompleteValues = false;
	} else if (formData['newPasswordRetype'].length === 0) {
		if (!$result.hasClass(warningCss)) {
			$result.addClass(warningCss);
		}
		$result.text("Type again the password");
		hasCompleteValues = false;
	} else if (formData['newPasswordRetype'] !== formData['newPassword']) {
		if (!$result.hasClass(warningCss)) {
			$result.addClass(warningCss);
		}
		$result.text("The new password fields must match");
		hasCompleteValues = false;
	} else if (formData['newPasswordRetype'] === formData['newPassword']) {
		if (!$result.hasClass(warningCss)) {
			$result.addClass(warningCss);
		}
		$result.text("The old password must not match the new password");
		hasCompleteValues = false;
	}

	return hasCompleteValues;
}

function initializeProfileForm() {
	if ($profileResult.hasClass(warningCss)) {
		$profileResult.removeClass(warningCss);
	}
	if ($profileResult.hasClass(successCss)) {
		$profileResult.removeClass(successCss);
	}

	$profileResult.text("");

	// retrieve current profile info
	$.ajax({
		url : contextPath + "/retrieveUser",
		type : "POST",
		contentType : "application/json; charset=utf-8",
		beforeSend : function() {
			$loader.show();
		},
		complete : function() {
			$loader.hide();
		},
		success : function(data) {
			if (data) {
				$('input[name="firstname"]').val(data.firstname);
				$('input[name="lastname"]').val(data.lastname);
			} else {
				$profileResult.addClass(warningCss);
				$profileResult.text("Unable to retrieve profile");
			}
		},
		error : function(data) {
			$profileResult.addClass(warningCss);
			$profileResult.text("Unable to retrieve profile");
		}
	});

	$profileForm.submit(function(event) {
		event.preventDefault();

		if ($profileResult.hasClass(warningCss)) {
			$profileResult.removeClass(warningCss);
		}
		if ($profileResult.hasClass(successCss)) {
			$profileResult.removeClass(successCss);
		}

		$profileResult.text("");

		var formData = objectifyForm($profileForm.serializeArray());

		if (validateProfileFormInput(formData, $profileResult)) {
			$.ajax({
				url : $profileForm.attr("action"),
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
					if (data && data !== "null") {
						data = JSON.parse(data);
						$('input[name="firstname"]').val(data.firstname);
						$('input[name="lastname"]').val(data.lastname);

						$profileResult.addClass(successCss);
						$profileResult.text("Successfully updated profile");
					} else {
						$profileResult.addClass(warningCss);
						$profileResult.text("Unable to update profile");
					}
				},
				error : function(data) {
					$profileResult.addClass(warningCss);
					$profileResult.text("Unable to retrieve profile");
				}
			});
		}
	});
}

function initializePasswordForm() {
	$passwordForm.submit(function(event) {
		event.preventDefault();

		if ($passwordResult.hasClass(warningCss)) {
			$profileResult.removeClass(warningCss);
		}
		if ($passwordResult.hasClass(successCss)) {
			$passwordResult.removeClass(successCss);
		}

		$passwordResult.text("");

		var formData = objectifyForm($passwordForm.serializeArray());

		if (validatePasswordFormInput(formData, $passwordResult)) {
			$.ajax({
				url : $passwordForm.attr("action"),
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
					data = JSON.parse(data);
					if (data.status.toUpperCase() === "FAIL".toUpperCase()) {
						$passwordResult.addClass(warningCss);
						$passwordResult.text(data.message);
					} else {
						$passwordResult.addClass(successCss);
						$passwordResult.text(data.message);
					}
				},
				error : function(data) {
					$passwordResult.addClass(warningCss);
					$passwordResult.text("Unable to retrieve password");
				}
			});
		}
	});
}

$(document).ready(function() {
	initializeProfileForm();
	initializePasswordForm();
});