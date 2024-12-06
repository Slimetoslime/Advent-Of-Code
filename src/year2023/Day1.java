package year2023;

import static java.util.Map.entry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day1 {

	public static void main(String[] args) {
		File puzzle = new File("input/2023/dayOne.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		Day1_1.part(puzzleLines);
		Day1_2.part(puzzleLines);
	}
}

/**
 * =========================================================================
 * START OF PART 1
 * =========================================================================
 */

class Day1_1 {

	/**
	 * Solves Advent of Code 2023 Day 1-1.
	 * 
	 * @param puzzleLines List of Strings read from the file line-by-line.
	 */
	public static void part(List<String> puzzleLines) {
		List<Integer> results = new ArrayList<>();

		for (String s : puzzleLines) {
			String numString = getFirstAndLastNumber(s);
			int result = Integer.valueOf(numString);
			results.add(result);
		}

		System.out.println("Part 1: " + results.stream().mapToInt(Integer::intValue).sum());
	}

	/**
	 * Returns a string representation of a number based on the first and last digit
	 * found in the string.
	 * 
	 * @param s String to get digits out of.
	 * @return String representation of a number.
	 */
	private static String getFirstAndLastNumber(String s) {
		char[] lettersOfS = s.toCharArray();
		boolean foundFirst = false, foundLast = false;
		int stringLength = lettersOfS.length;
		String[] results = new String[2];

		for (int i = 0; i < stringLength; i++) {
			if (!foundFirst && Character.isDigit(lettersOfS[i])) {
				foundFirst = true;
				results[0] = Character.toString(lettersOfS[i]);
			}
			if (!foundLast && Character.isDigit(lettersOfS[stringLength - i - 1])) {
				foundLast = true;
				results[1] = Character.toString(lettersOfS[stringLength - i - 1]);
			}
			if (foundFirst && foundLast)
				return results[0] + results[1];
		}

		throw new IllegalArgumentException("String '" + s + "' does not contain digits!");
	}
}

/**
 * =========================================================================
 * START OF PART 2
 * =========================================================================
 */

class Day1_2 {

	private static final Map<String, String> wordsToNumbers = Map.ofEntries(entry("one", "o1e"), entry("two", "t2o"),
			entry("three", "t3e"), entry("four", "f4r"), entry("five", "f5e"), entry("six", "s6x"),
			entry("seven", "s7n"), entry("eight", "e8t"), entry("nine", "n9e"));

	/**
	 * Solves Advent of Code Day 1-2.
	 * 
	 * @param puzzleLines List of Strings read from a file line-by-line.
	 */
	public static void part(List<String> puzzleLines) {
		List<Integer> results = new ArrayList<>();

		for (String s : puzzleLines) {
			String numString = getFirstAndLastNumber(s);
			int result = Integer.valueOf(numString);
			results.add(result);
		}

		System.out.println("Part 2: " + results.stream().mapToInt(Integer::intValue).sum());
	}

	/**
	 * Reads first and last digit and concatenates them into a string, including
	 * written out numbers (one, two, ...)
	 * 
	 * @param s String to check.
	 * @return String representation of the number.
	 */
	private static String getFirstAndLastNumber(String s) {
		String stringToChange = s;
		for (Map.Entry<String, String> entry : wordsToNumbers.entrySet()) {
			stringToChange = stringToChange.replaceAll(entry.getKey(), entry.getValue());
		}

		char[] lettersOfS = stringToChange.toCharArray();
		int stringLength = lettersOfS.length;
		boolean foundFirst = false, foundLast = false;
		String[] results = new String[2];

		for (int i = 0; i < stringLength; i++) {
			if (!foundFirst && Character.isDigit(lettersOfS[i])) {
				foundFirst = true;
				results[0] = Character.toString(lettersOfS[i]);
			}
			if (!foundLast && Character.isDigit(lettersOfS[stringLength - i - 1])) {
				foundLast = true;
				results[1] = Character.toString(lettersOfS[stringLength - i - 1]);
			}
			if (foundFirst && foundLast)
				return results[0] + results[1];
		}

		throw new IllegalArgumentException("String '" + s + "' does not contain digits!");
	}
}