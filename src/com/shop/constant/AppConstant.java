package com.shop.constant;

import java.util.Arrays;
import java.util.List;

import com.shop.util.PropertyUtil;

public class AppConstant {
	public static final String PROPERTY_FILE = "shop.properties";
	
	public static final String METHOD_IN = "Entering Method";
	
	public static final String METHOD_OUT = "Exiting Method";
	
	public static final String PARSE_ITEM_ERROR = "found problematic item";
	
	public static final String EMPTY_ITEM_ERROR = "No item found";
	
	public static final String SHOP_ITEM_REQUIRED = "{0} is required at line {1}";
	
	public static final String SHOP_ITEM_FILE_EMPTY = "Failed to upload because the file was empty";
	
	public static final String SHOP_ITEM_UPLOAD_FAIL_MESSAGE = "Failed to upload file";
	
	public static final String SHOP_ITEM_UPLOAD_FAIL = "Failed to upload file";
	
	public static final String SHOP_ITEM_UPLOAD_SUCCESS = "Successfully uploaded file: {0}";
	
	public static final String SHOP_ITEM_DELETE_SUCCESS = "Item successfully deleted";
	
	public static final String SHOP_ITEM_DELETE_FAIL = "Failed to delete the item";
	
	public static final int TOTAL_SHOP_ITEM_SIZE = 5;

	public static final String SHOP_ITEM_ROW_SIZE_ERROR = "Shop item should have exactly {0} values separated by comma at line {1}";

	public static final String INVALID_PRICE_ERROR = "The Price is not in a number format";

	public static final String DATE_ERROR = "Unable to parse Date";

	public static final String SHOP_ITEM_DELETE_MISSING_REQUIRED = "Unable to delete due to missing unique identifiers: id or title/releaseDate";

	public static final String SHOP_ITEM_DELETE_NOT_FOUND = "Unable to delete: {0} not found";
	
	public static final String SHOP_LOGIN_UNSUCCESSFUL_STATUS = "FAIL";
	
	public static final String SHOP_LOGIN_SUCCESSFUL_STATUS = "SUCCESS";
	
	public static final String SHOP_LOGIN_SUCCESSFUL_MESSAGE = "You have successfully logged in";
	
	public static final String SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_GENERIC = "Unable to Login";
	
	public static final String SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_LIMIT = "Account Locked. Reached the maximum number of login attempts. Contact an Admin to unlock";

	public static final String SHOP_SIGNUP_UNSUCCESSFUL_STATUS = "FAIL";
	
	public static final String SHOP_SIGNUP_SUCCESSFUL_STATUS = "SUCCESS";
	
	public static final String SHOP_SIGNUP_SUCCESSFUL_MESSAGE = "You have successfully signed up";
	
	public static final String SHOP_SIGNUP_UNSUCCESSFUL_MESSAGE_GENERIC = "Unable to Sign up";
	
	public static final String SHOP_SIGNUP_INVALID_CREDENTIALS = "Signup credentials are invalid";
	
	public static final String SHOP_SIGNUP_STORE_UNSUCCESSFUL = "Unable to store user";

	public static final String SHOP_SIGNUP_STORE_SUCCESSFUL = "User {0} is stored successfully";
	
	public static final String SHOP_SIGNUP_DUPLICATE_MESSAGE = "Username is already taken choose another";
	
	public static final String SHOP_USER_NOT_FOUND = "user {0} not found";

	public static final String SHOP_USER_PASSWORD_MISMATCH = "password does not match for user {0}";

	public static final String SHOP_USER_EXCEEDED_LOGIN_ATTEMPT = "user {0} has exceeded login attempt";
	
	public static final String SHOP_PROFILE_INVALID_INPUT = "invalid data: unable to update user profile";
	
	public static final String SHOP_PROFILE_INVALID_PICTURE = "invalid picture data";
	
	public static final String SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS = "FAIL";
	
	public static final String SHOP_PASSWORD_UPDATE_SUCCESSFUL_STATUS = "SUCCESS";
	
	public static final String SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_MISSING_PASSWORD = "The old password is missing";
	
	public static final String SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_MISSING_NEWPASSWORD = "The new password is missing";
	
	public static final String SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_MISSING_NEWPASSWORD2 = "Type again the new password";
	
	public static final String SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_INCORRECT_PASSWORD = "The old password is incorrect";
	
	public static final String SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_INCORRECT_NEWPASSWORD = "The new password fields do not match";
	
	public static final String SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_MATCHING_NEWPASSWORD = "The old password must not match the new password";
	
	public static final String SHOP_PASSWORD_UPDATE_MESSAGE_SUCCESS = "Successfully updated the password";
	
	public static final String SHOP_PASSWORD_UPDATE_MESSAGE_FAIL = "Failed to change the password";
	
	public static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
	
	public static final String SHOP_EXCEEDED_LOGIN_ATTEMPT = "exceeded login attempt";
	
	public static final String REMEMBER_ME_KEY = PropertyUtil.getProperty("remember.me.key");
	
	public static final int SESSION_TIMEOUT = Integer.parseInt(PropertyUtil.getProperty("session.timeout.minute"))*60;
	
	// Token is valid for one month
	public static final int TOKEN_VALIDITY_DAYS = 31;
	
	// maximum number of tokens allowed for one account
	public static final int TOKEN_MAX = 10;
	
	public static final String TOKEN_DELETE_MESSAGE = "Trying to delete Remember me token with id {0} for user {1}";
	
	public static final String TOKEN_DELETE_ERROR_MESSAGE = "Unable to delete Remember me token of user: {0}";
	
	public static final String TOKEN_RETRIEVE_ERROR_MESSAGE = "Unable to check the Remember me tokens of user: {0}";
	
}
