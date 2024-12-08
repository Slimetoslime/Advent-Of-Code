package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day8 {
	
	/** Point Class
	 * 
	 */
	static class Point {
		int rowIndex;
		int columnIndex;
		
		/** Constructor based on the row and column index.
		 * 
		 * @param rowIndex Integer row index.
		 * @param columnIndex Integer column index.
		 */
		public Point(int rowIndex, int columnIndex) {
			this.rowIndex = rowIndex;
			this.columnIndex = columnIndex;
		}
		
		/** Constructor based on another point and shift index and a multiplier.
		 * 
		 * @param startPoint Reference Point to shift from.
		 * @param shiftIndex List of Integers of how many times to shift to (first is for rowIndex and second is for columnIndex).
		 * @param mult Multiplies the shiftIndex by this Integer.
		 */
		public Point(Point startPoint, List<Integer> shiftIndex, int mult) {
			this.rowIndex = startPoint.rowIndex + shiftIndex.get(0) * mult;
			this.columnIndex = startPoint.columnIndex + shiftIndex.get(1) * mult;
		}
		
		/** Calculates the distance between 2 Points
		 *  
		 * @param pointTwo Second Point to compare to.
		 * @return List of Integers with the horizontal and vertical distance between the 2 points.
		 */
		public List<Integer> distanceTo(Point pointTwo) {
			List<Integer> result = new ArrayList<>();
			result.add(pointTwo.rowIndex - this.rowIndex);
			result.add(pointTwo.columnIndex - this.columnIndex);
			return result;
		}
		
		/** HashCode and equals to determine equality.
		 * 
		 */
		@Override
		public int hashCode() {
			return Objects.hash(columnIndex, rowIndex);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point other = (Point) obj;
			return columnIndex == other.columnIndex && rowIndex == other.rowIndex;
		}
		
		/** Converts the Point object to a String representation of the Point.
		 * 
		 */
		@Override
		public String toString() {
			return String.format("(%d, %d)", this.rowIndex, this.columnIndex);
		}
	}
	
	private static Map<String, List<Point>> antennasLocation = new HashMap<>();
	private static int maxRows;
	private static int maxColumns;
	
	// Runner Code
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day8.txt");
		List<List<String>> puzzle = ReadFiles.readIntoMatrix(puzzleFile);
		
		maxRows = puzzle.size();
		maxColumns = puzzle.get(0).size();
		addAntennasLocation(puzzle);
		
		Part1.part();
		Part2.part();
	}
	
	/** Initializes the antennaLocation HashMap based on the map itself.
	 * 
	 * @param map Map of the antennas in a List of List of Strings.
	 */
	public static void addAntennasLocation(List<List<String>> map) {
		for (int r = 0; r < map.size(); r++) {
			for (int c = 0; c < map.get(r).size(); c++) {
				String currentCharacter = map.get(r).get(c);
				if (currentCharacter.equals(".")) continue;
				else if (antennasLocation.containsKey(currentCharacter)) antennasLocation.get(currentCharacter).add(new Point(r, c));
				else antennasLocation.put(currentCharacter, new ArrayList<>(Arrays.asList(new Point(r, c))));
			}
		}
	}
	
	/** Calculates the antinodes from 2 Points.
	 * 
	 * @param coordinateOne First point.
	 * @param coordinateTwo Second point.
	 * @param mult Multiplier for the antinodes' location.
	 * @return List of Points representing the Antinodes.
	 */
	public static List<Point> calculateAntinodes(Point coordinateOne, Point coordinateTwo, int mult) {
		List<Integer> distance = coordinateOne.distanceTo(coordinateTwo);
		List<Integer> reverseDistance = coordinateTwo.distanceTo(coordinateOne);
		Point pointOne = new Point(coordinateTwo, distance, mult);
		Point pointTwo = new Point(coordinateOne, reverseDistance, mult);
		
		return Arrays.asList(pointOne, pointTwo);
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
			Set<Point> resultingAntinodes = new HashSet<>();
			
			for (List<Point> points: antennasLocation.values()) {
				for (int startIndex = 0; startIndex < points.size(); startIndex++) {
					Point firstCoordinate = points.get(startIndex);
					for (int i = startIndex+1; i < points.size(); i++) {
						Point secondCoordinate = points.get(i);
						List<Point> antinodes = calculateAntinodes(firstCoordinate, secondCoordinate, 1);
						for (Point antinode: antinodes) {
							if (antinode.rowIndex < 0 
									|| antinode.rowIndex >= maxRows 
									|| antinode.columnIndex < 0 
									|| antinode.columnIndex >= maxColumns) continue;
							resultingAntinodes.add(antinode);
						} 
					}
				}
			}

			System.out.println("Part 1: " + resultingAntinodes.size());
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
			Set<Point> resultingAntinodes = new HashSet<>();
			
			for (List<Point> points: antennasLocation.values()) {
				for (int startIndex = 0; startIndex < points.size(); startIndex++) {
					Point firstCoordinate = points.get(startIndex);
					for (int i = startIndex+1; i < points.size(); i++) {
						Point secondCoordinate = points.get(i);
						int n = 0;
						int badPointCount = 0;
						while (badPointCount < 2) {
							badPointCount = 0;
							List<Point> antinodes = calculateAntinodes(firstCoordinate, secondCoordinate, n);
							for (Point antinode: antinodes) {
								if (antinode.rowIndex < 0 
										|| antinode.rowIndex >= maxRows 
										|| antinode.columnIndex < 0 
										|| antinode.columnIndex >= maxColumns) {
									badPointCount++;
									continue;
								}
								resultingAntinodes.add(antinode);
							}
							n++;
						}
						
					}
				}
			}
	
			System.out.println("Part 2: " + resultingAntinodes.size());
		}
	}
}


