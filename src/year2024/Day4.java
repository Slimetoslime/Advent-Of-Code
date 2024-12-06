package year2024;

import java.io.File;
import java.util.List;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * 								RUNNER CODE
 * =========================================================================
 */

public class Day4 {
	private static final int[][] NEIGHBORS = {
			{-1, -1}, {-1, 0}, {-1, 1},
			{0, -1}, {0, 1},
			{1, -1}, {1, 0}, {1, 1}
		};
	
	private static final int[][][] CORNER_OPPOSITE_NEIGHBORS = {
			{{-1, -1}, {1, 1}},
			{{1, -1}, {-1, 1}}
	};
	
	public static void main(String[] args) {
		File puzzle = new File("input/2024/Day4.txt");
		List<List<String>> wordSearch = ReadFiles.readIntoMatrix(puzzle);
		Part1.part(wordSearch);
		Part2.part(wordSearch);
	}
	
	/**
 	* =========================================================================
 	* 								START OF PART 1
 	* =========================================================================
 	*/

	class Part1 {

		/**
		 * Solves the first part of the puzzle.
		 * 
		 * @param puzzleLines List of Strings read from the file matrix-style.
		 */
		public static void part(List<List<String>> puzzleLines) {
			int result = 0;
			
			for (int r = 0; r < puzzleLines.size(); r++) {
				for (int c = 0; c < puzzleLines.get(r).size(); c++) {
					result += findWordAtCoords(puzzleLines, "XMAS", r, c);
				}
			}
			
			System.out.println("Part 1: " + result);
		}
		
		/** Get the number of occurrences found of a word in the word search at a specified index.
		 * 
		 * @param board The word search board.
		 * @param word Target word to find.
		 * @param row Row index of the board.
		 * @param column Column index of the board.
		 * @return Number of occurrences of the word found in the board at that index.
		 */
		static int findWordAtCoords(List<List<String>> board, String word, int row, int column) {
			// Checks if the first letter matches with the first letter in the word, returns false immediately if not.
			if (board.get(row).get(column).charAt(0) != word.charAt(0)) return 0;
			int finalResult = 0;
			
			// Loops through the neighbors around the letter.
			for (int i = 0; i < NEIGHBORS.length; i++) {
				int k,
					cursorRow = row + NEIGHBORS[i][0],
					cursorColumn = column + NEIGHBORS[i][1];
				
				// Checks the letter in one of the directions. 
				// If one doesn't match or is out of bounds, it automatically breaks and checks another direction.
				for (k = 1; k < word.length(); k++) {
					try {
						if (board.get(cursorRow).get(cursorColumn).charAt(0) != word.charAt(k)) break;
						cursorRow += NEIGHBORS[i][0];
						cursorColumn += NEIGHBORS[i][1];
					} catch (IndexOutOfBoundsException ioobe) {
						break;
					}
				}
				
				// If the loop did not break at all, then this will pass and we have ourselves a match.
				// Otherwise, it goes on to the next direction of searching.
				if (k == word.length()) finalResult++;
			}
			
			// The code will get here if the directions are all exhausted, which means it didn't find a match.
			return finalResult;
		}
	}
	
	/**
	 * =========================================================================
	 * 								START OF PART 2
	 * =========================================================================
	 */

	class Part2 {

		/**
		 * Solves the second part of the puzzle.
		 * 
		 * @param puzzleLines List of Strings read from the file matrix style.
		 */
		public static void part(List<List<String>> puzzleLines) {
			int result = 0;
			
			for (int r = 0; r < puzzleLines.size(); r++) {
				for (int c = 0; c < puzzleLines.get(r).size(); c++) {
					if (findMASAtCoords(puzzleLines, r, c)) result++;
				}
			}
			
			System.out.println("Part 2: " + result);
		}
		
		/** Returns a boolean value of whether or not an X-shaped word "MAS" was found.
		 * 
		 * @param board Board of the word search.
		 * @param row Row index of the board.
		 * @param column Column index of the board.
		 * @return True if the specified pattern is found, false otherwise.
		 */
		static boolean findMASAtCoords(List<List<String>> board, int row, int column) {
			if (board.get(row).get(column).charAt(0) != 'A') return false;
			
			for (int[][] direction: CORNER_OPPOSITE_NEIGHBORS) {
				try {
					char firstChar = board.get(row + direction[0][0]).get(column + direction[0][1]).charAt(0);
					char secondChar = board.get(row + direction[1][0]).get(column + direction[1][1]).charAt(0);
				
					if (!(firstChar == 'M' && secondChar == 'S') && !(firstChar == 'S' && secondChar == 'M')) return false;
				} catch (IndexOutOfBoundsException ioobe) {
					return false;
				}
			}
			
			return true;
		}
	}
}


