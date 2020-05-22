package com.abc.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.config.ObjectMapperConfig;
import com.abc.dto.CatalogItemDto;
import com.abc.dto.ClientResponse;
import com.abc.entity.CatalogItem;
import com.abc.repo.CatalogRepo;
import com.abc.service.CatalogService;

@Service
public class CatalogServiceImpl implements CatalogService {
	private static final Logger log = LoggerFactory.getLogger(CatalogServiceImpl.class);

	@Autowired
	private ObjectMapperConfig mapper;
	@Autowired
	private CatalogRepo catalogRepo;

	@Override
	public ClientResponse getCatalog(String userId) {
		log.debug("# userId: {}", userId);
		List<CatalogItem> items = catalogRepo.getCatalogByUserId(userId);
		log.info("# savedItem: {}", items.toString());
		List<CatalogItemDto> dtos = mapper.createList(mapper.toJson(items), CatalogItemDto.class);
		return createClientResponse(dtos);
	}

	@Override
	public ClientResponse addItem(CatalogItemDto dto) {
		log.debug("# CatalogItemDto: {}", dto);
		CatalogItem item = mapper.convert(dto, CatalogItem.class);
		log.info("# CatalogItem: {}", item);
		CatalogItem savedItem = catalogRepo.save(item);
		log.info("# savedItem: {}", savedItem);
		CatalogItemDto savedItemDto = mapper.convert(savedItem, CatalogItemDto.class);
		return createClientResponse(savedItemDto);
	}

	private ClientResponse createClientResponse(Object obj) {
		ClientResponse res = new ClientResponse();
		res.setData(obj);
		return res;
	}
}
