package com.abc.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ObjectMapperConfig {
	private static final Logger log = LoggerFactory.getLogger(ObjectMapperConfig.class);
	public static ObjectMapper objMapper = new ObjectMapper();

	{
		objMapper.setVisibility(objMapper.getSerializationConfig().getDefaultVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY).withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE).withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
	}

	public <T> String toJson(T source) {
		String jsonTarget = null;
		try {
			jsonTarget = objMapper.writeValueAsString(source);
		} catch (JsonProcessingException e) {
			log.error("# Exception while converting object to json string: ", e);
		}
		return jsonTarget;
	}

	public <T> T createObject(String jsonSource, Class<T> targetType) {
		T target = null;
		try {
			target = objMapper.readValue(jsonSource, targetType);
		} catch (IOException e) {
			log.error("# Exception while creating object from json string: ", e);
		}
		return target;
	}

	public <K, V> Map<K, V> createMap(String jsonSource) {
		TypeReference<Map<K, V>> typeRef = new TypeReference<Map<K, V>>() {};
		Map<K, V> targetMap = null;
		try {
			targetMap = objMapper.readValue(jsonSource, typeRef);
		} catch (IOException e) {
			log.error("# Exception while creating Map from json string: ", e);
		}
		return targetMap;
	}

	public <T> T updateObject(String jsonSource, T target) {
		try {
			target = objMapper.readerForUpdating(target).readValue(jsonSource);
		} catch (IOException e) {
			log.error("# Exception while updating object from json string: ", e);
		}
		return target;
	}

	public <T> T convert(Object source, Class<T> targetType) {
		T target = null;
		try {
			objMapper.registerModule(new JavaTimeModule());
			target = objMapper.convertValue(source, targetType);
		} catch (Exception e) {
			log.error("# Exception while converting object: ", e);
		}
		return target;
	}

	public <T> List<T> createList(String jsonSource, Class<T> targetType) {
		objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		JavaType type = objMapper.getTypeFactory().constructCollectionType(List.class, targetType);
		List<T> list = null;
		try {
			list = objMapper.readValue(jsonSource, type);
		} catch (IOException e) {
			log.error("# Exception while mapping collection: ", e);
		}
		return list;
	}

	public <T> Set<T> createSet(String jsonSource, Class<T> targetType) {
		objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		JavaType type = objMapper.getTypeFactory().constructCollectionType(Set.class, targetType);
		Set<T> set = null;
		try {
			set = objMapper.readValue(jsonSource, type);
		} catch (IOException e) {
			log.error("# Exception while mapping collection: ", e);
		}
		return set;
	}
}