package com.abc.controller;

import java.util.List;
import java.util.Set;
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

/** RestTemplate -> getForEntity(...) VS getForObject(...) */
@RestController
@RequestMapping("/movie-catalog")
public class MvCtlgCntrlr_01_UsingRestTmplt {
	private static final Logger log = LoggerFactory.getLogger(MvCtlgCntrlr_01_UsingRestTmplt.class);
	@Autowired
	private ObjectMapperConfig mapper;
	@Autowired
	private RestTemplate restTemplate; // Not using CatalogService in this class
	@Value("${movie-by-id-as-resp-entity}")
	private String movieAsRespEntityUri;
	@Value("${movie-by-id-as-client-resp}")
	private String movieAsClientRespUri;
	@Value("${rating-by-movie-id-as-resp-entity}")
	private String ratingAsRespEntityUri;
	@Value("${rating-by-user-id-as-resp-entity}")
	private String ratingsByUserIdAsRespEntityUri;
	@Value("${rating-by-user-id-as-client-resp}")
	private String ratingsByUserIdAsClientRespUri;

	@GetMapping("/as-client-resp/{userId}")
	private ClientResponse getCatalogAsClientResponse(@Valid @PathVariable("userId") final String USER_ID) { // We get 'ResponseEntity' from other service
		// --------------------------- Get Ratings from Movie-Rating-Microservice
		ClientResponse ratingResp = restTemplate.getForObject(ratingsByUserIdAsClientRespUri + USER_ID, ClientResponse.class);
		List<RatingDto> ratings = mapper.createList(mapper.toJson(ratingResp.getData()), RatingDto.class);
		// Put these all together
		List<CatalogItemDto> catalog = ratings.stream().map(r -> {
			// --------------------------- Get Movie from Movie-Info-Microservice
			ClientResponse movieResp = restTemplate.getForObject(movieAsClientRespUri + r.getMovieId(), ClientResponse.class);
			MovieDto movie = mapper.convert(movieResp.getData(), MovieDto.class);
			// --------------------------- Create Catalog from Collected Data
			return DummyData.createCatalog(USER_ID, movie, r);
		}).collect(Collectors.toList());
		log.info("# Prepared Catalogs:\t{}", mapper.toJson(catalog));
		// --------------------------- Add result to single object i.e. ClientResponse
		return new ClientResponse(catalog); // Return ClientResponse as ResponseEntity for this API's response
	}

	@GetMapping("/as-resp-entity/users/{userId}")
	private ResponseEntity<ClientResponse> getCatalog(@Valid @PathVariable("userId") final String USER_ID) { // We get 'ResponseEntity' from other service
		Set<String> movieIds = DummyData.dummyMovieIds(5); // userId=DummyData.dummyUserId(); TODO - remove dummy IDs.
		// Put these all together
		List<CatalogItemDto> catalogs = movieIds.stream().map(id -> {
			// --------------------------- Get Movie from Movie-Info-Microservice
			ResponseEntity<ClientResponse> movieEntity = restTemplate.getForEntity(movieAsRespEntityUri + id, ClientResponse.class);
			ClientResponse movieResp = movieEntity.getBody();
			MovieDto movie = mapper.convert(movieResp.getData(), MovieDto.class);
			// --------------------------- Get Ratings from Movie-Rating-Microservice
			ResponseEntity<ClientResponse> ratingEntity = restTemplate.getForEntity(ratingAsRespEntityUri + id, ClientResponse.class);
			ClientResponse ratingResp = ratingEntity.getBody();
			RatingDto rating = mapper.convert(ratingResp.getData(), RatingDto.class);
			// --------------------------- Create Catalog from Collected Data
			return DummyData.createCatalog(USER_ID, movie, rating);
		}).collect(Collectors.toList());
		log.info("# Prepared Catalogs:\t{}", mapper.toJson(catalogs));
		// --------------------------- Add result to single object i.e. ClientResponse
		return createResponse(new ClientResponse(catalogs)); // Return ClientResponse as ResponseEntity for this API's response
	}

	@GetMapping("/as-resp-entity/ratings/{userId}") // GET ENTITY
	private ResponseEntity<ClientResponse> getRatingsByUserId(@Valid @PathVariable("userId") final String USER_ID) { // We get 'ResponseEntity' from other service
		// --------------------------- Get Ratings from Movie-Rating-Microservice
		ResponseEntity<ClientResponse> ratingEntity = restTemplate.getForEntity(ratingsByUserIdAsRespEntityUri + USER_ID, ClientResponse.class);
		ClientResponse ratingResp = ratingEntity.getBody();
		List<RatingDto> ratings = mapper.createList(mapper.toJson(ratingResp.getData()), RatingDto.class);
		log.info("# For user-ID: {}, \tRatings Received:\t{}", USER_ID, mapper.toJson(ratings));
		// --------------------------- Return received Entity as it is for this API's response
		return ratingEntity;
	}

	// -------------------------- HELPER METHODS
	private ResponseEntity<ClientResponse> createResponse(ClientResponse res) {
		return (res == null) ? new ResponseEntity<>(res, HttpStatus.CONFLICT) : new ResponseEntity<>(res, HttpStatus.OK);
	}
}
