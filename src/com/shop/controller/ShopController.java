package com.shop.controller;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.dto.InventoryItem;
import com.shop.dto.Item;
import com.shop.dto.ShopContentPage;
import com.shop.service.ProductManager;

import com.shop.constant.AppConstant;

@CrossOrigin //allow CORS for local testing
@RestController
public class ShopController {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired @Qualifier("productManagerImpl")	
	private ProductManager productManagerImpl;
	
	@RequestMapping(value = "/viewList")
	public String viewList() {
		logger.debug(AppConstant.METHOD_IN);
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("items", productManagerImpl.viewAll());
			json = mapper.writeValueAsString(items);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return json;
	}
	
	@RequestMapping(value = "/manufacturer")
	public String viewManufacturerItems(@RequestBody Item searchQuery) {
		logger.debug(AppConstant.METHOD_IN);
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(productManagerImpl.searchByManufacturer(searchQuery.getManufacturer()));
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return json;
	}
	
	@RequestMapping(value = "/addItems")
	public String addItems(@RequestBody List<InventoryItem> searchQuery) {
		logger.debug(AppConstant.METHOD_IN);
		
		String json = "";
		try {
			json = new ObjectMapper().writeValueAsString(productManagerImpl.saveAll(searchQuery));
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return json;		
	}
	
	@RequestMapping(value = "/viewPagedList")
	public HttpEntity<ShopContentPage> viewPagedList() {
		logger.debug(AppConstant.METHOD_IN);
		
		ShopContentPage shopContentPage = new ShopContentPage(productManagerImpl.viewAll());
		shopContentPage.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ShopController.class).viewPagedList()).withSelfRel());

		logger.debug(AppConstant.METHOD_OUT);
		
		return new ResponseEntity<ShopContentPage> (shopContentPage, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/content")
	protected ModelAndView viewContentPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug(AppConstant.METHOD_IN);
		
		ModelAndView model = new ModelAndView("content");
		
		logger.debug(AppConstant.METHOD_OUT);
		return model;
	}
	
	@RequestMapping(value = "/upload")
	protected ModelAndView viewUploadPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug(AppConstant.METHOD_IN);
		
		ModelAndView model = new ModelAndView("upload");
		
		logger.debug(AppConstant.METHOD_OUT);
		return model;
	}
	
	@RequestMapping(value = "/viewListUnparsed")
	public String viewListRaw() {
		logger.debug(AppConstant.METHOD_IN);
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("itemList", productManagerImpl.viewAllUnparsed());
			json = mapper.writeValueAsString(items);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return json;
	}
	
	@RequestMapping(value = "uploadItems")
	@ResponseBody
	public String uploadItems(@RequestParam("file") MultipartFile file) {
		logger.debug(AppConstant.METHOD_IN);
		
		String message = "";
		if (file != null && !file.isEmpty()) {
			try {
				File shopFile = new File(file.getOriginalFilename()); //get the filename, works for all browsers especially IE/Edge
				
				productManagerImpl.saveAll(file);
				
				message = MessageFormat.format(AppConstant.SHOP_ITEM_UPLOAD_SUCCESS, shopFile.getName());
			} catch (Exception e) {
				logger.error(e);
				message = AppConstant.SHOP_ITEM_UPLOAD_FAIL_MESSAGE;
			}
		} else {
			message = AppConstant.SHOP_ITEM_FILE_EMPTY;
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return message;
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(@RequestBody InventoryItem item) {
		logger.debug(AppConstant.METHOD_IN);
		
		String message = "";
		if (!StringUtils.isEmpty(item)) {
			try {
				
				productManagerImpl.delete(item);
				
				message = AppConstant.SHOP_ITEM_DELETE_SUCCESS;
			} catch (Exception e) {
				logger.error(e);
				message = AppConstant.SHOP_ITEM_DELETE_FAIL;
			}
		} else {
			message = AppConstant.SHOP_ITEM_DELETE_FAIL;
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return message;
	}
	
}
