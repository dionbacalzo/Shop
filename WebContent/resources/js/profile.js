var $loader = $('#loader');
var successCss = "success";
var warningCss = "warning";

var $profileResult = $("#profileResult");
var $profileForm = $("#profileForm");
var $firstnameInput = $('input[name="firstname"]');
var $lastnameInput = $('input[name="lastname"]');
var $pictureInput = $('input[name="picture"]');
var $picPreview = $('#pic-preview');

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
	} else if (formData['password'] === formData['newPassword']) {
		if (!$result.hasClass(warningCss)) {
			$result.addClass(warningCss);
		}
		$result.text("The old password must not match the new password");
		hasCompleteValues = false;
	}

	return hasCompleteValues;
}

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        
        reader.onload = function (e) {
        	$picPreview.attr('src', e.target.result);
        }
        
        reader.readAsDataURL(input.files[0]);
    } else {
    	$picPreview.attr('src', '/resources/images/default-pic.png');
    }
}

$("#profile-pic").change(function(){
    readURL(this);
});

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
				data = JSON.parse(data);
				$firstnameInput.val(data.firstname);
				$lastnameInput.val(data.lastname);
				if (data.picture) {
					$picPreview.attr('src', 'data:image/png;base64,'+data.picture);
				} else {
					$picPreview.attr('src', contextPath + '/resources/images/default-pic.png');
				}
			} else {
				$picPreview.attr('src', contextPath + '/resources/images/default-pic.png');
				$profileResult.addClass(warningCss);
				$profileResult.text("Unable to retrieve profile");
			}
		},
		error : function(data) {
			$picPreview.attr('src', contextPath + '/resources/images/default-pic.png');
			$profileResult.addClass(warningCss);
			$profileResult.text("Unable to retrieve profile");
		}
	});

	$profileForm.submit(function(event) {
		event.preventDefault();

		//reset messages
		if ($profileResult.hasClass(warningCss)) {
			$profileResult.removeClass(warningCss);
		}
		if ($profileResult.hasClass(successCss)) {
			$profileResult.removeClass(successCss);
		}

		$profileResult.text("");
		
		//validate image input
		var imageValidationResult = validatePicture($profileResult)
		
		var formData = objectifyForm($profileForm.serializeArray());
		
		var fileFormData = new FormData();
		if (imageValidationResult) {
			fileFormData.append("picture", $pictureInput[0].files[0]);
		}
		fileFormData.append("user", JSON.stringify(formData));

		if (validateProfileFormInput(formData, $profileResult) && imageValidationResult) {
			$.ajax({
				url : $profileForm.attr("action"),
				type : "POST",
				data : fileFormData,
				contentType : false,
				processData : false,
				cache : false,
				//contentType : "application/json; charset=utf-8",
				beforeSend : function() {
					$loader.show();
				},
				complete : function() {
					$loader.hide();
				},
				success : function(data) {
					if (data && data !== "null") {
						data = JSON.parse(data);
						$firstnameInput.val(data.firstname);
						$lastnameInput.val(data.lastname);

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

function validatePicture($result) {	
	var hasCompleteValues = true;
	var validImageExtensions = ["jpg", "gif", "png", "jpeg"];
	if ($pictureInput[0].files[0]) {
		var fileSize = $pictureInput[0].files[0].size;
		var fileName = $pictureInput.val();
		var fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		if (fileSize > 5242880) {
			$result.addClass(warningCss);
			$result.text("File size must be less than 5mb");
			hasCompleteValues = false;
		} else if (validImageExtensions.indexOf(fileExtension) === -1) {
			$result.addClass(warningCss);
			$result.text("File must be a valid image (png, jpg or gif)");
			hasCompleteValues = false;
		}
	}
	
	return hasCompleteValues;
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