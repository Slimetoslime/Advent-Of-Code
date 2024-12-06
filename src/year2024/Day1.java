package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day1 {

	public static void main(String[] args) {
		File puzzle = new File("input/2024/Day1.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		List<String> leftList = new ArrayList<>();
		List<String> rightList = new ArrayList<>();

		for (String s : puzzleLines) {
			String[] leftRightElements = s.strip().split("\\s+");
			leftList.add(leftRightElements[0]);
			rightList.add(leftRightElements[1]);
		}

		Part1.part(leftList, rightList);
		Part2.part(leftList, rightList);
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
		public static void part(List<String> leftSide, List<String> rightSide) {
			Collections.sort(leftSide);
			Collections.sort(rightSide);
			List<Integer> distances = new ArrayList<>();

			for (int i = 0; i < leftSide.size(); i++) {
				distances.add(Math.abs(Integer.valueOf(leftSide.get(i)) - Integer.valueOf(rightSide.get(i))));
			}

			System.out.println("Part 1: " + distances.stream().mapToInt(Integer::valueOf).sum());
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
		public static void part(List<String> leftSide, List<String> rightSide) {
			List<Integer> similarityScores = new ArrayList<>();

			for (String leftSideElement : leftSide) {
				int occurences = Collections.frequency(rightSide, leftSideElement);
				similarityScores.add(Integer.valueOf(leftSideElement) * occurences);
			}

			System.out.println("Part 2: " + similarityScores.stream().mapToInt(Integer::valueOf).sum());
		}
	}
}
