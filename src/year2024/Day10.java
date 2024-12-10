package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day10 {
	
	/** Class for Location
	 * 
	 */
	static class Location {
		
		private int row;
		private int column;
		private int height;
		
		/** Initialize based on row and column index and the height.
		 * 
		 * @param row Row index
		 * @param column Column index
		 * @param height Height of the tile.
		 */
		public Location(int row, int column, int height) {
			this.row = row;
			this.column = column;
			this.height = height;
		}
		
		/** Initialize based on another location, shifted a certain amount with some height.
		 * 
		 * @param currentLocation Reference location.
		 * @param rowShift Row index shift.
		 * @param columnShift Column index shift.
		 * @param height Height of the tile.
		 */
		public Location(Location currentLocation, int rowShift, int columnShift, int height) {
			this.row = currentLocation.row + rowShift;
			this.column = currentLocation.column + columnShift;
			this.height = height;
		}
		
		/** For equality checking and HashSets.
		 * 
		 */
		@Override
		public int hashCode() {
			return Objects.hash(column, height, row);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Location other = (Location) obj;
			return column == other.column && height == other.height && row == other.row;
		}

		/** Visualizing purposes.
		 * 
		 */
		@Override
		public String toString() {
			return String.format("(%d, %d, %d)", this.row, this.column, this.height);
		}
	}
	
	private static List<List<String>> map = new ArrayList<>();
	private static final int[][] CARDINAL_DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
	private static Map<Location, Set<Location>> startEndLocations = new HashMap<>();
	
	// Runner Code
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day10.txt");
		map = ReadFiles.readIntoMatrix(puzzleFile);
		
		Set<Location> startingPosition = getLocationAtHeight(0);
		for (Location startPosition: startingPosition) {
			startEndLocations.put(startPosition, new HashSet<>(Arrays.asList(startPosition)));
		}
		
		Part1.part();
		Part2.part();
	}
	
	/** Retrieves a Set of locations on the map based on height.
	 * 
	 * @param targetHeight Specified height to search for.
	 * @return List of Locations with the specified height.
	 */
	public static Set<Location> getLocationAtHeight(int targetHeight) {
		Set<Location> startPos = new HashSet<>();
		
		for (int r = 0; r < map.size(); r++) {
			List<String> row = map.get(r);
			for (int c = 0; c < row.size(); c++) {
				int height;
				if (row.get(c).equals(".")) height = -1;
				else height = Integer.valueOf(row.get(c));
				if (height == targetHeight) startPos.add(new Location(r, c, Integer.valueOf(row.get(c))));
			}
		}
		
		return startPos;
	}
	
	/** Returns the next possible location based on a List of current locations.
	 * 
	 * @param currentLocations List of current Locations.
	 * @param nextStep next height to go from current height.
	 * @return A List of Locations that are adjacent to some current location and can be reached to next height from said location.
	 */
	private static List<Location> getNextLocations(List<Location> currentLocations, int nextStep) {
		List<Location> nextLocations = new ArrayList<>();
		Set<Location> possibleLocations = getLocationAtHeight(nextStep);
		
		for (Location location: currentLocations) {
			List<Location> neighboringLocations = new ArrayList<>();
			for (int[] direction: CARDINAL_DIRECTIONS) {
				neighboringLocations.add(new Location(location, direction[0], direction[1], nextStep));
			}
			
			nextLocations.addAll(neighboringLocations.stream().filter(neighborLocation -> possibleLocations.contains(neighborLocation)).toList());
		}
		
		return nextLocations;
	}
	
	/** Moves a location from one height to a higher-by-one height.
	 * 
	 * @param currentLocation Current location.
	 * @return List of possible Locations to move to.
	 */
	private static List<Location> move(Location currentLocation) {
		int nextStep = currentLocation.height + 1;
		List<Location> nextLocations = new ArrayList<>();
		Set<Location> possibleLocations = getLocationAtHeight(nextStep);
		
		for (int[] direction: CARDINAL_DIRECTIONS) {
			Location neighborLocation = new Location(currentLocation, direction[0], direction[1], nextStep);
			if (possibleLocations.contains(neighborLocation)) nextLocations.add(neighborLocation);
		}
		
		return nextLocations;
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
		public static void part() {
			Map<Location, Set<Location>> finalLocations = new HashMap<>();
			
			for (Map.Entry<Location, Set<Location>> entry: startEndLocations.entrySet()) {
				Set<Location> locationsToCheck = entry.getValue();
				for (int i = 1; i < 10; i++) {
					locationsToCheck = new HashSet<>(getNextLocations(new ArrayList<>(locationsToCheck), i));
				}
				finalLocations.put(entry.getKey(), locationsToCheck);
			}
			
			System.out.println("Part 1: " + finalLocations.values().stream().mapToInt(Set::size).sum());
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
			Map<Location, Integer> finalRoutes = new HashMap<>();
			
			for (Map.Entry<Location, Set<Location>> entry: startEndLocations.entrySet()) {
				Set<List<Location>> paths = new LinkedHashSet<>();
				Queue<List<Location>> toVisit = new LinkedList<>();
				List<Location> path = new LinkedList<>();
				path.add(entry.getKey());
				toVisit.add(path);
				
				while (!toVisit.isEmpty()) {
					path = toVisit.poll();
					Location currentLocation = path.getLast();
					
					if (currentLocation.height >= 9) {
						paths.add(path);
						continue;
					}
					
					List<Location> nextLocations = move(currentLocation);
					for (Location nextLocation: nextLocations) {
						List<Location> newPath = new LinkedList<>(path);
						newPath.add(nextLocation);
						toVisit.offer(newPath);
					}
				} 
				
				finalRoutes.put(entry.getKey(), paths.size());
			}
	
			System.out.println("Part 2: " + finalRoutes.values().stream().mapToInt(Integer::valueOf).sum());
		}
	}
}


