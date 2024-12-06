package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * 								RUNNER CODE
 * =========================================================================
 */

public class Day6 {
	
	private static enum Directions {up, down, left, right};
	
	/** Class to keep track of the security guard's location and direction.
	 * 
	 */
	static class SecurityGuard {
		
		private int[] location;
		private Directions direction;
		
		public SecurityGuard(int[] location, Directions direction) {
			this.location = location;
			this.direction = direction;
		}
		
		@Override
		public String toString() {
			return String.format("Guard is at index [row: %d, column: %d] and is facing [%s]", location[0], location[1], direction);
		}
	}
	
	/** Runner code.
	 * 
	 * @param args String arguments.
	 */
	public static void main(String[] args) {
		File puzzle = new File("input/2024/Day6.txt");
		List<List<String>> map = ReadFiles.readIntoMatrix(puzzle);
		int[] originalLocation = getLocation(map);
		Directions originalDirection = getDirection(map);
		SecurityGuard guard = new SecurityGuard(originalLocation, originalDirection);
		
		Set<List<Integer>> tilesVisited = Part1.part(map, guard);
		Part2.part(map, tilesVisited, originalLocation, originalDirection);
	}
	
	/** Returns the initial direction of the security guard.
	 * 
	 * @param map A grid containing the security guard and obstacles.
	 * @return the initial direction that the security guard is facing.
	 */
	private static Directions getDirection(List<List<String>> map) {
		Directions result = null;
		for (List<String> row: map) {
			for (String element: row) {
				switch (element) {
					case "^" -> result = Directions.up;
					case ">" -> result = Directions.right;
					case "v" -> result = Directions.down;
					case "<" -> result = Directions.left;
				}
				
				if (result != null) return result;
			}
		}
		throw new IllegalArgumentException("Map has no security guards!");
	}
	
	/** Returns a new direction for the guard, following the 90 degrees right turn rule.
	 * 
	 * @param guard the security guard.
	 * @return the updated direction of the guard.
	 */
	private static Directions getNewDirection(SecurityGuard guard) {
		Directions newDir = null;
		switch (guard.direction) {
		case Directions.up -> newDir = Directions.right;
		case Directions.right -> newDir = Directions.down;
		case Directions.down -> newDir = Directions.left;
		case Directions.left -> newDir = Directions.up;
		}
		return newDir;
	}
	
	/** Returns an initial location of the security guard. 
	 * 
	 * @param map the grid containing the security guard and obstacles.
	 * @return an Integer Array index of the security guard's location.
	 */
	private static int[] getLocation(List<List<String>> map) {
		List<String> directionsChar = new ArrayList<String>(Arrays.asList("^", ">", "v", "<"));
		for (int r = 0; r < map.size(); r++) {
			List<String> row = map.get(r);
			for (int c = 0; c < row.size(); c++) {
				if (directionsChar.contains(row.get(c))) return new int[] {r, c}; 
			}
		}
		throw new IllegalArgumentException("Map has no security guards!");
	}
	
	/** Computes a new location for the guard to move, according to the direction they're facing.
	 * 
	 * @param guard the security guard.
	 * @return an Integer Array index of the new location.
	 */
	private static int[] getNewLocation(SecurityGuard guard) {
		int[] newLocation = new int[] {guard.location[0], guard.location[1]};
		switch (guard.direction) {
		case Directions.up -> newLocation[0]--;
		case Directions.down -> newLocation[0]++;
		case Directions.right -> newLocation[1]++;
		case Directions.left -> newLocation[1]--;
		}
		return newLocation;
	}
	
	/** Progresses the simulation of the security guard by one step.
	 * 
	 * @param map Map of the place.
	 * @param guard the security guard.
	 * @return a boolean value if the guard leaves the map entirely.
	 */
	private static boolean step(List<List<String>> map, SecurityGuard guard) {
		int[] newLocation = getNewLocation(guard);
		Directions newDirection = getNewDirection(guard);
		
		try {
			if (map.get(newLocation[0]).get(newLocation[1]).equals("#")) guard.direction = newDirection;
			else guard.location = newLocation;
			return true;
		} catch (IndexOutOfBoundsException ioobe) {
			return false;
		}
		
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
		public static Set<List<Integer>> part(List<List<String>> map, SecurityGuard guard) {
			Set<List<Integer>> tilesVisited = new HashSet<>();
			
			do {
				List<Integer> currentLocation = Arrays.asList(Arrays.stream(guard.location).boxed().toArray(Integer[]::new));
				tilesVisited.add(currentLocation);
			} while (step(map, guard));

			System.out.println("Part 1: " + tilesVisited.size());
			return tilesVisited;
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
		 * Brute-force approach, never feels good. I don't know how to do this efficiently though.
		 * 
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part(List<List<String>> map, Set<List<Integer>> tilesVisited, int[] orLoc, Directions orDir) {
			int maxSteps = map.size() * map.get(0).size(); // Arbitrary value so the simulataion would actually end. 
			int result = 0;
			
			for (List<Integer> indexBlocks: tilesVisited) {
				List<List<String>> newMap = new ArrayList<>(map);
				List<String> newRow = new ArrayList<>(map.get(indexBlocks.get(0)));
				newRow.set(indexBlocks.get(1), "#");
				newMap.set(indexBlocks.get(0), newRow);
				SecurityGuard newGuard = new SecurityGuard(orLoc, orDir);
				
				int n = 0;
				while (step(newMap, newGuard) && n < maxSteps) {
					n++;
				}
				
				if (n >= maxSteps) result++;
			}
	
			System.out.println("Part 2: " + result);
		}
	}
}


