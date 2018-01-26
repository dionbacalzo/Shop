package com.shop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shop.domain.ItemDomainObject;
import com.shop.dto.Container;
import com.shop.dto.InventoryItem;

@Service
public interface ProductManager {

	public Map<String, Object> viewAll();
	
	public Map<String, Object> searchByManufacturer(String manufacturer);
	
	public Map<String, Object> parseItem(List<ItemDomainObject> itemList);	
	
	public BigDecimal formatPrice(String price);
	
	public Container getCategory(String type);

	public List<ItemDomainObject> saveAll(List<InventoryItem> shopList);

}
