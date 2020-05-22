package com.abc.util;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

public class Util {
	public static boolean nullOrEmpty(String data) {
		return data == null || data.isEmpty();
	}

	public static boolean nullOrEmpty(Collection c) {
		return c == null || c.isEmpty();
	}

	public static boolean nullOrEmpty(Map m) {
		return m == null || m.isEmpty();
	}

	public static void sleep(final int NUM, final TimeEnum UNIT) {
		sleep(getMillis(NUM, UNIT));
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int getMillis(final int NUM, final TimeEnum UNIT) {
		if (NUM < 1)
			throw new IllegalArgumentException("\nPlease provide positive amount of time.");
		int millis = NUM;
		boolean fallThrough = false;
		switch (UNIT) {
		case DAY:
			if (NUM > 24)
				throw new IllegalArgumentException("\nPlease provide num of DAYS <= 24");
			fallThrough = true;
			millis = millis * 24;
		case HOUR:
			if (NUM > 596 && !fallThrough)
				throw new IllegalArgumentException("\nPlease provide num of HOURS <= 596");
			fallThrough = true;
			millis = millis * 60;
		case MINUTE:
			if (NUM > 35791 && !fallThrough)
				throw new IllegalArgumentException("\nPlease provide num of MINUTES <= 35791");
			fallThrough = true;
			millis = millis * 60;
		case SECOND:
			if (NUM > 2147483 && !fallThrough)
				throw new IllegalArgumentException("\nPlease provide num of SECONDS <= 2147483");
			millis = millis * 1000;
		}
		return millis;
	}

	public static void printLn(int numTimes) {
		for (int i = 0; i < numTimes; i++)
			System.out.println();
	}

	public static void printObjLn(final Object OBJ) {
		printObjLn(OBJ, "");
	}

	public static void printObjLn(final Object OBJ, final String TITLE) {
		System.out.println(TITLE + OBJ);
	}

	public static void demarcationLn() {
		demarcationLn("");
	}

	public static void demarcation() {
		demarcation("");
	}

	public static void demarcation(final String TITLE) {
		System.out.print("--------------------------------- " + TITLE);
	}

	public static void demarcationLn(final String TITLE) {
		demarcationLn("--------------------------------- ", TITLE);
	}

	public static void demarcation(final String TITLE, final String LINE) {
		demarcation(LINE, TITLE, "");
	}

	public static void demarcationLn(final String TITLE, final String LINE) {
		demarcation(LINE, TITLE, "\n");
	}

	private static void demarcation(final String TITLE, final String LINE, String NEW_LINE_CHAR) {
		System.out.print(LINE + TITLE + (NEW_LINE_CHAR.equals("\n") ? NEW_LINE_CHAR : ""));
	}

	public static char asciiValue(int i) {
		return (char) i;
	}

	public static int asciiValue(char c) {
		return (int) c;
	}

	public static String randomStrLowerCase(int len) {
		String str = "";
		for (int i = 0; i < len; i++) {
			char rndm = (char) (97 + new Random().nextInt(26));
			str += rndm;
		}
		return str;
	}

	public static String randomName(int numWords) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numWords; i++) {
			sb.append(asciiValue(65 + Util_Elementary.randomNum(25))).append(randomStrLowerCase(1 + Util_Elementary.randomNum(6))).append(" ");
		}
		return sb.toString().trim();
	}
}
