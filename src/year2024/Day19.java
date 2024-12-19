package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day19 {
	
	private static List<String> availableStripes = new ArrayList<>();
	
	// Runner Code
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day19.txt");
		List<String> puzzle = ReadFiles.readDoubleGap(puzzleFile);
		List<String> listOfDesigns = Arrays.asList(puzzle.get(1).split(System.lineSeparator()));
		
		parseAvailableStripes(puzzle.get(0));
		List<String> possibleDesigns = listOfDesigns.stream().filter(a -> checkDesign(a, new HashMap<>())).toList();
		
		Part1.part(possibleDesigns);
		Part2.part(possibleDesigns);
	}
	
	/** Initializes the available stripes list.
	 * 
	 * @param stripesString a String containing the different stripes to use.
	 */
	private static void parseAvailableStripes(String stripesString) {
		String[] stripeArray = stripesString.split(", ");
		for (String stripe: stripeArray) {
			availableStripes.add(stripe);
		}
	}
	
	/** Checks the design to see if its possible to construct from the available stripes.
	 * 
	 * @param design String design.
	 * @param memory HashMap for memory
	 * @return Boolean value if its possible.
	 */
	private static boolean checkDesign(String design, Map<String, Boolean> memory) {
		if (design.isBlank()) return true;
		if (memory.containsKey(design) && memory.get(design)) return memory.get(design);
		
		memory.put(design, false);
		
		for (String possibleStripe: availableStripes) {
			if (design.startsWith(possibleStripe) && checkDesign(design.substring(possibleStripe.length()), memory)) 
				memory.put(design, true);
		}
		
		return memory.get(design);
	}
	
	/** Counts the number of ways a design could be replicated with the available stripes.
	 * 
	 * @param design String design.
	 * @return a Long number of the number of ways to create the design.
	 */
	private static long countWays(String design) {
		long[] result = new long[design.length()+1];
		result[0] = 1;
		
		for (int i = 0; i < design.length(); i++) {
			if (result[i] == 0) continue;
			
			for (String s: availableStripes) {
				if (design.regionMatches(i, s, 0, s.length())) {
					result[i + s.length()] += result[i];
				}
			}
		}
		
		return result[design.length()];
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
		public static void part(List<String> possibleDesigns) {

			System.out.println("Part 1: " + possibleDesigns.size());
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
		public static void part(List<String> possibleDesigns) {
			long finalResult = 0;
			for (String design: possibleDesigns) {
				finalResult += countWays(design);
			}
	
			System.out.println("Part 2: " + finalResult);
		}
	}
}


