package year2023;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * 								RUNNER CODE
 * =========================================================================
 */

public class Day2 {
	// Runner code
	public static void main(String[] args) {
		File puzzle = new File("input/2023/dayTwo.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		Day2_1.part(puzzleLines);
		Day2_2.part(puzzleLines);
	}
}

/**
 * =========================================================================
 * 								START OF PART 1
 * =========================================================================
 */

class Day2_1 {

	private static final Map<String, Integer> itemsInBag = new HashMap<>() {
		private static final long serialVersionUID = 1L;
		{
			put("red", 12);
			put("blue", 14);
			put("green", 13);
		}
	};

	/**
	 * Solves Advent of Code Day 2-1.
	 * 
	 * @param puzzleInput Map mapping integers to lists of maps containing string
	 *                    keys and integer values.
	 */
	public static void part(List<String> puzzleLines) {
		Map<Integer, List<Map<String, Integer>>> puzzleInput = parseString(puzzleLines);
		int results = 0;

		for (Map.Entry<Integer, List<Map<String, Integer>>> entry : puzzleInput.entrySet()) {
			if (isPossible(entry.getValue()))
				results += entry.getKey();
		}

		System.out.println("Part 1: " + results);
	}

	/**
	 * Checks if the given number of cubes in each colour can fit into the bag
	 * defined in itemsInBag.
	 * 
	 * @param shows a Map mapping the String cube colour and Integer number of cubes
	 *              in said colour.
	 * @return Boolean value to see if any of the cubes shown exceeds the number of
	 *         items in the bag.
	 */
	public static boolean isPossible(List<Map<String, Integer>> shows) {
		for (Map<String, Integer> show : shows) {
			Set<String> itemTypesShown = show.keySet();
			for (String itemType : itemTypesShown) {
				if (show.get(itemType) > itemsInBag.get(itemType))
					return false;
			}
		}

		return true;
	}

	/**
	 * Parses the input into the necessary data structure.
	 * 
	 * @param input String input read line-by-line
	 * @return a Map of the Integer game number as keys and a list of maps
	 *         containing the colour and number of cubes shown at an instance.
	 */
	public static Map<Integer, List<Map<String, Integer>>> parseString(List<String> input) {
		Map<Integer, List<Map<String, Integer>>> result = new HashMap<>();

		int i = 1;
		for (String line : input) {
			String newLine = line.substring(line.indexOf(":") + 2);
			List<Map<String, Integer>> shownRes = new ArrayList<>();
			for (String shown : newLine.split(";")) {
				Map<String, Integer> mapResult = new HashMap<>();
				for (String entry : shown.split(",")) {
					String[] numberAndColour = entry.trim().split(" ");
					mapResult.put(numberAndColour[1], Integer.valueOf(numberAndColour[0]));
				}
				shownRes.add(mapResult);
			}
			result.put(i, shownRes);
			i++;
		}

		return result;
	}
}

/**
 * =========================================================================
 * 								START OF PART 2
 * =========================================================================
 */

class Day2_2 {

	/**
	 * Solves Advent of Code Day 2-2.
	 * 
	 * @param puzzleInput Map mapping integers to lists of maps containing string
	 *                    keys and integer values.
	 */
	public static void part(List<String> puzzleLines) {
		Map<Integer, List<Map<String, Integer>>> puzzleInput = parseString(puzzleLines);
		int results = 0;

		for (List<Map<String, Integer>> values : puzzleInput.values()) {
			int product = 1;
			Map<String, Integer> fewestElements = getFewestElements(values);
			for (int i : fewestElements.values())
				product *= i;
			results += product;
		}

		System.out.println("Part 2: " + results);
	}

	/**
	 * Get the number of the fewest possible cubes in order to make a game valid.
	 * 
	 * @param shows List of maps mapping from the String colour of cubes to the
	 *              Integer amount of cubes.
	 * @return a Map mapping the String colour of cubes to the fewest Integer amount
	 *         of cubes needed in order to make the game work.
	 */
	public static Map<String, Integer> getFewestElements(List<Map<String, Integer>> shows) {
		Map<String, Integer> fewestElements = new HashMap<>();
		fewestElements.put("red", 1);
		fewestElements.put("green", 1);
		fewestElements.put("blue", 1);

		for (Map<String, Integer> show : shows) {
			for (String itemType : show.keySet()) {
				int numOfItem = show.get(itemType);
				if (numOfItem > fewestElements.get(itemType))
					fewestElements.put(itemType, numOfItem);
			}
		}

		return fewestElements;
	}

	/**
	 * Parses the input into the necessary data structure.
	 * 
	 * @param input String input read line-by-line
	 * @return a Map of the Integer game number as keys and a list of maps
	 *         containing the colour and number of cubes shown at an instance.
	 */
	public static Map<Integer, List<Map<String, Integer>>> parseString(List<String> input) {
		Map<Integer, List<Map<String, Integer>>> result = new HashMap<>();

		int i = 1;
		for (String line : input) {
			String newLine = line.substring(line.indexOf(":") + 2);
			List<Map<String, Integer>> shownRes = new ArrayList<>();
			for (String shown : newLine.split(";")) {
				Map<String, Integer> mapResult = new HashMap<>();
				for (String entry : shown.split(",")) {
					String[] numberAndColour = entry.trim().split(" ");
					mapResult.put(numberAndColour[1], Integer.valueOf(numberAndColour[0]));
				}
				shownRes.add(mapResult);
			}
			result.put(i, shownRes);
			i++;
		}

		return result;
	}
}
