package com.abc.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abc.config.Constants;
import com.abc.dto.RatingDto;
import com.abc.util.ReadFile;
import com.abc.util.Util;
import com.abc.util.Util_Elementary;

public class DummyData {
	private static final Logger log = LoggerFactory.getLogger(DummyData.class);

	public static RatingDto randomRating() {
		return randomRating(randomMovieId());
	}

	public static RatingDto randomRating(String movieId) {
		RatingDto dto = new RatingDto();
		dto.setMovieId(movieId);
		dto.setRating(randomRatingVal());
		return dto;
	}

	public static int randomRatingVal() {
		return Util_Elementary.randomNumInRange(1, 5);
	}

	public static String randomMovieId() {
		return Util_Elementary.randomNumInRange(1950, 2020) + "-" + Util_Elementary.randomNumInRange(1, 99);
	}

	public static HashSet<RatingDto> addRandomItems(List<RatingDto> savedRatings) {
		List<String> movieProps = ReadFile.getLinesAsList("Movies.properties");
		int totalSaved = savedRatings.size();

		int canBeAdded = Constants.MAX_ITEMS_TO_SAVE < 0 ? movieProps.size() : Constants.MAX_ITEMS_TO_SAVE - totalSaved;
		if (Constants.MAX_ITEMS_TO_SAVE > -1 && canBeAdded <= 0) {
			log.error("Enough sample ratings({}) stored.", totalSaved);
			return null;
		}

		List<String> savedIds = savedRatings.stream().map(r -> r.getMovieId()).collect(Collectors.toList());
		movieProps.removeAll(savedIds); // OR duplicate
		// -----------------------------------
		HashSet<RatingDto> ratings = new HashSet<>();
		while (movieProps.size() > 0 && canBeAdded-- > 0) {
			String movieId = movieProps.remove(Util_Elementary.randomNum(movieProps.size() - 1));
			if (Util.nullOrEmpty(movieId))
				movieId = DummyData.randomMovieId();
			if (!ratings.add(DummyData.randomRating(movieId))) // if not added
				++canBeAdded;
		}
		return ratings;
	}

	public static Set<RatingDto> dummyRatings(int count) {
		List<String> movieProps = ReadFile.getLinesAsList("Movies.properties");
		count = Math.min(count, movieProps.size());
		log.info("Creating {} dummy ratings.", count);
		Set<RatingDto> ratings = new HashSet<>();
		while (ratings.size() < count)
			ratings.add(randomRating(movieProps.get(Util_Elementary.randomNum(movieProps.size() - 1))));
		return ratings;
	}

	public static Set<RatingDto> dummyRatings(Collection<String> movieIds) {
		log.info("Creating dummy ratings for movie-IDs: {}", movieIds.toString());
		HashSet<RatingDto> ratings = new HashSet<>();
		for (String movieId : movieIds)
			ratings.add(randomRating(movieId));
		return ratings;
	}

	public static Set<String> dummyMovieIds(int count) {
		List<String> movieProps = ReadFile.getLinesAsList("Movies.properties");
		count = Math.min(count, movieProps.size());
		log.info("Creating {} dummy Movie-IDs.", count);
		Set<String> movieIds = new HashSet<>();
		while (movieIds.size() < count)
			movieIds.add(movieProps.get(Util_Elementary.randomNum(movieProps.size() - 1)));
		return movieIds;
	}
}
