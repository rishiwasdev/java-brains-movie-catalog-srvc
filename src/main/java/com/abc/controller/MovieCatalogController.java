package com.abc.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.abc.dto.MovieDto;
import com.abc.dto.RatingDto;
import com.abc.service.CatalogService;
import com.abc.test.DummyData;
import com.abc.util.Util_Elementary;

@RestController
@RequestMapping("/movie-catalog")
public class MovieCatalogController {
	private static final Logger log = LoggerFactory.getLogger(MovieCatalogController.class);
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private ObjectMapperConfig mapper;

	@GetMapping("/test/{userId}")
	private ResponseEntity<ClientResponse> getCatalogDummy(@Valid @PathVariable("userId") String userId) {
		log.debug("# User ID: {}", userId);
		return createResponse(catalogService.getCatalog(userId));
	}

	@GetMapping("/test-response-entity/{userId}")
	private ResponseEntity<ClientResponse> getCatalogFromResponseEntity(@Valid @PathVariable("userId") String usrId) {
		RestTemplate restTemplate = new RestTemplate();
		Set<String> movieIds = DummyData.dummyMovieIds(5);
		String movieUrl = "http://localhost:8082/movie-info/";
		String ratingUrl = "http://localhost:8083/movie-rating/";

		// Put these all together
		List<CatalogItemDto> list = movieIds.stream().map(i -> {
			String userId = Util_Elementary.randomNum(998) + 1001 + "";
			log.info("# Dummy User ID: {}", userId);
			ResponseEntity<ClientResponse> movieEntity = restTemplate.getForEntity(movieUrl + i, ClientResponse.class);
			ClientResponse movieResp = movieEntity.getBody();
			MovieDto movie = mapper.convert(movieResp.getData(), MovieDto.class);

			ResponseEntity<ClientResponse> ratingEntity = restTemplate.getForEntity(ratingUrl + i, ClientResponse.class);
			ClientResponse ratingResp = ratingEntity.getBody();
			RatingDto rating = mapper.convert(ratingResp.getData(), RatingDto.class);

			CatalogItemDto catalog = new CatalogItemDto();
			catalog.setUserId(userId);
			catalog.setName(movie != null ? movie.getName() : null);
			catalog.setDescription(movie != null ? movie.getDescription() : null);
			catalog.setRating(rating != null ? rating.getRating() : null);
			return catalog;
		}).collect(Collectors.toList());
		ClientResponse catalogResp = new ClientResponse();
		catalogResp.setData(list);
		return createResponse(catalogResp);
	}

	@GetMapping("/test-by-movie-id/{userId}")
	private ResponseEntity<ClientResponse> getCatalog(@Valid @PathVariable("userId") String userId) {
		log.info("# User ID: {}", userId);
		RestTemplate restTemplate = new RestTemplate();
		Set<String> movieIds = DummyData.dummyMovieIds(5);
		String movieUrl = "http://localhost:8082/movie-info/";
		String ratingUrl = "http://localhost:8083/movie-rating/";
		List<CatalogItemDto> list = movieIds.stream().map(i -> {
			MovieDto movie = restTemplate.getForObject(movieUrl + i, MovieDto.class);
			RatingDto rating = restTemplate.getForObject(ratingUrl + i, RatingDto.class);
			CatalogItemDto catalog = new CatalogItemDto();
			catalog.setUserId(userId);
			catalog.setName(movie != null ? movie.getName() : null);
			catalog.setDescription(movie != null ? movie.getDescription() : null);
			catalog.setRating(rating != null ? rating.getRating() : null);
			return catalog;
		}).collect(Collectors.toList());
		ClientResponse catalogResp = new ClientResponse();
		catalogResp.setData(list);
		return createResponse(catalogResp);
	}

	@GetMapping("/test-by-rating/{userId}")
	private ResponseEntity<ClientResponse> getCatalogFromRating(@Valid @PathVariable("userId") String userId) {
		log.info("# User ID: {}", userId);
		RestTemplate restTemplate = new RestTemplate();
		Set<RatingDto> ratings = DummyData.dummyRatings(5);
		log.info("# ratings: {}", ratings.toString());
		String movieUrl = "http://localhost:8082/movie-info/";
		List<CatalogItemDto> catalog = ratings.stream().map(r -> {
			MovieDto movie = restTemplate.getForObject(movieUrl + r.getMovieId(), MovieDto.class);
			return movie != null ? new CatalogItemDto(userId, movie.getName(), movie.getDescription(), r.getRating()) : null;
		}).collect(Collectors.toList());
		ClientResponse catalogResp = new ClientResponse();
		catalogResp.setData(catalog);
		return createResponse(catalogResp);
	}

	@PostMapping("/")
	private ResponseEntity<ClientResponse> addItem(@Valid @RequestBody CatalogItemDto dto) {
		log.debug("# CatalogItemDto: {}", dto);
		// DummyData
		return createResponse(catalogService.addItem(dto));
	}

	// HELPER METHODS
	private ResponseEntity<ClientResponse> createResponse(ClientResponse res) {
		return (res == null) ? new ResponseEntity<>(res, HttpStatus.CONFLICT) : new ResponseEntity<>(res, HttpStatus.OK);
	}
}
