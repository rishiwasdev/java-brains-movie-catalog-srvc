package com.abc.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.core.io.ClassPathResource;

public class ReadFile {
	public static String getFilePath(String resourceFile) throws IOException {
		if (resourceFile == null || resourceFile.isEmpty())
			throw new IllegalArgumentException("Name of File must be provided.");
		ClassPathResource cpr = new ClassPathResource(resourceFile);
		return cpr.getFile().getPath();
	}

	public static String[] getLinesAsArray(String resourceFile) throws IOException {
		ArrayList<String> list = getLinesAsList(resourceFile);
		String[] lineArr = new String[list.size()];
		return lineArr;
	}

	public static ArrayList<String> getLinesAsList(String resourceFile) {
		ClassPathResource cpr = new ClassPathResource(resourceFile);
		ArrayList<String> list = new ArrayList<>();
		try (FileReader reader = new FileReader(cpr.getFile()); BufferedReader br = new BufferedReader(reader);) {
			String s = null;
			while ((s = br.readLine()) != null)
				list.add(s);
			return list;
		} catch (IOException e) {
			throw new IllegalArgumentException("File named '" + resourceFile + "' NOT FOUND");
		}
	}
}
