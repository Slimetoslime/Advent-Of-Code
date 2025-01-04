package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day21 {
	
	static class Position {
		private int r;
		private int c;
		
		public Position(int r, int c) {
			this.r = r;
			this.c = c;
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
	
	static class Path {
		private Position p;
		private String dir;
		
		public Path(Position p, String dir) {
			this.p = p;
			this.dir = dir;
		}
		
		public Path(int r, int c, String dir) {
			this.p = new Position(r, c);
			this.dir = dir;
		}

		@Override
		public String toString() {
			return "Path [p=" + p + ", dir=" + dir + "]";
		}
	}
	
	private static Map<RecursionTuple, Long> cache = new ConcurrentHashMap<>();
	static class RecursionTuple {
		private String code;
		private int depth;
		
		public RecursionTuple(String code, int depth) {
			this.code = code;
			this.depth = depth;
		}
		
		private long getRobotLength() {
			if (cache.containsKey(this)) return cache.get(this);
			if (depth == 0) return code.length();
			
			String source = "A";
			long total = 0;
			for (char c: code.toCharArray()) {
				String dest = Character.toString(c);
				List<Long> temporary = new ArrayList<>();
				for (String path: getPaths(directionalKeypad, source, dest)) {
					long res = new RecursionTuple(path, depth-1).getRobotLength();
					temporary.add(res);
				}
				total += Collections.min(temporary);
				source = dest;
			}
			
			cache.put(this, total);
			return total;
		}

		@Override
		public int hashCode() {
			return Objects.hash(code, depth);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RecursionTuple other = (RecursionTuple) obj;
			return Objects.equals(code, other.code) && depth == other.depth;
		}
	}
	
	private static String[][] numericKeypad = {
			{"7", "8", "9"},
			{"4", "5", "6"},
			{"1", "2", "3"},
			{"X", "0", "A"}
	};
	private static String[][] directionalKeypad = {
			{"X", "^", "A"},
			{"<", "v", ">"}
	};
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day21.txt");
		List<String> puzzle = ReadFiles.readLineByLine(puzzleFile);
		
		Part1.part(puzzle);
		Part2.part(puzzle);
	}
	
	private static Position[] findStartEnd(String[][] keypad, String button1, String button2, String bad) {
		Position[] results = new Position[3];
		for (int r = 0; r < keypad.length; r++) {
			boolean found = false;
			String[] row = keypad[r];
			for (int c = 0; c < row.length; c++) {
				String element = row[c];
				if (element.equals(button1)) results[0] = new Position(r, c);
				if (element.equals(button2)) results[1] = new Position(r, c);
				else if (element.equals(bad)) results[2] = new Position(r, c);
				if (results[0] != null && results[1] != null && results[2] != null) {
					found = false;
					break;
				}
			}
			if (found) break;
		}
		return results;
	}
	
	private static List<String> getPaths(String[][] keypad, String source, String destination) {
		int rMax = keypad.length;
		int cMax = keypad[0].length;
		
		Position[] startEndPos = findStartEnd(keypad, source, destination, "X");
		Position start = startEndPos[0];
		Position end = startEndPos[1];
		Position bad = startEndPos[2];
		
		Queue<Path> q = new LinkedList<>();
		q.add(new Path(start, ""));
		List<String> endingPaths = new ArrayList<>();
		
		while (!q.isEmpty()) {
			Path popped = q.poll();
			Position popPos = popped.p;
			if (popPos.r < 0 || popPos.c < 0 || popPos.r >= rMax || popPos.c >= cMax) continue;
			
			if (popped.p.equals(end)) {
				endingPaths.add(popped.dir + "A");
			} else if (!popped.p.equals(bad)) {
				if (popPos.r < end.r) q.add(new Path(popPos.r + 1, popPos.c, popped.dir + "v"));
				else if (popPos.r > end.r) q.add(new Path(popPos.r - 1, popPos.c, popped.dir + "^"));
				if (popPos.c < end.c) q.add(new Path(popPos.r, popPos.c + 1, popped.dir + ">"));
				else if (popPos.c > end.c) q.add(new Path(popPos.r, popPos.c - 1, popped.dir + "<"));
			}
		}
		
		return endingPaths;
	}
	
	private static List<String> getCartProdDir(List<List<String>> directions) {
		List<String> cartesianProduct = new ArrayList<>();
		cartesianProduct.add("");
		
		for (List<String> dir: directions) {
			List<String> temp = new ArrayList<>();
			for (String s: cartesianProduct) {
				for (String s1: dir) {
					temp.add(s + s1);
				}
			}
			cartesianProduct = temp;
		}
		
		return cartesianProduct;
	}
	
	private static List<String> getDoorRobotPaths(String code) {
		List<List<String>> paths = new ArrayList<>();
		for (int i = -1; i < code.length()-1; i++) {
			String source = (i == -1) ? "A" : Character.toString(code.charAt(i));
			String dest = Character.toString(code.charAt(i+1));
			paths.add(getPaths(numericKeypad, source, dest));
		}
		return getCartProdDir(paths);
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
		public static void part(List<String> doorCodes) {
			List<Long> trueRes = new ArrayList<>();
			
			for (String code: doorCodes) {
				List<String> directionalCodes = getDoorRobotPaths(code);
				List<Long> result = new ArrayList<>();
				for (String dirCode: directionalCodes) {
					RecursionTuple t = new RecursionTuple(dirCode, 2);
					result.add(t.getRobotLength());
				}
				trueRes.add(Collections.min(result) * Long.valueOf(code.substring(0, code.length()-1)));
			}

			System.out.println("Part 1: " + trueRes.stream().mapToLong(Long::valueOf).sum());
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
		public static void part(List<String> doorCodes) {
			List<Long> trueRes = new ArrayList<>();
			
			for (String code: doorCodes) {
				List<String> directionalCodes = getDoorRobotPaths(code);
				List<Long> result = new ArrayList<>();
				for (String dirCode: directionalCodes) {
					RecursionTuple t = new RecursionTuple(dirCode, 25);
					result.add(t.getRobotLength());
				}
				trueRes.add(Collections.min(result) * Long.valueOf(code.substring(0, code.length()-1)));
			}

			System.out.println("Part 2: " + trueRes.stream().mapToLong(Long::valueOf).sum());
		}
	}
}

