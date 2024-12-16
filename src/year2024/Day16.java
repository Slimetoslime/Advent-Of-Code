package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import universalFunctions.CommonObjects.Position;
import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day16 {
	
	private static enum Direction {north, south, east, west};
	private static final Map<Direction, Direction> flippedMap = Map.of(
			Direction.north, Direction.south,
			Direction.south, Direction.north,
			Direction.west, Direction.east,
			Direction.east, Direction.west
		);
	
	// Class for each tile (node).
	static class Tile {
		private static final Map<Direction, int[]> directionsMap = Map.of(
				Direction.north, new int[] {-1, 0}, 
				Direction.south, new int[] {1, 0}, 
				Direction.east, new int[] {0, 1}, 
				Direction.west, new int[] {0, -1}
			);
		
		private Position p;
		private Direction dir;
		
		/** Constructor for a tile.
		 * 
		 * @param r Row index.
		 * @param c Column index.
		 * @param d Direction.
		 */
		public Tile(int r, int c, Direction d) {
			this.p = new Position(r, c);
			this.dir = d;
		}
		
		/** Constructor for a tile.
		 * 
		 * @param p Position object.
		 * @param dir Direction
		 */
		public Tile(Position p, Direction dir) {
			this.p = p;
			this.dir = dir;
		}
		
		/** Returns a new Tile with its position changed according to the direction.
		 * 
		 * @return A new Tile object with its position moved.
		 */
		public Tile move() {
			return new Tile(p.move(directionsMap.get(dir)), dir);
		}
		
		/** Returns a new Tile with its direction changed to its left.
		 * 
		 * @return a new Tile object with its direction changed facing left.
		 */
		public Tile turnLeft() {
			Direction newDir = null;
			switch (dir) {
			case Direction.north -> newDir = Direction.west;
			case Direction.south -> newDir = Direction.east;
			case Direction.east -> newDir = Direction.north;
			case Direction.west -> newDir = Direction.south;
			}
			return new Tile(p, newDir);
		}
		
		/** Returns a new Tile with its direction changed to its right.
		 * 
		 * @return a new Tile object with its direction changed facing right.
		 */
		public Tile turnRight() {
			Direction newDir = null;
			switch (dir) {
			case Direction.north -> newDir = Direction.east;
			case Direction.south -> newDir = Direction.west;
			case Direction.east -> newDir = Direction.south;
			case Direction.west -> newDir = Direction.north;
			}
			return new Tile(p, newDir);
		}
		
		/** Returns the Position of the Tile.
		 * 
		 * @return Tile Position.
		 */
		public Position getPosition() {
			return p;
		} 
		
		/** Returns a mutable Tile with a specified direction.
		 * 
		 * @param dir new Direction for the Tile.
		 * @return new Tile with the new Direction.
		 */
		public Tile setMutDir(Direction dir) {
			return new Tile(p, dir);
		}
		
		/** Checks if the object has the same position as specified.
		 * 
		 * @param pos Position to check.
		 * @return Boolean value if the position of the Tile is equal to that of specified.
		 */
		public boolean isSamePosition(Position pos) {
			return (p.getR() == pos.getR()) && (p.getC() == pos.getC());
		}
		
		// For sets and equality checking.
		@Override
		public int hashCode() {
			return Objects.hash(dir, p);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Tile other = (Tile) obj;
			return dir == other.dir && Objects.equals(p, other.p);
		}

		// Formatting purposes.
		@Override
		public String toString() {
			String direction = "";
			try {
				direction += dir.toString();
			} catch (NullPointerException npe) {
				direction += "null";
			}
			return String.format("(%d, %d, %s)", p.getR(), p.getC(), direction);
		}
	}
	
	private static Tile startTile;
	private static Tile finalTile;
	
	// Runner Code
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day16.txt");
		List<List<String>> puzzle = ReadFiles.readIntoMatrix(puzzleFile);
		
		startTile = findElement(puzzle, "S");
		finalTile = findElement(puzzle, "E");
		
		Part1.part(puzzle);
		Part2.part(puzzle);
	}
	
	/** Finds an element of the maze and returns its indices.
	 * 
	 * @param maze Grid representation of the maze.
	 * @param element String element to find in the maze.
	 * @return a Tile containing the row and column index of the maze, facing east.
	 */
	public static Tile findElement(List<List<String>> maze, String element) {
		for (int r = 0; r < maze.size(); r++) {
			List<String> row = maze.get(r);
			for (int c = 0; c < row.size(); c++) {
				String current = row.get(c);
				if (current.equals(element)) {
					return new Tile(new Position(r, c), Direction.east);
				}
			}
		}
		return null;
	}
	
	/** Returns a list of non-wall elements of the maze.
	 * 
	 * @param maze Grid representation of the maze.
	 * @return a List of Tiles referring to Positions that are not a wall.
	 */
	private static List<Tile> findAllEmpties(List<List<String>> maze) {
		List<Tile> emptySpaces = new ArrayList<>();
		for (int r = 0; r < maze.size(); r++) {
			List<String> row = maze.get(r);
			for (int c = 0; c < row.size(); c++) {
				String current = row.get(c);
				if (!current.equals("#")) {
					emptySpaces.add(new Tile(r, c, Direction.east));
					emptySpaces.add(new Tile(r, c, Direction.west));
					emptySpaces.add(new Tile(r, c, Direction.north));
					emptySpaces.add(new Tile(r, c, Direction.south));
				}
			}
		}
		return emptySpaces;
	}
	
	/** Dijkstra's Algorithm to return a distance map from a point to every other point, based on the specified scoring function in the problem.
	 * 
	 * @param maze Grid representation of the maze.
	 * @param startTiles a List of starting Tiles.
	 * @return a Map containing Tiles as Keys and Long numbers as distances from the key to one of the start tiles.
	 */
	private static Map<Tile, Long> dijkstraAlgorithm(List<List<String>> maze, List<Tile> startTiles) {
		Map<Tile, Long> tiles = new HashMap<>();
		List<Tile> emptyTiles = findAllEmpties(maze);
		for (Tile startTile: startTiles) {
			tiles.put(startTile, 0L);
		}
		
		PriorityQueue<Tile> search = new PriorityQueue<Tile>((a, b) -> tiles.get(a).compareTo(tiles.get(b)));
		search.addAll(startTiles);
		
		while (!search.isEmpty()) {
			Tile currentTile = search.poll();
			long currentTileScore = tiles.get(currentTile);
			
			List<Tile> turns = new ArrayList<>();
			turns.addAll(Arrays.asList(currentTile.turnLeft(), currentTile.turnRight()));
			for (Tile turn: turns) {
				if (!tiles.containsKey(turn) || tiles.get(turn) > (currentTileScore + 1000)) {
					tiles.put(turn, currentTileScore + 1000);
					search.add(turn);
				}
			}
			
			Tile forwardTile = currentTile.move();
			if (emptyTiles.contains(forwardTile) && (!tiles.containsKey(forwardTile) || tiles.get(forwardTile) > currentTileScore + 1)) {
				tiles.put(forwardTile, currentTileScore + 1);
				search.add(forwardTile);
			}
		}
		
		return tiles;
	}
	
	/** Calculates the smallest score to the final Tile, irregardless of ending direction.
	 * 
	 * @param tiles distance Map as generated by the Dijkstra's Algorithm. 
	 * @return the smallest Long distance.
	 */
	private static long calculateBest(Map<Tile, Long> tiles) {
		List<Long> finalResults = new ArrayList<>();
		for (Tile tileKey: tiles.keySet()) {
			if (tileKey.isSamePosition(finalTile.p)) {
				finalResults.add(tiles.get(tileKey));
			}
		}
		return finalResults.stream().mapToLong(Long::valueOf).min().getAsLong();
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
		public static void part(List<List<String>> map) {
			Map<Tile, Long> tiles = dijkstraAlgorithm(map, Arrays.asList(startTile));

			System.out.println("Part 1: " + calculateBest(tiles));
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
		 * Credit to u/ParanoidAndroidQ for the algorithm.
		 * 
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part(List<List<String>> map) {
			Map<Tile, Long> benchmark = dijkstraAlgorithm(map, Arrays.asList(startTile));
			long optimal = calculateBest(benchmark);
			Map<Tile, Long> newTiles = dijkstraAlgorithm(map, Arrays.asList(finalTile, 
					finalTile.setMutDir(Direction.west), 
					finalTile.setMutDir(Direction.north), 
					finalTile.setMutDir(Direction.south)));
			
			Set<Position> result = new HashSet<>();
			for (int r = 0; r < map.size(); r++) {
				for (int c = 0; c < map.get(r).size(); c++) {
					for (Direction dir: new Direction[] {Direction.east, Direction.north, Direction.south, Direction.west}) {
						Tile start = new Tile(r, c, dir);
						Tile end = new Tile(r, c, flippedMap.get(dir));
						if (benchmark.containsKey(start) && newTiles.containsKey(end)) {
							if (benchmark.get(start) + newTiles.get(end) == optimal) result.add(start.p);
						}
					}
				}
			}
			
			System.out.println("Part 2: " + result.size());
		}
	}
}


