package com.shop.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.dto.InventoryItem;
import com.shop.dto.Item;
import com.shop.dto.ShopContentPage;
import com.shop.service.ProductManager;

@RestController
public class ShopController {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired @Qualifier("productManagerImpl")	
	private ProductManager productManagerImpl;
	
	@RequestMapping(value = "/viewList")
	public String viewList() {
		final String METHOD_NAME = "viewList";
		logger.info("Entering method " + METHOD_NAME);
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(productManagerImpl.viewAll());
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.info("Exiting method " + METHOD_NAME);
		return json;
	}
	
	@RequestMapping(value = "/manufacturer")
	public String viewManufacturerItems(@RequestBody Item searchQuery) {
		final String METHOD_NAME = "viewManufacturerItems";
		logger.info("Entering method " + METHOD_NAME);
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(productManagerImpl.searchByManufacturer(searchQuery.getManufacturer()));
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.info("Exiting method " + METHOD_NAME);
		return json;
	}
	
	@RequestMapping(value = "/addItems")
	public String addItems(@RequestBody List<InventoryItem> searchQuery) {
		final String METHOD_NAME = "addItems";
		logger.info("Entering method " + METHOD_NAME);
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(productManagerImpl.saveAll(searchQuery));
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.info("Exiting method " + METHOD_NAME);
		return json;		
	}
	
	@RequestMapping(value = "/viewPagedList")
	public HttpEntity<ShopContentPage> viewPagedList() {
		final String METHOD_NAME = "viewPagedList";
		logger.info("Entering method " + METHOD_NAME);
		
		ShopContentPage shopContentPage = new ShopContentPage(productManagerImpl.viewAll());
		shopContentPage.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ShopController.class).viewPagedList()).withSelfRel());

		logger.info("Exiting method " + METHOD_NAME);
		
		return new ResponseEntity<ShopContentPage> (shopContentPage, HttpStatus.OK);
	}
	
}
