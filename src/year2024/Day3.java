package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day3 {

	public static void main(String[] args) {
		File puzzle = new File("input/2024/Day3.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		Part1.part(puzzleLines);
		Part2.part(puzzleLines);
	}

	/**
	 * =========================================================================
	 * START OF PART 1
	 * =========================================================================
	 */

	class Part1 {

		/**
		 * Solves the first part of the puzzle.
		 * 
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part(List<String> puzzleLines) {
			List<Integer> productResults = new ArrayList<>();

			Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
			Matcher matcher = pattern.matcher(String.join("", puzzleLines));

			while (matcher.find()) {
				productResults
						.add(multiplyNumbers(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2))));
			}

			System.out.println("Part 1: " + productResults.stream().mapToInt(Integer::valueOf).sum());
		}
	}

	public static int multiplyNumbers(int a, int b) {
		return a * b;
	}

	/**
	 * =========================================================================
	 * START OF PART 2
	 * =========================================================================
	 */

	class Part2 {

		/**
		 * Solves the second part of the puzzle.
		 * 
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part(List<String> puzzleLines) {
			List<Integer> productResults = new ArrayList<>();
			String bigString = String.join("", puzzleLines);
			boolean canMatch = true;

			Pattern pattern = Pattern.compile("(?:mul\\((\\d+),(\\d+)\\)|don't\\(\\)|do\\(\\))");
			Matcher matcher = pattern.matcher(bigString);

			while (matcher.find()) {
				if (matcher.group(0).equals("don't()")) {
					canMatch = false;
					continue;
				} else if (matcher.group(0).equals("do()")) {
					canMatch = true;
					continue;
				}
				if (canMatch) {
					productResults
							.add(multiplyNumbers(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2))));
				}
			}

			System.out.println("Part 2: " + productResults.stream().mapToInt(Integer::valueOf).sum());
		}
	}
}
