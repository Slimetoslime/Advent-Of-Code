package year2024;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import universalFunctions.ReadFiles;
import universalFunctions.CommonObjects.Position;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day18 {
	
	private static final Long infinity = Long.MAX_VALUE;
	private static final Integer[][] directions = {
			{0, 1}, {1, 0}, {0, -1}, {-1, 0}
	};
	private static String[][] map;
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day18.txt");
		List<String> puzzle = ReadFiles.readLineByLine(puzzleFile);
		
		Part1.part(puzzle, 1024, 70);
		Part2.part(puzzle, 70);
	}
	
	/** Parses the maze into a String representation of the maze.
	 * 
	 * @param wallCoordinates Coordinates of the walls.
	 * @param wallNum Number of walls that should be placed.
	 * @param sizeOfMap Size of the maze.
	 */
	private static void parseToMap(List<String> wallCoordinates, int wallNum, int sizeOfMap) {
		map = new String[sizeOfMap+1][sizeOfMap+1];
		for (String[] row: map) Arrays.fill(row, ".");
		
		for (String coordinate: wallCoordinates.subList(0, wallNum)) {
			String[] wallCoordinate = coordinate.split(",");
			map[Integer.valueOf(wallCoordinate[1])][Integer.valueOf(wallCoordinate[0])] = "#";
		}
	}
	
	/** Finds every empty point in the maze.
	 * 
	 * @return a Set of Positions containing every traversable empty space.
	 */
	private static Set<Position> getAllEmpties() {
		Set<Position> empties = new HashSet<>();
		
		for (int r = 0; r < map.length; r++) {
			String[] row = map[r];
			for (int c = 0; c < row.length; c++) {
				String element = row[c];
				if (element.equals(".")) empties.add(new Position(r, c));
			}
		}
		
		return empties;
	}
	
	/** Dijkstra's Algorithm to traverse the maze of bytes.
	 * 
	 * @param start Position to start from.
	 * @param stopAtFinal Boolean value if search must stop when reaching the finish tile.
	 * @param goal Where the final tile is.
	 * @return A distance matrix containing distance from the start to every other traversable point.
	 */
	private static Map<Position, Long> dijkstra(Position start, boolean stopAtFinal, Position goal) {
		boolean cont = true;
		Set<Position> empties = getAllEmpties();
		Map<Position, Long> distanceMapping = new HashMap<>();
		for (Position empty: empties) {
			distanceMapping.put(empty, infinity);
		}
		PriorityQueue<Position> toCheck = new PriorityQueue<>((a, b) -> distanceMapping.get(a).compareTo(distanceMapping.get(b)));
		Set<Position> seen = new HashSet<>();
		
		distanceMapping.put(start, 0L);
		toCheck.add(start);
		
		while (!toCheck.isEmpty() && cont) {
			Position polled = toCheck.poll();
			seen.add(polled);
			long distance = distanceMapping.get(polled) + 1;
			Set<Position> canMoveTo = new HashSet<>();
			for (Integer[] direction: directions) {
				int newR = polled.getR() + direction[0];
				int newC = polled.getC() + direction[1];
				Position posToCheck = new Position(newR, newC);
				if (empties.contains(posToCheck)) canMoveTo.add(posToCheck);
			}
			
			for (Position newPosition: canMoveTo) {
				if (distanceMapping.get(newPosition) > distance) {
					if (!seen.contains(newPosition)) toCheck.add(newPosition);
					distanceMapping.put(newPosition, distance);
				}
				if (stopAtFinal && newPosition.equals(goal)) cont = false;
			}
		}
		
		return distanceMapping;
	}
	
	/** Finds and returns the first byte that makes the final destination unreachable.
	 * 
	 * @param bytes List of Byte locations.
	 * @param size Size of the map.
	 * @return The coordinate of the first byte blocking the goal.
	 */
	private static String firstToBlock(List<String> bytes, int size) {
		int leftMost = 0;
		int rightMost = bytes.size();
		Position finalPos = new Position(size, size);
		
		while (leftMost < rightMost-1) {
			int timeToCheck = (rightMost + leftMost)/2;
			parseToMap(bytes, timeToCheck, size);
			Map<Position, Long> distanceMatrix = dijkstra(new Position(), true, finalPos);
			long finalPositionValue = distanceMatrix.get(finalPos);
			if (finalPositionValue == infinity) rightMost = timeToCheck;
			else leftMost = timeToCheck;
		}
		
		return bytes.get(leftMost);
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
		public static void part(List<String> bytes, int numOfBytes, int size) {
			parseToMap(bytes, numOfBytes, size);
			Map<Position, Long> distanceMatrix = dijkstra(new Position(), true, new Position(size, size));

			System.out.println("Part 1: " + distanceMatrix.get(new Position(size, size)));
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
		public static void part(List<String> bytes, int size) {
	
			System.out.println("Part 2: " + firstToBlock(bytes, size));
		}
	}
}


