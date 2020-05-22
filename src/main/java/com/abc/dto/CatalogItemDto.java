package com.abc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogItemDto {
	private String userId;
	private String name;
	private String description;
	private int rating;

	public CatalogItemDto() {}

	public CatalogItemDto(String userId, String name, String description, int rating) {
		this.userId = userId;
		this.name = name;
		this.description = description;
		this.rating = rating;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "CatalogItemDto [userId=" + userId + ", name=" + name + ", description=" + description + ", rating=" + rating + "]";
	}
}
