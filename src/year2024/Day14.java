package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day14 {
	
	public static class Robot {
		private int[] position;
		private int[] velocity;
		
		public Robot(int[] position, int[] velocity) {
			this.position = position;
			this.velocity = velocity;
		}
		
		public Robot travel(int seconds, int maxX, int maxY) {
			int[] newPosition = new int[2];
			newPosition[0] = position[0] + (velocity[0] * seconds);
			newPosition[0] -= maxX * (int) Math.floor((double) newPosition[0] / (double) maxX);
			newPosition[1] = position[1] + (velocity[1] * seconds);
			newPosition[1] -= maxY * (int) Math.floor((double) newPosition[1] / (double) maxY);
			return new Robot(newPosition, velocity);
		}
		
		public int isHigher(int coordinate, int boundary) {
			if (position[coordinate] > boundary) return 1;
			else if (position[coordinate] < boundary) return -1;
			else return 0;
		}
		
		public static Set<List<Integer>> getSetOfPositions(List<Robot> robots) {
			Set<List<Integer>> result = new HashSet<>();
			
			for (Robot robot: robots) {
				List<Integer> positionList = List.of(robot.position[0], robot.position[1]);
				result.add(positionList);
			}
			
			return result;
		}
		
		public static List<List<Integer>> getListOfPositions(List<Robot> robots) {
			List<List<Integer>> result = new ArrayList<>();
			
			for (Robot robot: robots) {
				List<Integer> positionList = List.of(robot.position[0], robot.position[1]);
				result.add(positionList);
			}
			
			return result;
		}

		@Override
		public String toString() {
			return String.format("p=(%d,%d); v=(%d,%d)", position[0], position[1], velocity[0], velocity[1]);
		}
	}
	
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day14.txt");
		List<String> puzzle = ReadFiles.readLineByLine(puzzleFile);
		List<Robot> robots = parseToRobots(puzzle);
		
		Part1.part(robots, 100, 101, 103);
		Part2.part(robots, 101, 103);
	}
	
	public static List<Robot> parseToRobots(List<String> puzzleData) {
		List<Robot> result = new ArrayList<>();
		String regex = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)";
		Pattern pattern = Pattern.compile(regex);
		
		for (String data: puzzleData) {
			Matcher matcher = pattern.matcher(data);
			int[] position = new int[2];
			int[] velocity = new int[2];
			
			while (matcher.find()) {
				position[0] = Integer.valueOf(matcher.group(1));
				position[1] = Integer.valueOf(matcher.group(2));
				velocity[0] = Integer.valueOf(matcher.group(3));
				velocity[1] = Integer.valueOf(matcher.group(4));
				result.add(new Robot(position, velocity));
			}
		}
		
		return result;
	}
	
	public static List<Robot> steppedRobots(List<Robot> robots, int seconds, int maxX, int maxY) {
		List<Robot> steppedRobots = robots.stream().map(robot -> robot.travel(seconds, maxX, maxY)).toList();
		return steppedRobots;
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
		public static void part(List<Robot> robots, int seconds, int maxX, int maxY) {
			List<Robot> steppedRobots = steppedRobots(robots, seconds, maxX, maxY);
			List<List<Robot>> inQuadrant = new ArrayList<>(4);
			for (int i = 0; i < 4; i++) inQuadrant.add(new ArrayList<>());
			
			int midwayPointX = Math.floorDiv(maxX, 2);
			int midwayPointY = Math.floorDiv(maxY, 2);
			
			for (Robot robot: steppedRobots) {
				if (robot.isHigher(0, midwayPointX) == 1) {
					if (robot.isHigher(1, midwayPointY) == 1) inQuadrant.get(0).add(robot);
					if (robot.isHigher(1, midwayPointY) == -1) inQuadrant.get(1).add(robot); 
				} else if (robot.isHigher(0, midwayPointX) == -1) {
					if (robot.isHigher(1, midwayPointY) == 1) inQuadrant.get(2).add(robot);
					if (robot.isHigher(1, midwayPointY) == -1) inQuadrant.get(3).add(robot); 
				}
			}

			System.out.println(Robot.getListOfPositions(steppedRobots));
			System.out.println("Part 1: " + inQuadrant.stream().map(x -> x.size()).reduce((a, b) -> a*b).get());
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
		public static void part(List<Robot> robots, int maxX, int maxY) {
			for (int seconds = 0; seconds < 10431; seconds++) {
				List<Robot> steppedRobots = steppedRobots(robots, seconds, maxX, maxY);
				List<List<Integer>> positions = Robot.getListOfPositions(steppedRobots);
				Set<List<Integer>> uniqueElements = new HashSet<>(positions);
				if (uniqueElements.size() == steppedRobots.size()) {
					System.out.println("Part 2: " + seconds);
					break;
				}
			}
		}
	}
}


