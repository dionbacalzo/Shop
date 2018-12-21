package com.shop.constant;

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
	
	public static final String SHOP_UNSUCCESSFUL_LOGIN = "FAIL";
	
	public static final String SHOP_SUCCESSFUL_LOGIN = "SUCCESS";

	public static final String SHOP_UNSUCCESSFUL_SIGNUP = "FAIL";
	
	public static final String SHOP_SUCCESSFUL_SIGNUP = "SUCCESS";

	public static final String SHOP_USER_NOT_FOUND = "user {0} not found";

	public static final String SHOP_USER_PASSWORD_MISMATCH = "password does not match for user {0}";

	public static final String SHOP_USER_EXCEEDED_LOGIN_ATTEMPT = "user {0} has exceeded login attempt";

	public static final String SHOP_EXCEEDED_LOGIN_ATTEMPT = "exceeded login attempt";
	
	public static final String REMEMBER_ME_KEY = PropertyUtil.getProperty("remember.me.key");
	
	public static final int SESSION_TIMEOUT = Integer.parseInt(PropertyUtil.getProperty("session.timeout.minute"))*60; 
}
