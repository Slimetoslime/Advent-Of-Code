package year2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import universalFunctions.ReadFiles;

/**
 * =========================================================================
 * RUNNER CODE
 * =========================================================================
 */

public class Day11 {
	
	// Runner Code
	public static void main(String[] args) {
		File puzzleFile = new File("input/2024/Day11.txt");
		String puzzle = ReadFiles.readFileRaw(puzzleFile);
		
		List<Long> stones = Arrays.asList(puzzle.strip().split(" ")).stream().mapToLong(Long::valueOf).boxed().toList();
		Map<Long, Long> stonesFrequency = stones.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.summingLong(e -> 1)));
		Part1.part(stonesFrequency);
		Part2.part(stonesFrequency);
	}
	
	/** Computes a blink.
	 * 
	 * @param stones frequency Map of the stones.
	 * @return new stone frequency Map after a blink.
	 */
	private static Map<Long, Long> blink(Map<Long, Long> stones) {
		Map<Long, Long> newFrequencyMap = new HashMap<>();
		
		for (Long stone: stones.keySet()) {
			if (stone == 0) {
				newFrequencyMap.put((long) 1, stones.get(stone));
			}
			else if (Math.ceil(Math.log10(stone + 1)) % 2 == 0) {
				List<Long> newKeys = splitNumber(stone);
				for (Long newKey: newKeys) {
					newFrequencyMap.computeIfPresent(newKey, (key, num) -> num + stones.get(stone));
					newFrequencyMap.putIfAbsent(newKey, stones.get(stone));
				}
			}
			else newFrequencyMap.put(stone * 2024, stones.get(stone));
		}
		
		return newFrequencyMap;
	}
	
	/** Mathematically split a number into 2 numbers, based on length.
	 * 
	 * @param number Long number to split.
	 * @return 2-length List of Longs, splitting the original number into 2 smaller numbers. 
	 */
	private static List<Long> splitNumber(long number) {
		int halfLength = (int) Math.ceil(Math.log10(number + 1)) / 2;
		List<Long> splitNum = new ArrayList<>();
		
		splitNum.add((long) (Double.valueOf(number) / Math.pow(10, halfLength)));
		splitNum.add((long) (number % Math.pow(10, halfLength)));
		
		return splitNum;
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
		public static void part(Map<Long, Long> stones) {
			Map<Long, Long> partOneStones = new HashMap<>();
			partOneStones.putAll(stones);
			
			for (int n = 0; n < 25; n++) {
				partOneStones = blink(partOneStones);
			}
			
			System.out.println("Part 1: " + partOneStones.values().stream().mapToLong(Long::valueOf).sum());
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
		public static void part(Map<Long, Long> stones) {
			Map<Long, Long> partTwo = new HashMap<>();
			partTwo.putAll(stones);
			
			for (int n = 0; n < 75; n++) {
				partTwo = blink(partTwo);
			}
	
			System.out.println("Part 2: " + partTwo.values().stream().mapToLong(Long::valueOf).sum());
		}
	}
}


