package com.shop.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.constant.AppConstant;
import com.shop.domain.ItemDomainObject;
import com.shop.dto.Container;
import com.shop.dto.InventoryItem;
import com.shop.dto.Item;
import com.shop.exception.ShopException;
import com.shop.util.DateUtil;
import com.shop.util.ShopUtil;

public class ShopItemAdapter {
	
	private final static Logger logger = Logger.getLogger(ShopItemAdapter.class);
	
	/**
	 * Frontend Display
	 *
	 */
	private enum Category {
		LAPTOP("Laptops", "c000", "c001"), 
		PHONE("Phones", "c000", "c002"), 
		ACCESSORY("Accessories", "c000", "c003");
		
		private final String title;   
	    private final String parentContainer;
	    private final String container;
	    
	    Category(String title, String parentContainer, String container) {
	        this.title = title;
	        this.parentContainer = parentContainer;
	        this.container = container;
	    }
	    
	    private String getTitle() {
	    	return this.title;
	    }
	    
	    private String getParentContainer() {
	    	return this.parentContainer;
	    }
	    
	    private String getContainer() {
	    	return this.container;
	    }
		
		private Container getCategory() {
			Container container = new Container();
			container.setParent(getParentContainer());
			container.setTitle(getTitle());
			return container;
		}
		
	}
	
	public static Container getCategory(String type) {		
		Category category = Category.valueOf(type.toUpperCase());		
		return category.getCategory();
	}
	
	/**
	 * parse item list for Shop display
	 * @param itemList
	 * @return
	 */
	public static Map<String, Object> parseItem(List<ItemDomainObject> itemList) {
		logger.debug(AppConstant.METHOD_IN);
		
		Map<String, Object> shopContent = new TreeMap<String, Object>();
			
		List<Item> products = new ArrayList<Item>();
		if (itemList != null) {
			IntStream.range(0, itemList.size())
				.forEach(idx ->
				{
					ItemDomainObject item = itemList.get(idx);
					try {
						if(ShopUtil.validateItemDomain(item)) {
							shopContent.put(Category.valueOf(item.getType().toUpperCase()).getContainer(), getCategory(item.getType()));
							
							Item product = new Item();
							product.setParent(Category.valueOf(item.getType().toUpperCase()).getContainer());
							product.setTitle(item.getTitle());
							product.setPrice(item.getPrice());
							product.setReleaseDate(DateUtil.formatDate(item.getReleaseDate()));
							product.setManufacturer(item.getManufacturer());
							
							products.add(product);
							idx = idx + 1;
							shopContent.put("p00"+idx, product);
							
						} else {
							throw new ShopException(AppConstant.PARSE_ITEM_ERROR);
						}
					} catch (Exception e) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							String itemMapped = mapper.writeValueAsString(item);
							logger.error(AppConstant.PARSE_ITEM_ERROR + itemMapped);
						} catch (JsonProcessingException ex) {
							logger.error(AppConstant.PARSE_ITEM_ERROR);
						}
					}
				}
			);
		} else {
			logger.warn(AppConstant.EMPTY_ITEM_ERROR);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return shopContent;
	}
	
	/**
	 * Parse Items for Manage Display
	 * @param itemList
	 * @return
	 */
	public static List<InventoryItem> parseItemRaw(List<ItemDomainObject> itemList) {
		logger.debug(AppConstant.METHOD_IN);
		
		List<InventoryItem> shopContent = new ArrayList<InventoryItem>();
			
		if (itemList != null) {
			IntStream.range(0, itemList.size())
				.forEach(idx ->
				{
					ItemDomainObject item = itemList.get(idx);
					try {
						if(ShopUtil.validateItemDomain(item)) {
							InventoryItem product = new InventoryItem();
							product.setId(item.get_id().toString());
							product.setTitle(item.getTitle());
							product.setPrice(item.getPrice());
							product.setReleaseDate(DateUtil.formatDate(item.getReleaseDate()));
							product.setManufacturer(item.getManufacturer());
							product.setType(item.getType());
							shopContent.add(product);
							
							idx = idx + 1;
						} else {
							throw new ShopException(AppConstant.PARSE_ITEM_ERROR);
						}
					} catch (Exception e) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							String itemMapped = mapper.writeValueAsString(item);
							logger.error(AppConstant.PARSE_ITEM_ERROR + itemMapped);
						} catch (JsonProcessingException ex) {
							logger.error(AppConstant.PARSE_ITEM_ERROR);
						}
					}
				}
			);
		} else {
			logger.warn(AppConstant.EMPTY_ITEM_ERROR);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return shopContent;
	}

	public static List<ItemDomainObject> parseItemInventory(List<InventoryItem> itemList) {
		logger.debug(AppConstant.METHOD_IN);
		
		List<ItemDomainObject> products = new ArrayList<ItemDomainObject>();
		
		if (itemList != null) {
			itemList.forEach((InventoryItem item) -> {
				try {
					if(ShopUtil.validateItemInventory(item)) {
						ItemDomainObject product = new ItemDomainObject();					
						product.setTitle(item.getTitle());
						product.setType(item.getType());
						product.setPrice(item.getPrice());
						product.setReleaseDate(DateUtil.getDate(item.getReleaseDate(), DateUtil.DEFAULT_DATETIME_FORMAT));
						product.setManufacturer(item.getManufacturer());
						products.add(product);
					} else {
						throw new ShopException(AppConstant.PARSE_ITEM_ERROR);
					}
				} catch (ShopException e) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String itemMapped = mapper.writeValueAsString(item);
						logger.error(AppConstant.PARSE_ITEM_ERROR + itemMapped);
					} catch (JsonProcessingException ex) {
						logger.error(AppConstant.PARSE_ITEM_ERROR);
					}
				}
			});
		} else {
			logger.warn(AppConstant.METHOD_OUT);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return products;
	}
	
}
