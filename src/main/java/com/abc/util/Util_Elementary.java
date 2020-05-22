package com.abc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class Util_Elementary {
	static final int[] digits = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	/** returns a sequence(num-array) of numbers between min(parameter-1) & max(parameter-2) both inclusive */
	public static int[] sequence_nonNegativeNums(int min, int max) {
		int[] seq = null;
		if (max >= 0 && min >= 0) {
			int low = Math.min(min, max), high = Math.max(min, max);
			seq = new int[high - low + 1];
			for (int i = 0; i < seq.length; i++)
				seq[i] = low + i;
		} else
			throw new IllegalArgumentException("RANGE SHOULD HAVE VALUES >= 0.");
		return seq;
	}

	/** checks if given num(parameter) is prime */
	public static boolean isPrime(int num) {
		int sqrt = (int) Math.sqrt(num);
		for (int i = 2; i <= sqrt; i++)
			if (num % i == 0)
				return false;
		return true;
	}

	public static int findMax(int[] nums) {
		int max = nums[0];
		for (int i = 1; i < nums.length; i++)
			if (nums[i] > max)
				max = nums[i];
		return max;
	}

	public static int findMax(List<Integer> nums) {
		int max = nums.get(0);
		for (int i = 1; i < nums.size(); i++)
			if (nums.get(i) > max)
				max = nums.get(i);
		return max;
	}

	/** provide range */
	public static TreeSet<Integer> setOfNonPrimeNums(int min, int max) {
		TreeSet<Integer> dividends = null;
		if (max >= 0 && min >= 0) {
			int low = Math.min(min, max), high = Math.max(min, max);
			dividends = new TreeSet<Integer>();
			for (int i = low; i <= high; i++)
				if (!isPrime(i))
					dividends.add(i);
		} else
			throw new IllegalArgumentException("Dividends range should have values >= 0.");
		return dividends;
	}

	/** returns a random num between 0 & limit(parameter) both inclusive */
	public static int randomNum(int limit) {
		return randomNumInRange(0, limit);
	}

	private static String randomNum(int numLength, boolean nonZero) {
		if (numLength <= 0)
			throw new IllegalArgumentException("\n\t-> ENTER POSITIVE LENGTH TO CREATE A VALID NUMBER.");
		String num = "";
		for (int i = 0; i < numLength; i++) {
			int dgt = digits[randomNum(digits.length - 1)];
			if (nonZero && i == 0 && dgt == 0)
				--i;
			else
				num = num + dgt;
		}
		return num;
	}

	/** returns a random num between min(parameter-1) & max(parameter-2) both inclusive */
	public static int randomNumInRange(int min, int max) {
		int n1 = Math.min(min, max), n2 = Math.max(min, max);
		return n1 + (new Random().nextInt(n2 + 1 - n1));
	}

	/** Create non-negative <b>integer, as String,</b> of length=parameter. <br>
	 * EXCEPTION if negative parameter. */
	public static String randomNonNegNumAsString(int numLength) {
		return randomNum(numLength, false);
	}

	/** Create non-negative <b>integer</b> of length=parameter. <br>
	 * EXCEPTION if negative parameter. */
	public static int randomNonNegNum(int numLength) {
		return Integer.parseInt(randomNum(numLength, true));
	}

	/** Create non-negative, non-zero <b>integer, as String,</b> of length=parameter.<br>
	 * EXCEPTION if negative parameter. */
	public static String randomPositiveNumAsString(int numLength) {
		return randomNum(numLength, true);
	}

	/** Create non-negative, non-zero <b>integer</b> of length=parameter.<br>
	 * EXCEPTION if negative parameter. */
	public static int randomPositiveNum(int numLength) {
		return Integer.parseInt(randomNum(numLength, true));
	}

	/** To print a space-delimited, int-array(parameter-1) with given String title(parameter-2) */
	public static void printIntArr(int[] arr, String heading) {
		System.out.print("--------------------------------------------------" + heading + "\t[ ");
		for (int i : arr)
			System.out.print(i + " ");
		System.out.println(" ]");
	}

	/** To print newLine-delimited, toString() of Object-array(param_1) elements with given String title(param_2) */
	public static void printObjArr(Object[] arr, String heading) {
		printObjArr(arr, heading, "\n");
	}

	/** To print param_3-delimited, toString() of Object-array(param_1) elements with given String title(param_2) */
	public static void printObjArr(Object[] arr, String heading, String delimiter) {
		System.out.print("--------------------------------------------------" + heading + "\n[ ");
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i]);
			if (i != arr.length - 1)
				System.out.print(delimiter);
		}
		System.out.println(" ]");
	}

	/** To simply print toString() of an Object(param_1) with given String title(param_2) */
	public static void printObj(Object obj, String heading) {
		System.out.println("--------------------------------------------------" + heading);
		System.out.println(obj);
	}

	/** To print hard-coded instructions to delay some task by given time in sec(parameter-1) */
	public static void instructions(int timerSeconds) {
		instructions("NEXT TASK WILL START IN (SECONDS):", timerSeconds);
	}

	/** To print given instructions(parameter-1) to delay some task by given time in sec(parameter-2) */
	public static void instructions(String msg, int timerSeconds) {
		System.out.print(msg);
		while (timerSeconds > 0) {
			System.out.print("\t" + timerSeconds--);
			rest(1000);
		}
		System.out.println("\t.\t.\t.");
		rest(timerSeconds);
		System.out.println("\n----------------------------------------------------------------------------------------------------");
	}

	public static void printString(String s, int waitTime, String before, String after) {
		System.out.print(before + s + after);
		rest(waitTime);
	}

	public static void printChar(char i, int waitTime, String before, String after) {
		System.out.print(before + i + after);
		rest(waitTime);
	}

	public static void printInt(int i, int waitTime, String before, String after) {
		System.out.print(before + i + after);
		rest(waitTime);
	}

	/** <b>params:<br>
	 * print_t, printAfterTime, printBefore, printAfter, append_t_ToFile </b> */
	public static <T> void printGeneric(T t, int wait, String bfr, String aftr, String file) {
		System.out.print(bfr.toString() + t + aftr.toString());
		rest(wait);
		if (file != null && !file.isEmpty())
			appendToFile(file, "" + t);
	}

	/** <b><u>default seaparation</u>: newline</b> **/
	public static <T> void printGeneric(String seaparateBy, T... Ts) {
		printGeneric(seaparateBy, 0, Ts);
	}

	/** <b><u>default seaparation</u>: newline</b> **/
	public static <T> void printGeneric(String seaparateBy, int wait, T... Ts) {
		for (T t : Ts) {
			rest(wait);
			System.out.print(t + (seaparateBy == null || seaparateBy.isEmpty() ? "\n" : seaparateBy));
		}
	}

	public static void welcome() {
		welcome("\n\tHELLO!\n");
	}

	public static void welcome(String msg) {
		System.out.println(msg);
	}

	public static void bye() {
		bye("\n\n\n\n\t\t\t\t\t*** BYE! Have A Good Day. ***");
	}

	public static void bye(String msg) {
		System.out.println(msg);
	}

	public static void rest(int waitTime) {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** <b>Write to file:<br>
	 * <font color=red>###CAUTION###:</b><br>
	 * 'TO APPEND or REMOVE OLD DATA?'</font> */
	public static boolean writeToFile(final String FILE_NAME, String string, boolean append) {
		try (FileWriter fw = new FileWriter(FILE_NAME, append); PrintWriter pw = new PrintWriter(fw)) {
			pw.println(string);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** Write: <b><font style='color:green'>same as rewrite. Old Content GETS DELETED</font></b> */
	public static boolean writeToFile(final String FILE_NAME, String string) {
		return writeToFile(FILE_NAME, string, false);
	}

	/** Append: <b><font style='color:green'>add to existing. Old Content NOT DELETED</font></b> */
	public static boolean appendToFile(final String FILE_NAME, String string) {
		return writeToFile(FILE_NAME, string, true);
	}

	public static File clearFile(final String FILE_NAME) {
		ArrayList<String> strs = new ArrayList<>();
		File f = new File(FILE_NAME);
		if (f.exists())
			try {
				f.delete();
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return f;
	}

	/** return current date in 'yyyymmdd' format as a String */
	public static String currentDate() {
		LocalDate now = LocalDate.now();
		String year = "" + now.getYear();
		String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : "" + now.getMonthValue();
		String day = now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : "" + now.getDayOfMonth();
		return "" + year + month + day;
	}

	/** return current time in 'hhmmss' format as a String */
	public static String currentTime() {
		LocalTime now = LocalTime.now();
		String hh = now.getHour() < 10 ? "0" + now.getHour() : "" + now.getHour();
		String mm = now.getMinute() < 10 ? "0" + now.getMinute() : "" + now.getMinute();
		String ss = now.getSecond() < 10 ? "0" + now.getSecond() : "" + now.getSecond();
		return "" + hh + mm + ss;
	}

	public static int LCM(List<Integer> numbers) {
		for (int i = 1; i < numbers.size(); i++) {
			if (numbers.get(0) != numbers.get(i))
				break;
			else if (i < numbers.size() - 1)
				continue;
			System.out.println("\tBecause 'given numbers are equal', so:");
			return numbers.get(0);
		}

		boolean raise = false;
		int max = findMax(numbers);
		List<Integer> ints = numbers;
		List<Integer> lcmList = new ArrayList<Integer>(16);
		int i = 2;
		one: while (i <= max) {
			two: for (int j = 0; j < ints.size(); j++) {
				if (ints.get(j) % i == 0) {
					// System.out.print("\t " + i + "\t| ");
					// for( int x = 0; x < ints.size(); x++ )
					// System.out.print(ints.get(x) + (x == ints.size() - 1 ? "" : ", "));
					for (int x = 0; x < ints.size(); x++)
						if (ints.get(x) % i == 0)
							ints.set(x, ints.get(x) / i);
					lcmList.add(i);
					// System.out.println("\n\t---------------------------------------------------------------");
					// rest(500); // waitTime per step
					raise = false;
					break two;
				} else
					raise = true;
			}
			three: for (int x = 0; x < ints.size(); x++) {
				if (ints.get(x) != 1)
					break three;
				if (x == ints.size() - 1) {
					// System.out.print("\t\t| ");
					// for( int j = 0; j < ints.size(); j++ )
					// System.out.print(ints.get(x) + (j == ints.size() - 1 ? "" : ", "));
					break one;
				}
			}
			if (raise)
				i++;
		}
		return calcLCM(lcmList);
	}

	public static int calcLCM(List<Integer> lcmList) {
		int lcm = 1;
		for (Integer fctr : lcmList) {
			// sb.append(fctr).append(" X ");
			lcm = lcm * fctr;
		}
		// sb.replace(sb.length() - 3, sb.length() - 1, " ").trimToSize();
		return lcm;
	}

	public static String getInputProp(String prop) throws IOException {
		String propFile = "properties/input.properties";
		return ReadProperties.getProperty(propFile, prop);
	}
}
