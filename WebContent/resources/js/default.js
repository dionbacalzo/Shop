/**
 * this javascript file will run through all pages on every first page load
 */

// TODO: add active css to the current menu item chosen 
if(document.title == "Shop Display: Home") {
	$menuHome = $("#menu-home");
	$menuHome.removeAttr("href");
	$menuHome.addClass("active");
} else if (document.title == "Shop Display: Login"){
	$menuLogin = $("#menu-login");
	$menuLogin.removeAttr("href");
	$menuLogin.addClass("active");
} else if (document.title == "Shop Display: Upload"){
	$menuUpload = $("#menu-upload");
	$menuUpload.removeAttr("href");
	$menuUpload.addClass("active");
}