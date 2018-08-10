package com.shop.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import com.mongodb.client.model.UpdateOptions;
import com.shop.adapter.FileContentAdapter;
import com.shop.adapter.ShopItemAdapter;
import com.shop.constant.AppConstant;
import com.shop.dao.ProductDao;
import com.shop.domain.ItemDomainObject;
import com.shop.dto.InventoryItem;

@Service
@Component("productManagerImpl")
public class ProductManagerImpl implements ProductManager {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private ProductDao productDaoImpl;
	
	public ProductManagerImpl(ProductDao productDaoImpl) {
		this.productDaoImpl = productDaoImpl;
	}
	
	@Override
	public Map<String, Object> viewAll() {
		return ShopItemAdapter.parseItem(productDaoImpl.findAll());
	}
	
	@Override
	public List<InventoryItem> viewAllUnparsed() {
		return ShopItemAdapter.parseItemRaw(productDaoImpl.findAll());
	}
	
	@Override
	public Map<String, Object> searchByManufacturer(String manufacturer) {
		List<ItemDomainObject> itemList = productDaoImpl.findByManufacturer(manufacturer);
		return ShopItemAdapter.parseItem(itemList);
	}

	@Override
	public List<ItemDomainObject> saveAll(List<InventoryItem> addList) {
		logger.debug(AppConstant.METHOD_IN);		
		List<ItemDomainObject> itemList = null;
		try {
			itemList = productDaoImpl.insert(ShopItemAdapter.parseItemInventory(addList));
		} catch (DuplicateKeyException e) {
			logger.error("Duplicate item " + e.getMessage());
	    } catch (MongoException e) {
	    	logger.error(e.getMessage());
	    };
		
		logger.debug(AppConstant.METHOD_OUT);
		return itemList;		
	}

	@Override
	public void saveAll(MultipartFile file) throws Exception {
		
		logger.debug(AppConstant.METHOD_IN);		
		List<ItemDomainObject> itemList = null;
		try {
			
			FileContentAdapter fileContentAdapter = new FileContentAdapter(); 
			
			itemList = fileContentAdapter.parseFile(file, productDaoImpl.findAll());
			logger.info(new ObjectMapper().writeValueAsString(itemList));
			productDaoImpl.saveAll(itemList);
			
		} catch (DuplicateKeyException e) {
			logger.error("Duplicate item " + e.getMessage());
			throw(e);
	    } catch (MongoException e) {
	    	logger.error(e.getMessage());
	    	throw(e);
	    };
		
		logger.debug(AppConstant.METHOD_OUT);
	}

}
