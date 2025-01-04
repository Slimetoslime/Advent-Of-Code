package year2024;

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

public class Day25 {
	
	static class Schematic {
		private char lockOrKey;
		private int[] pinHeights;
		
		public Schematic(String[] locks) {
			if (locks[0].equals("#####")) {
				lockOrKey = '#';
			} else if (locks[0].equals(".....")) {
				lockOrKey = '.';
			}
			
			pinHeights = new int[5];
			for (int i = 1; i < locks.length-1; i++) {
				String row = locks[i];
				for (int p = 0; p < row.length(); p++) {
					char state = row.charAt(p);
					if (state == '#') pinHeights[p]++;
				}
			}
		}
		
		@Override
		public String toString() {
			return String.format("%s: %s", (lockOrKey == '#') ? "lock" : "key", Arrays.toString(pinHeights));
		}
	}
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day25.txt");
		List<String> puzzle = ReadFiles.readDoubleGap(puzzleFile);
		
		Part1.part(puzzle);
		Part2.part();
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
		public static void part(List<String> schematics) {
			List<Schematic> locks = new ArrayList<>();
			List<Schematic> keys = new ArrayList<>();
			
			for (String s: schematics) {
				Schematic toAdd = new Schematic(s.split(System.lineSeparator()));
				if (toAdd.lockOrKey == '#') locks.add(toAdd);
				else if (toAdd.lockOrKey == '.') keys.add(toAdd);
			}
			
			int result = 0;
			for (Schematic lock: locks) {
				int[] lockHeights = lock.pinHeights;
				for (Schematic key: keys) {
					boolean valid = true;
					int[] keyHeights = key.pinHeights;
					for (int i = 0; i < keyHeights.length; i++) {
						if (lockHeights[i] + keyHeights[i] > 5) {
							valid = false;
							break;
						}
					}
					if (valid) result++;
				}
			}

			System.out.println("Part 1: " + result);
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
		public static void part() {
	
			System.out.println("Part 2: Merry Christmas!");
		}
	}
}


