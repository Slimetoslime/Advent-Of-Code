package year2023;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day7 {

	public static void main(String[] args) {
		File puzzle = new File("test/2023/Day7.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		List<List<String>> handsAndBids = puzzleLines.stream().map(s -> Arrays.asList(s.split(" "))).toList();
		Part1.part(handsAndBids);
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
		public static void part(List<List<String>> puzzleLines) {
			for (List<String> handAndBid : puzzleLines) {
				System.out.println(handAndBid);
				String hand = handAndBid.get(0);
				getHandTypes(hand);
			}

			System.out.println("Part 1: " + puzzleLines);
		}

		public static String getHandTypes(String hand) {
			int[] handCount = new int[128];
			for (char card : hand.toCharArray())
				handCount[card]++;

			for (int i : handCount)
				System.out.print(i + " ");
			System.out.println();
			return null;
		}
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

			System.out.println("Part 2: ");
		}
	}
}
