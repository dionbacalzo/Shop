package com.shop.dto;

import java.math.BigDecimal;

/**
 * 
 * @author Dionisio.Bacalzo
 *
 * RequestBody to add to shop database
 *
 */
public class InventoryItem {
	
	private String id;

	private String title;
	
	private String manufacturer;
	
	private BigDecimal price;
	
	private String releaseDate;
	
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
