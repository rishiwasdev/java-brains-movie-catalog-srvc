package com.abc.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public class ReadProperties {
	public static String getProperty(String resourceFile, String propertyName) {
		ClassPathResource cpr = new ClassPathResource(resourceFile);
		try (FileReader reader = new FileReader(cpr.getFile())) {
			Properties p = new Properties();
			p.load(reader);
			String value = p.getProperty(propertyName);
			if (Util.nullOrEmpty(value))
				throw new IllegalArgumentException("Property named '" + propertyName + "' NOT FOUND");
			return p.getProperty(propertyName);
		} catch (IOException e) {
			throw new IllegalArgumentException("File named '" + resourceFile + "' NOT FOUND");
		}
	}
}
