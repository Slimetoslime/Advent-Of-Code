package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day20 {
	
	static class Position {
		private int r;
		private int c;
		
		public Position(int r, int c) {
			this.r = r;
			this.c = c;
		}
		
		public Position add(int[] relativeDist) {
			return new Position(r + relativeDist[0], c + relativeDist[1]);
		}

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

		@Override
		public String toString() {
			return "Position [r=" + r + ", c=" + c + "]";
		}
	}
	
	private static Position startPos;
	private static String[][] map;
	private static Map<Position, Long> distance;
	private static final long infinity = Long.MAX_VALUE;
	private static final int[][] nearDistance = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
	private static List<Position> emptySpaces = new ArrayList<>();
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day20.txt");
		List<List<String>> puzzle = ReadFiles.readIntoMatrix(puzzleFile);
		
		parseToArrayMap(puzzle);
		getEmpties();
		distance = dijkstraAlgorithm();
		
		Part1.part();
		Part2.part();
	}
	
	private static void parseToArrayMap(List<List<String>> listMap) {
		map = new String[listMap.size()][];
		String[] blankArray = new String[0];
		for (int i = 0; i < listMap.size(); i++) {
			map[i] = listMap.get(i).toArray(blankArray);
		}
	}
	
	private static void getEmpties() {
		for (int r = 0; r < map.length; r++) {
			String[] mapRow = map[r];
			for (int c = 0; c < mapRow.length; c++) {
				String element = mapRow[c];
				if (!element.equals("#")) {
					emptySpaces.add(new Position(r, c));
					if (element.equals("S")) startPos = new Position(r, c);
				}
			}
		}
	}
	
	private static Map<Position, Long> initializeDistance(List<Position> emptyPositions) {
		Map<Position, Long> distance = new HashMap<>();
		
		for (Position empty: emptyPositions) {
			if (empty.equals(startPos)) distance.put(empty, 0L);
			else distance.put(empty, infinity);
		}
		
		return distance;
	}
	
	private static Map<Position, Long> dijkstraAlgorithm() {
		Map<Position, Long> distance = initializeDistance(emptySpaces);
		List<Position> previous = new ArrayList<>();
		
		PriorityQueue<Position> toVisit = new PriorityQueue<>((a, b) -> -1 * distance.get(a).compareTo(distance.get(b)));
		Set<Position> haveVisited = new HashSet<>();
		toVisit.add(startPos);
		
		while (!toVisit.isEmpty()) {
			Position polled = toVisit.poll();
			long currDist = distance.get(polled);
			
			for (int[] relativeDist: nearDistance) {
				Position newPosition = polled.add(relativeDist);
				if (emptySpaces.contains(newPosition) && distance.get(newPosition) > currDist+1) {
					distance.put(newPosition, currDist+1);
					previous.add(polled);
					if (!haveVisited.contains(newPosition)) toVisit.add(newPosition);
				}
			}
			
			haveVisited.add(polled);
		}
		
		return distance;
	}
	
	private static int getSaves(Map<Position, Long> distances, int maxCheat, int minSaved) {
		int saved = 0;
		Set<Position> positions = distances.keySet();
		
		for (Position p: positions) {
			for (Position np: positions) {
				long cheatDist = Math.abs(p.r - np.r) + Math.abs(p.c - np.c);
				long init = distances.get(np) - distances.get(p);
				if (cheatDist <= maxCheat && (init - cheatDist) >= minSaved) saved++;
			}
		}
		
		return saved;
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
			System.out.println("Part 1: " + getSaves(distance, 2, 100));
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
			System.out.println("Part 2: " + getSaves(distance, 20, 100));
		}
	}
}


