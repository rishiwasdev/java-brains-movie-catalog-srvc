package com.abc.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.abc.config.ObjectMapperConfig;
import com.abc.dto.CatalogItemDto;
import com.abc.dto.ClientResponse;
import com.abc.dto.MovieDto;
import com.abc.dto.RatingDto;
import com.abc.test.DummyData;
import com.abc.util.Util;

/** RestTemplate -> getForEntity(...) VS getForObject(...) */
@RestController
@RequestMapping("/movie-catalog-eureka")
public class MvCtlgCntrlr_02_EurekaDscvrySrvr {
	private static final Logger log = LoggerFactory.getLogger(MvCtlgCntrlr_02_EurekaDscvrySrvr.class);
	@Autowired
	private ObjectMapperConfig mapper;
	@Autowired
	private RestTemplate restTemplate; // Not using CatalogService in this class
	@Value("${movie-url-eureka-test}")
	private String movieAsClientRespUri;
	@Value("${rating-url-eureka-test}")
	private String ratingsByUserIdAsClientRespUri;

	@GetMapping("/as-client-resp/users/{userId}")
	private ClientResponse getCatalog(@Valid @PathVariable("userId") final String USER_ID) { // We get 'ResponseEntity' from other service
		// --------------------------- Get Ratings from Movie-Rating-Microservice
		ClientResponse ratingResp = restTemplate.getForObject(ratingsByUserIdAsClientRespUri + USER_ID, ClientResponse.class);
		List<RatingDto> ratings = mapper.createList(mapper.toJson(ratingResp.getData()), RatingDto.class);
		// Put these all together
		List<CatalogItemDto> catalogs = Util.nullOrEmpty(ratings) ? null : ratings.stream().map(r -> {
			log.info("# Total Ratings:\t{}", ratings.size());
			// --------------------------- Get Movie from Movie-Info-Microservice
			ClientResponse movieResp = restTemplate.getForObject(movieAsClientRespUri + r.getMovieId(), ClientResponse.class);
			MovieDto movie = mapper.convert(movieResp.getData(), MovieDto.class);
			// --------------------------- Create Catalog from Collected Data
			return DummyData.createCatalog(USER_ID, movie, r);
		}).collect(Collectors.toList());
		log.info("# Prepared Catalogs:\t{}", mapper.toJson(catalogs));
		// --------------------------- Add result to single object i.e. ClientResponse
		return new ClientResponse(catalogs); // Return ClientResponse as ResponseEntity for this API's response
	}

	// -------------------------- HELPER METHODS
	private ResponseEntity<ClientResponse> createResponse(ClientResponse res) {
		return (res == null) ? new ResponseEntity<>(res, HttpStatus.CONFLICT) : new ResponseEntity<>(res, HttpStatus.OK);
	}
}
