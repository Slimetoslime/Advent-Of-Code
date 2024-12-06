package year2023;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * 								RUNNER CODE
 * =========================================================================
 */

public class Day6 {
	
	public static void main(String[] args) {
		File puzzle = new File("input/2023/Day6.txt");
		List<String> puzzleLines = ReadFiles.readLineByLine(puzzle);
		
		Part1.part(puzzleLines);
		Part2.part(puzzleLines);
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
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part(List<String> puzzleLines) {
			List<Integer> timeAllowed = new ArrayList<>();
			List<Integer> minimumDistance = new ArrayList<>();
			
			String[] rawTime = puzzleLines.get(0).split("\\s+");
			String[] rawDistance = puzzleLines.get(1).split("\\s+");
			for (int i = 1; i < rawTime.length; i++) {
				timeAllowed.add(Integer.valueOf(rawTime[i]));
				minimumDistance.add(Integer.valueOf(rawDistance[i]));
			}
			
			List<Integer> result = new ArrayList<>();
			
			for (int i = 0; i < timeAllowed.size(); i++) {
				int time = timeAllowed.get(i);
				int minDistance = minimumDistance.get(i);
				int passingTimes = 0;
				
				for (int n = 0; n < time; n++) {
					if (n * (time - n) > minDistance) passingTimes++;
				}
				
				result.add(passingTimes);
			}

			System.out.println("Part 1: " + result.stream().reduce(1, (a, b) -> a * b));
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
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part(List<String> puzzleLines) {
			String timeAllowedString = "";
			String minimumDistanceString = "";
			
			String[] rawTime = puzzleLines.get(0).split("\\s+");
			String[] rawDistance = puzzleLines.get(1).split("\\s+");
			for (int i = 1; i < rawTime.length; i++) {
				timeAllowedString += rawTime[i];
				minimumDistanceString += rawDistance[i];
			}
			
			long timeAllowed = Long.valueOf(timeAllowedString);
			long minimumDistance = Long.valueOf(minimumDistanceString);
			long result = 0;
			
			for (long n = 0; n < timeAllowed; n++) {
				if (n * (timeAllowed-n) > minimumDistance) result++;
			}
			
			System.out.println("Part 2: " + result);
		}
	}
}


