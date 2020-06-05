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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.abc.config.ObjectMapperConfig;
import com.abc.dto.CatalogItemDto;
import com.abc.dto.ClientResponse;
import com.abc.dto.MovieDto;
import com.abc.dto.RatingDto;
import com.abc.test.DummyData;

@RestController
//@RequestMapping("/movie-catalog")
public class MvCtlgCntrlr_Zzz_UsingWebClient {
	private static final Logger log = LoggerFactory.getLogger(MvCtlgCntrlr_Zzz_UsingWebClient.class);
	@Autowired
	private ObjectMapperConfig mapper;
	@Autowired
	private WebClient.Builder webClientbBuilder;
	@Value("${rating-by-movie-id-as-resp-entity}")
	private String movieAsRespEntityUri;
	@Value("${rating-by-user-id-as-resp-entity}")
	private String ratingAsRespEntityUri;

	// Not using CatalogService
	// @GetMapping("/test-response-entity-new/{userId}")
	private ResponseEntity<ClientResponse> getCatalogFromResponseEntity(@Valid @PathVariable("userId") final String USER_ID) {
		Set<String> movieIds = DummyData.dummyMovieIds(5);
		log.info("# Movie IDs to process: {}", movieIds.toString());
		// Put these all together
		List<CatalogItemDto> catalogs = movieIds.stream().map(id -> {
			// ResponseEntity<ClientResponse> movieEntity = restTemplate.getForEntity(moviesBaseUrl + id, ClientResponse.class);
			// ClientResponse movieResp = movieEntity.getBody();
			// MovieDto movie = mapper.convert(movieResp.getData(), MovieDto.class);
			/********** USING Asynchronous Reactive WebClient, above RestTemplate 3 lines would be replaced by below code */
			// --------------------------- Get Movie from Movie-Info-Microservice
			ClientResponse res = webClientbBuilder.build().get().uri(movieAsRespEntityUri + id).retrieve().bodyToMono(ClientResponse.class).block();
			MovieDto movie = mapper.createObject(mapper.toJson(res.getData()), MovieDto.class);
			// --------------------------- Get Ratings from Movie-Rating-Microservice
			res = webClientbBuilder.build().get().uri(ratingAsRespEntityUri + id).retrieve().bodyToMono(ClientResponse.class).block();
			RatingDto rating = mapper.createObject(mapper.toJson(res.getData()), RatingDto.class);
			// --------------------------- Create Catalog from Collected Data
			return DummyData.createCatalog(USER_ID, movie, rating);
		}).collect(Collectors.toList());
		log.info("# Prepared Catalogs:\t{}", mapper.toJson(catalogs));
		ClientResponse catalogResp = new ClientResponse();
		catalogResp.setData(catalogs);
		return createResponse(catalogResp);
	}

	// -------------------------- HELPER METHODS
	private ResponseEntity<ClientResponse> createResponse(ClientResponse res) {
		return (res == null) ? new ResponseEntity<>(res, HttpStatus.CONFLICT) : new ResponseEntity<>(res, HttpStatus.OK);
	}
}
