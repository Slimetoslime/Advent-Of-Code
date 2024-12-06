package year2024;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day2 {

	public static void main(String[] args) {
		File puzzle = new File("input/2024/Day2.txt");
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
			int result = 0;

			for (String report : puzzleLines) {
				List<Integer> levels = Arrays.asList(report.split("\\s+")).stream().map(Integer::valueOf)
						.collect(Collectors.toList());

				if (isSafe(levels)) {
					result++;
				}
			}

			System.out.println("Part 1: " + result);
		}
	}

	static boolean isSafe(List<Integer> numList) {
		boolean isIncreasing = numList.get(0) < numList.get(1);
		for (int i = 1; i < numList.size(); i++) {
			int gap = Math.abs(numList.get(i - 1) - numList.get(i));
			if (gap < 1 || gap > 3 || (isIncreasing && numList.get(i - 1) > numList.get(i))
					|| (!isIncreasing && numList.get(i - 1) < numList.get(i))) {
				return false;
			}
		}

		return true;
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
			int result = 0;

			for (String report : puzzleLines) {
				List<Integer> levels = Arrays.asList(report.split("\\s+")).stream().map(Integer::valueOf)
						.collect(Collectors.toList());
				
				if (isSafe(levels)) {
					result++;
				} else {
					for (int i = 0; i < levels.size(); i++) {
						int removed = levels.remove(i);
						
						if (isSafe(levels)) {
							result++;
							break;
						}
						
						levels.add(i, removed);
					}
				}
			}

			System.out.println("Part 2: " + result);
		}
	}
}
