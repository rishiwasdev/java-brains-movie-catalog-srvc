package com.abc.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.abc.config.ObjectMapperConfig;
import com.abc.dto.CatalogItemDto;
import com.abc.dto.ClientResponse;
import com.abc.service.CatalogService;

@RestController
@RequestMapping("/movie-catalog")
public class MvCtlgCntrlr_00_Basic {
	private static final Logger log = LoggerFactory.getLogger(MvCtlgCntrlr_00_Basic.class);
	@Autowired
	private CatalogService catalogService;

	@GetMapping("/test/{userId}")
	private ResponseEntity<ClientResponse> getCatalogDummy(@Valid @PathVariable("userId") String userId) {
		log.debug("# User ID: {}", userId);
		return createResponse(catalogService.getCatalog(userId));
	}

	@PostMapping("/")
	private ResponseEntity<ClientResponse> addItem(@Valid @RequestBody CatalogItemDto dto) {
		log.debug("# CatalogItemDto: {}", dto);
		return createResponse(catalogService.addItem(dto));
	}

	/** IMPLEMENTED IN FURTHER CONTROLLER CLASSES */
	// @GetMapping("/users/{userId}")
	private ResponseEntity<ClientResponse> getCatalog(@Valid @PathVariable("userId") final String USER_ID) {
		return null;
	}

	/** IMPLEMENTED IN FURTHER CONTROLLER CLASSES */
	// @GetMapping("/ratings/{userId}")
	private ResponseEntity<ClientResponse> getCatalogByUserId(@Valid @PathVariable("userId") final String USER_ID) {
		return null;
	}

	// -------------------------- HELPER METHODS
	private ResponseEntity<ClientResponse> createResponse(ClientResponse res) {
		return (res == null) ? new ResponseEntity<>(res, HttpStatus.CONFLICT) : new ResponseEntity<>(res, HttpStatus.OK);
	}
}
