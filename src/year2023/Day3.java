package year2023;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * 								RUNNER CODE
 * =========================================================================
 */

public class Day3 {
	
	public static void main(String[] args) {
		File puzzle = new File("input/2023/day3.txt");
		List<List<String>> puzzleLines = ReadFiles.readIntoMatrix(puzzle);
		ReadFiles.printMatrix(puzzleLines);
		Day3_1.part(puzzleLines);
		Day3_2.part(puzzleLines);
	}
}

/**
 * =========================================================================
 * 								START OF PART 1
 * =========================================================================
 */

class Day3_1 {

	/**
	 * Solves Advent of Code 2023 Day 3-1.
	 * 
	 * @param puzzleLines List of list of Strings read from the file line-by-line.
	 */
	public static void part(List<List<String>> puzzleLines) {
		List<Integer> result = validNumbers(puzzleLines);
		
		System.out.println("Part 1: " + result.stream().mapToInt(Integer::intValue).sum());
	}
	
	/** Returns a list of Integers that has a special, non-dot character surrounding it.
	 * 
	 * @param lines List of list of strings from the puzzle input file.
	 * @return A list of Integers that has adjacency with a special character.
	 */
	public static List<Integer> validNumbers(List<List<String>> lines) {
		List<Integer> result = new ArrayList<>();
		
		for (int i = 0; i < lines.size(); i++) {
			String currentNumber = "";
			boolean hasAnySpecials = false;
			List<String> listOfCharacters = lines.get(i);
			for (int j = 0; j < listOfCharacters.size(); j++) {
				String current = listOfCharacters.get(j);
				if (!checkDigit(current)) {
					if (hasAnySpecials) result.add(Integer.valueOf(currentNumber));
					currentNumber = "";
					hasAnySpecials = false;
					continue;
				}
				currentNumber += current;
				hasAnySpecials |= checkSurroundings(lines, i, j);
			}
			if (hasAnySpecials) result.add(Integer.valueOf(currentNumber));
		}
		
		return result;
	}
	
	/** Checks if the single length String is a digit.
	 * 
	 * @param s single length String to check.
	 * @return Boolean value if the String is a digit.
	 */
	public static boolean checkDigit(String s) {
		return Character.isDigit(s.charAt(0));
	}
	
	/** Checks the surroundings of a tile and returns a boolean value based on whether or not the search found a special character.
	 * 
	 * @param board Board state as list of list of singly length Strings.
	 * @param iindex vertical Integer index
	 * @param jindex horizontal Integer index
	 * @return Boolean value of whether its neighbors are anything special.
	 */
	public static boolean checkSurroundings(List<List<String>> board, int iindex, int jindex) {
		final int[][] DIRECTIONS = {
				{-1, 1}, {0, 1}, {1, 1},
				{-1, 0}, {0, 0}, {1, 0},
				{-1, -1}, {0, -1}, {1, -1}
			};
		for (int[] direction: DIRECTIONS) {
			try {
				String adjacentCharacter = board.get(iindex+direction[1]).get(jindex+direction[0]);
				if (!(checkDigit(adjacentCharacter) || adjacentCharacter.contains("."))) return true;
			} catch (IndexOutOfBoundsException ioobe) {
				continue;
			}
		}
		return false;
	}
}

/**
 * =========================================================================
 * 								START OF PART 2
 * =========================================================================
 */

class Day3_2 {

	/**
	 * Solves Advent of Code 2023 Day 3-2.
	 * 
	 * @param puzzleLines List of list of Strings read from the file line-by-line.
	 */
	public static void part(List<List<String>> puzzleLines) {
		Map<List<Integer>, List<Integer>> result = validNumbers(puzzleLines);
		int finalResult = 0;
		for (List<Integer> numsList: result.values()) {
			if (numsList.size() == 2) {
				finalResult += (numsList.get(0) * numsList.get(1));
			}
		}
		
		System.out.println("Part 2: " + finalResult);
	}
	
	/** Returns a list of Integers that has a special, non-dot character surrounding it.
	 * 
	 * @param lines List of list of strings from the puzzle input file.
	 * @return A list of Integers that has adjacency with a special character.
	 */
	public static Map<List<Integer>, List<Integer>> validNumbers(List<List<String>> lines) {
		Map<List<Integer>, List<Integer>> result = new HashMap<>();
		
		for (int i = 0; i < lines.size(); i++) {
			String currentNumber = "";
			List<Integer> coords = new ArrayList<>();
			List<String> listOfCharacters = lines.get(i);
			for (int j = 0; j < listOfCharacters.size(); j++) {
				String current = listOfCharacters.get(j);
				if (!checkDigit(current)) {
					if (coords != null && !coords.isEmpty()) {
						if (result.keySet().contains(coords)) result.get(coords).add(Integer.valueOf(currentNumber));
						else {
							List<Integer> tempList = new ArrayList<Integer>();
							tempList.add(Integer.valueOf(currentNumber));
							result.put(coords, tempList);
						}
					}
					currentNumber = "";
					coords = new ArrayList<>();
					continue;
				}
				currentNumber += current;
				if (coords == null || coords.isEmpty()) coords = checkSurroundings(lines, i, j);
			}
			if (coords != null && !coords.isEmpty()) {
				if (result.keySet().contains(coords)) result.get(coords).add(Integer.valueOf(currentNumber));
				else {
					List<Integer> tempList = new ArrayList<Integer>();
					tempList.add(Integer.valueOf(currentNumber));
					result.put(coords, tempList);
				}
			}
		}
		
		return result;
	}
	
	/** Checks if the single length String is a digit.
	 * 
	 * @param s single length String to check.
	 * @return Boolean value if the String is a digit.
	 */
	public static boolean checkDigit(String s) {
		return Character.isDigit(s.charAt(0));
	}
	
	/** Checks the surroundings of a tile and returns an array containing the coordinates for the gear symbol.
	 *  Returns null if the search doesn't come up with any gear icons. 
	 * 
	 * @param board Board state as list of list of singly length Strings.
	 * @param iindex vertical Integer index
	 * @param jindex horizontal Integer index
	 * @return Integer Array of Size 2 containing the coordinates of the gear icon.
	 */
	public static List<Integer> checkSurroundings(List<List<String>> board, int iindex, int jindex) {
		final int[][] DIRECTIONS = {
				{-1, 1}, {0, 1}, {1, 1},
				{-1, 0}, {0, 0}, {1, 0},
				{-1, -1}, {0, -1}, {1, -1}
			};
		for (int[] direction: DIRECTIONS) {
			try {
				String adjacentCharacter = board.get(iindex+direction[1]).get(jindex+direction[0]);
				if (!checkDigit(adjacentCharacter) && adjacentCharacter.contains("*")) {
					List<Integer> results = new ArrayList<Integer>();
					results.add(iindex+direction[1]);
					results.add(jindex+direction[0]);
					return results;
				};
			} catch (IndexOutOfBoundsException ioobe) {
				continue;
			}
		}
		return null;
	}
}