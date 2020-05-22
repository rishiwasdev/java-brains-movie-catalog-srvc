package com.abc.service;

import org.springframework.stereotype.Service;

import com.abc.dto.CatalogItemDto;
import com.abc.dto.ClientResponse;

@Service
public interface CatalogService {
	ClientResponse getCatalog(String userId);

	ClientResponse addItem(CatalogItemDto dto);
}
