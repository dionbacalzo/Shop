package com.shop.util;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.shop.adapter.ShopItemAdapter;
import com.shop.constant.AppConstant;
import com.shop.domain.ItemDomainObject;
import com.shop.dto.InventoryItem;
import com.shop.exception.ShopException;

public class ShopUtil {
	
	private final static Logger logger = Logger.getLogger(ShopItemAdapter.class);
	
	public static boolean validateItemDomain(ItemDomainObject item) {
		boolean result = true;
		if (item != null) {
			if(item.getTitle() == null || item.getTitle() == "") {
				result = false;
			} else if (item.getManufacturer() == null || item.getManufacturer() == "") {
				result = false;
			} else if (item.getReleaseDate() == null) {
				result = false;
			} else if (item.getPrice() == null) {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}
	
	public static boolean validateItemInventory(InventoryItem item) {
		boolean result = true;
		if (item != null) {
			if(item.getTitle() == null || item.getTitle() == "") {
				result = false;
			} else if (item.getManufacturer() == null || item.getManufacturer() == "") {
				result = false;
			} else if (item.getReleaseDate() == null || item.getReleaseDate() == "") {
				result = false;
			} else if (item.getType() == null || item.getType() == "") {
				result = false;
			} else if (item.getPrice() == null) {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}
	
	public static BigDecimal formatPrice(String price) throws ShopException {
		BigDecimal parsedPrice = null;
		if (price != null) {
			try {
				parsedPrice =  new BigDecimal(price);
			} catch (NumberFormatException e){
				logger.error(e.getMessage());
				throw new ShopException(AppConstant.INVALID_PRICE_ERROR);
			}
		}
		return parsedPrice;
	}
	
	/**
	 * wrap the call to the field compareTo method with a small static method to sort nulls high or low:
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> int compare(T a, T b) {
	     return
	         a==null ?
	         (b==null ? 0 : Integer.MIN_VALUE) :
	         (b==null ? Integer.MAX_VALUE : a.compareTo(b));
	}
}
