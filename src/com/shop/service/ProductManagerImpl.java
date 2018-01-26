package com.shop.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import com.shop.dao.ProductDao;
import com.shop.domain.ItemDomainObject;
import com.shop.dto.Container;
import com.shop.dto.InventoryItem;
import com.shop.dto.Item;
import com.shop.util.DateUtil;

@Service
@Component("productManagerImpl")
public class ProductManagerImpl implements ProductManager {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private ProductDao productDaoImpl;
	
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
	
	public ProductManagerImpl(ProductDao productDaoImpl) {
		this.productDaoImpl = productDaoImpl;
	}
	
	@Override
	public Map<String, Object> viewAll() {
		return parseItem(productDaoImpl.findAll());
	}
	
	@Override
	public Map<String, Object> searchByManufacturer(String manufacturer) {
		List<ItemDomainObject> itemList = productDaoImpl.findByManufacturer(manufacturer);
		return parseItem(itemList);
	}

	@Override
	public Map<String, Object> parseItem(List<ItemDomainObject> itemList) {
		final String METHOD_NAME = "parseItem";
		logger.info("Entering method " + METHOD_NAME);
		
		Map<String, Object> shopContent = new HashMap<String, Object>();
			
		List<Item> products = new ArrayList<Item>();
		if (itemList != null) {
			IntStream.range(0, itemList.size())
				.forEach(idx ->
				{
					ItemDomainObject item = itemList.get(idx);
					if(validateItemDomain(item)) {
						
						shopContent.put(Category.valueOf(item.getType().toUpperCase()).getContainer(), getCategory(item.getType()));
						
						Item product = new Item();
						product.setParent(Category.valueOf(item.getType().toUpperCase()).getContainer());
						product.setTitle(item.getTitle());
						product.setPrice(item.getPrice());
						product.setReleaseDate(DateUtil.formatDate(item.getReleaseDate()));
						product.setManufacturer(item.getManufacturer());
						
						products.add(product);
						
						shopContent.put("p00"+idx+1, product);
						
					} else {
						logger.error("found problematic item");
					}
				}
			);
		} else {
			logger.warn("No item found");
		}
		
		logger.info("Exiting method " + METHOD_NAME);
		
		return shopContent;
	}
	
	

	@Override
	public BigDecimal formatPrice(String price) {
		BigDecimal parsedPrice = null;
		if (price != null) {
			parsedPrice =  new BigDecimal(price);
		}
		return parsedPrice;
	}

	@Override
	public Container getCategory(String type) {		
		Category category = Category.valueOf(type.toUpperCase());		
		return category.getCategory();
	}

	@Override
	public List<ItemDomainObject> saveAll(List<InventoryItem> addList) {
		final String METHOD_NAME = "saveAll";
		logger.info("Entering method " + METHOD_NAME);		
		List<ItemDomainObject> itemList = null;
		try {
			itemList = productDaoImpl.insert(parseItemInventory(addList));
		} catch (DuplicateKeyException e) {
			logger.error("Duplicate item " + e.getMessage());
	    } catch (MongoException e) {
	    	logger.error(e.getMessage());
	    };
		
		logger.info("Exiting method " + METHOD_NAME);
		return itemList;		
	}
	
	public List<ItemDomainObject> parseItemInventory(List<InventoryItem> itemList) {
		final String METHOD_NAME = "parseItemInventory";
		logger.info("Entering method " + METHOD_NAME);
		
		List<ItemDomainObject> products = new ArrayList<ItemDomainObject>();
		
		if (itemList != null) {
			itemList.forEach((InventoryItem item) -> {
				if(validateItemInventory(item)) {
					
					ItemDomainObject product = new ItemDomainObject();					
					product.setTitle(item.getTitle());
					product.setType(item.getType());
					product.setPrice(item.getPrice());
					product.setReleaseDate(DateUtil.getDate(item.getReleaseDate(), DateUtil.DEFAULT_DATETIME_FORMAT));
					product.setManufacturer(item.getManufacturer());
					
					products.add(product);
					
				} else {
					logger.error("found problematic item");
				}
			});
		} else {
			logger.warn("No item found");
		}
		
		logger.info("Exiting method " + METHOD_NAME);
		
		return products;
	}
	
	public boolean validateItemDomain(ItemDomainObject item) {
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
	
	public boolean validateItemInventory(InventoryItem item) {
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

}
