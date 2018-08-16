package com.shop.util;

import org.apache.log4j.Logger;

import com.shop.constant.AppConstant;
import com.shop.exception.ShopException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	protected static final Logger logger = Logger.getLogger(DateUtil.class);

	public static final String MONGODB_DATETIME_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
	
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static final String DATE_PATTERN_1 = "yyyy/MM/dd";

	public static String formatDate(Date date) {
		return formatDate(date, DATE_PATTERN_1);
	}
	
	public static String formatDate(Date date, String format) {
		String parsedDate = "";
		if(date != null) {
			parsedDate = new SimpleDateFormat(format).format(date);				
		}
		return parsedDate;
	}
	
	public static Date getDate(String stringDate) throws ShopException{
		return getDate(stringDate, MONGODB_DATETIME_FORMAT);
	}
	
	public static Date getDate(String stringDate, String pattern) throws ShopException {
		Date parsedDate = null;
		if(stringDate != null && stringDate != "") {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		    try
		    {
		    	parsedDate = formatter.parse(stringDate);		    	
		    } catch (ParseException e) {
		      logger.error(e.getMessage());
		      throw new ShopException(AppConstant.DATE_ERROR);
		    }			
		}
		return parsedDate;
	}
	
	public static boolean isThisDateValid(String dateToValidate, String dateFromat){
		boolean result = false;
		if(dateToValidate != null){
			SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
			sdf.setLenient(false);
			try {
				//if not valid, it will throw ParseException
				sdf.parse(dateToValidate);
				logger.debug("Matching date format is " + dateFromat);
				result = true;
			} catch (ParseException e) {
				logger.debug(e.getMessage());
				result = false;
			}
		}
		return result;
	}
	
}
