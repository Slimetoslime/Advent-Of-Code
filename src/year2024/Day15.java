package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day15 {
	
	/** Class to store positions on the grid.
	 * 
	 */
	static class Position {
		private int r;
		private int c;
		
		/** Constructor for primitive integer values.
		 * 
		 * @param r Row index.
		 * @param c Column index.
		 */
		public Position(int r, int c) {
			this.r = r;
			this.c = c;
		}
		
		/** Constructor for copying purposes.
		 * 
		 * @param p Position.
		 */
		public Position(Position p) {
			this.r = p.r;
			this.c = p.c;
		}
		
		/** Moves the position based on an Integer array of direction and magnitude.
		 * 
		 * @param change Integer array to change position.
		 * @return A Position with its values modified from the previous.
		 */
		public Position move(int[] change) {
			return new Position(r + change[0], c + change[1]);
		}
		
		/** Returns the position to the right.
		 * 
		 * @return New Position one to the right from the old position.
		 */
		public Position rightOne() {
			return this.move(new int[] {0, 1});
		}
		
		/** Returns the position to the left.
		 * 
		 * @return New Position one to the left from the old position.
		 */
		public Position leftOne() {
			return this.move(new int[] {0, -1});
		}
		
		// Checking equality and sets purpose
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
			Position other = (Position) obj;
			return c == other.c && r == other.r;
		}

		// Formatting purposes
		@Override
		public String toString() {
			return String.format("(%d, %d)", r, c);
		}
	}
	
	private static List<Position> walls = new ArrayList<>();
	private static final Map<String, int[]> directions = Map.ofEntries(
			entry("<", new int[] {0, -1}),
			entry(">", new int[] {0, 1}),
			entry("v", new int[] {1, 0}),
			entry("^", new int[] {-1, 0})
		);
	
	// Runner Code.
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day15.txt");
		List<String> puzzle = ReadFiles.readCustomGap(puzzleFile, System.lineSeparator() + System.lineSeparator());
		List<List<String>> grid = ReadFiles.parseIntoMatrix(puzzle.get(0));
		
		List<Position> notablePositions = parseToPositionsList(grid);
		
		StringBuffer bString = new StringBuffer();
		for (String s: puzzle.get(1).split(System.lineSeparator())) {
			bString.append(s);
		}
		String instructions = bString.toString();
		
		Part1.part(notablePositions, instructions);
		Part2.part(grid, instructions);
	}
	
	/** Returns a list of positions as well as initializing the walls' position.
	 * 
	 * @param grid The grid map of the whole facility.
	 * @return A Position for the robot at the first index, and a List of Positions for the boxes.
	 */
	private static List<Position> parseToPositionsList(List<List<String>> grid) {
		List<Position> results = new ArrayList<>();
		
		for (int r = 0; r < grid.size(); r++) {
			List<String> row = grid.get(r);
			for (int c = 0; c < row.size(); c++) {
				String element = row.get(c);
				switch (element) {
				case "#" -> walls.add(new Position(r, c));
				case "O" -> results.add(new Position(r, c));
				case "@" -> results.add(0, new Position(r,c));
				}
			}
		}
		
		return results;
	}
	
	/** Returns a new Position for the moved target and modifies the list of positions based on the target's move. 
	 * 
	 * @param currentPosition Current position of the moving target.
	 * @param boxesPosition List of current movables Position.
	 * @param instruction Direction for where to move to.
	 * @return A new Position (if there's no wall) and an updated list of positions of the boxes.
	 */
	private static Position moveToPosition(Position currentPosition, List<Position> boxesPosition, String instruction) {
		Position nextPosition = currentPosition.move(directions.get(instruction));
		if (walls.contains(nextPosition)) return currentPosition;
		else if (boxesPosition.contains(nextPosition)) {
			boxesPosition.remove(nextPosition);
			Position nextBoxPosition = moveToPosition(nextPosition, boxesPosition, instruction);
			boxesPosition.add(nextBoxPosition);
			if (nextBoxPosition.equals(nextPosition)) {
				return currentPosition;
			}
		}
		return nextPosition;
	}
	
	/** Calculates the sum of the GPS Coordinates from the boxes.
	 * 
	 * @param boxes List of box Positions.
	 * @return An integer of the sum of GPS Coordinates.
	 */
	private static int calculateGPSCoordinates(List<Position> boxes) {
		List<Integer> results = new ArrayList<>();
		
		for (Position box: boxes) {
			results.add(box.r * 100 + box.c);
		}
		
		return results.stream().mapToInt(Integer::valueOf).sum();
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
		public static void part(List<Position> notablePositions, String instructions) {
			Position robot = new Position(notablePositions.get(0));
			List<Position> boxes = new ArrayList<>(notablePositions.subList(1, notablePositions.size()).stream().map(x -> new Position(x)).toList());
			String[] instructionArray = instructions.split("");
			
			for (String instruction: instructionArray) {
				robot = moveToPosition(robot, boxes, instruction);
			}
			
			System.out.println("Part 1: " + calculateGPSCoordinates(boxes));
		}
	}
	
	/**
	 * =========================================================================
	 * START OF PART 2
	 * =========================================================================
	 */
	
	/** Transforms the grid to double its width.
	 * 
	 * @param originalGrid Original grid.
	 * @return A new grid twice its width of the previous grid.
	 */
	private static List<List<String>> mutateGrid(List<List<String>> originalGrid) {
		List<List<String>> transformedGrid = new ArrayList<>();
		
		for (List<String> row: originalGrid) {
			List<String> transformedRow = new ArrayList<>();
			for (String element: row) {
				if (element.equals("#")) transformedRow.addAll(Arrays.asList("#", "#"));
				else if (element.equals("O")) transformedRow.addAll(Arrays.asList("[", "]"));
				else if (element.equals("@")) transformedRow.addAll(Arrays.asList("@", "."));
				else transformedRow.addAll(Arrays.asList(".", "."));
			}
			transformedGrid.add(transformedRow);
		}
		
		return transformedGrid;
	}
	
	/** Returns the robot's position.
	 * 
	 * @param grid Grid to search from.
	 * @return A Position for the robot.
	 */
	private static Position getRobot(List<List<String>> grid) {
		for (int i = 0; i < grid.size(); i++) {
			List<String> row = grid.get(i);
			for (int j = 0; j < row.size(); j++) {
				String element = row.get(j);
				if (element.equals("@")) return new Position(i, j);
			}
		}
		return null;
	}
	
	/** Copies an entire grid, for dereferencing purposes.
	 * 
	 * @param grid Grid to copy from.
	 * @return Identical grid from the original, dereferenced.
	 */
	private static List<List<String>> copyGrid(List<List<String>> grid) {
		List<List<String>> result = new ArrayList<>();
		
		for (List<String> row: grid) {
			List<String> resultRow = new ArrayList<>();
			for (String element: row) {
				resultRow.add(element);
			}
			result.add(resultRow);
		}
		
		return result;
	} 
	
	/** Moves the robot and the boxes if they interact.
	 * 
	 * @param grid Grid of the whole facility.
	 * @param instruction Direction to move to.
	 */
	private static void moveRobot(List<List<String>> grid, String instruction) {
		Position robot = getRobot(grid);
		List<Position> thingsToMove = new ArrayList<>(Arrays.asList(robot));
		boolean free = true;
		int n = 0;
		while (n < thingsToMove.size()) {
			Position p = thingsToMove.get(n);
			n++;
			Position newPosition = p.move(directions.get(instruction));
			if (thingsToMove.contains(newPosition)) continue;
			String element = grid.get(newPosition.r).get(newPosition.c);
			if (element.equals("#")) {
				free = false;
				break;
			}
			if (element.equals("[")) {
				thingsToMove.add(new Position(newPosition));
				thingsToMove.add(new Position(newPosition.rightOne()));
			}
			if (element.equals("]")) {
				thingsToMove.add(new Position(newPosition));
				thingsToMove.add(new Position(newPosition.leftOne()));
			}
		}
		if (!free) return;
		List<List<String>> copy = copyGrid(grid);
		Position newDirection = new Position(directions.get(instruction)[0], directions.get(instruction)[1]);
		grid.get(robot.r).set(robot.c, ".");
		for (Position p: thingsToMove.subList(1, thingsToMove.size())) {
			grid.get(p.r).set(p.c, ".");
		}
		for (Position p: thingsToMove.subList(1, thingsToMove.size())) {
			grid.get(p.r + newDirection.r).set(p.c + newDirection.c, copy.get(p.r).get(p.c));
		}
		grid.get(robot.r + newDirection.r).set(robot.c + newDirection.c, "@");
	}
	
	/** Returns the sum of the GPS Coordinates from a doubled grid.
	 * 
	 * @param grid Doubled Grid to search from.
	 * @return an Integer sum of the GPS Coordinates from the grid.
	 */
	private static int getDoubledGPSCoordinate(List<List<String>> grid) {
		int result = 0;
		
		for (int r = 0; r < grid.size(); r++) {
			List<String> row = grid.get(r);
			for (int c = 0; c < row.size(); c++) {
				if (row.get(c).equals("[")) result += (100 * r + c);
			}
		}
		
		return result;
	}
	
	class Part2 {

		/**
		 * Solves the second part of the puzzle.
		 * Credit to HyperNeutrino for the solution to this puzzle. 
		 * I could not figure it out with my implementation.
		 * 
		 * @param puzzleLines List of Strings read from the file line-by-line.
		 */
		public static void part(List<List<String>> grid, String instructions) {
			List<List<String>> mutableGrid = new ArrayList<>(grid);
			List<List<String>> expandedGrid = mutateGrid(mutableGrid);
			
			for (String instruction: instructions.split("")) {
				moveRobot(expandedGrid, instruction);
			}
			
			System.out.println("Part 2: " + getDoubledGPSCoordinate(expandedGrid));
		}
	}
}


