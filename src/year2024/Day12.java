package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day12 {
	private static final int[][] ORDINAL_DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
	
	/** Point class.
	 * 
	 */
	static class Point {

		private int r;
		private int c;
		
		/** Constructor for a point.
		 * 
		 * @param r Row index.
		 * @param c Column index.
		 */
		public Point(int r, int c) {
			this.r = r;
			this.c = c;
		}
		
		/** Returns a List of Points containing the neighbors of the point.
		 * 
		 * @return List of neighboring Points.
		 */
		public List<Point> getNeighbors() {
			List<Point> adjacentPoints = new ArrayList<>();
			
			for (int[] direction: ORDINAL_DIRECTIONS) {
				adjacentPoints.add(new Point(this.r + direction[0], this.c + direction[1]));
			}
			
			return adjacentPoints;
		}
		
		// Overriding hashCode and equals for comparison checking.
		@Override
		public int hashCode() {
			return Objects.hash(c, r);
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
			return c == other.c && r == other.r;
		}

		// Formatting purposes.
		@Override
		public String toString() {
			return String.format("(%d, %d)", this.r, this.c);
		}
	}
	
	// Runner Code.
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day12.txt");
		List<List<String>> puzzle = ReadFiles.readIntoMatrix(puzzleFile);
		List<List<Point>> plantPoints = getListOfPlantPoints(puzzle);
		
		List<List<Integer>> statistics = getStatistics(plantPoints);
		Part1.part(statistics);
		Part2.part(statistics);
	}
	
	/** Returns a nested list of points, split by the regions.
	 * 
	 * @param map Map of the regions.
	 * @return Nested list of points divided by the regions.
	 */
	public static List<List<Point>> getListOfPlantPoints(List<List<String>> map) {
		List<List<Point>> result = new ArrayList<>();
		
		for (int r = 0; r < map.size(); r++) {
			List<String> row = map.get(r);
			for (int c = 0; c < row.size(); c++) {
				Point plantPoint = new Point(r, c);
				if (!result.stream().anyMatch(x -> x.contains(plantPoint))) {
					result.add(floodFill(map, plantPoint, row.get(c)));
				}
			}
		}
		
		return result;
	}
	
	/** Flood fill algorithm.
	 * 
	 * @param map Map of the regions.
	 * @param startPoint Where the flood fill search should start.
	 * @param toSearch What region to search for.
	 * @return A list of Points that flood filling has visited.
	 */
	public static List<Point> floodFill(List<List<String>> map, Point startPoint, String toSearch) {
		List<Point> adjacencyPoints = new ArrayList<>();
		
		Queue<Point> toCheck = new LinkedList<>();
		adjacencyPoints.add(startPoint);
		toCheck.add(startPoint);
		
		while (!toCheck.isEmpty()) {
			Point currentPoint = toCheck.poll();
			List<Point> neighboringPoints = currentPoint.getNeighbors();
			for (Point neighborPoint: neighboringPoints) {
				try {
					if (map.get(neighborPoint.r).get(neighborPoint.c).equals(toSearch) && !adjacencyPoints.contains(neighborPoint)) {
						adjacencyPoints.add(neighborPoint);
						toCheck.add(neighborPoint);
					}
				} catch (IndexOutOfBoundsException ioobe) {
					continue;
				}
			}
		}
		
		return adjacencyPoints;
	}
	
	/** Returns the Area, Perimeter, and the number of Sides for each region. 
	 * 
	 * @param plantPoints Nested List of Points divided by regions.
	 * @return a 3-length array containing the area, perimeter, and the number of sides each region has, respectively. 
	 */
	public static List<List<Integer>> getStatistics(List<List<Point>> plantPoints) {
		List<List<Integer>> result = new ArrayList<>();
		
		for (List<Point> entry: plantPoints) {
			List<Integer> statisticEntry = new ArrayList<>();
			statisticEntry.add(entry.size());
			
			List<Point> adjacentPoints = new ArrayList<>();
			for (Point plant: entry) {
				List<Point> neighborPoints = plant.getNeighbors().stream().filter(x -> !entry.contains(x)).toList();
				adjacentPoints.addAll(neighborPoints);
			} 
			statisticEntry.add(adjacentPoints.size());
			statisticEntry.add(getSides(entry));
			result.add(statisticEntry);
		}
		
		return result;
	}
	
	public static int getSides(List<Point> plantPoints) {
		int result = 0;
		final int[] dir = {-1, 1};
		
		for (Point point: plantPoints) {
			for (int a: dir) {
				for (int b: dir) {
					Point rChange = new Point(point.r + a, point.c);
					Point cChange = new Point(point.r, point.c + b);
					if (!(plantPoints.contains(rChange) || plantPoints.contains(cChange))) result++;
					Point rcChange = new Point(point.r + a, point.c + b);
					if (plantPoints.contains(rChange) && plantPoints.contains(cChange) && !plantPoints.contains(rcChange)) result++;
				}
			}
		}
		
//		ReadFiles.printMap(frequencyPoints);
		return result;
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
		public static void part(List<List<Integer>> statistics) {
			System.out.println("Part 1: " + statistics.stream().map(x -> x.get(0) * x.get(1)).mapToInt(Integer::valueOf).sum());
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
		public static void part(List<List<Integer>> statistics) {
			System.out.println("Part 2: " + statistics.stream().map(x -> x.get(0) * x.get(2)).mapToInt(Integer::valueOf).sum());
		}
	}
}


