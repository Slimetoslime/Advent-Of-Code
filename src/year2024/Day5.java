package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day5 {

	public static Map<String, List<String>> rules = new HashMap<>();
	public static Map<String, List<String>> reversedRules = new HashMap<>();

	public static void main(String[] args) {
		File puzzle = new File("input/2024/Day5.txt");
		List<String> puzzleLines = Arrays.asList(ReadFiles.readFileRaw(puzzle).split("\\r\\n\\r\\n"));

		setRules(Arrays.asList(puzzleLines.get(0).split("\\r\\n")));

		Part1.part(Arrays.asList(puzzleLines.get(1).split("\\r\\n")));
		Part2.part(Arrays.asList(puzzleLines.get(1).split("\\r\\n")));
	}

	/**
	 * Sets the ordering rule of page numbers.
	 * 
	 * @param rulesString A list of Strings containing the rules for the available
	 *                    page numbers.
	 */
	private static void setRules(List<String> rulesString) {
		for (String rule : rulesString) {
			String[] splitRule = rule.split("\\|");
			String before = splitRule[0];
			String after = splitRule[1];

			if (rules.containsKey(before)) {
				rules.get(before).add(after);
			} else {
				rules.put(before, new ArrayList<>(Arrays.asList(after)));
			}

			if (reversedRules.containsKey(after)) {
				reversedRules.get(after).add(before);
			} else {
				reversedRules.put(after, new ArrayList<>(Arrays.asList(before)));
			}
		}
	}

	/**
	 * Custom comparator object that compares 2 Strings with the HashMap rules
	 * listed above.
	 * 
	 */
	static class sortOnRules implements Comparator<String> {
		@Override
		public int compare(String s1, String s2) {
			if (rules.containsKey(s1) && rules.get(s1).contains(s2))
				return -1;
			else if (reversedRules.containsKey(s2) && reversedRules.get(s2).contains(s1))
				return 1;
			return 0;
		}
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
			List<Integer> validUpdates = new ArrayList<>();

			for (String unsplitPages : puzzleLines) {
				List<String> pageNumbers = Arrays.asList(unsplitPages.split(","));
				List<String> sortedPageNumbers = pageNumbers.stream().sorted(new sortOnRules()).toList();

				if (pageNumbers.equals(sortedPageNumbers)) {
					validUpdates.add(Integer.valueOf(pageNumbers.get(pageNumbers.size() / 2)));
				}
			}

			System.out.println("Part 1: " + validUpdates.stream().mapToInt(Integer::valueOf).sum());
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
			List<Integer> validatedInvalids = new ArrayList<>();

			for (String unsplitPages : puzzleLines) {
				List<String> pageNumbers = Arrays.asList(unsplitPages.split(","));
				List<String> sortedPageNumbers = pageNumbers.stream().sorted(new sortOnRules()).toList();

				if (!pageNumbers.equals(sortedPageNumbers)) {
					Collections.sort(pageNumbers, new sortOnRules());
					validatedInvalids.add(Integer.valueOf(pageNumbers.get(pageNumbers.size() / 2)));
				}
			}

			System.out.println("Part 2: " + validatedInvalids.stream().mapToInt(Integer::valueOf).sum());
		}
	}

	/**
	 * Checks if the list of page numbers is in the correct order. [OBSOLETE]
	 * 
	 * @param pageNumbers List of page numbers.
	 * @return Boolean value to see if its already sorted.
	 */
	public static boolean checkForValidity(List<String> pageNumbers) {
		int currentPage = 0;
		int reversedCurrentPage = pageNumbers.size() - 1;
		boolean valid = true;

		while (currentPage < pageNumbers.size()) {
			List<String> listRules = rules.get(pageNumbers.get(currentPage));
			List<String> listReversedRules = reversedRules.get(pageNumbers.get(reversedCurrentPage));
			for (int i = currentPage + 1; i < pageNumbers.size(); i++) {
				try {
					if (!listRules.contains(pageNumbers.get(i)))
						valid = false;
				} catch (NullPointerException npe) {
					continue;
				}
			}
			for (int i = reversedCurrentPage - 1; i > -1; i--) {
				try {
					if (!listReversedRules.contains(pageNumbers.get(i)))
						valid = false;
				} catch (NullPointerException npe) {
					continue;
				}
			}
			currentPage++;
			reversedCurrentPage--;
		}

		if (valid) {
			return true;
		}
		return false;
	}
}
